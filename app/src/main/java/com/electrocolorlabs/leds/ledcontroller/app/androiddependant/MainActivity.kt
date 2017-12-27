package com.electrocolorlabs.leds.ledcontroller.app.androiddependant

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.electrocolorlabs.leds.ledcontroller.app.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
