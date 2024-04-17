package com.ascendcorp.androidtechpoc.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import androidx.core.app.NotificationCompat
import com.ascendcorp.androidtechpoc.R
import com.ascendcorp.androidtechpoc.extensions.logD
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.net.SocketException
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class MyVpnService : VpnService() {

    private var vpnThread: Thread? = null
    private var vpnInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Check if the service is started with a specific action
        intent?.let {
            when (it.action) {
                ACTION_STOP -> {
                    stopVpn()
                    return START_NOT_STICKY
                }

                ACTION_START -> {
                    startForeground()
                    startConnection()
                }
            }
        }
        return START_STICKY
    }

    private fun startConnection() {
        // Replace any existing connecting thread with the  new one.
        vpnThread = Thread {
            startVpn()
        }
        vpnThread?.start()
    }

    private fun startForeground() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, getNotification())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as? NotificationManager
            val channel = NotificationChannel(CHANNEL_ID, "VPN Channel", NotificationManager.IMPORTANCE_MAX)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun getNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("VPN Service")
            .setContentText("VPN service is running")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .addAction(
                R.drawable.ic_launcher_foreground,
                "Stop VPN",
                PendingIntent.getService(
                    this,
                    0,
                    Intent(this, MyVpnService::class.java)
                        .apply { action = ACTION_STOP },
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()
    }

    private fun startVpn() {
        try {
            logD("Starting")

            // If anything needs to be obtained using the network, get it now.
            // This greatly reduces the complexity of seamless handover, which
            // tries to recreate the tunnel without shutting down everything.
            // In this demo, all we need to know is the server address.
            val serverAddress: SocketAddress = InetSocketAddress(ADDRESS, ADDRESS_PORT)

            // We try to create the tunnel several times.
            // TODO: The better way is to work with ConnectivityManager, trying only when the
            // network is available.
            // Here we just use a counter to keep things simple.
            var attempt = 0
            while (attempt < 10) {
                // Reset the counter if we were connected.
                if (run(serverAddress)) {
                    attempt = 0
                }

                // Sleep for a while. This also checks if we got interrupted.
                Thread.sleep(3000)
                ++attempt
            }
            logD("Giving up")
        } catch (e: IOException) {
            logD("Connection failed, exiting $e")
        } catch (e: InterruptedException) {
            logD("Connection failed, exiting $e")
        } catch (e: java.lang.IllegalArgumentException) {
            logD("Connection failed, exiting $e")
        }
    }

    @Throws(IOException::class, InterruptedException::class, java.lang.IllegalArgumentException::class)
    private fun run(server: SocketAddress): Boolean {
        var iface: ParcelFileDescriptor? = null
        var connected = false
        // Create a DatagramChannel as the VPN tunnel.
        try {
            DatagramChannel.open().use { tunnel ->

                // Protect the tunnel before connecting to avoid loopback.
                check(this.protect(tunnel.socket())) { "Cannot protect the tunnel" }
                logD("run1")

                // Connect to the server.
                tunnel.connect(server)
                logD("run2")

                // For simplicity, we use the same thread for both reading and
                // writing. Here we put the tunnel into non-blocking mode.
                tunnel.configureBlocking(false)
                logD("run3")

                // Authenticate and configure the virtual network interface.
                iface = handshake(tunnel)
                logD("run4")

                // Now we are connected. Set the flag.
                connected = true

                // Packets to be sent are queued in this input stream.
                val `in` = FileInputStream(iface!!.fileDescriptor)
                logD("run5")

                // Packets received need to be written to this output stream.
                val out = FileOutputStream(iface!!.fileDescriptor)
                logD("run6")

                // Allocate the buffer for a single packet.
                val packet = ByteBuffer.allocate(MAX_PACKET_SIZE)
                logD("run7")

                // Timeouts:
                //   - when data has not been sent in a while, send empty keepalive messages.
                //   - when data has not been received in a while, assume the connection is broken.
                var lastSendTime = System.currentTimeMillis()
                var lastReceiveTime = System.currentTimeMillis()

                // We keep forwarding packets till something goes wrong.
                while (true) {
                    // Assume that we did not make any progress in this iteration.
                    var idle = true

                    // Read the outgoing packet from the input stream.
                    var length = `in`.read(packet.array())
                    if (length > 0) {
                        // Write the outgoing packet to the tunnel.
                        packet.limit(length)
                        tunnel.write(packet)
                        packet.clear()

                        // There might be more outgoing packets.
                        idle = false
                        lastReceiveTime = System.currentTimeMillis()
                    }

                    // Read the incoming packet from the tunnel.
                    length = tunnel.read(packet)
                    if (length > 0) {
                        // Ignore control messages, which start with zero.
                        if (packet[0].toInt() != 0) {
                            // Write the incoming packet to the output stream.
                            out.write(packet.array(), 0, length)
                        }
                        packet.clear()

                        // There might be more incoming packets.
                        idle = false
                        lastSendTime = System.currentTimeMillis()
                    }

                    // If we are idle or waiting for the network, sleep for a
                    // fraction of time to avoid busy looping.
                    if (idle) {
                        Thread.sleep(IDLE_INTERVAL_MS)
                        val timeNow = System.currentTimeMillis()
                        if (lastSendTime + KEEPALIVE_INTERVAL_MS <= timeNow) {
                            // We are receiving for a long time but not sending.
                            // Send empty control messages.
                            packet.put(0.toByte()).limit(1)
                            for (i in 0..2) {
                                packet.position(0)
                                tunnel.write(packet)
                            }
                            packet.clear()
                            lastSendTime = timeNow
                        } else {
                            check(RECEIVE_TIMEOUT_MS > timeNow) { "Timed out" }
                        }
                    }
                }
            }
        } catch (e: SocketException) {
            logD("Cannot use socket $e")
        } finally {
            if (iface != null) {
                try {
                    iface!!.close()
                } catch (e: IOException) {
                    logD("Unable to close interface $e")
                }
            }
        }
        return connected
    }

    private val sharedSecret: ByteArray? = null

    @Throws(IOException::class, InterruptedException::class)
    private fun handshake(tunnel: DatagramChannel): ParcelFileDescriptor? {
        // To build a secured tunnel, we should perform mutual authentication
        // and exchange session keys for encryption. To keep things simple in
        // this demo, we just send the shared secret in plaintext and wait
        // for the server to send the parameters.

        // Allocate the buffer for handshaking. We have a hardcoded maximum
        // handshake size of 1024 bytes, which should be enough for demo
        // purposes.
        val packet = ByteBuffer.allocate(1024)
        logD("handshake1")

        // Control messages always start with zero.
        if (sharedSecret != null) {
            packet.put(0.toByte()).put(sharedSecret).flip()
            logD("handshake2")
        }

        // Send the secret several times in case of packet loss.
        for (i in 0..2) {
            packet.position(0)
            tunnel.write(packet)
            logD("handshake3-$i")
        }
        packet.clear()

        // Wait for the parameters within a limited time.
        for (i in 0 until MAX_HANDSHAKE_ATTEMPTS) {
            Thread.sleep(IDLE_INTERVAL_MS)

            // Normally we should not receive random packets. Check that the first
            // byte is 0 as expected.
            val length = tunnel.read(packet)
            logD("handshake4-$length-${packet[0]}")
            if (length > 0 && packet[0].toInt() == 0) {
                logD("handshake5-$i")
                return configure(/*String(packet.array(), 1, length - 1, StandardCharsets.US_ASCII).trim { it <= ' ' }*/)
            }
        }
        logD("handshake6")
        throw IOException("Timed out")
    }

    @Throws(IllegalArgumentException::class)
    private fun configure(/*parameters: String*/): ParcelFileDescriptor? {
        // Configure a builder while parsing the parameters.
        val builder = Builder()
        /*for (parameter in parameters.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            val fields = parameter.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            try {
                when (fields[0][0]) {
                    'm' -> builder.setMtu(fields[1].toShort().toInt())
                    'a' -> builder.addAddress(fields[1], fields[2].toInt())
                    'r' -> builder.addRoute(fields[1], fields[2].toInt())
                    'd' -> builder.addDnsServer(fields[1])
                    's' -> builder.addSearchDomain(fields[1])
                }
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException("Bad parameter: $parameter")
            }
        }*/
        builder.addAddress(ADDRESS, ADDRESS_SUBNET_MASK)
        builder.addRoute(ROUTE, ROUTE_SUBMASK)

        // Create a new interface using the builder and save the parameters.
//        val vpnInterface: ParcelFileDescriptor?
//        for (packageName in mPackages) {
//            try {
//                if (mAllow) {
//                    builder.addAllowedApplication(packageName)
//                } else {
//                    builder.addDisallowedApplication(packageName)
//                }
//            } catch (e: PackageManager.NameNotFoundException) {
//                Log.w(getTag(), "Package not available: $packageName", e)
//            }
//        }
        builder.setSession(SERVER_NAME)/*.setConfigureIntent(mConfigureIntent)*/
        /*if (!TextUtils.isEmpty(mProxyHostName)) {
            builder.setHttpProxy(ProxyInfo.buildDirectProxy(mProxyHostName, mProxyHostPort))
        }*/
        synchronized(this) {
            vpnInterface = builder.establish()
//            if (mOnEstablishListener != null) {
//                mOnEstablishListener.onEstablish(vpnInterface)
//            }
        }
        logD("New interface: $vpnInterface ($/*parameters*/)")
        return vpnInterface
    }

    private fun stopVpn() {
        vpnThread?.interrupt()
        vpnInterface?.close()
        stopSelf()
    }

    companion object {
        private const val NOTIFICATION_ID = 123
        private const val CHANNEL_ID = "VPN_CHANNEL_ID"

        private const val SERVER_NAME = "SERVER_NAME"
        private const val ADDRESS = "10.0.0.2"
        private const val ADDRESS_PORT = 443
        private const val ADDRESS_SUBNET_MASK = 32
        private const val ROUTE = "0.0.0.0"
        private const val ROUTE_SUBMASK = 0

        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"

        /**
         * Number of periods of length {@IDLE_INTERVAL_MS} to wait before declaring the handshake a
         * complete and abject failure.
         *
         * TODO: use a higher-level protocol; hand-rolling is a fun but pointless exercise.
         */
        private const val MAX_HANDSHAKE_ATTEMPTS = 50

        /**
         * Time between polling the VPN interface for new traffic, since it's non-blocking.
         *
         * TODO: really don't do this; a blocking read on another thread is much cleaner.
         */
        private val IDLE_INTERVAL_MS = TimeUnit.MILLISECONDS.toMillis(100)

        /** Maximum packet size is constrained by the MTU, which is given as a signed short.  */
        private const val MAX_PACKET_SIZE = Short.MAX_VALUE.toInt()

        /** Time between keepalives if there is no traffic at the moment.
         *
         * TODO: don't do this; it's much better to let the connection die and then reconnect when
         * necessary instead of keeping the network hardware up for hours on end in between.
         */
        private val KEEPALIVE_INTERVAL_MS = TimeUnit.SECONDS.toMillis(15)

        /** Time to wait without receiving any response before assuming the server is gone.  */
        private val RECEIVE_TIMEOUT_MS = TimeUnit.SECONDS.toMillis(20)
    }
}
