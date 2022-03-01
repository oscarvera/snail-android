package com.oscarvera.snail.util

import android.content.Context
import android.content.Intent
import com.oscarvera.snail.usecases.addcard.AddCardActivity
import com.oscarvera.snail.usecases.crossdata.CrossDataActivity
import com.oscarvera.snail.usecases.deskdetail.DeskDetailActivity
import com.oscarvera.snail.usecases.deskshareddetail.DeskSharedDetailActivity
import com.oscarvera.snail.usecases.home.MainActivity
import com.oscarvera.snail.usecases.learning.LearningActivity
import com.oscarvera.snail.usecases.login.LoginActivity
import com.oscarvera.snail.usecases.onboarding.OnboardingActivity
import com.oscarvera.snail.usecases.sharedesk.ShareDeskActivity

class Router {

    companion object {

        fun launchLoginIntent(context: Context?) {
            val intent = Intent(context, LoginActivity::class.java)
            context?.startActivity(intent)
        }

        fun launchAddCardActivity(context: Context, idDesk: String, nameDesk: String) {
            val intent = Intent(context, AddCardActivity::class.java)
            intent.putExtra(AddCardActivity.EXTRA_ID_DESK, idDesk)
            intent.putExtra(AddCardActivity.EXTRA_NAME_DESK, nameDesk)
            context.startActivity(intent)
        }

        fun launchLearningActivity(context: Context?, idDesk: String, nameDesk: String) {
            val intent = Intent(context, LearningActivity::class.java)
            intent.putExtra(LearningActivity.EXTRA_ID_DESK, idDesk)
            intent.putExtra(LearningActivity.EXTRA_NAME_DESK, nameDesk)
            context?.startActivity(intent)
        }

        fun launchDeskDetailActivity(context: Context?, idDesk: String) {
            val intent = Intent(context, DeskDetailActivity::class.java)
            intent.putExtra(DeskDetailActivity.EXTRA_ID_DESK, idDesk)
            context?.startActivity(intent)
        }

        fun launchDeskSharedDetailActivity(context: Context?, idRemoteDesk: String) {
            val intent = Intent(context, DeskSharedDetailActivity::class.java)
            intent.putExtra(DeskSharedDetailActivity.EXTRA_ID_DESK, idRemoteDesk)
            context?.startActivity(intent)
        }

        fun launchMainActivity(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

        fun launchOnbordingActivity(context: Context) {
            val intent = Intent(context, OnboardingActivity::class.java)
            context.startActivity(intent)
        }

        fun launchShareDeskActivity(context: Context?) {
            val intent = Intent(context, ShareDeskActivity::class.java)
            context?.startActivity(intent)
        }

        fun launchDataCrossActivity(context: Context?) {
            val intent = Intent(context, CrossDataActivity::class.java)
            context?.startActivity(intent)
        }


    }


}
