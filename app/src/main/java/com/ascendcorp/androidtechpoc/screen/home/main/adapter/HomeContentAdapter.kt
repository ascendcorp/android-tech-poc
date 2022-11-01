package com.ascendcorp.androidtechpoc.screen.home.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ascendcorp.androidtechpoc.databinding.ItemHomeContentBinding
import com.ascendcorp.androidtechpoc.screen.home.main.HomeContentUiModel
import com.ascendcorp.androidtechpoc.screen.home.main.adapter.HomeContentAdapter.HomeContentViewHolder

internal class HomeContentAdapter : RecyclerView.Adapter<HomeContentViewHolder>() {

    internal var items = listOf<HomeContentUiModel>()
        set(value) {
            field = value
            notifyDataSetChanged()

            lastHorizontalScrollOffset = List(value.size) { 0 }
        }

    private lateinit var lastHorizontalScrollOffset: List<Int>

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeContentViewHolder {
        return HomeContentViewHolder(
            ItemHomeContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HomeContentViewHolder, position: Int) {
        val item = items[position]
        holder.bindData(item, lastHorizontalScrollOffset[position])
    }

    override fun onViewRecycled(holder: HomeContentViewHolder) {
        lastHorizontalScrollOffset = holder.updateHorizontalScrollOffset(lastHorizontalScrollOffset)
    }

    internal class HomeContentViewHolder(private val binding: ItemHomeContentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(
            homeContents: HomeContentUiModel,
            lastHorizontalScrollOffset: Int
        ) {
            with(binding) {
                tvContentTitle.text = homeContents.title
                rvContent.apply {
                    adapter = HomeContentDetailAdapter()
                        .apply { items = homeContents.homeContentDetails }
                    layoutManager = getLayoutManager(context)

                    doOnPreDraw { scrollBy(lastHorizontalScrollOffset, 0) }
                }
            }
        }

        fun updateHorizontalScrollOffset(horizontalScrollOffsets: List<Int>): List<Int> {
            return horizontalScrollOffsets.mapIndexed { index, value ->
                if (index == bindingAdapterPosition) {
                    binding.rvContent.computeHorizontalScrollOffset()
                } else {
                    value
                }
            }
        }

        private fun getLayoutManager(context: Context): RecyclerView.LayoutManager {
            return if (bindingAdapterPosition % 2 != 0) {
                LinearLayoutManager(context)
            } else {
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
        }
    }
}
