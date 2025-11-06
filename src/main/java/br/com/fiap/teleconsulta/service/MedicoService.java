package br.com.fiap.teleconsulta.service;

import br.com.fiap.teleconsulta.dominio.Medico; // Assumindo que a classe Medico está no pacote 'dominio'
import br.com.fiap.teleconsulta.infra.dao.MedicoDAO; // Assumindo que o MedicoDAO está no pacote 'dao'

import java.util.List;

public class MedicoService {

    private MedicoDAO medicoDAO;

    public MedicoService(MedicoDAO medicoDAO) {
        this.medicoDAO = medicoDAO;
    }

    /**
     * Adiciona um novo médico, validando se o CRM já está cadastrado.
     * @param medico O objeto Medico a ser adicionado.
     * @throws IllegalArgumentException se o CRM já existir.
     */
    public void adicionar(Medico medico) {
        // Regra de Negócio Mínima: Verificar se o médico já existe (unicidade do CRM)
        // Precisamos de um método no DAO para buscar por CRM, que você já tem:
        if (medicoDAO.buscarPorCRM(medico.getCrm()) != null) {
            throw new IllegalArgumentException("Erro: O CRM " + medico.getCrm() + " já está cadastrado no sistema.");
        }

        medicoDAO.inserir(medico);
    }

    /**
     * Atualiza os dados de um médico existente.
     * @param medico O objeto Medico com os dados atualizados (o CRM deve ser válido).
     * @return O objeto Medico atualizado ou null se não for encontrado.
     */
    public Medico atualizar(Medico medico) {
        // 1. Verifica se o médico existe antes de atualizar
        if (medicoDAO.buscarPorCRM(medico.getCrm()) == null) {
            return null; // Retorna null para o Controller tratar como 404 NOT FOUND
        }

        // 2. Repassa a chamada para o DAO
        medicoDAO.atualizar(medico);
        return medico;
    }

    /**
     * Deleta um médico por CRM.
     * @param crm O CRM do médico a ser deletado.
     * @return true se o médico foi deletado, false caso não tenha sido encontrado.
     */
    public boolean deletar(String crm) {
        // 1. Verifica se o médico existe
        if (medicoDAO.buscarPorCRM(crm) == null) {
            return false;
        }

        // 2. Repassa a chamada para o DAO
        medicoDAO.deletar(crm);
        return true;
    }

    // --- Métodos de Busca (Repassando diretamente) ---

    public List<Medico> buscarTodos() {
        return medicoDAO.listarTodos();
    }

    public Medico buscarPorCrm(String crm) {
        return medicoDAO.buscarPorCRM(crm);
    }
}