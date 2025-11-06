package br.com.fiap.teleconsulta.service;

import br.com.fiap.teleconsulta.dominio.Paciente; // Assumindo que a classe Paciente está aqui
import br.com.fiap.teleconsulta.infra.dao.PacienteDAO; // Usaremos o DAO diretamente
import java.util.List;

public class PacienteService {

    private PacienteDAO pacienteDAO;

    public PacienteService(PacienteDAO pacienteDAO) {
        this.pacienteDAO = pacienteDAO;
    }

    /**
     * Adiciona um novo paciente, validando apenas se o CPF já existe.
     * @param paciente O objeto Paciente a ser adicionado.
     * @throws IllegalArgumentException se o CPF já estiver cadastrado.
     */
    public void adicionar(Paciente paciente) {
        // Regra de Negócio Mínima: Garantir Unicidade do CPF antes de salvar
        if (pacienteDAO.cpfExiste(paciente.getCpf())) {
            throw new IllegalArgumentException("Erro: O CPF " + paciente.getCpf() + " já está cadastrado no sistema.");
        }

        pacienteDAO.inserir(paciente);
    }

    /**
     * Busca todos os pacientes.
     */
    public List<Paciente> buscarTodos() {
        return pacienteDAO.listarTodos();
    }

    /**
     * Atualiza os dados de um paciente existente.
     * @param paciente O objeto Paciente com os dados atualizados (o ID/CPF deve ser válido).
     */
    public Paciente atualizar(Paciente paciente) {
        // Poderia haver mais validações aqui, mas mantemos o básico
        pacienteDAO.atualizar(paciente);
        return paciente;
    }

    /**
     * Deleta um paciente por ID.
     * @return true se o paciente foi deletado, false caso contrário.
     */
    public boolean deletar(int id) {
        Paciente paciente = pacienteDAO.buscarPorId(id);
        if (paciente == null) {
            return false;
        }
        pacienteDAO.deletar(id);
        return true;
    }
}