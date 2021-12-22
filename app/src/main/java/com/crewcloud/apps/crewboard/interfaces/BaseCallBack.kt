package com.crewcloud.apps.crewboard.interfaces

import com.crewcloud.apps.crewboard.dtos.ErrorDto

interface ICheckSSL {
    fun hasSSL(hasSSL: Boolean)
    fun checkSSLError(errorData: ErrorDto)
}
