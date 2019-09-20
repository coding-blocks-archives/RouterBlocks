package com.codingblocks.routerblocks.sample

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codingblocks.routerblocks.annotations.Route

@Route("a")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}
