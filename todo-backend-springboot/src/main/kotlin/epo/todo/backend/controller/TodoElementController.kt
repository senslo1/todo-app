package epo.todo.backend.controller

import epo.todo.backend.model.TodoElementDto
import epo.todo.backend.service.TodoElementService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * REST Controller that serves all of the CRUD operations available in this app.
 */
@RestController
@RequestMapping(path = ["/api"])
class TodoElementController(val todoElementService: TodoElementService) {

    @PostMapping(path = ["/todo"])
    fun create(@RequestBody todoElementDto: TodoElementDto): ResponseEntity<TodoElementDto> {
        return ResponseEntity.ok(todoElementService.create(todoElementDto))
    }

    @PutMapping(path = ["/todo/{id}"])
    fun update(@RequestBody todoElementDto: TodoElementDto,
               @PathVariable id: Int): ResponseEntity<TodoElementDto> {
        return ResponseEntity.ok(todoElementService.update(id, todoElementDto))
    }

    @GetMapping(path = ["/todo"])
    fun get(@RequestParam category: String?): ResponseEntity<List<TodoElementDto>> {
        val todos = todoElementService.get(category)
        return when {
            todos.isEmpty() -> ResponseEntity.noContent().build()
            else -> ResponseEntity.ok(todos)
        }
    }

    @DeleteMapping(path = ["/todo/{id}"])
    fun delete(@PathVariable id: Int): ResponseEntity<Unit> {
        todoElementService.delete(id)
        return ResponseEntity.ok().build()
    }
}
