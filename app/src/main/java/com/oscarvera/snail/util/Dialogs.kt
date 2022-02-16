package com.oscarvera.snail.util

import android.animation.Animator
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import com.airbnb.lottie.LottieAnimationView
import com.oscarvera.snail.R

object Dialogs {

    interface NewDeskDialog{
        fun createDialog(name : String, dialog : Dialog)
    }

    interface NewShareNameDialog{
        fun shareDesk(name : String, dialog : Dialog)
    }

    interface LoadingDialog{
        fun onFinish(dialog : Dialog)
    }

    fun createNewDeskDialog(context : Context, callback : NewDeskDialog){
        val dialog = Dialog(context, R.style.CustomAlertDialog)
        dialog.setContentView(R.layout.dialog_add_desk)
        dialog.show()

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = lp

        val inset = InsetDrawable(AppCompatResources.getDrawable(context,R.drawable.background_dialog), 40)
        dialog.window?.setBackgroundDrawable(inset)

        val animation = dialog.findViewById<LottieAnimationView>(R.id.animation_view)
        animation.playAnimation()

        val textName = dialog.findViewById<EditText>(R.id.edittext_name)

        val btnClose = dialog.findViewById<Button>(R.id.btn_close)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        val btnAdd = dialog.findViewById<CardView>(R.id.btn_add_desk)
        val btnAddtext = dialog.findViewById<TextView>(R.id.button_loading_text)
        val btnAddAnimation = dialog.findViewById<LottieAnimationView>(R.id.button_loading_animation)
        //btnAdd.backgroundTintMode = null
        btnAdd.setOnClickListener {
            if (textName.text.isNotEmpty()) {
                btnAddtext.visibility = View.INVISIBLE
                btnAddAnimation.visibility = View.VISIBLE
                btnAddAnimation.playAnimation()
                callback.createDialog(textName.text.toString(), dialog)
            }else{
                textName.error = ""
            }
        }

    }


    fun createShareNameDialog(context : Context, callback : NewShareNameDialog){
        val dialog = Dialog(context, R.style.CustomAlertDialog)
        dialog.setContentView(R.layout.dialog_add_name)
        dialog.show()

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = lp

        val inset = InsetDrawable(AppCompatResources.getDrawable(context,R.drawable.background_dialog), 40)
        dialog.window?.setBackgroundDrawable(inset)

        val textName = dialog.findViewById<EditText>(R.id.edittext_name)

        val btnClose = dialog.findViewById<Button>(R.id.btn_close)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        val btnAdd = dialog.findViewById<CardView>(R.id.btn_add_name)
        val btnAddtext = dialog.findViewById<TextView>(R.id.button_loading_text)
        val btnAddAnimation = dialog.findViewById<LottieAnimationView>(R.id.button_loading_animation)
        //btnAdd.backgroundTintMode = null
        btnAdd.setOnClickListener {
            if (textName.text.isNotEmpty()) {
                btnAddtext.visibility = View.INVISIBLE
                btnAddAnimation.visibility = View.VISIBLE
                btnAddAnimation.playAnimation()
                callback.shareDesk(textName.text.toString(), dialog)
            }else{
                textName.error = ""
            }
        }

    }

    fun createLoadingDialog(context : Context, callback : LoadingDialog){
        val dialog = Dialog(context, R.style.CustomAlertDialog)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.show()

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = lp

        //val inset = InsetDrawable(AppCompatResources.getDrawable(context,R.drawable.background_dialog), 40)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val animation = dialog.findViewById<LottieAnimationView>(R.id.animation_loading)
        animation.playAnimation()
        animation.addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                callback.onFinish(dialog)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })
    }




}