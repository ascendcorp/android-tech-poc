package com.ascendcorp.androidtechpoc.screen.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ascendcorp.androidtechpoc.databinding.ItemTopicBinding

internal class TopicAdapter : RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    var topics = listOf<TopicUiModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = topics.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        return TopicViewHolder(
            ItemTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val item = topics[position]
        with(holder) {
            bindData(item)
            bindEvents(item)
        }
    }

    internal class TopicViewHolder(private val binding: ItemTopicBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(topicUiModel: TopicUiModel) {
            binding.tvTopicTitle.apply {
                text = context.getString(topicUiModel.titleRes)
            }
        }

        fun bindEvents(topicUiModel: TopicUiModel) {
            binding.root.setOnClickListener {
                topicUiModel.listener.invoke()
            }
        }
    }
}
