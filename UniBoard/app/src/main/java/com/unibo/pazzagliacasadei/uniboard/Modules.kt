package com.unibo.pazzagliacasadei.uniboard

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.unibo.pazzagliacasadei.uniboard.data.repositories.SettingsRepository
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.screens.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("settings")

val modules = module {
    viewModel { AuthViewModel() }
    viewModel { SettingsViewModel(get()) }

    single { SettingsRepository(get()) }
    single { get<Context>().dataStore }
}