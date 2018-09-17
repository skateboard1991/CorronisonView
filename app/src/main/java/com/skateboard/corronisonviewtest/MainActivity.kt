package com.skateboard.corronisonviewtest

import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        corronisonView.setBitmap(BitmapFactory.decodeResource(resources,R.drawable.icon))
    }

    override fun onResume()
    {
        super.onResume()
        corronisonView.onResume()
    }

    override fun onPause()
    {
        super.onPause()
        corronisonView.onPause()
    }
}
