package com.unibo.pazzagliacasadei.uniboard.data.repositories.publish

import androidx.compose.ui.util.fastForEachIndexed
import com.unibo.pazzagliacasadei.uniboard.data.models.post.Photo
import com.unibo.pazzagliacasadei.uniboard.data.models.post.Position
import com.unibo.pazzagliacasadei.uniboard.data.models.post.PostRetrieved
import com.unibo.pazzagliacasadei.uniboard.data.models.post.PostToPublish
import com.unibo.pazzagliacasadei.uniboard.data.repositories.PHOTOS_TABLE
import com.unibo.pazzagliacasadei.uniboard.data.repositories.POSITIONS_TABLE
import com.unibo.pazzagliacasadei.uniboard.data.repositories.POSTS_TABLE
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.datetime.Clock.System
import org.maplibre.android.geometry.LatLng

enum class PublicationErrors {
    EMPTY_STRINGS,
    GENERIC
}

class PublishRepository(val supabase: SupabaseClient) : IPublishRepository {
    private suspend fun createPostRow(
        postTitle: String,
        postTextContent: String,
        isAnonymous: Boolean
    ): PostRetrieved {
        return supabase.from(POSTS_TABLE).insert(
            PostToPublish(
                supabase.auth.currentUserOrNull()!!.id,
                System.now(),
                postTextContent,
                postTitle,
                isAnonymous
            )
        ) { select() }.decodeSingle<PostRetrieved>()
    }

    private suspend fun createPhotoRow(
        postId: String,
        imageName: String,
    ) {
        supabase.from(PHOTOS_TABLE).insert(
            Photo(
                postId,
                imageName
            )
        )
    }

    private suspend fun createPosition(
        postId: String,
        position: LatLng
    ) {
        supabase.from(POSITIONS_TABLE).insert(
            Position(
                postId,
                null,
                null,
                null,
                null,
                latLng = "POINT(${position.latitude} ${position.longitude})",
            )
        )
    }

    override suspend fun publishPost(
        postTitle: String,
        postTextContent: String,
        isAnonymous: Boolean,
        position: LatLng?,
        images: List<ByteArray>
    ): Boolean {
        val post = createPostRow(postTitle, postTextContent, isAnonymous)
        val postImagesBucketApi = supabase.storage.from("post-images")
        images.fastForEachIndexed { index, imageBytes ->
            createPhotoRow(post.id, "${post.id}/${index}.jpg")
            postImagesBucketApi.upload("${post.id}/${index}.jpg", imageBytes) {
                upsert = false
            }
        }
        if (position != null) {
            createPosition(post.id, position)
        }
        return true
    }
}