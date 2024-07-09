package com.teamsparta.todo.redis

import io.lettuce.core.SetArgs
import io.lettuce.core.api.sync.RedisCommands
import org.springframework.stereotype.Repository

@Repository
class LockRedisRepository(private val redisCommands: RedisCommands<String, String>)
{
    fun tryLock(
        key: String,
        timeout: Long
    ):Boolean
    {
        val result = redisCommands.set(key, "LOCKED", SetArgs().nx().px(timeout))
        return result == "OK"
    }

    fun unlock(
        key: String
    )
    {
        redisCommands.del(key)
    }
}