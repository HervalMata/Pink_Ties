package com.herval.pink_ties.domain

class NavMenuItem(val id: Long, val label: String, val iconId: Int = DEFAULT_ICON_ID) {
    companion object {
        const val DEFAULT_ICON_ID = -1
    }
}