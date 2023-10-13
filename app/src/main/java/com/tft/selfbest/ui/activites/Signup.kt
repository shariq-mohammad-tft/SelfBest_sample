package com.tft.selfbest.ui.activites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tft.selfbest.R
import com.tft.selfbest.databinding.ActivitySignupBinding
import com.tft.selfbest.ui.fragments.signup.SignupFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Signup : AppCompatActivity() {

    lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val transaction =
            this.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, SignupFragment())
        transaction.disallowAddToBackStack()
        transaction.commit()
    }
}