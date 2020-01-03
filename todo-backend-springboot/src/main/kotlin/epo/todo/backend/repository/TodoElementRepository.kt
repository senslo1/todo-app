package epo.todo.backend.repository

import epo.todo.backend.domain.TodoElementEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TodoElementRepository : JpaRepository<TodoElementEntity, Int> {
    fun findAllByCategory(category: String): List<TodoElementEntity>
}
