package com.unibo.pazzagliacasadei.uniboard

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import org.koin.dsl.module

val supabaseModule = module {
    single {
        createSupabaseClient(
            supabaseUrl = "https://zeexxdpiintigxuglcwt.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InplZXh4ZHBpaW50aWd4dWdsY3d0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDU5NDM3NTEsImV4cCI6MjA2MTUxOTc1MX0.D6JS3PQc2HxwKpZu5lq0tUf3hdVM3ZA78ky2OsAuqs0"
        ) {
            install(Auth)
            install(Postgrest)
            install(Realtime)
            install(Storage)
        }
    }
}