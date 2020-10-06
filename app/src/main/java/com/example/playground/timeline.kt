package com.example.playground

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main() {
    runSequential()
//    runParallel()

    // keep jvm alive
    Thread.sleep(5000L)
}

private fun runSequential() {
    GlobalScope.launch {
        println("This is executed before the first delay")
        stallForTime()
        println("This is executed after the first delay")

        println("This is executed before the second delay")
        stallForTime()
        println("This is executed after the second delay")
    }
    println("This is executed immediately")
}

private fun runParallel() {
    GlobalScope.launch {
        println("This is executed before the first delay")
        stallForTime()
        println("This is executed after the first delay")
    }
    GlobalScope.launch {
        println("This is executed before the second delay")
        stallForTime()
        println("This is executed after the second delay")
    }
    println("This is executed immediately")
}

private suspend fun stallForTime() {
    delay(2000L)
}