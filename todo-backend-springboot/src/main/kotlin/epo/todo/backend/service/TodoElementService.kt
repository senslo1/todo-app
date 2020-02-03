package epo.todo.backend.service

import epo.todo.backend.domain.TodoElementEntity
import epo.todo.backend.error.exception.BadRequestException
import epo.todo.backend.error.exception.NotFoundException
import epo.todo.backend.model.TodoElementDto
import epo.todo.backend.repository.TodoElementRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TodoElementService(val todoElementRepository: TodoElementRepository) {

    private val log = KotlinLogging.logger {}

    fun create(todoElementDto: TodoElementDto): TodoElementDto {
        if (todoElementRepository.existsByCategoryAndText(todoElementDto.category,
                                                          todoElementDto.text)) {
            throw BadRequestException("A todo element with category=${todoElementDto.category} " +
                                              "and text=${todoElementDto.text} already exists.")
        }
        return upsert(todoElementDto)
                .also { log.info { "Created todo element=$it" } }
    }

    fun update(id: Int, todoElementDto: TodoElementDto): TodoElementDto {
        if (todoElementDto.id == null) {
            throw BadRequestException("Id of todo element must be specified in the body.")
        }
        if (todoElementDto.id != id) {
            throw BadRequestException("Id of todo element must match the id of the path.")
        }

        return upsert(todoElementDto)
                .also { log.info { "Updated todo element=$todoElementDto" } }
    }

    fun get(category: String?): List<TodoElementDto> {
        log.info { "Getting todos with category=$category" }
        val todoElements: List<TodoElementEntity> =
                when (category) {
                    null -> todoElementRepository.findAll()
                    else -> todoElementRepository.findAllByCategory(category)
                }
        return todoElements.map { TodoElementDto(it) }
                .also { log.info { "get by category=$category returned todos=$it" } }
    }

    private fun upsert(todoElementDto: TodoElementDto): TodoElementDto {
        val todoElementEntity = TodoElementEntity(todoElementDto)
        return TodoElementDto(todoElementRepository.save(todoElementEntity))
    }

    fun delete(id: Int) {
        if (!todoElementRepository.existsById(id)) {
            throw NotFoundException("Could not delete todo with id=$id as it does not exist.")
        }

        todoElementRepository.deleteById(id)
                .also { log.info { "Deleted todo with id=$id" } }
    }
}
