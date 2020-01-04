package epo.todo.backend.service

import epo.todo.backend.domain.TodoElementEntity
import epo.todo.backend.exception.BadRequestException
import epo.todo.backend.model.TodoElementDto
import epo.todo.backend.repository.TodoElementRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TodoElementService(val todoElementRepository: TodoElementRepository) {

    fun create(todoElementDto: TodoElementDto): TodoElementDto {
        if (todoElementRepository.findByCategoryAndText(todoElementDto.category,
                                                        todoElementDto.text).isNotEmpty()) {
            throw BadRequestException("Todo element with the same category and text already exists.")
        }
        return upsert(todoElementDto)
    }

    fun update(id: Int, todoElementDto: TodoElementDto): TodoElementDto {
        if (todoElementDto.id == null) {
            throw BadRequestException("Id of todo element must be specified in the body.")
        }
        if (todoElementDto.id != id) {
            throw BadRequestException("Id of todo element must match the id of the path.")
        }

        return upsert(todoElementDto)
    }

    fun get(category: String?): List<TodoElementDto> {
        val todoElements: List<TodoElementEntity> =
                when {
                    category == null -> todoElementRepository.findAll()
                    else -> todoElementRepository.findAllByCategory(category)
                }
        return todoElements.map { TodoElementDto(it) }
    }

    private fun upsert(todoElementDto: TodoElementDto): TodoElementDto {
        val todoElementEntity = TodoElementEntity(todoElementDto)
        return TodoElementDto(todoElementRepository.save(todoElementEntity))
    }

    fun delete(id: Int) {
        todoElementRepository.deleteById(id)
    }
}
