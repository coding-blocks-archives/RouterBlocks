package com.codingblocks.routerblocks.router

import android.app.Activity
import android.content.Context
import android.content.Intent
import java.util.*

abstract class BaseRouter {
    val routes: HashMap<String, Class<out Activity>> = hashMapOf()

    fun resolveIntent(context: Context, path: String): Intent? {
        return if (routes.containsKey(path))
            Intent(context, routes[path])
        else
            null
    }
}