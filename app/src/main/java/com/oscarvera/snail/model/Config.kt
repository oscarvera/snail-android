package com.oscarvera.snail.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.oscarvera.snail.R


/*enum class MainTabs(
    @DrawableRes val icon: Int
) {
    HOME(R.drawable.ic_home_selected),
    CHARTS(R.drawable.ic_chart_unselected),
    SETTINGS(R.drawable.ic_settings_unselected)
}*/
enum class NAME_SCREENS(
    val route : String
){
    ACTIVITY_MAIN("ACTIVITY_MAIN"),
    FRAGMENT_MAIN_HOME("FRAGMENT_MAIN_HOME"),
    FRAGMENT_MAIN_CHARTS("FRAGMENT_MAIN_CHARTS"),
    FRAGMENT_MAIN_SETTINGS("FRAGMENT_MAIN_SETTINGS")
}

/*sealed class MainTabs(val route: String, val resourceId: String, @DrawableRes val icon: Int) {
    object Home : MainTabs(NAME_SCREENS.FRAGMENT_MAIN_HOME.route, "R.string.profile", R.drawable.ic_home_selected)
    object Shared : MainTabs(NAME_SCREENS.FRAGMENT_MAIN_CHARTS.route, "R.string.friends", R.drawable.ic_shared_unselected)
    object Settings : MainTabs(NAME_SCREENS.FRAGMENT_MAIN_SETTINGS.route, "R.string.settings", R.drawable.ic_settings_unselected)
}*/


/*
   enum class MainTabs(
       @StringRes val title: Int,
       @DrawableRes val icon: Int,
       val route: String
   ) {
       MY_COURSES(R.string.my_courses, R.drawable.ic_grain, CoursesDestinations.MY_COURSES_ROUTE),
       FEATURED(R.string.featured, R.drawable.ic_featured, CoursesDestinations.FEATURED_ROUTE),
       SEARCH(R.string.search, R.drawable.ic_search, CoursesDestinations.SEARCH_COURSES_ROUTE)
   }
   */

