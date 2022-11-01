package com.ascendcorp.androidtechpoc.screen.home.main

import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ascendcorp.androidtechpoc.R
import com.ascendcorp.androidtechpoc.screen.home.main.HomeTab.A
import com.ascendcorp.androidtechpoc.screen.home.main.HomeTab.B
import com.ascendcorp.androidtechpoc.screen.home.main.HomeTab.C
import com.ascendcorp.androidtechpoc.screen.home.main.HomeTab.D
import com.ascendcorp.androidtechpoc.screen.home.main.HomeTab.values
import com.ascendcorp.androidtechpoc.screen.home.main.tab.HomeAFragment
import com.ascendcorp.androidtechpoc.screen.home.main.tab.HomeBFragment
import com.ascendcorp.androidtechpoc.screen.home.main.tab.HomeCFragment
import com.ascendcorp.androidtechpoc.screen.home.main.tab.HomeDFragment

internal class HomePagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = values().size

    override fun createFragment(position: Int): Fragment {
        return when (HomeTab.values().getOrNull(position)) {
            A -> HomeAFragment()
            B -> HomeBFragment()
            C -> HomeCFragment()
            D -> HomeDFragment()
            else -> throw IllegalArgumentException("Position exceeds ${values().size - 1}")
        }
    }
}

internal enum class HomeTab(val titleRes: Int, @DrawableRes val iconRes: Int) {
    A(R.string.home_tab_a, R.drawable.ic_home_a),
    B(R.string.home_tab_b, R.drawable.ic_home_b),
    C(R.string.home_tab_c, R.drawable.ic_home_c),
    D(R.string.home_tab_d, R.drawable.ic_home_d);

    companion object {

        fun getTitleAndIcon(position: Int): Pair<Int?, Int?> {
            return with(values().getOrNull(position)) { this?.titleRes to this?.iconRes }
        }
    }
}

internal const val CURRENT_VIEW_PAGER_ITEM_KEY = "CURRENT_VIEW_PAGER_ITEM_KEY"
