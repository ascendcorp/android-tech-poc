package com.ascendcorp.androidtechpoc.screen.navgraph.home

import android.content.Intent
import android.net.Uri
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
        binding.bAction.setOnClickListener {
            val url = args.displayText
            val intent = if (url.startsWith("intent://")) {
                Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
            } else {
                Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        }
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
