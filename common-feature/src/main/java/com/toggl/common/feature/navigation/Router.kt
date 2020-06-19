package com.toggl.common.feature.navigation

import android.content.Context
import androidx.navigation.NavController
import com.toggl.common.extensions.performClickHapticFeedback
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

@Singleton
class Router @Inject constructor(private val context: Context) {

    private var currentBackStack: BackStack = emptyList()

    @Suppress("UNCHECKED_CAST")
    fun processNewBackStack(
        newBackStack: BackStack,
        navController: NavController
    ) {
        val newBackStackSize = newBackStack.size
        val oldBackStackSize = currentBackStack.size

        val sizeDifference = newBackStackSize - oldBackStackSize
        val operations = when {
            sizeDifference > 0 -> newBackStack.drop(oldBackStackSize).map { BackStackOperation.Push(it.deepLink(context)) }
            sizeDifference < 0 -> currentBackStack.dropLast(abs(sizeDifference)).map { BackStackOperation.Pop }
            else -> emptyList()
        }

        for (operation in operations) {

            context.performClickHapticFeedback()
            when (operation) {
                is BackStackOperation.Push -> navController.navigate(operation.deepLink)
                BackStackOperation.Pop -> navController.popBackStack()
            }
        }

        currentBackStack = newBackStack
    }
}