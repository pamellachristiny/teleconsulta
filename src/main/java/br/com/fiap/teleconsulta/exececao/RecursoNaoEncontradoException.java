package br.com.fiap.teleconsulta.exececao;

public class RecursoNaoEncontradoException extends RuntimeException {

    // Construtor padrão que recebe a mensagem de erro
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}