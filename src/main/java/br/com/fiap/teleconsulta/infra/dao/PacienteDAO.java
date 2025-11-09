package br.com.fiap.teleconsulta.infra.dao;

import br.com.fiap.teleconsulta.dominio.Paciente;
import jakarta.enterprise.context.ApplicationScoped; // CDI

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped // Torna a classe injetável
public class PacienteDAO {

    private static final List<Paciente> pacientes = new ArrayList<>();

    public List<Paciente> buscarTodos() {
        return new ArrayList<>(pacientes);
    }

    public Paciente buscarPorCPF(String cpf) {
        return pacientes.stream()
                .filter(p -> p.getCpf().equalsIgnoreCase(cpf))
                .findFirst()
                .orElse(null);
    }

    public void inserir(Paciente paciente) {
        pacientes.add(paciente);
    }

    public boolean deletar(String cpf) {
        // Retorna true se um elemento foi removido
        return pacientes.removeIf(p -> p.getCpf().equalsIgnoreCase(cpf));
    }

    public Paciente atualizar(Paciente paciente) {
        for (int i = 0; i < pacientes.size(); i++) {
            if (pacientes.get(i).getCpf().equalsIgnoreCase(paciente.getCpf())) {
                pacientes.set(i, paciente);
                return paciente;
            }
        }
        return null; // Retorna null se não encontrou
    }
}