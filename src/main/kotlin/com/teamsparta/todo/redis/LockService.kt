package com.teamsparta.todo.redis

import io.lettuce.core.api.sync.RedisCommands
import org.springframework.stereotype.Service

@Service
class LockService(
    private val redisCommands: RedisCommands<String, String>
) {
    fun <T> withLock(lockKey: String, timeout: Long, action: () -> T): T? {
        val lockValue = Thread.currentThread().id.toString()
        val lockAcquired = redisCommands.setnx(lockKey, lockValue)

        return if (lockAcquired) {
            try {
                redisCommands.expire(lockKey, timeout)
                action()
            } finally {
                if (redisCommands.get(lockKey) == lockValue) {
                    redisCommands.del(lockKey)
                }
            }
        } else {
            println("Lock 사용 불가")
            null
        }
    }
}