package com.teamsparta.todo.infra.redis

import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.sync.RedisCommands
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedisConfig {

    @Bean
    fun redisClient(): RedisClient {
        return RedisClient.create("redis://localhost:6379")
    }

    @Bean
    fun redisConnection(redisClient: RedisClient): StatefulRedisConnection<String, String> {
        return redisClient.connect()
    }

    @Bean
    fun redisCommands(connection: StatefulRedisConnection<String, String>): RedisCommands<String, String> {
        return connection.sync()
    }
}