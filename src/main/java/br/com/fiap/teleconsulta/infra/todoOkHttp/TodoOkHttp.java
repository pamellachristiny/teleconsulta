package br.com.fiap.teleconsulta.infra.todoOkHttp;

import br.com.fiap.teleconsulta.dominio.Todo;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;

public class TodoOkHttp {

    public Todo enviar(Todo todo){
        OkHttpClient clienteHttp = new OkHttpClient().newBuilder().build();

        Gson gson = new Gson();
        String json = gson.toJson(todo);

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, JSON);

        Request requisicao = new Request.Builder()
                .url("http://jsonplaceholder.typicode.com/todos")
                .method("POST", body)
                .build();

        Todo todoRetornado = null;
        try(Response resposta = clienteHttp.newCall(requisicao).execute()){
            todoRetornado = gson.fromJson(resposta.body().string(), Todo.class);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
        return todoRetornado;
    }
}
