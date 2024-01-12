package com.game.macaroniapp.adapter

import androidx.databinding.BaseObservable
import com.game.macaroniapp.CookingItemData
import com.game.macaroniapp.callback.MacaroniCallback

class MacaroniItem(
    val itemData: CookingItemData,
    val callback: MacaroniCallback,
    val lockingVisibility: Int
): BaseObservable() {

}