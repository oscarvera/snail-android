package com.oscarvera.snail.util.customs

import android.animation.Animator
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.airbnb.lottie.LottieAnimationView
import com.oscarvera.snail.R

class LoadingDialog(context: Context) {

    /*
    How to use:
        - Beginning class :
        lateinit var loadingDialog : LoadingDialog
        onCreate:
        loadingDialog = LoadingDialog(this)
        - Where loading start :
        loadingDialog.setCallback(object : LoadingDialog.LoadingDialogCallback {
                    override fun onFinish(dialog: Dialog) {
                        dialog.dismiss()
                        //finish()
                    }
                })
                loadingDialog.showLoadingDialog()
        - Where loading end:
         loadingDialog.finishLoadingDialog()

     */

    var dialog: Dialog = Dialog(context, R.style.CustomAlertDialog)
    private var animationLoading : LottieAnimationView
    private var callback : LoadingDialogCallback? = null

    init {
        dialog.setContentView(R.layout.dialog_loading)
        animationLoading = dialog.findViewById<LottieAnimationView>(R.id.animation_loading)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = lp
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    interface LoadingDialogCallback {
        fun onFinish(dialog: Dialog)
    }

    fun setCallback(dialogCallback: LoadingDialogCallback){
        this.callback = dialogCallback
    }

    fun showLoadingDialog() {
        var isStarted = false
        dialog.show()
        animationLoading.setMaxFrame("Marker2")
        animationLoading.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                if (!isStarted){
                    animationLoading.setMinFrame("Marker1")
                    isStarted = true
                }
            }

            override fun onAnimationEnd(animation: Animator?) {

            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })
        animationLoading.playAnimation()

    }

    fun finishLoadingDialog(dismiss : Boolean = false){
        Handler(Looper.getMainLooper()).post {
            animationLoading.setMinFrame("Marker2")
            animationLoading.setMaxFrame("Marker3")
            animationLoading.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                    animationLoading.cancelAnimation()
                    callback?.onFinish(dialog)
                    if (dismiss){
                        dialog.dismiss()
                    }
                }

                override fun onAnimationEnd(animation: Animator?) {

                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
            animationLoading.playAnimation()
        }
    }


}