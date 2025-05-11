package com.unibo.pazzagliacasadei.uniboard

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer

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
        MapLibre.getInstance(this, null, WellKnownTileServer.MapLibre)
    }
}