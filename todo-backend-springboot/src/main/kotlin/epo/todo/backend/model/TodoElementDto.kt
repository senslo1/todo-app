package epo.todo.backend.model

import epo.todo.backend.domain.TodoElementEntity

data class TodoElementDto(val id: Int? = null, val text: String, val category: String) {
    constructor(entity: TodoElementEntity) : this(entity.id, entity.text, entity.category)
}
