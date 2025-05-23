package com.unibo.pazzagliacasadei.uniboard.data.repositories.publish

import androidx.compose.ui.util.fastForEachIndexed
import com.unibo.pazzagliacasadei.uniboard.data.models.post.PostRetrieved
import com.unibo.pazzagliacasadei.uniboard.data.models.post.PostToPublish
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.datetime.Clock.System
import org.maplibre.android.geometry.LatLng

class PublishRepository(val supabase: SupabaseClient) : IPublishRepository {
    private suspend fun createPostRow(
        postTitle: String,
        postTextContent: String,
        isAnonymous: Boolean
    ): PostRetrieved {
        return supabase.from("posts").insert(
            PostToPublish(
                supabase.auth.currentUserOrNull()!!.id,
                System.now(),
                postTextContent,
                postTitle,
                isAnonymous
            )
        ) { select() }.decodeSingle<PostRetrieved>()
    }

    override suspend fun publishPost(
        postTitle: String,
        postTextContent: String,
        isAnonymous: Boolean,
        position: LatLng?,
        images: List<ByteArray>
    ) {
        val post = createPostRow(postTitle, postTextContent, isAnonymous)
        val postImagesBucketApi = supabase.storage.from("post-images")
        images.fastForEachIndexed { index, imageBytes ->
            postImagesBucketApi.upload("${post.id}/${index}.jpg", imageBytes){
                upsert = false
            }
        }
    }
}