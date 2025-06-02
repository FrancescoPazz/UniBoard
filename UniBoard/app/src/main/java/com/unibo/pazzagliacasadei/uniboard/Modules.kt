package com.unibo.pazzagliacasadei.uniboard

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.unibo.pazzagliacasadei.uniboard.data.repositories.settings.SettingsRepository
import com.unibo.pazzagliacasadei.uniboard.data.repositories.auth.AuthRepository
import com.unibo.pazzagliacasadei.uniboard.data.repositories.chat.ChatRepository
import com.unibo.pazzagliacasadei.uniboard.data.repositories.detail.DetailRepository
import com.unibo.pazzagliacasadei.uniboard.data.repositories.home.HomeRepository
import com.unibo.pazzagliacasadei.uniboard.data.repositories.profile.UserRepository
import com.unibo.pazzagliacasadei.uniboard.data.repositories.publish.PublishRepository
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.screens.chat.ChatViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.screens.detail.DetailViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.screens.home.HomeViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.screens.profile.ProfileViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.screens.publish.PublishViewModel
import com.unibo.pazzagliacasadei.uniboard.ui.screens.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("settings")

val modules = module {
    viewModel { AuthViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { ChatViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { PublishViewModel(get()) }

    single { AuthRepository(get()) }
    single { UserRepository(get()) }
    single { HomeRepository(get()) }
    single { SettingsRepository(get()) }
    single { DetailRepository(get()) }
    single { ChatRepository(get()) }
    single { get<Context>().dataStore }
    single { PublishRepository(get()) }
}