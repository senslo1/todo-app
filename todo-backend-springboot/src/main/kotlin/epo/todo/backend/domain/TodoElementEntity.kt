package epo.todo.backend.domain

import epo.todo.backend.model.TodoElementDto
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "TodoElement")
data class TodoElementEntity(val text: String, val category: String) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0

    constructor(todoElementDto: TodoElementDto) : this(todoElementDto.text, todoElementDto.category) {
        if (todoElementDto.id != null) {
            this.id = todoElementDto.id
        }
    }
}
