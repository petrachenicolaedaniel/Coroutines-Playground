package com.example.playground

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

class MainActivity: AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val job = GlobalScope.launch(Dispatchers.Main) {
            Log.d(TAG, "This is executed before the delay")
            stallForTime()
            Log.d(TAG, "This is executed after the delay")
        }

        GlobalScope.launch(Dispatchers.Main) {
            Log.d(TAG, "This is executed before the delay")
            stallForTime()
            Log.d(TAG, "This is executed after the delay")
        }

        GlobalScope.launch(Dispatchers.Main) {
            Log.d(TAG, "This is executed before the delay")
            stallForTime()
            Log.d(TAG, "This is executed after the delay")
        }

        Log.d(TAG,"This is executed immediately")
    }
}

private suspend fun stallForTime() {
    withContext(Executors.newSingleThreadExecutor().asCoroutineDispatcher()) {
        delay(2000L)
    }
}