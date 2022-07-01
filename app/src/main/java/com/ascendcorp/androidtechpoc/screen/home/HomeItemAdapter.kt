package com.ascendcorp.androidtechpoc.screen.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.ascendcorp.androidtechpoc.databinding.ItemHomeItemBinding

class HomeItemAdapter(private val itemWidth: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = listOf<HomeItemUiModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(ItemHomeItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bind(itemWidth, items[position])
        }
    }

    override fun getItemCount(): Int = items.size

    internal class ItemViewHolder(private val binding: ItemHomeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemWidth: Int, model: HomeItemUiModel) {
            binding.root.post {
                binding.root.updateLayoutParams {
                    width = itemWidth
                }
            }
            binding.tvTitle.apply {
                text = model.title
            }
            binding.ivIcon.apply {
                setImageDrawable(context.resources.getDrawable(model.imageRes, context.theme))
            }
            binding.root.setOnClickListener {
                model.itemClick.invoke()
            }
        }
    }
}
