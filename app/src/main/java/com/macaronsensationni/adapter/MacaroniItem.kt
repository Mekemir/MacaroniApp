package com.macaronsensationni.adapter

import androidx.databinding.BaseObservable
import com.macaronsensationni.macaroniapp.CookingItemData
import com.macaronsensationni.macaroniapp.callback.MacaroniCallback

class MacaroniItem(
    val itemData: CookingItemData,
    val callback: MacaroniCallback,
    val lockingVisibility: Int
): BaseObservable() {

}