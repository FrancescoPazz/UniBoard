package com.unibo.pazzagliacasadei.uniboard.data.repositories.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User
import com.unibo.pazzagliacasadei.uniboard.data.repositories.USERS_TABLE
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthResponse
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthState
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.auth.providers.builtin.OTP
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.security.MessageDigest
import java.util.UUID

private const val GOOGLE_SERVER_CLIENT_ID =
    "960246085459-a8evvuosmhpp9kggk7bbjulmtttbjc1u.apps.googleusercontent.com"

class AuthRepository(
    private val supabase: SupabaseClient
) : IAuthRepository {
    override fun authState(): Flow<AuthState> = supabase.auth.sessionStatus.map { status ->
        when (status) {
            is SessionStatus.Initializing -> AuthState.Loading
            is SessionStatus.Authenticated ->
                if (supabase.auth.currentUserOrNull()?.userMetadata?.get(
                        "email",
                    ) != null
                ) {
                    AuthState.Authenticated
                } else {
                    AuthState.AnonymousAuthenticated
                }
            is SessionStatus.NotAuthenticated,
            is SessionStatus.RefreshFailure -> AuthState.Unauthenticated
        }
    }

    override fun signIn(email: String, password: String): Flow<AuthResponse> = flow {
        try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            emit(AuthResponse.Success)
        } catch (e: Exception) {
            emit(AuthResponse.Failure(e.message ?: "Login error"))
        }
    }

    override fun signInAsGuest(): Flow<AuthResponse> = flow {
        try {
            supabase.auth.signInAnonymously()
            emit(AuthResponse.Success)
        } catch (e: Exception) {
            emit(AuthResponse.Failure(e.message ?: "Guest login error"))
        }
    }

    override fun signUp(
        email: String,
        password: String,
        username: String,
        name: String?,
        surname: String?,
        tel: String?
    ): Flow<AuthResponse> = flow {
        try {

            val userExists = supabase.from(USERS_TABLE).select {
                filter { or {
                    eq("email", email)
                    eq("username", username)
                } }
            }.decodeSingleOrNull<User>() != null

            Log.d("UserExists", "User exists: $userExists")

            if (userExists) {
                emit(AuthResponse.Failure("User with this email or username already exists"))
            } else {
                val result = supabase.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }
                val userId = result?.id
                    ?: throw IllegalStateException("SignUp succeeded but userId is null")

                val user = User(
                    id = userId,
                    email = email,
                    name = name,
                    surname = surname,
                    username = username,
                    tel = tel,
                )
                supabase.from(USERS_TABLE).upsert(user)
                emit(AuthResponse.Success)
            }
        } catch (e: Exception) {
            emit(AuthResponse.Failure(e.message ?: "Sign up error"))
        }
    }

    private fun createNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(rawNonce.toByteArray())
        return digest.fold("") { str, it ->  str + "%02x".format(it) }
    }

    override fun signInWithGoogle(context: Context): Flow<AuthResponse> = flow {
        val nonce = createNonce()
        val option = GetGoogleIdOption.Builder()
            .setServerClientId(GOOGLE_SERVER_CLIENT_ID)
            .setFilterByAuthorizedAccounts(false)
            .setNonce(nonce)
            .build()
        val request = GetCredentialRequest.Builder().addCredentialOption(option).build()
        val manager = CredentialManager.create(context)

        try {
            val cred = manager.getCredential(context, request)
            val tokenCred = GoogleIdTokenCredential.createFrom(cred.credential.data)
            val idToken = tokenCred.idToken

            supabase.auth.signInWith(IDToken) {
                this.idToken = idToken
                provider = Google
            }
            supabase.auth.sessionStatus.filterIsInstance<SessionStatus.Authenticated>().first()

            val session = supabase.auth.currentSessionOrNull()
                ?: throw IllegalStateException("No session after Google login")
            val info = session.user!!
            val fullName = info.userMetadata?.get("full_name").toString().trim('"')
            val (fn, ln) = fullName.split(" ", limit = 2).let { it[0] to it.getOrElse(1) { "" } }
            val tel = info.userMetadata?.get("tel")?.toString()?.trim('"')
                ?: info.userMetadata?.get("phone_number")?.toString()?.trim('"')
            val username = info.email!!.split('@')[0]

            val user = User(
                id = info.id,
                email = info.email!!,
                name = fn,
                surname = ln,
                username = username,
                tel = tel
            )
            supabase.from(USERS_TABLE).upsert(user)
            emit(AuthResponse.Success)
        } catch (e: Exception) {
            emit(AuthResponse.Failure(e.message ?: "Google login error"))
        }
    }

    override fun signOut(): Flow<AuthResponse> = flow {
        try {
            supabase.auth.signOut()
            emit(AuthResponse.Success)
        } catch (e: Exception) {
            emit(AuthResponse.Failure(e.message ?: "Sign out error"))
        }
    }

    override fun resetPassword(email: String): Flow<AuthResponse> = flow {
        try {
            supabase.auth.resetPasswordForEmail(email)
            emit(AuthResponse.Success)
        } catch (e: Exception) {
            emit(AuthResponse.Failure(e.message ?: "Reset password error"))
        }
    }

    fun sendOtp(email: String, otp: String): Flow<AuthResponse> = flow {
        try {
            supabase.auth.verifyEmailOtp(type = OtpType.Email.EMAIL, email = email, token = otp)
            if (supabase.auth.currentUserOrNull() == null) {
                Log.d("test AuthRepository", "No user found, signing in with OTP")
                supabase.auth.signInWith(OTP) {
                    this.email = email
                }
            }
            Log.d("test AuthRepository", "User signed in with OTP: ${supabase.auth.currentUserOrNull()?.email}")
            emit(AuthResponse.Success)
        } catch (e: Exception) {
            emit(AuthResponse.Failure(e.message ?: "Send OTP error"))
        }
    }

    override suspend fun currentUser(): User? {
        val session = supabase.auth.currentSessionOrNull() ?: return null
        val data = session.user!!
        return User(
            id = data.id,
            username = data.userMetadata?.get("full_name")?.toString()?.trim('"') ?: data.id,
            email = data.email!!,
            name = data.userMetadata?.get("name")?.toString()?.trim('"'),
            surname = data.userMetadata?.get("surname")?.toString()?.trim('"'),
            tel = data.userMetadata?.get("tel")?.toString()?.trim('"')
                ?: data.userMetadata?.get("phone_number")?.toString()?.trim('"'),
        )
    }

    override suspend fun changePassword(email: String, oldPassword: String, newPassword: String) {
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = oldPassword
        }
        if (supabase.auth.currentUserOrNull() == null) {
            throw IllegalStateException("Authentication failed for user: $email")
        }
        Log.d("test ProfileViewModel", "User authenticated successfully for email: $email")
        supabase.auth.updateUser {
            password = newPassword
        }
        Log.d("ProfileViewModel", "Password changed successfully for user: $email")
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String) {
        val userEmail = supabase.auth.currentUserOrNull()?.email ?: return
        changePassword(userEmail, oldPassword, newPassword)
        Log.d("ProfileViewModel", "Password changed successfully for user: $userEmail")
    }

    fun changeForgottenPassword(newPassword: String) = flow  {
        try {
            supabase.auth.updateUser {
                password = newPassword
            }
            Log.d("test changeForgottenPassword", "Password changed successfully for user")
            emit (AuthResponse.Success)
        } catch (e: Exception) {
            Log.e("test changeForgottenPassword", "Error changing password: ${e.message}")
            emit (AuthResponse.Failure(e.message ?: "Error changing password"))
        }
    }
}
