package br.com.fiap.teleconsulta.service;

import br.com.fiap.teleconsulta.dominio.Medico;
import br.com.fiap.teleconsulta.infra.dao.MedicoDAO;

import java.util.List;

public class MedicoService {

    private final MedicoDAO medicoDAO;

    public MedicoService(MedicoDAO medicoDAO) {
        this.medicoDAO = medicoDAO;
    }

    public List<Medico> listarTodos() {
        return medicoDAO.buscarTodos();
    }

    public Medico buscarPorCRM(String crm) {
        return medicoDAO.buscarPorCRM(crm);
    }

    public void adicionar(Medico medico) {
        medicoDAO.inserir(medico);
    }

    public boolean deletar(String crm) {
        return medicoDAO.deletar(crm);
    }

    public Medico atualizar(Medico medico) {
        return medicoDAO.atualizar(medico);
    }
}
