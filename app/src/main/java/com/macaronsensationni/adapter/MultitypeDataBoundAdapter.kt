package com.macaronsensationni.adapter

import androidx.databinding.ViewDataBinding
import com.macaronsensationni.macaroniapp.BaseDataBoundAdapter
import com.macaronsensationni.macaroniapp.DataboundViewHolder

abstract class MultitypeDataBoundAdapter(
    private val dataResId: Int,
    items: MutableList<Any?>? = mutableListOf()
): BaseDataBoundAdapter<ViewDataBinding>() {

    val items  = items ?: mutableListOf()
    val isEmpty get() = this.items.isEmpty()

    override fun bindItem(
        holder: DataboundViewHolder<ViewDataBinding>?,
        position: Int,
        payloads: List<Any?>?
    ) {
        holder?.binding?.setVariable(dataResId, items[position])
    }

    override fun getItemCount() = items.size

    fun getItem(position: Int): Any? {
        if (position < 0 || position >= items.size) return null
        return items[position]
    }

    fun getItemPosition(item: Any?): Int {
        if (!items.contains(item)) return -1
        items.forEachIndexed { position, _ ->
            if (items[position] == true) return position
        }
        return -1
    }

    fun addItem(position: Int, item: Any?) {
        items.add(position, item)
        notifyItemInserted(position)
    }

    fun removeItemAt(position: Int) {
        if (items.size <= position || position < 0) return
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun replaceItem(position: Int, item: Any?) {
        if (items.size <= position) return
        items[position] = item
        notifyItemChanged(position)
    }

    fun setItem(listItems: MutableList<Any>?) {
        items.clear()
        if (listItems != null) {
            items.addAll(listItems)
        }
        notifyDataSetChanged()
    }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        if (position < 0 || position >= items.size) return
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}