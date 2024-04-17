package com.ascendcorp.androidtechpoc.screen.vpn

import android.content.Intent
import android.net.VpnService
import android.view.LayoutInflater
import com.ascendcorp.androidtechpoc.databinding.ActivityVpnBinding
import com.ascendcorp.androidtechpoc.screen.navgraph.base.BaseActivity
import com.ascendcorp.androidtechpoc.service.MyVpnService
import com.ascendcorp.androidtechpoc.service.MyVpnService2

class VpnActivity : BaseActivity<ActivityVpnBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityVpnBinding
        get() = { layoutInflater ->
            ActivityVpnBinding.inflate(layoutInflater)
        }

    private val vpnService = MyVpnService2::class.java

    override fun setupView() {
        binding.bStart.setOnClickListener {
            val result = VpnService.prepare(this)
            if (result != null) {
                startActivityForResult(result, 1)
            } else {
                startService()
            }
        }
        binding.bStop.setOnClickListener {
            stopService()
        }
    }

    private fun startService() {
        val intent = Intent(this, vpnService)
            .apply { action = MyVpnService.ACTION_START }
        startService(intent)
    }

    private fun stopService() {
        val intent = Intent(this, vpnService)
            .apply { action = MyVpnService.ACTION_STOP }
        startService(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                startService()
            }
        }
    }
}
