package com.unibo.pazzagliacasadei.uniboard

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.unibo.pazzagliacasadei.uniboard.data.repositories.SettingsRepository
import com.unibo.pazzagliacasadei.uniboard.data.repositories.auth.AuthRepository
import com.unibo.pazzagliacasadei.uniboard.data.repositories.home.HomeRepository
import com.unibo.pazzagliacasadei.uniboard.data.repositories.profile.UserRepository
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.screens.home.HomeViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.ProfileViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.screens.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("settings")

val modules = module {
    viewModel { AuthViewModel(get(), get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { ProfileViewModel(get()) }

    single { AuthRepository(get()) }
    single { UserRepository(get()) }
    single { HomeRepository(get()) }
    single { SettingsRepository(get()) }
    single { get<Context>().dataStore }
}