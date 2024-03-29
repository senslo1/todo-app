package epo.todo.backend.controller.integration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import epo.todo.backend.TodoApplication
import epo.todo.backend.fixtures.TodoElementDtoFixtures
import epo.todo.backend.model.TodoElementDto
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.lessThan
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@AutoConfigureMockMvc
@SpringBootTest(classes = [TodoApplication::class])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TodoIT {

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var dtoFixtures: TodoElementDtoFixtures

    val objectMapper = jacksonObjectMapper()

    private fun createTodo(todoElementDto: TodoElementDto): ResultActions = mvc.perform(
        MockMvcRequestBuilders.post(TODO_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todoElementDto))
    )

    private fun createMultipleTodos(todoElementDtoList: List<TodoElementDto>) =
        todoElementDtoList.forEachIndexed { index, todo ->
            createTodo(todo)
                    .andExpect(MockMvcResultMatchers.status().isOk)
                    .andExpect(jsonPath("$.text", `is`(todo.text)))
                    .andExpect(jsonPath("$.category", `is`(todo.category)))
                    .andExpect(jsonPath("$.id", `is`(index + 1)))
        }

    private fun updateTodo(todoElementDto: TodoElementDto): ResultActions =
        mvc.perform(
            MockMvcRequestBuilders.put("${TODO_PATH}/{id}", todoElementDto.id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(todoElementDto))
        )

    private fun getTodos() = mvc.perform(
        MockMvcRequestBuilders
                .get(TODO_PATH)
                .accept(MediaType.APPLICATION_JSON)
    )

    private fun getTodosWithCategory(category: String): ResultActions {
        return mvc.perform(
            MockMvcRequestBuilders
                    .get(TODO_PATH)
                    .accept(MediaType.APPLICATION_JSON)
                    .queryParam("category", category)
        )
    }

    @Test
    fun `get todos with empty database should return 204`() {
        getTodos().andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    @Test
    fun `get todos with invalid origin should return 403`() {
        mvc.perform(
            MockMvcRequestBuilders
                    .get(TODO_PATH)
                    .header("Origin", "https://disallowed-domain.com")
                    .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isForbidden)
                .andExpect(MockMvcResultMatchers.content().string("Invalid CORS request"))
    }

    @Test
    fun `get should return all todos`() {
        val todoElementDtoList = dtoFixtures.todoElementDtoListWithTwoCategories()

        createMultipleTodos(todoElementDtoList)

        getTodos().andExpect(jsonPath("$.length()", `is`(todoElementDtoList.size)))
    }

    @Test
    fun `get by category should only return todos with matching category`() {
        val todoElementDtoList = dtoFixtures.todoElementDtoListWithTwoCategories()

        createMultipleTodos(todoElementDtoList)

        getTodosWithCategory("Skateboarding")
                .andExpect(jsonPath("$.length()", `is`(lessThan(todoElementDtoList.size))))
                // when filtering away todos whose category is not 'Skateboarding', there should be no todos left
                .andExpect(jsonPath("$[*][?(@.category != 'Skateboarding')]").isEmpty)
    }

    @Test
    fun `get by category null should return bad request`() {
        mvc.perform(
            MockMvcRequestBuilders
                    .get("${TODO_PATH}?category=null")
                    .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `post with malformed body should return bad request`() {
        val malformedTodo = dtoFixtures.malformedTodoAsJsonString()
        mvc.perform(
            MockMvcRequestBuilders.post(TODO_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(malformedTodo)
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `get by category should return empty list when no todos match`() {
        val todoElementDtoList = dtoFixtures.todoElementDtoListWithTwoCategories()

        createMultipleTodos(todoElementDtoList)

        mvc.perform(
            MockMvcRequestBuilders
                    .get(TODO_PATH)
                    .accept(MediaType.APPLICATION_JSON)
                    .queryParam("category", "Physical exercise")
        )
                .andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    @Test
    fun `create todo should return 200 and then exist in the database`() {
        val todoElementDto = dtoFixtures.simpleTodoElementDto()

        createTodo(todoElementDto)
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(jsonPath("$.text", `is`(todoElementDto.text)))
                .andExpect(jsonPath("$.category", `is`(todoElementDto.category)))
                .andExpect(jsonPath("$.id", `is`(1)))

        getTodos()
                .andExpect(jsonPath("$[0].length()", `is`(3)))
                .andExpect(jsonPath("$[0].text", `is`(todoElementDto.text)))
                .andExpect(jsonPath("$[0].category", `is`(todoElementDto.category)))
                .andExpect(jsonPath("$[0].id", `is`(1)))
    }

    @Test
    fun `create two todos, both should exist in the database`() {
        val todoElementDtoList = dtoFixtures.simpleTodoElementDtoList()

        createTodo(todoElementDtoList[0])
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(jsonPath("$.text", `is`(todoElementDtoList[0].text)))
                .andExpect(jsonPath("$.category", `is`(todoElementDtoList[0].category)))
                .andExpect(jsonPath("$.id", `is`(1)))

        createTodo(todoElementDtoList[1])
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(jsonPath("$.text", `is`(todoElementDtoList[1].text)))
                .andExpect(jsonPath("$.category", `is`(todoElementDtoList[1].category)))
                .andExpect(jsonPath("$.id", `is`(2)))

        getTodos()
                // check response body length
                .andExpect(jsonPath("$.length()", `is`(2)))
                // check element 1
                .andExpect(jsonPath("$[0].length()", `is`(3)))
                .andExpect(jsonPath("$[0].text", `is`(todoElementDtoList[0].text)))
                .andExpect(jsonPath("$[0].category", `is`(todoElementDtoList[0].category)))
                .andExpect(jsonPath("$[0].id", `is`(1)))
                // check element 2
                .andExpect(jsonPath("$[1].text", `is`(todoElementDtoList[1].text)))
                .andExpect(jsonPath("$[1].category", `is`(todoElementDtoList[1].category)))
                .andExpect(jsonPath("$[1].id", `is`(2)))
    }

    @Test
    fun `create two identical todos should return bad request`() {
        val todoElementDto = dtoFixtures.simpleTodoElementDto()

        createTodo(todoElementDto)
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(jsonPath("$.text", `is`(todoElementDto.text)))
                .andExpect(jsonPath("$.category", `is`(todoElementDto.category)))
                .andExpect(jsonPath("$.id", `is`(1)))

        createTodo(todoElementDto)
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `create two todos with identical texts but different categories should return 200`() {
        val todoElementDto = dtoFixtures.simpleTodoElementDto()

        createTodo(todoElementDto)
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(jsonPath("$.text", `is`(todoElementDto.text)))
                .andExpect(jsonPath("$.category", `is`(todoElementDto.category)))
                .andExpect(jsonPath("$.id", `is`(1)))


        val todoElementDtoWithDifferentCategory = todoElementDto.copy(category = "Some other category")
        createTodo(todoElementDtoWithDifferentCategory)
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(jsonPath("$.text", `is`(todoElementDtoWithDifferentCategory.text)))
                .andExpect(jsonPath("$.category", `is`(todoElementDtoWithDifferentCategory.category)))
                .andExpect(jsonPath("$.id", `is`(2)))
    }

    @Test
    fun `update todo should update todo`() {
        val todoElementDto = dtoFixtures.simpleTodoElementDto()

        createTodo(todoElementDto)
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(jsonPath("$.text", `is`(todoElementDto.text)))
                .andExpect(jsonPath("$.category", `is`(todoElementDto.category)))
                .andExpect(jsonPath("$.id", `is`(1)))

        val updateTodoElementDto = todoElementDto.copy(id = 1, text = "${todoElementDto.text} but more thoroughly")

        updateTodo(updateTodoElementDto)
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(jsonPath("$.length()", `is`(3)))
                .andExpect(jsonPath("$.text", `is`(updateTodoElementDto.text)))
                .andExpect(jsonPath("$.category", `is`(updateTodoElementDto.category)))
                .andExpect(jsonPath("$.id", `is`(1)))
    }

    @Test
    fun `update todo with identical todo should be idempotent`() {
        val todoElementDto = dtoFixtures.simpleTodoElementDto()

        createTodo(todoElementDto)
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(jsonPath("$.text", `is`(todoElementDto.text)))
                .andExpect(jsonPath("$.category", `is`(todoElementDto.category)))
                .andExpect(jsonPath("$.id", `is`(1)))

        val updateTodoElementDto = todoElementDto.copy(id = 1, text = "${todoElementDto.text} but more thoroughly")

        updateTodo(updateTodoElementDto)
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(jsonPath("$.length()", `is`(3)))
                .andExpect(jsonPath("$.text", `is`(updateTodoElementDto.text)))
                .andExpect(jsonPath("$.category", `is`(updateTodoElementDto.category)))
                .andExpect(jsonPath("$.id", `is`(1)))

        updateTodo(updateTodoElementDto)
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(jsonPath("$.length()", `is`(3)))
                .andExpect(jsonPath("$.text", `is`(updateTodoElementDto.text)))
                .andExpect(jsonPath("$.category", `is`(updateTodoElementDto.category)))
                .andExpect(jsonPath("$.id", `is`(1)))
    }

    @Test
    fun `update nonexistent todo should return 404`() {
        val todoElementDto = dtoFixtures.simpleTodoElementDto()
        val updateTodoElementDto = todoElementDto.copy(id = 1, text = "Some other activity")

        updateTodo(updateTodoElementDto)
                .andExpect(MockMvcResultMatchers.status().isNotFound)
                .andExpect(jsonPath("$.message", `is`(notNullValue())))
    }

    @Test
    fun `delete nonexistent todo should return 404`() {
        mvc.perform(MockMvcRequestBuilders.delete("${TODO_PATH}/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound)
                .andExpect(jsonPath("$.message", `is`(notNullValue())))
    }

    @Test
    fun `delete todo should return ok assert that todo is deleted`() {
        createTodo(dtoFixtures.simpleTodoElementDto())
        getTodos().andExpect(jsonPath("$.length()", `is`(1)))
        mvc.perform(MockMvcRequestBuilders.delete("${TODO_PATH}/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk)
        getTodos().andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    companion object {
        private const val TODO_PATH = "/api/todo"
    }
}
