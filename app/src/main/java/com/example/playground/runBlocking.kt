package com.example.playground

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() {
    println("This is executed immediately")

    runBlocking {
        stallForTime()
        println("This is executed after delay")
    }

    println("This is executed after runBlocking returns")

    // keep jvm alive
    Thread.sleep(3000L)
}

private suspend fun stallForTime() {
    delay(2000L)
}