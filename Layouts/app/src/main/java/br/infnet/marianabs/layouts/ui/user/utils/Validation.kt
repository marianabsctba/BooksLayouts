package br.infnet.marianabs.layouts.ui.user.utils

object Validation {

    private const val EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    fun email (emails : String):Boolean{
        when {
            emails.matches(EMAIL_PATTERN.toRegex()) -> {
                return true
            }
            else -> {
                return false
            }
        }
    }
}