package com.oscarvera.snail.util.customs


import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.airbnb.lottie.LottieAnimationView
import com.oscarvera.snail.R


class LoadingButton(context: Context,view: View){

    private val textView : TextView = view.findViewById(R.id.button_loading_text)
    private val animationView : LottieAnimationView = view.findViewById(R.id.button_loading_animation)

    fun startLoading(){
        textView.visibility = View.INVISIBLE
        animationView.playAnimation()
    }

    fun finishLoading(){
        textView.visibility = View.VISIBLE
        animationView.cancelAnimation()
    }

}