package com.example.playground

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.util.concurrent.Executors

fun main() {
    cancelWithTimeout()

    // keep jvm alive
    Thread.sleep(3000L)
}

private suspend fun stallForTime() {
    withContext(Dispatchers.Default) {
        println("I'm executed inside stall")
        delay(2000L)
        println("I'm executed inside stall after delay")
    }
}

private suspend fun stallForTimeWithParent(parent: Job) {
    withContext(Dispatchers.Default) {
        println("Job inside withContext() context: ${coroutineContext[Job]}")
        println("Parent job children: ${parent.children.joinToString()}")
        delay(2000L)
    }
}

private fun createLazyJob() {
    val job = GlobalScope.launch(Dispatchers.Default, start = CoroutineStart.LAZY) {
        stallForTime()
        println("This is executed after the delay")
    }
    println("This is executed immediately")

//    job.start()
//    println("This is executed after starting the job")
}

private fun joinJob() {
    GlobalScope.launch(Dispatchers.Default) {
        val job = GlobalScope.launch(Dispatchers.Default) {
            delay(2000L)
            println("This is executed after the delay")
        }
        println("This is executed after calling launch()")
        job.join()
        println("This is executed after join()")
    }
    println("This is executed immediately")
}

private fun cancelJob() {

    val executor = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    val job = GlobalScope.launch(executor) {
        println("This is executed before the first delay")
        println("This is executed before the first delay")
        stallForTime()
        println("This is executed before the first delay")
        println("This is executed before the first delay")
        println("This is executed before the first delay")
        stallForTime()
        println("This is executed after the first delay")
    }
    GlobalScope.launch(executor) {
        println("This is executed before the second delay")
        job.cancel()
        stallForTime()
        println("This is executed after the second delay")
    }
    println("This is executed immediately")
}

private fun cancelAndJoinJob() {
    val job = GlobalScope.launch {
        println("This is executed before the first delay")
        stallForTime()
        println("This is executed after the first delay")
    }
    GlobalScope.launch {
        println("This is executed at the start of the second coroutine")
        job.cancelAndJoin()
        println("This is executed before the second delay")
        stallForTime()
        println("This is executed after the second delay")
    }
    println("This is executed immediately")
}

private fun cancelParentJob() {
    val job = GlobalScope.launch {
        println("This is executed before the delay")
        stallForCancelTime(coroutineContext[Job]!!)
        println("This is executed after the delay")
    }
    println("This is executed immediately")
}

private suspend fun stallForCancelTime(parent: Job) {
    withContext(Dispatchers.Default) {
        println("This is executed at the start of the child job")
        parent.cancel()
        println("This is executed after canceling the parent")
        delay(2000L)
        println("This is executed at the end of the child job")
    }
}

private fun cancelWithException() {
    val job = GlobalScope.launch {
        println("This is executed before the first delay")
        stallForTime()
        println("This is executed after the first delay")
    }
    GlobalScope.launch {
        println("This is executed before the second delay")
        var thisIsReallyNull: String? = null
        println("This will result in a NullPointerException: ${thisIsReallyNull!!.length}")
        stallForTime()
        println("This is executed after the second delay")
    }
    println("This is executed immediately")
}

private fun cancelWithExceptionChild() {
    val job = GlobalScope.launch {
        println("This is executed before the delay")
        cancelWithExceptionStall()
        println("This is executed after the delay")
    }
    println("This is executed immediately")
}

private suspend fun cancelWithExceptionStall() {
    withContext(Dispatchers.Default) {
        println("This is executed at the start of the child job")
        var thisIsReallyNull: String? = null
        println("This will result in a NullPointerException: ${thisIsReallyNull!!.length}")
        delay(2000L)
        println("This is executed at the end of the child job")
    }
}

private fun cancelWithTimeout() {
    GlobalScope.launch {
        try {
            withTimeout(1000L) {
                println("This is executed before the delay")
                stallForTime()
                println("This is executed after the delay")
            }

        } catch (t: TimeoutCancellationException) {
            println("We got a timeout error")
        }

        println("This is printed after the timeout")
    }
    println("This is executed immediately")
}

private fun uncancelableJob() {
    val job = GlobalScope.launch {
        try {
            println("This is executed before the first delay")
            stallForTime()
            println("This is executed after the first delay")
        } finally {
            withContext(NonCancellable) {
                println("This is executed before the finally block delay")
                stallForTime()
                println("This is executed after the finally block delay")
            }
        }
    }

    GlobalScope.launch {
        println("This is executed at the start of the second coroutine")
        job.cancelAndJoin()
        println("This is executed before the second delay")
        stallForTime()
        println("This is executed after the second delay")
    }

    println("This is executed immediately")
}