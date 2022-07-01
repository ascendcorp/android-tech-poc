package com.ascendcorp.androidtechpoc.screen.navgraph.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewbinding.ViewBinding
import com.ascendcorp.androidtechpoc.R

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupActionBar()
        return bindingInflater(inflater, container, false)
            .also { _binding = it }
            .root
    }

    private fun setupActionBar() {
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.setupWithNavController(findNavController())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        bindViewEvents()
    }

    open fun setupView() = Unit

    abstract fun bindViewEvents()

    open fun displayError(error: Throwable) {
        // TODO : display error message
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
