package com.ascendcorp.androidtechpoc.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import androidx.core.app.NotificationCompat
import com.ascendcorp.androidtechpoc.R
import com.ascendcorp.androidtechpoc.extensions.logD
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.DatagramSocket
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel

class MyVpnService2 : VpnService() {

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
                    startVpn()
                }
            }
        }
        return START_STICKY
    }

    private fun startForeground() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, getNotification())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as? NotificationManager
            val channel = NotificationChannel(CHANNEL_ID, "VPN Channel", NotificationManager.IMPORTANCE_DEFAULT)
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
                    Intent(this, MyVpnService2::class.java)
                        .apply { action = ACTION_STOP },
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()
    }

    private fun startVpn() {
        vpnThread = Thread {
            try {
                DatagramChannel.open().use { tunnel ->
                    protect(tunnel.socket())
//                    tunnel.connect()

                    // Start your VPN logic here
                    establishVpnConnection()

                    // Your VPN logic should go here
                    while (true) {
                        // Read data from the VPN interface
                        val input = FileInputStream(vpnInterface?.fileDescriptor)
                        val buffer = ByteBuffer.allocate(4096)
                        val length = input.read(buffer.array())
                        if (length > 0) {
                            // Process the received data
                            logD("${vpnInterface?.detachFd()}")

                            // Write data back to the VPN interface (echo)
                        val output = FileOutputStream(vpnInterface?.fileDescriptor)
                        output.write(buffer.array(), 0, length)
                        output.close()
                        }
                        input.close()
                    }
                }



            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        vpnThread?.start()
    }

    private fun establishVpnConnection() {
        val builder = Builder()
        vpnInterface = builder.setSession("LocalVPNService")
            .addAddress(ADDRESS, ADDRESS_SUBNET_MASK)
//            .addRoute(ROUTE, ROUTE_SUBNET_MASK)
            .addRoute("172.67.178.182", 32)
            .addRoute("104.21.88.122", 32)
            .establish()
    }

    private fun stopVpn() {
        vpnThread?.interrupt()
        vpnInterface?.close()
        stopSelf()
    }

    companion object {
        private const val NOTIFICATION_ID = 123
        private const val CHANNEL_ID = "VPN_CHANNEL_ID"

        private const val ADDRESS = "10.0.0.2"
        private const val ADDRESS_SUBNET_MASK = 32
        private const val ROUTE = "0.0.0.0"
        private const val ROUTE_SUBNET_MASK = 0

        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}
