package com.codingblocks.routerblocks.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class RouterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Router.resolveIntent(this, intent?.action.toString()).let {
            startActivity(it)
        }
    }
}
