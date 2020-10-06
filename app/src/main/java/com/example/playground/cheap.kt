package com.example.playground

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun main() {
    for (i in 0..100) {
        GlobalScope.launch {
            println("This is executed before delay $i")
            stallForTime()
            println("This is executed after delay $i")
        }
    }

    // keep jvm alive
    Thread.sleep(5000L)
}

private suspend fun stallForTime() {
    withContext(Dispatchers.Default) {
        delay(2000L)
    }
}