package com.ascendcorp.androidtechpoc.screen.navgraph.home

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.ascendcorp.androidtechpoc.databinding.FragmentNavGraphHomeDBinding
import com.ascendcorp.androidtechpoc.screen.navgraph.base.BaseFragment
import kotlinx.parcelize.Parcelize

class NavGraphHomeDFragment : BaseFragment<FragmentNavGraphHomeDBinding>() {

    private val args: NavGraphHomeDFragmentArgs by navArgs()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNavGraphHomeDBinding
        get() = { layoutInflater, viewGroup, attachToParent ->
            FragmentNavGraphHomeDBinding.inflate(layoutInflater, viewGroup, attachToParent)
        }

    override fun bindViewEvents() {
        binding.tvDisplay.text = getDisplayText()
    }

    private fun getDisplayText(): String {
        return with(args) {
            bundle?.let {
                "$displayText\nNavigated from ${it.previousScreen} (${it.previousScreenId})"
            } ?: displayText
        }
    }
}

@Parcelize
data class NavGraphHomeDBundle(
    val previousScreenId: String,
    val previousScreen: String
) : Parcelable
