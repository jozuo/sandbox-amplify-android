package com.studio.jozu.linkcognito

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify

class MainActivity : AppCompatActivity() {
    companion object {
        private const val USER_NAME = "username1"
    }

    private val viewSignUpButton: Button by lazy { findViewById(R.id.view_sign_up_button) }
    private val viewConfirmSignUpButton: Button by lazy { findViewById(R.id.view_confirm_sign_up_button) }
    private val viewConfirmCode: EditText by lazy { findViewById(R.id.view_confirm_code) }
    private val viewSignInButton: Button by lazy { findViewById(R.id.view_sign_in_button) }
    private val viewSignInUsername: EditText by lazy { findViewById(R.id.view_sing_in_username) }
    private val viewSignInPassword: EditText by lazy { findViewById(R.id.view_sing_in_password) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i("AmplifyQuickstart", "main activity on create")
        Amplify.Auth.fetchAuthSession(
            { Log.i("AmplifyQuickstart", "Auth session = $it") },
            { Log.e("AmplifyQuickstart", "Failed to fetch auth session", it) }
        )
        Log.i("AmplifyQuickstart", "main activity on created!!")

        setEvent()
    }

    private fun setEvent() {
        // Sign-Up
        viewSignUpButton.setOnClickListener {
            val options = AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), "jozuo.dev+002@gmail.com")
                .build()
            Amplify.Auth.signUp(USER_NAME, "Password123", options,
                { Log.i("AuthQuickStart", "Sign up succeeded: $it") },
                { Log.e ("AuthQuickStart", "Sign up failed", it) }
            )
        }

        // Confirm Sign-Up
        viewConfirmSignUpButton.setOnClickListener {
            val confirmCode = viewConfirmCode.text.toString()

            Amplify.Auth.confirmSignUp(
                USER_NAME, confirmCode,
                { result ->
                    if (result.isSignUpComplete) {
                        Log.i("AuthQuickstart", "Confirm signUp succeeded")
                    } else {
                        Log.i("AuthQuickstart","Confirm sign up not complete")
                    }
                },
                { Log.e("AuthQuickstart", "Failed to confirm sign up", it) }
            )
        }

        // Sign-In
        viewSignInButton.setOnClickListener {
            val username = viewSignInUsername.text.toString()
            val password = viewSignInPassword.text.toString()

            Amplify.Auth.signIn(username, password,
                { result ->
                    if (result.isSignInComplete) {
                        Log.i("AuthQuickstart", "Sign in succeeded")
                    } else {
                        Log.i("AuthQuickstart", "Sign in not complete")
                    }
                },
                { Log.e("AuthQuickstart", "Failed to sign in", it) }
            )
        }
    }
}