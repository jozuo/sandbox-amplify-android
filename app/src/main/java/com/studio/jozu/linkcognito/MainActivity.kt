package com.studio.jozu.linkcognito

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.amplifyframework.auth.AuthProvider
import com.amplifyframework.auth.AuthUserAttribute
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.options.AuthSignOutOptions
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify

class MainActivity : AppCompatActivity() {
    companion object {
        private const val USER_NAME = "username1"
    }

    private val viewSignupEmail: EditText by lazy { findViewById(R.id.view_sign_up_email) }
    private val viewSignupPhone: EditText by lazy { findViewById(R.id.view_sign_up_phone) }
    private val viewSignUpButton: Button by lazy { findViewById(R.id.view_sign_up_button) }
    private val viewConfirmSignUpButton: Button by lazy { findViewById(R.id.view_confirm_sign_up_button) }
    private val viewConfirmSignUpCode: EditText by lazy { findViewById(R.id.view_confirm_sign_up_code) }
    private val viewSignInButton: Button by lazy { findViewById(R.id.view_sign_in_button) }
    private val viewSignInUsername: EditText by lazy { findViewById(R.id.view_sing_in_username) }
    private val viewSignInPassword: EditText by lazy { findViewById(R.id.view_sing_in_password) }
    private val viewConfirmSignInButton: Button by lazy { findViewById(R.id.view_confirm_sign_in_button) }
    private val viewConfirmSignInCode: EditText by lazy { findViewById(R.id.view_confirm_sign_in_code) }
    private val viewGoogleSignInButton: Button by lazy { findViewById(R.id.view_google_sign_in_button) }
    private val viewSignOutButton: Button by lazy { findViewById(R.id.view_google_sign_out_button) }

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

        Amplify.Auth.fetchUserAttributes(
            {
                it.forEach { attr ->
                    Log.i("AuthDemo", "User attributes: ${attr.key}, ${attr.value}")
                }
            },
            { Log.e("AuthDemo", "Failed to fetch user attributes", it) }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AWSCognitoAuthPlugin.WEB_UI_SIGN_IN_ACTIVITY_CODE) {
            Amplify.Auth.handleWebUISignInResponse(data)
        }
    }

    private fun setEvent() {
        // Sign-Up
        viewSignUpButton.setOnClickListener {
            val email = viewSignupEmail.text.toString()
            val phone = viewSignupPhone.text.toString()
            val attrs = mapOf(
                AuthUserAttributeKey.email() to email,
                AuthUserAttributeKey.phoneNumber() to phone,
            )

            val options = AuthSignUpOptions.builder()
                .userAttributes(attrs.map { AuthUserAttribute(it.key, it.value) })
                .build()
            Amplify.Auth.signUp(USER_NAME, "Password123", options,
                { Log.i("AuthQuickStart", "Sign up succeeded: $it") },
                { Log.e("AuthQuickStart", "Sign up failed", it) }
            )
        }

        // Confirm Sign-Up
        viewConfirmSignUpButton.setOnClickListener {
            val confirmCode = viewConfirmSignUpCode.text.toString()

            Amplify.Auth.confirmSignUp(
                USER_NAME, confirmCode,
                { result ->
                    if (result.isSignUpComplete) {
                        Log.i("AuthQuickstart", "Confirm signUp succeeded")
                    } else {
                        Log.i("AuthQuickstart", "Confirm sign up not complete")
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

        // Confirm Sign-In
        viewConfirmSignInButton.setOnClickListener {
            val confirmCodee = viewConfirmSignInCode.text.toString()

            Amplify.Auth.confirmSignIn(confirmCodee,
                { Log.i("AuthQuickstart", "Confirmed signin: $it") },
                { Log.e("AuthQuickstart", "Failed to confirm signin", it) }
            )
        }

        // GoogleSign-In
        viewGoogleSignInButton.setOnClickListener {
            Amplify.Auth.signInWithSocialWebUI(
                AuthProvider.google(), this,
                { Log.i("AuthQuickstart", "Sign in OK: $it") },
                { Log.e("AuthQuickstart", "Sign in failed", it) }
            )
        }

        // Sign-Out
        viewSignOutButton.setOnClickListener {
            val options = AuthSignOutOptions.builder()
                .globalSignOut(true)
                .build()
            Amplify.Auth.signOut(options,
                { Log.i("AuthQuickstart", "Signed out globally") },
                { Log.e("AuthQuickstart", "Sign out failed", it) }
            )
        }
    }
}