package br.com.fiap.teleconsulta.infra.dao;

import br.com.fiap.teleconsulta.dominio.Paciente;

import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    private static final List<Paciente> pacientes = new ArrayList<>();

    public List<Paciente> buscarTodos() {
        return new ArrayList<>(pacientes);
    }

    public Paciente buscarPorId(int id) {
        return pacientes.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void inserir(Paciente paciente) {
        pacientes.add(paciente);
    }

    public boolean deletar(int id) {
        return pacientes.removeIf(p -> p.getId() == id);
    }

    public Paciente atualizar(Paciente paciente) {
        for (int i = 0; i < pacientes.size(); i++) {
            if (pacientes.get(i).getId() == paciente.getId()) {
                pacientes.set(i, paciente);
                return paciente;
            }
        }
        return null;
    }
}
