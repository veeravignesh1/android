package com.toggl.common.feature.navigation

import android.content.Context
import androidx.navigation.NavController
import com.toggl.common.extensions.performClickHapticFeedback
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Router @Inject constructor(private val context: Context) {

    private var currentBackStack: BackStack = emptyList()

    @Suppress("UNCHECKED_CAST")
    fun processNewBackStack(
        newBackStack: BackStack,
        navController: NavController
    ) {
        val oldBackStack = currentBackStack

        val operations = sequence {

            val neededPopOperations = oldBackStack.foldIndexed(emptyList<BackStackOperation>()) { index, popOperations, oldRoute ->
                if (popOperations.any()) {
                    // As soon as any operation is invalid, we need to pop the rest
                    return@foldIndexed popOperations + BackStackOperation.Pop
                }

                val newRoute = newBackStack[index]

                if (newRoute == oldRoute) popOperations
                else listOf(BackStackOperation.Pop)
            }

            yieldAll(neededPopOperations)

            val numberOfValidOperations = oldBackStack.size - neededPopOperations.size

            for (i in numberOfValidOperations until newBackStack.size) {
                val deepLink = newBackStack[i - 1].deepLink(context)
                val route = BackStackOperation.Push(deepLink)
                yield(route)
            }
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