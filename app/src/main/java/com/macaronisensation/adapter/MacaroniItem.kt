package com.macaronisensation.adapter

import androidx.databinding.BaseObservable
import com.macaronisensation.base.CookingItemData
import com.macaronisensation.callback.MacaroniCallback

class MacaroniItem(
    val itemData: CookingItemData,
    val callback: MacaroniCallback,
    val lockingVisibility: Int
): BaseObservable() {

}