package com.teamsparta.todo.infra.security

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.management.MBeanServer
import javax.management.ObjectName

@Configuration
class JmxConfig {

    @Bean
    fun commandLineRunner(mBeanServer: MBeanServer): CommandLineRunner {
        return CommandLineRunner {
            val mBeans = mBeanServer.queryNames(ObjectName("*:*"), null)
            mBeans.forEach { println(it) }
        }
    }
}
