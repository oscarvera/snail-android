package com.oscarvera.snail.usecases.onboarding

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.oscarvera.snail.R
import com.oscarvera.snail.util.Router
import kotlinx.android.synthetic.main.activity_onboarding.*


class OnboardingActivity : AppCompatActivity() {

    private var numPage = 1
    private val totalPages = 5

    enum class OnboardingScreenTexts(val text1: Int, val text2: Int, val text3: Int) {
        FIRST(
            R.string.activity_onboarding_screen1_title1,
            R.string.activity_onboarding_screen1_title2,
            R.string.activity_onboarding_screen1_title3
        ),
        SECOND(
            R.string.activity_onboarding_screen2_title1,
            R.string.activity_onboarding_screen2_title2,
            R.string.activity_onboarding_screen2_title3
        ),
        THIRD(
            R.string.activity_onboarding_screen3_title1,
            R.string.activity_onboarding_screen3_title2,
            R.string.activity_onboarding_screen3_title3
        ),
        FOURTH(
            R.string.activity_onboarding_screen4_title1,
            R.string.activity_onboarding_screen4_title2,
            R.string.activity_onboarding_screen4_title3
        ),
        FIFTH(
            R.string.activity_onboarding_screen5_title1,
            R.string.activity_onboarding_screen5_title2,
            R.string.activity_onboarding_screen5_title3
        ),

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val w: Window = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setScreenTexts(numPage)

        playAnimationPage(numPage)

        onboarding_button_next.setOnClickListener {
            if (numPage == totalPages){
                //Go to Login
                Router.launchLoginIntent(this)
            }else{
                numPage++
                setScreenTexts(numPage = numPage)
                playAnimationPage(numPage)
            }

        }

    }

    private fun playAnimationPage(numPage: Int) {
        when (numPage) {
            5 -> {
                animation_view.visibility = View.VISIBLE
                animation_view.setAnimation(R.raw.animation_onboarding2)
                animation_view.repeatCount = 3
                animation_view.playAnimation()

            }
            else -> {
                animation_view.setMinFrame("Marker${numPage - 1}")
                animation_view.setMaxFrame("Marker$numPage")
                animation_view.playAnimation()
            }
        }
    }

    private fun setScreenTexts(numPage: Int) {

        val textsScreen = when (numPage) {
            1 -> OnboardingScreenTexts.FIRST
            2 -> OnboardingScreenTexts.SECOND
            3 -> OnboardingScreenTexts.THIRD
            4 -> OnboardingScreenTexts.FOURTH
            5 -> OnboardingScreenTexts.FIFTH
            else -> OnboardingScreenTexts.FIRST
        }

        title_text_1.text = getText(textsScreen.text1)
        title_text_2.text = getText(textsScreen.text2)
        title_text_3.text = getText(textsScreen.text3)
    }
}