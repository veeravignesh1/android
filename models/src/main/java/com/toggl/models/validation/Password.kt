package com.toggl.models.validation

sealed class Password {
    abstract val password: String

    class Invalid internal constructor(override val password: String) : Password()
    open class Valid internal constructor(override val password: String) : Password() {
        override fun equals(other: Any?) = other is Valid && other.password == password

        override fun hashCode() = password.hashCode()
    }
    class Strong internal constructor(override val password: String) : Valid(password)

    override fun toString(): String = password

    companion object {
        fun from(password: String) =
            when {
                password.isStrong() -> Strong(password)
                password.isNotBlank() -> Valid(password)
                else -> Invalid(password)
            }

        private fun String.isStrong() =
            length >= 8 && any { it.isDigit() } && any { it.isUpperCase() } && any { it.isLowerCase() }
    }
}

fun String.toPassword() =
    Password.from(this)
