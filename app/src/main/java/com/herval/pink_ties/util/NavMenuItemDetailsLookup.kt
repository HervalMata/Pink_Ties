package com.herval.pink_ties.util

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.herval.pink_ties.view.NavMenuItemsAdapter

class NavMenuItemDetailsLookup(val rvMenuItems: RecyclerView): ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = rvMenuItems.findChildViewUnder(event.x, event.y)
        if (view != null){
            val holder = rvMenuItems.getChildViewHolder(view)
            return (holder as NavMenuItemsAdapter.ViewHolder).itemDetails
        }
        return null
    }

}