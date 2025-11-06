package br.com.fiap.teleconsulta.infra.todoOkHttp;

import br.com.fiap.teleconsulta.dominio.Todo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TodoOkHttpTest {

    @Test
    void retornarOTodoEnviado(){
        TodoOkHttp todoOkHttp = new TodoOkHttp();

        Todo umTodo = new Todo(1, 201, "devo estudar", false);

        Todo outroTodo = todoOkHttp.enviar(umTodo);

        assertEquals(umTodo, outroTodo);
    }
}