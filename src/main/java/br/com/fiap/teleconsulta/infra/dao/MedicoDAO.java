package br.com.fiap.teleconsulta.infra.dao;

import br.com.fiap.teleconsulta.dominio.Medico;
import jakarta.enterprise.context.ApplicationScoped; // Necessário para Injeção de Dependência (CDI/Quarkus)

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped // Torna o DAO gerenciado e injetável
public class MedicoDAO {

    // Simulação de banco de dados em memória
    private static final List<Medico> medicos = new ArrayList<>();

    public List<Medico> buscarTodos() {
        return new ArrayList<>(medicos);
    }

    public Medico buscarPorCRM(String crm) {
        return medicos.stream()
                .filter(m -> m.getCrm().equalsIgnoreCase(crm))
                .findFirst()
                .orElse(null);
    }

    public void inserir(Medico medico) {
        medicos.add(medico);
    }

    public boolean deletar(String crm) {
        return medicos.removeIf(m -> m.getCrm().equalsIgnoreCase(crm));
    }

    public Medico atualizar(Medico medico) {
        for (int i = 0; i < medicos.size(); i++) {
            if (medicos.get(i).getCrm().equalsIgnoreCase(medico.getCrm())) {
                medicos.set(i, medico);
                return medico;
            }
        }
        // Retorna null se não encontrou para que o Service possa lançar a exceção
        return null;
    }
}