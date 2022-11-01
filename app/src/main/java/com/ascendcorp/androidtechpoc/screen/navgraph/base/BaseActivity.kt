package com.ascendcorp.androidtechpoc.screen.navgraph.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected abstract val bindingInflater: (LayoutInflater) -> VB

    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            bindingInflater.invoke(layoutInflater)
                .also { binding = it }
                .root
        )

        setupView()
    }

    open fun setupView() = Unit
}
