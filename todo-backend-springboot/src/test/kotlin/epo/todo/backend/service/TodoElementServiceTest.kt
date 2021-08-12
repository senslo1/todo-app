package epo.todo.backend.service

import epo.todo.backend.domain.TodoElementEntity
import epo.todo.backend.error.exception.BadRequestException
import epo.todo.backend.model.TodoElementDto
import epo.todo.backend.repository.TodoElementRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
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
class TodoElementServiceTest {

    @InjectMocks
    lateinit var todoElementService: TodoElementService

    @Mock
    lateinit var repository: TodoElementRepository

    @Test
    fun `get invokes repository`() {
        `when`(repository.findAll()).thenReturn(emptyList())
        `when`(repository.findAllByCategory(anyString())).thenReturn(emptyList())

        todoElementService.get(null)
        todoElementService.get("House chores")

        verify(repository).findAll()
        verify(repository).findAllByCategory(anyString())
    }

    @Test
    fun `get converts and returns result`() {
        val todoElementEntity = TodoElementEntity("Gain 20 pounds", "Lifestyle")
        `when`(repository.findAll()).thenReturn(listOf(todoElementEntity))

        val retrievedTodoList = todoElementService.get(null)
        assertNotNull(retrievedTodoList)

        val retrievedTodo = retrievedTodoList[0]
        assertNotNull(retrievedTodo)
        assertEquals(todoElementEntity.text, retrievedTodo.text)
        assertEquals(todoElementEntity.category, retrievedTodo.category)
    }

    @Test
    fun `get by category converts and returns result`() {
        val todoElementEntity = TodoElementEntity("Gain 20 pounds", "Lifestyle")
        `when`(repository.findAllByCategory(anyString())).thenReturn(listOf(todoElementEntity))

        val retrievedTodoList = todoElementService.get("Lifestyle")
        assertNotNull(retrievedTodoList)

        val retrievedTodo = retrievedTodoList[0]
        assertNotNull(retrievedTodo)
        assertEquals(todoElementEntity.text, retrievedTodo.text)
        assertEquals(todoElementEntity.category, retrievedTodo.category)
    }

    @Test
    fun `get by category converts and returns empty list`() {
        `when`(repository.findAllByCategory(anyString())).thenReturn(emptyList())

        val retrievedTodoList = todoElementService.get("Lifestyle")
        assertNotNull(retrievedTodoList)
        assertTrue(retrievedTodoList.isEmpty())
    }

    @Test
    fun `get converts and returns empty list`() {
        `when`(repository.findAll()).thenReturn(emptyList())

        val retrievedTodoList = todoElementService.get(null)
        assertNotNull(retrievedTodoList)
        assertTrue(retrievedTodoList.isEmpty())
    }

    @Test
    fun `create invokes repository`() {
        val todoElementDto = TodoElementDto(text = "Gain 20 pounds", category = "Lifestyle")
        `when`(repository.save(any(TodoElementEntity::class.java)))
                .thenReturn(TodoElementEntity("Gain 20 pounds", "Lifestyle"))

        todoElementService.create(todoElementDto)

        verify(repository).save(any(TodoElementEntity::class.java))
    }

    @Test
    fun `create returns created todo`() {
        val todoElementDto = TodoElementDto(text = "Gain 20 pounds", category = "Lifestyle")

        `when`(repository.save(any(TodoElementEntity::class.java)))
                .thenReturn(TodoElementEntity("Gain 20 pounds", "Lifestyle"))

        val createdTodo = todoElementService.create(todoElementDto)

        assertEquals(todoElementDto.text, createdTodo.text)
        assertEquals(todoElementDto.category, createdTodo.category)
    }

    @Test
    fun `update invokes repository`() {
        val todoElementDto = TodoElementDto(id = 1, text = "Gain 20 pounds", category = "Lifestyle")

        `when`(repository.existsById(anyInt())).thenReturn(true)
        `when`(repository.save(any(TodoElementEntity::class.java)))
                .thenReturn(TodoElementEntity("Gain 20 pounds", "Lifestyle"))

        todoElementService.update(1, todoElementDto)
        verify(repository).save(any(TodoElementEntity::class.java))
    }

    @Test
    fun `update returns updated todo`() {
        val todoElementDto = TodoElementDto(id = 1, text = "Gain 20 pounds", category = "Lifestyle")

        `when`(repository.existsById(anyInt())).thenReturn(true)
        `when`(repository.save(any(TodoElementEntity::class.java)))
                .thenReturn(TodoElementEntity("Gain 20 pounds", "Lifestyle"))

        val updatedTodo = todoElementService.update(1, todoElementDto)

        assertEquals(todoElementDto.text, updatedTodo.text)
        assertEquals(todoElementDto.category, updatedTodo.category)
    }

    @Test
    fun `update todo without id in body should throw bad request`() {
        val todoElementDto = TodoElementDto(id = null, text = "Complete Half-Life", category = "Gaming")

        Assertions.assertThrows(BadRequestException::class.java) {
            todoElementService.update(1, todoElementDto)
        }
    }

    @Test
    fun `update todo with mismatching id in body should throw bad request`() {
        val todoElementDto = TodoElementDto(id = 3, text = "Buy new gaming rig for HL:Alyx", category = "Gaming")

        Assertions.assertThrows(BadRequestException::class.java) {
            todoElementService.update(1, todoElementDto)
        }
    }

    @Test
    fun `delete invokes repository`() {
        doNothing().`when`(repository).deleteById(anyInt())
        `when`(repository.existsById(anyInt())).thenReturn(true)

        todoElementService.delete(1)
        verify(repository).deleteById(1)
        verify(repository).existsById(1)
    }
}
