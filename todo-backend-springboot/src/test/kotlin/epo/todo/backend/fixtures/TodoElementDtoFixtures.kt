package epo.todo.backend.fixtures

import epo.todo.backend.model.TodoElementDto
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class TodoElementDtoFixtures {

    @Bean
    fun simpleTodoElementDto(): TodoElementDto {
        return TodoElementDto(text = "Clean the kitchen", category = "House chores")
    }

    @Bean
    fun simpleTodoElementDtoList(): List<TodoElementDto> {
        return listOf(
                TodoElementDto(text = "Clean the kitchen", category = "House chores"),
                TodoElementDto(text = "Learn to kickflip", category = "Skateboarding")
        )
    }

    @Bean
    fun todoElementDtoListWithTwoCategories(): List<TodoElementDto> {
        return listOf(
                TodoElementDto(text = "Clean the kitchen", category = "House chores"),
                TodoElementDto(text = "Learn to kickflip", category = "Skateboarding"),
                TodoElementDto(text = "Get my first sponsor", category = "Skateboarding")
        )
    }
}
