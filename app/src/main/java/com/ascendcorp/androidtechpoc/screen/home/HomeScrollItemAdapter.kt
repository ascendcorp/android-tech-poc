package com.ascendcorp.androidtechpoc.screen.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ascendcorp.androidtechpoc.databinding.ItemHomeScrollItemBinding

class HomeScrollItemAdapter(val itemWidth: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = listOf<HomeScrollItemUiModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(
            ItemHomeScrollItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bind(itemWidth, items[position])
        }
    }

    override fun getItemCount(): Int = items.size

    internal class ItemViewHolder(private val binding: ItemHomeScrollItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemWidth: Int, model: HomeScrollItemUiModel) {
            binding.tvTitle.apply {
                text = model.title
            }
            binding.rvContent.apply {
                adapter = HomeItemAdapter(itemWidth).apply {
                    items = model.items
                }
                layoutManager =
                    LinearLayoutManager(
                        binding.root.context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
            }
        }
    }
}
