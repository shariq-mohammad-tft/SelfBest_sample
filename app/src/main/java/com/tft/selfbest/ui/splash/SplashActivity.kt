package com.tft.selfbest.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tft.selfbest.R
import com.tft.selfbest.data.SelfBestPreference
import com.tft.selfbest.ui.activites.HomeActivity
import com.tft.selfbest.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var pref: SelfBestPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        checkIsLogin()
    }

    private fun checkIsLogin() {
        val data = pref.getLoginData
        /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             val now: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"))
             val dtf2: DateTimeFormatter =
                 DateTimeFormatter.ofPattern("EEE, dd MMM uuuu HH:mm:ss OOOO", Locale.ENGLISH)
             val date = now.format(dtf2)
         } else {
             TODO("VERSION.SDK_INT < O")
         }*/
        val backgroundImage: ImageView = findViewById(R.id.app_logo)
        val rotateAnim = RotateAnimation(
            0.0f, 180f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f
        )
        backgroundImage.bringToFront()

        rotateAnim.duration = 1000
        rotateAnim.fillAfter = true
        backgroundImage.startAnimation(rotateAnim)
//        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_splash)
//        backgroundImage.startAnimation(slideAnimation)

        val text: TextView = findViewById(R.id.app_name)
        val slideAnimation1 = AnimationUtils.loadAnimation(this, R.anim.splash_text)
        text.startAnimation(slideAnimation1)

        Handler(Looper.getMainLooper()).postDelayed({
            if (data == null) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                startActivity(Intent(this, HomeActivity::class.java))
            }
            finish()
        }, 3000)

    }
}