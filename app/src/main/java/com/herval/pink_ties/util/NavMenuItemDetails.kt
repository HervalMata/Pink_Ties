package com.herval.pink_ties.util

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import com.herval.pink_ties.domain.NavMenuItem
import java.text.ParsePosition

class NavMenuItemDetails(var item: NavMenuItem? = null, var adapterPosition: Int = -1): ItemDetailsLookup.ItemDetails<Long>() {
    override fun getPosition() = adapterPosition

    override fun getSelectionKey() = item!!.id

    override fun inSelectionHotspot(e: MotionEvent) = true
}