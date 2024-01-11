package com.macaroni.macaroniapp.adapter

import androidx.databinding.BaseObservable
import com.macaroni.macaroniapp.CookingItemData
import com.macaroni.macaroniapp.callback.MacaroniCallback

class MyDishesItem(
    val itemData: CookingItemData,
    val callback: MacaroniCallback
): BaseObservable() {
}