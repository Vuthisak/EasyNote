package com.example.easynote.util

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun <T> Task<T>.await(): T {
    if (isComplete) {
        val ex = exception
        return if (ex == null) {
            if (isCanceled) {
                throw CancellationException("$this was cancelled.")
            } else {
                result
            }
        } else {
            throw ex
        }
    }
    return suspendCancellableCoroutine { con ->
        addOnCompleteListener {
            val ex = exception
            if (ex == null) {
                if (isCanceled) con.cancel() else con.resume(result)
            } else {
                con.resumeWithException(ex)
            }
        }
    }
}
