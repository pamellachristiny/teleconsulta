package br.com.fiap.teleconsulta.service;

import br.com.fiap.teleconsulta.dominio.Medico;
import br.com.fiap.teleconsulta.infra.dao.MedicoDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class MedicoService {

    @Inject
    private MedicoDAO medicoDAO;


    /**
     * Adiciona um novo médico, validando se o CRM já está cadastrado.
     * @param medico O objeto Medico a ser adicionado.
     * @throws IllegalArgumentException se o CRM já existir.
     */
    public void adicionar(Medico medico) {
        // Regra de Negócio Mínima: Verificar se o médico já existe
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
        // Verifica se o médico existe antes de atualizar
        if (medicoDAO.buscarPorCRM(medico.getCrm()) == null) {
            return null;
        }

        medicoDAO.atualizar(medico);
        return medico;
    }

    /**
     * Deleta um médico por CRM.
     * @param crm O CRM do médico a ser deletado.
     * @return true se o médico foi deletado, false caso não tenha sido encontrado.
     */
    public boolean deletar(String crm) {
        if (medicoDAO.buscarPorCRM(crm) == null) {
            return false;
        }

        medicoDAO.deletar(crm);
        return true;
    }
    public List<Medico> buscarTodos() {
        return medicoDAO.buscarTodos();
    }

    public Medico buscarPorCrm(String crm) {
        return medicoDAO.buscarPorCRM(crm);
    }
}