package com.teamsparta.todo.common.dto

import com.teamsparta.todo.common.status.ResultCode


data class BaseResponse<T>(
    val result: String = ResultCode.SUCCESS.name,
    val data: T? = null,
    val message: String = ResultCode.SUCCESS.msg
)