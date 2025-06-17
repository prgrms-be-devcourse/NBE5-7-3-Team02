package io.twogether.nbe_5_7_2_02team.global.exception

import io.twogether.nbe_5_7_2_02team.global.response.error.ErrorCode

class TokenException(
    val errorCode: ErrorCode,
) : RuntimeException()
