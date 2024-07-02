package com.teamsparta.todo.domain.todo.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.BooleanExpression
import com.teamsparta.todo.domain.todo.model.QTodo
import com.teamsparta.todo.domain.todo.model.Todo
import com.teamsparta.todo.domain.user.model.QUser
import com.teamsparta.todo.infra.querydsl.QueryDslSupport
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class TodoRepositoryImpl : QueryDslSupport(), CustomTodoRepository {

    private val todo = QTodo.todo
    private val user = QUser.user



    override fun searchTodoList(
        pageable: Pageable,
        title: String?,
        author: String?,
        status: Boolean?,
        daysAgo: Long?
    ): Page<Todo> {

        val totalCount = queryFactory.select(todo.count())
            .from(todo)
            .where(
                titleContains(title),
                authorEq(author),
                statusEq(status),
                createdAfter(daysAgo)
            )
            .fetchOne() ?: 0L

        val sort = if(pageable.sort.isSorted){
            when(pageable.sort.first()?.property){
                "writeDate" -> todo.writeDate.asc()
                "title" -> todo.id.asc()
                else -> todo.id.desc()
            }
        }else{
            todo.id.desc()
        }

        val todoList = queryFactory.selectFrom(todo)
            .leftJoin(todo.user, user).fetchJoin()
            .where(
                titleContains(title),
                authorEq(author),
                statusEq(status),
                createdAfter(daysAgo)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(sort)
            .fetch()


        return PageImpl(todoList, pageable, totalCount)
    }

    private fun titleContains(title: String?): BooleanExpression? {
        return if (title != null) {
            todo.title.containsIgnoreCase(title)
        } else {
            null
        }
    }

    private fun authorEq(author: String?): BooleanExpression? {
        return if (author != null) {
            todo.user.nickname.eq(author)
        } else {
            null
        }
    }

    private fun statusEq(status: Boolean?): BooleanExpression? {
        return if (status != null) {
            todo.status.eq(status)
        } else {
            null
        }
    }

    private fun createdAfter(daysAgo: Long?): BooleanExpression? {
        return if (daysAgo != null) {
            todo.writeDate.after(LocalDateTime.now().minusDays(daysAgo))
        } else {
            null
        }
    }





    override fun findByPageableAndStatus(pageable: Pageable, status: Boolean?): Page<Todo> {
        val whereClause = BooleanBuilder()
        status?.let{ whereClause.and(todo.status.eq(status))}

        val totalCount = queryFactory.select(todo.count()).from(todo).where(whereClause).fetchOne() ?: 0L

        val query = queryFactory.selectFrom(todo)
            .where(whereClause)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        if(pageable.sort.isSorted){
            when(pageable.sort.first()?.property){
                "writeDate" -> query.orderBy(todo.writeDate.asc())
                "title" -> query.orderBy(todo.id.asc())
                else -> query.orderBy(todo.id.desc())
            }
        }

        val contents = query.fetch()

        return PageImpl(contents, pageable, totalCount)
    }


}