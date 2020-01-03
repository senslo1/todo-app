package epo.todo.backend.controller

import epo.todo.backend.model.TodoElementDto
import epo.todo.backend.service.TodoElementService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
@ExtendWith(SpringExtension::class)
class TodoElementControllerTest {

    @InjectMocks
    lateinit var todoElementController: TodoElementController

    @Mock
    lateinit var todoElementService: TodoElementService

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
}
