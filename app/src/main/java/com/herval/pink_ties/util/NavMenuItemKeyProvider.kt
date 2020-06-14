package com.herval.pink_ties.util

import androidx.recyclerview.selection.ItemKeyProvider
import com.herval.pink_ties.domain.NavMenuItem

class NavMenuItemKeyProvider(val items: List<NavMenuItem>) : ItemKeyProvider<Long>(ItemKeyProvider.SCOPE_MAPPED) {
    override fun getKey(position: Int) = items[position].id

    override fun getPosition(key: Long) = items.indexOf(
        items.filter { item-> item.id == key }.single())
}