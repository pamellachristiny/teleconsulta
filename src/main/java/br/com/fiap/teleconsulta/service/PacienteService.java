package br.com.fiap.teleconsulta.service;

import br.com.fiap.teleconsulta.dominio.Paciente;
import br.com.fiap.teleconsulta.infra.dao.PacienteDAO;
import br.com.fiap.teleconsulta.exececao.RecursoNaoEncontradoException; // Importado
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class PacienteService {

    @Inject
    private PacienteDAO pacienteDAO;

    /**
     * Adiciona um novo paciente. (Sem regra de CPF duplicado, conforme solicitado)
     */
    public void adicionar(Paciente paciente) {
        pacienteDAO.inserir(paciente);
    }

    /**
     * Atualiza os dados de um paciente existente.
     * @throws RecursoNaoEncontradoException se o paciente não for encontrado.
     */
    public Paciente atualizar(Paciente paciente) {
        Paciente pacienteAtualizado = pacienteDAO.atualizar(paciente);

        if (pacienteAtualizado == null) {
            throw new RecursoNaoEncontradoException("Paciente com CPF " + paciente.getCpf() + " não encontrado para atualização.");
        }

        return pacienteAtualizado;
    }

    /**
     * Deleta um paciente por CPF.
     * @throws RecursoNaoEncontradoException se o paciente não for encontrado.
     */
    public void deletar(String cpf) {
        if (!pacienteDAO.deletar(cpf)) {
            throw new RecursoNaoEncontradoException("Paciente com CPF " + cpf + " não encontrado para exclusão.");
        }
    }

    public List<Paciente> buscarTodos() {
        return pacienteDAO.buscarTodos();
    }

    public Paciente buscarPorCpf(String cpf) {
        return pacienteDAO.buscarPorCPF(cpf);
    }
}