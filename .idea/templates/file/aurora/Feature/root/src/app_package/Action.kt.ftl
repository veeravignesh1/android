package ${escapeKotlinIdentifiers(packageName)}.domain

import arrow.optics.optics

@optics
sealed class ${actionName} {
    object ExampleAction : ${actionName}()

    companion object
}

fun ${actionName}.formatForDebug() =
    when (this) {
        is ${actionName}.ExampleAction -> "Example Action"
    }