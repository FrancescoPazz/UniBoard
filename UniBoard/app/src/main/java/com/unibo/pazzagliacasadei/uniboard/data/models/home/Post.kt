package com.unibo.pazzagliacasadei.uniboard.data.models.home

import java.util.UUID

data class Post(
    val id: UUID,
    val title: String,
    val subtitle: String,
    val imageUrl: String
)