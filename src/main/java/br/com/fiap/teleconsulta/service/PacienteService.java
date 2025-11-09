package br.com.fiap.teleconsulta.service;

import br.com.fiap.teleconsulta.dominio.Paciente;
import br.com.fiap.teleconsulta.infra.dao.PacienteDAO;

import java.util.List;

public class PacienteService {

    private final PacienteDAO pacienteDAO;

    public PacienteService() {
        this.pacienteDAO = new PacienteDAO(); // ✅ construtor padrão
    }

    public List<Paciente> buscarTodos() {
        return pacienteDAO.buscarTodos();
    }

    public Paciente buscarPorId(int id) {
        return pacienteDAO.buscarPorId(id);
    }

    public void cadastrar(Paciente paciente) {
        pacienteDAO.inserir(paciente);
    }

    public boolean deletar(int id) {
        return pacienteDAO.deletar(id);
    }

    public Paciente atualizar(Paciente paciente) {
        return pacienteDAO.atualizar(paciente);
    }
}
