package com.unibo.pazzagliacasadei.uniboard

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class UniBoardApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@UniBoardApplication)
            modules(
                supabaseModule, modules
            )
        }
    }
}