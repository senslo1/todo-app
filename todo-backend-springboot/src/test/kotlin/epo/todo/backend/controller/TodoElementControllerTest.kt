package epo.todo.backend.controller

import epo.todo.backend.model.TodoElementDto
import epo.todo.backend.service.TodoElementService
import org.hamcrest.Matchers.notNullValue
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
@AutoConfigureMockMvc
class TodoElementControllerTest {

    @InjectMocks
    lateinit var todoElementController: TodoElementController

    @Mock
    lateinit var todoElementService: TodoElementService

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `correlationId from GET request should be returned in response header`() {
        val correlationId = UUID.randomUUID().toString()

        mockMvc.perform(
                MockMvcRequestBuilders.get(TODO_PATH)
                        .header(CORRELATION_ID_HEADER_NAME, correlationId)
        )
                .andExpect(header().string(CORRELATION_ID_HEADER_NAME, correlationId))
    }

    @Test
    fun `generated correlationId should be returned in response header when not provided in request`() {
        mockMvc.perform(MockMvcRequestBuilders.get(TODO_PATH))
                .andExpect(header().string(CORRELATION_ID_HEADER_NAME, `is`(notNullValue())))
    }

    @Test
    fun `correlationId from DELETE request should be returned in error response header`() {
        val correlationId = UUID.randomUUID().toString()
        mockMvc.perform(
                MockMvcRequestBuilders.delete("${TODO_PATH}/{id}", 1)
                        .header(CORRELATION_ID_HEADER_NAME, correlationId)
        )
                .andExpect(MockMvcResultMatchers.status().isNotFound)
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty)
                .andExpect(header().string(CORRELATION_ID_HEADER_NAME, correlationId))
    }

    @Test
    fun `get with category filter should return todo`() {
        val todoElementDto = TodoElementDto(text = "Clean the kitchen", category = "House chores")
        `when`(todoElementService.get("House chores")).thenReturn(listOf(todoElementDto))

        val result = todoElementController.get("House chores")
        verify(todoElementService).get("House chores")
        assertTrue(result.hasBody())
        assertEquals(1, result.body?.size)

        val todoResult = result.body?.get(0)
        assertNotNull(todoResult)
        assertEquals(todoElementDto, todoResult)
    }

    @Test
    fun `get without category filter should return todo`() {
        val todoElementDto = TodoElementDto(text = "Learn to kickflip", category = "Skateboarding")

        `when`(todoElementService.get(null)).thenReturn(listOf(todoElementDto))

        val result = todoElementController.get(null)
        verify(todoElementService).get(null)
        assertTrue(result.hasBody())
        assertEquals(1, result.body?.size)

        val todoResult = result.body?.get(0)
        assertNotNull(todoResult)
        assertEquals(todoElementDto, todoResult)
    }

    @Test
    fun `update todo should call service and return element`() {
        val todoElementDto = TodoElementDto(id = 1, text = "Reach global elite", category = "Gaming")
        `when`(todoElementService.update(1, todoElementDto)).thenReturn(todoElementDto)

        val result = todoElementController.update(todoElementDto, 1)
        verify(todoElementService).update(1, todoElementDto)

        assertTrue(result.hasBody())
        val todoResult = result.body
        assertNotNull(todoResult)
        assertEquals(todoElementDto, todoResult)
    }

    @Test
    fun `create todo should call service and return element`() {
        val todoElementDto = TodoElementDto(text = "Reach global elite", category = "Gaming")
        val todoElementReturned = TodoElementDto(id = 1, text = "Reach global elite", category = "Gaming")
        `when`(todoElementService.create(todoElementDto)).thenReturn(todoElementReturned)

        val result = todoElementController.create(todoElementDto)
        verify(todoElementService).create(todoElementDto)

        assertTrue(result.hasBody())
        val todoResult = result.body
        assertNotNull(todoResult)
        assertEquals(todoElementReturned, todoResult)
    }

    @Test
    fun `delete should call service`() {
        doNothing().`when`(todoElementService).delete(1)
        todoElementController.delete(1)
        verify(todoElementService).delete(1)
    }

    companion object {
        private const val TODO_PATH = "/api/todo"
        private const val CORRELATION_ID_HEADER_NAME = "X-Correlation-Id"
    }
}
