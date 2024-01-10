package com.macaroni.macaroniapp.adapter

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.databinding.BaseObservable
import com.macaroni.macaroniapp.CookingItemData
import com.macaroni.macaroniapp.callback.MacaroniCallback

class MacaroniItem(
    val itemData: CookingItemData,
    val callback: MacaroniCallback
): BaseObservable() {
}