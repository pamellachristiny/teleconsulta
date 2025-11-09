package br.com.fiap.teleconsulta.infra.dao;

import br.com.fiap.teleconsulta.dominio.Medico;
import jakarta.enterprise.context.ApplicationScoped; // Adicione esta importação

import java.util.ArrayList;
import java.util.List;

// [CORREÇÃO] Anotação para que o Quarkus gerencie e crie esta instância
@ApplicationScoped
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
        return null;
    }
}