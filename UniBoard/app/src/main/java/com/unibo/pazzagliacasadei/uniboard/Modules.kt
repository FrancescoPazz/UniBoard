package com.unibo.pazzagliacasadei.uniboard

import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val modules = module {
    viewModel { AuthViewModel() }
}