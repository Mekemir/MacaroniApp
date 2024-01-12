package com.game.macaroniapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.OnRebindCallback
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseDataBoundAdapter<T : ViewDataBinding> : RecyclerView.Adapter<DataboundViewHolder<T>>() {

    private var recyclerView: RecyclerView? = null

    private val mOnRebindCallback: OnRebindCallback<*> =
        object : OnRebindCallback<ViewDataBinding?>() {
            override fun onPreBind(binding: ViewDataBinding?): Boolean {
                if (recyclerView == null || recyclerView?.isComputingLayout == true) {
                    return true
                }

                val childAdapterPosition = binding?.root?.let {
                    recyclerView?.getChildAdapterPosition(it)
                } ?: 0

                if (childAdapterPosition == RecyclerView.NO_POSITION) return true

                recyclerView?.post{
                    notifyItemChanged(childAdapterPosition, Any())
                }

                return false
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataboundViewHolder<T> {
        val vh: DataboundViewHolder<T> = DataboundViewHolder.create(parent, viewType)
        vh.binding?.addOnRebindCallback(mOnRebindCallback)
        return vh
    }

    override fun onBindViewHolder(
        holder: DataboundViewHolder<T>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty() ||  hasNonDataBindingInvalidate(payloads))  {
            bindItem(holder, position, payloads)
        }
        holder.binding.executePendingBindings()
    }

    private fun hasNonDataBindingInvalidate(payloads: List<Any>): Boolean {
        payloads.forEach {
            if (it != Any()) {
                return true
            }
        }
        return true
    }

    protected abstract fun bindItem(
        holder: DataboundViewHolder<T>?,
        position: Int,
        payloads: List<Any?>?
    )

    override fun onBindViewHolder(holder: DataboundViewHolder<T>, position: Int) {
        throw java.lang.IllegalArgumentException("error")
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = null
    }

    override fun getItemViewType(position: Int): Int {
        return getItemLayoutId(position)
    }

    abstract fun getItemLayoutId(position: Int): Int

}

class DataboundViewHolder<T: ViewDataBinding?>(val binding: T) : RecyclerView.ViewHolder(
    binding?.root ?: throw NullPointerException("binding is null")
) {
    companion object {
        fun <T : ViewDataBinding?> create(
            parent: ViewGroup,
            @LayoutRes layoutId: Int
        ): DataboundViewHolder<T> {
            val binding: T = DataBindingUtil.inflate<T>(
                LayoutInflater.from(parent.context),
                layoutId,
                parent,
                false
            )
            return DataboundViewHolder(binding)
        }
    }
}