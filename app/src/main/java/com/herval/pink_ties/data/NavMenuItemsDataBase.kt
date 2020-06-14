package com.herval.pink_ties.data

import com.herval.pink_ties.R
import com.herval.pink_ties.domain.NavMenuItem
import android.content.Context

class NavMenuItemsDataBase(context: Context) {
    val items = listOf(
        NavMenuItem(
            R.id.item_all_pinks.toLong(),
            context.getString(R.string.item_all_pinks),
            R.drawable.ic_bow_tie_black_24dp),
        NavMenuItem(R.id.item_all_tiaras.toLong(),
            context.getString(R.string.item_all_tiaras),
            R.drawable.ic_crown_black_24dp),
        NavMenuItem(R.id.item_viseiras.toLong(),
            context.getString(R.string.item_viseiras)),
        NavMenuItem(R.id.item_sandals.toLong(),
            context.getString(R.string.item_sandals)),
        NavMenuItem(R.id.item_contact.toLong(),
            context.getString(R.string.item_contact),
            R.drawable.ic_email_black_24dp),
        NavMenuItem(R.id.item_about.toLong(),
            context.getString(R.string.item_about),
            R.drawable.ic_domain_black_24dp),
        NavMenuItem(R.id.item_privacy_policy.toLong(),
            context.getString(R.string.item_privacy_policy),
            R.drawable.ic_shield_lock_black_24dp)
    )

    val itemsLogged = listOf(
        NavMenuItem(R.id.item_my_orders.toLong(),
            context.getString(R.string.item_my_orders),
            R.drawable.ic_package_variant_closed_black_24dp),
        NavMenuItem(R.id.item_settings.toLong(),
            context.getString(R.string.item_settings),
            R.drawable.ic_settings_black_24dp),
        NavMenuItem(R.id.item_sign_out.toLong(),
            context.getString(R.string.item_sign_out),
            R.drawable.ic_exit_run_black_24dp)
    )

    fun getLastItemId() = items.last().id
    fun getFirstItemLoggedId() = itemsLogged.first().id
}