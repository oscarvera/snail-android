package com.oscarvera.snail.util

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.InsetDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.oscarvera.snail.R

object Dialogs {

    interface NewDeskDialog {
        fun createDialog(name: String, dialog: Dialog)
    }

    interface NewShareNameDialog {
        fun shareDesk(name: String, dialog: Dialog)
    }



    interface DeskSettingsDialog {
        fun onDelete(dialog: Dialog)
    }

    fun createNewDeskDialog(context: Context, callback: NewDeskDialog) {
        val dialog = Dialog(context, R.style.CustomAlertDialog)
        dialog.setContentView(R.layout.dialog_add_desk)
        dialog.show()

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = lp

        val inset =
            InsetDrawable(AppCompatResources.getDrawable(context, R.drawable.background_dialog), 40)
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
        val btnAddAnimation =
            dialog.findViewById<LottieAnimationView>(R.id.button_loading_animation)
        //btnAdd.backgroundTintMode = null
        btnAdd.setOnClickListener {
            if (textName.text.isNotEmpty()) {
                btnAddtext.visibility = View.INVISIBLE
                btnAddAnimation.visibility = View.VISIBLE
                btnAddAnimation.playAnimation()
                callback.createDialog(textName.text.toString(), dialog)
            } else {
                textName.error = ""
            }
        }

    }


    fun createShareNameDialog(context: Context, callback: NewShareNameDialog) {
        val dialog = Dialog(context, R.style.CustomAlertDialog)
        dialog.setContentView(R.layout.dialog_change_text)
        dialog.show()

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = lp

        val inset =
            InsetDrawable(AppCompatResources.getDrawable(context, R.drawable.background_dialog), 40)
        dialog.window?.setBackgroundDrawable(inset)

        val title = dialog.findViewById<TextView>(R.id.title_name)
        title.text = context.getText(R.string.dialog_addname_title)
        val subtitle = dialog.findViewById<TextView>(R.id.subtitle_name)
        subtitle.text = context.getText(R.string.dialog_addname_subtitle)

        val textName = dialog.findViewById<EditText>(R.id.edittext_name)

        val btnClose = dialog.findViewById<Button>(R.id.btn_close)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        val btnAdd = dialog.findViewById<CardView>(R.id.btn_add_name)
        val btnAddtext = dialog.findViewById<TextView>(R.id.button_loading_text)
        btnAddtext.text = context.getText(R.string.dialog_addname_btn_share)
        val btnAddAnimation =
            dialog.findViewById<LottieAnimationView>(R.id.button_loading_animation)
        //btnAdd.backgroundTintMode = null
        btnAdd.setOnClickListener {
            btnAddtext.visibility = View.INVISIBLE
            btnAddAnimation.visibility = View.VISIBLE
            btnAddAnimation.playAnimation()
            callback.shareDesk(textName.text.toString(), dialog)
        }

    }

    fun createChangeNumber(context: Context, title : String, subtitle : String, hint : String, btnText : String, callback : (Int) -> Unit) {
        val dialog = Dialog(context, R.style.CustomAlertDialog)
        dialog.setContentView(R.layout.dialog_change_number)
        dialog.show()

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = lp

        val inset =
            InsetDrawable(AppCompatResources.getDrawable(context, R.drawable.background_dialog), 40)
        dialog.window?.setBackgroundDrawable(inset)

        val titleTv = dialog.findViewById<TextView>(R.id.title_name)
        titleTv.text = title
        val subtitleTv = dialog.findViewById<TextView>(R.id.subtitle_name)
        subtitleTv.text = subtitle

        val textName = dialog.findViewById<EditText>(R.id.edittext_name)
        textName.hint = hint

        val btnClose = dialog.findViewById<Button>(R.id.btn_close)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        val btnAdd = dialog.findViewById<CardView>(R.id.btn_add_name)
        val btnAddtext = dialog.findViewById<TextView>(R.id.button_loading_text)
        btnAddtext.text = btnText
        btnAdd.setOnClickListener {
            callback.invoke(textName.text.toString().toInt())
            dialog.dismiss()
        }

    }

    fun createChangeText(context: Context, title : String, subtitle : String, hint : String, btnText : String, callback : (String) -> Unit) {
        val dialog = Dialog(context, R.style.CustomAlertDialog)
        dialog.setContentView(R.layout.dialog_change_text)
        dialog.show()

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = lp

        val inset =
            InsetDrawable(AppCompatResources.getDrawable(context, R.drawable.background_dialog), 40)
        dialog.window?.setBackgroundDrawable(inset)

        val titleTv = dialog.findViewById<TextView>(R.id.title_name)
        titleTv.text = title
        val subtitleTv = dialog.findViewById<TextView>(R.id.subtitle_name)
        subtitleTv.text = subtitle

        val textName = dialog.findViewById<EditText>(R.id.edittext_name)
        textName.hint = hint

        val btnClose = dialog.findViewById<Button>(R.id.btn_close)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        val btnAdd = dialog.findViewById<CardView>(R.id.btn_add_name)
        val btnAddtext = dialog.findViewById<TextView>(R.id.button_loading_text)
        btnAddtext.text = btnText
        btnAdd.setOnClickListener {
            callback.invoke(textName.text.toString())
            dialog.dismiss()
        }

    }



    fun optionsBottomSheetDialog(
        context: Context,
        layoutInflater: LayoutInflater,
        callback: DeskSettingsDialog
    ) {
        val dialog = BottomSheetDialog(context)
        val view = layoutInflater.inflate(R.layout.dialog_bottom_sheet_options_desk, null)

        val btnDelete = view.findViewById<LinearLayout>(R.id.btn_delete)
        btnDelete.setOnClickListener {
            callback.onDelete(dialog)
        }

        dialog.setCancelable(true)

        dialog.setContentView(view)
        dialog.show()
    }


}