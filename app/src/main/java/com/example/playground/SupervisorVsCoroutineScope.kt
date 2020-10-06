package com.example.playground

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.supervisorScope
import java.lang.Exception

/**
    compute() fetches two things from the network and combines them into a string description.
    In this case the first fetch is taking a long time, but succeeds in the end; the second one fails almost right away, after 100 milliseconds.

    What behavior would you like for the above code?

        1.Would you like to color.await() for a minute, only to realize that the other network call has long failed?

        2.Or perhaps you'd like the compute() function to realize after 100 ms that one of its network calls has failed and immediately fail itself?

    With supervisorScope you're getting 1., with coroutineScope you're getting 2.

    The behavior of 2. means that, even though async doesn't itself throw the exception (it just completes the Deferred you got from it),
    the failure immediately cancels its coroutine, which cancels the parent, which then cancels all the other children.

    This behavior can be weird when you're unaware of it. If you go and catch the exception from await(), you'll think you've recovered from it, but you haven't.
    The entire coroutine scope is still being cancelled. In some cases there's a legitimate reason you don't want it: that's when you'll use supervisorScope.
 */

suspend fun main() {
    println(computeSupervisor())
}

suspend fun computeCoroutine(): String = coroutineScope {
    val color = async { delay(60_000); "purple" }
    val height = async<Double> { delay(100); throw Exception() }
    "A %s box %.1f inches tall".format(color.await(), height.await())
}

suspend fun computeSupervisor(): String = supervisorScope {
    val color = async { delay(60_000); "purple" }
    val height = async<Double> { delay(100); throw Exception() }
    "A %s box %.1f inches tall".format(color.await(), height.await())
}