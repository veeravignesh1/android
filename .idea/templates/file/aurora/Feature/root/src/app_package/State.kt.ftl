package ${escapeKotlinIdentifiers(packageName)}.domain

import arrow.optics.optics

@optics
data class ${stateName}(
    val exampleText: String
) {
    companion object
}