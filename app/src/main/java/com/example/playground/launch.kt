package com.example.playground

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main() {

    val job = GlobalScope.launch(start = CoroutineStart.LAZY) {
        println("This is executed before the 1 delay")
        stallForTime()
        println("This is executed after the 1 delay")
    }

    job.start()

    println("This is executed immediately")

    // keep jvm alive
    Thread.sleep(3000L)
}

private suspend fun stallForTime() {
    println("Thread is ${Thread.currentThread().name}")
    delay(2000L)
}
