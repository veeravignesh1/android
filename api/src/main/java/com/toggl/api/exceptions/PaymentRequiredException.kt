package com.toggl.api.exceptions

class PaymentRequiredException(localizedErrorMessage: String?) : ClientErrorException(localizedErrorMessage ?: defaultErrorMessage) {
    companion object {
        const val httpCode = 402
        const val defaultErrorMessage = "Payment is required for this request."
    }
}
