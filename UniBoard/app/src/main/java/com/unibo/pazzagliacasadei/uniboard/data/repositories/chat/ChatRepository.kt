package com.unibo.pazzagliacasadei.uniboard.data.repositories.chat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User
import com.unibo.pazzagliacasadei.uniboard.data.models.profile.Conversation
import com.unibo.pazzagliacasadei.uniboard.data.models.profile.Message
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock

class ChatRepository(
    private val supabase: SupabaseClient
) {
    val currentContactId = MutableLiveData<String?>()
    val currentContactUsername = MutableLiveData<String?>()

    suspend fun searchUsers(query: String) : List<User> {
        return try {
            val userId = supabase.auth.currentUserOrNull()?.id
                ?: throw IllegalStateException("No authenticated user")

            val resp = supabase.from("users").select {
                filter {
                    or {
                        ilike("username", "%$query%")
                        ilike("email", "%$query%")
                    }
                }
                filter { neq("id", userId) }
            }
            resp.decodeList<User>()
        } catch (e: Exception) {
            Log.e("ChatRepository", "searchUser failed", e)
            emptyList()
        }
    }

    fun setContactInfo(contactId: String, contactUsername: String) {
        currentContactId.postValue(contactId)
        currentContactUsername.postValue(contactUsername)
    }

    suspend fun fetchAllConversations(): List<Conversation> {
        val userId = supabase.auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("No authenticated user")

        try {
            val realConversations = supabase
                .from("view_conversations")
                .select{
                    filter { eq("user_id", userId) }
                }
                .decodeList<Conversation>()
            val otherUsersConversations = supabase
                .from("users")
                .select {
                    filter {
                        and {
                            neq("id", userId)
                        }
                    }
                }
                .decodeList<User>().filter { emptyConv ->
                    realConversations.none { realConv ->
                        realConv.contactId == emptyConv.id
                    }
                }.map { emptyConv ->
                    Conversation(
                        userId = userId,
                        contactId = emptyConv.id,
                        contactUsername = emptyConv.username,
                        lastMessage = null,
                        lastTime = null
                    )
                }

            val conversations = (realConversations + otherUsersConversations)
                .distinctBy { it.contactId }
                .sortedByDescending { it.lastTime }
            return conversations
        } catch (e: Exception) {
            throw e
        }
    }

    fun fetchmessagesWith(): Flow<List<Message>> = flow {
        val userId = supabase.auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("No authenticated user")
        val contactId = currentContactId.value
            ?: throw IllegalStateException("No contact selected")

        val resp = supabase
            .from("messages")
            .select {
                filter {
                    or {
                        and {
                            eq("sender_id", contactId)
                            eq("receiver_id", userId)
                        }
                        and {
                            eq("sender_id", userId)
                            eq("receiver_id", contactId)
                        }
                    }
                }
                order(
                    column = "sent_time",
                    Order.ASCENDING
                )
            }
        Log.d("TEST ChatRepository", "fetchMessagesWith: ${resp.decodeList<Message>()}")
        emit(resp.decodeList())
    }

    suspend fun sendMessage(messageInput: String) {
        val userId = supabase.auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("No authenticated user")
        val contactId = currentContactId.value
            ?: throw IllegalStateException("No contact selected")

        try {
            val message = Message(
                senderId = userId,
                receiverId = contactId,
                content = messageInput,
                sentTime = Clock.System.now()
            )
            val test = supabase.from("messages")
                .insert(message) {
                    select()
                }
                .decodeSingle<Message>()
            Log.d("TEST ChatRepository", "sendMessage: $test")
        } catch (e: Exception) {
            throw e
        }
    }
}