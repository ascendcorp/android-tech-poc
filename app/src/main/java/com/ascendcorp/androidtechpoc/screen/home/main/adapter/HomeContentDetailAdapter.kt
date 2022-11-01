package com.ascendcorp.androidtechpoc.screen.home.main.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ascendcorp.androidtechpoc.databinding.ItemHomeContentDetailBinding
import com.ascendcorp.androidtechpoc.screen.home.main.HomeContentDetailUiModel
import com.ascendcorp.androidtechpoc.screen.home.main.adapter.HomeContentDetailAdapter.HomeContentDetailViewHolder
import kotlin.random.Random

internal class HomeContentDetailAdapter : RecyclerView.Adapter<HomeContentDetailViewHolder>() {

    internal var items = listOf<HomeContentDetailUiModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeContentDetailViewHolder {
        return HomeContentDetailViewHolder(
            ItemHomeContentDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HomeContentDetailViewHolder, position: Int) {
        val item = items[position]
        holder.bindData(item)
        holder.bindEvents(item)
    }

    internal class HomeContentDetailViewHolder(private val binding: ItemHomeContentDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(homeContentDetail: HomeContentDetailUiModel) {
            binding.tvDetailTitle.apply {
                text = homeContentDetail.title
                val randomRgb = { Random.nextInt(until = COLOR_RANGE) }
                setBackgroundColor(Color.argb(COLOR_ALPHA, randomRgb(), randomRgb(), randomRgb()))
            }
        }

        fun bindEvents(homeContentDetail: HomeContentDetailUiModel) {
            with(homeContentDetail) {
                binding.root.apply {
                    setOnClickListener {
                        Toast.makeText(context, title, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

private const val COLOR_RANGE = 255
private const val COLOR_ALPHA = 80
