package com.example.playground

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main() {
    GlobalScope.launch {
        val deferred = GlobalScope.async {
            delay(2000L)
            println("This is executed after delay")
            2020
        }

        println("This is executed after calling async()")

        val result = deferred.await()

        println("This is the year $result")
    }

    println("This is executed immediately")

    // keep jvm alive
    Thread.sleep(3000L)
}