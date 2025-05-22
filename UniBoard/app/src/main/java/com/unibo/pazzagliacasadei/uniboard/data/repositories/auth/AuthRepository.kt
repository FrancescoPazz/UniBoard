package com.unibo.pazzagliacasadei.uniboard.data.repositories.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.unibo.pazzagliacasadei.uniboard.data.models.auth.User
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthResponse
import com.unibo.pazzagliacasadei.uniboard.ui.screens.auth.AuthState
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
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
    "565030994457-omk63okmp2lqc1pqr5bpc5tqntt9g5ls.apps.googleusercontent.com"

class AuthRepository(
    private val supabase: SupabaseClient
) : IAuthRepository {
    override fun authState(): Flow<AuthState> = supabase.auth.sessionStatus.map { status ->
        when (status) {
            is SessionStatus.Initializing -> AuthState.Loading
            is SessionStatus.Authenticated -> AuthState.Authenticated
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

    override fun signUp(
        email: String,
        password: String,
        username: String,
        name: String?,
        surname: String?,
        tel: String?
    ): Flow<AuthResponse> = flow {
        try {
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
            supabase.from("users").upsert(user)
            emit(AuthResponse.Success)
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

            val user = User(
                id = info.id,
                email = info.email!!,
                name = fn,
                surname = ln,
                username = fullName,
                tel = tel
            )
            supabase.from("users").upsert(user)
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

    override suspend fun changePassword(oldPassword: String, newPassword: String) {
        val email = supabase.auth.currentUserOrNull()?.email ?: return
        Log.d("ProfileViewModel", "Changing password for user: $email")
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = oldPassword
        }
        supabase.auth.updateUser {
            password = newPassword
        }
        Log.d("ProfileViewModel", "Password changed successfully for user: $email")
    }

    suspend fun changeForgottenPassword(email: String, newPassword: String) = flow  {
        try {
            val user = supabase.from("users")
                .select {
                    filter {
                        eq("email", email)
                    }
                }.decodeSingle<User>()

            supabase.auth.admin.updateUserById(user.id) {
                password = newPassword
            }
            emit (AuthResponse.Success)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error changing password: ${e.message}")
            emit (AuthResponse.Failure(e.message ?: "Error changing password"))
        }
    }
}
