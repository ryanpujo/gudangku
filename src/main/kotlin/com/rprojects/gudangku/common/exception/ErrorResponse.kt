package com.rprojects.gudangku.common.exception

data class ErrorResponse(
    val message: String,
    val details: Map<String, String> = emptyMap()
) {

    init {
        require(message.isNotBlank()) {
            "Error message cannot be null or empty"
        }
    }

    override fun toString(): String =
        "ErrorResponse(message='$message', details=$details)"
}

