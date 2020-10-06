package com.example.playground

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

fun main() {
    GlobalScope.launch(Dispatchers.Default) {
        println("This is executed before the delay on ${Thread.currentThread()}")
        stallForTime()
        println("This is executed after the delay on ${Thread.currentThread()}")
    }
    println("This is executed immediately on ${Thread.currentThread()}")

    // keep jvm alive
    Thread.sleep(3000L)
}

private suspend fun stallForTime() {
    // create e new pool
    withContext(Executors.newSingleThreadExecutor().asCoroutineDispatcher()) {
        delay(2000L)
        println("This is executed withContext on ${Thread.currentThread()}")
    }
}