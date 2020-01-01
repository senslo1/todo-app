package epo.todo.todobackendspringboot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TodoBackendSpringbootApplication

fun main(args: Array<String>) {
	runApplication<TodoBackendSpringbootApplication>(*args)
}
