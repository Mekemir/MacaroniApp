package com.macaroni.macaroniapp.adapter

import androidx.databinding.library.baseAdapters.BR
import com.macaroni.macaroniapp.R

class MacaroniAdapter: MultitypeDataBoundAdapter(BR.data) {

    override fun getItemLayoutId(position: Int): Int {
        return when (val item  =  getItem(position)) {
            is MacaroniItem -> R.layout.macaroni_item
            else ->  throw java.lang.IllegalArgumentException("Unknown item type $item")
        }
    }

}