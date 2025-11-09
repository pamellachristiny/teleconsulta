package br.com.fiap.teleconsulta.dominio;

import java.util.Objects;

public class Todo {

    private long userId;
    private long id;
    private String title;
    private boolean completed;

    public Todo(long userId, long id, String title, boolean completed){
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.completed = completed;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Todo todo)) return false;
        return userId == todo.userId && id == todo.id && completed == todo.completed && Objects.equals(title, todo.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, id, title, completed);
    }

    @Override
    public String toString() {
        return "Todo{" +
                "userId=" + userId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", completed=" + completed +
                '}';
    }
}
