package br.com.fiap.teleconsulta.infra.dao;

import br.com.fiap.teleconsulta.dominio.Medico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAO {

    // [REMOVIDO] A variável 'private Connection conn;' e o construtor problemático.
    // A conexão será obtida e fechada localmente em cada método.

    public MedicoDAO() {
        // O construtor fica vazio, mantendo o padrão JAX-RS (inicia o DAO sem dependências no construtor)
    }

    // --- C (CREATE) - Inserir Medico ---
    public void inserir(Medico m) {
        String sql = "INSERT INTO MEDICO (CRM, NOME_MEDICO, ESPECIALIDADE_MEDICO) VALUES (?, ?, ?)";

        // Uso de try-with-resources para obter a conexão e garantir o fechamento
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, m.getCrm());
            ps.setString(2, m.getNome());
            ps.setString(3, m.getEspecialidade());
            ps.executeUpdate();
            System.out.println("Médico inserido!");

        } catch (SQLException e) {
            System.err.println("Erro ao inserir médico: " + e.getMessage());
            // Lançar RuntimeException é uma boa prática para DAOs
            throw new RuntimeException("Erro de persistência ao inserir Médico.", e);
        }
    }

    // --- U (UPDATE) - Atualizar Medico ---
    public void atualizar(Medico m) {
        String sql = "UPDATE MEDICO SET NOME_MEDICO = ?, ESPECIALIDADE_MEDICO = ? WHERE CRM = ?";

        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, m.getNome());
            ps.setString(2, m.getEspecialidade());
            ps.setString(3, m.getCrm());
            ps.executeUpdate();
            System.out.println("Médico atualizado!");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar médico: " + e.getMessage());
            throw new RuntimeException("Erro de persistência ao atualizar Médico.", e);
        }
    }

    // --- D (DELETE) - Deletar Medico ---
    public void deletar(String crm) {
        String sql = "DELETE FROM MEDICO WHERE CRM = ?";

        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, crm);
            ps.executeUpdate();
            System.out.println("Médico deletado!");

        } catch (SQLException e) {
            System.err.println("Erro ao deletar médico: " + e.getMessage());
            throw new RuntimeException("Erro de persistência ao deletar Médico.", e);
        }
    }

    // --- R (READ) - Listar Todos ---
    public List<Medico> listarTodos() {
        List<Medico> lista = new ArrayList<>();
        String sql = "SELECT CRM, NOME_MEDICO, ESPECIALIDADE_MEDICO FROM MEDICO";

        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); // Melhor usar PreparedStatement por consistência
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Medico(
                        rs.getString("CRM"),
                        rs.getString("NOME_MEDICO"),
                        rs.getString("ESPECIALIDADE_MEDICO")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar médicos: " + e.getMessage());
            throw new RuntimeException("Erro de persistência ao listar Médicos.", e);
        }
        return lista;
    }

    // --- R (READ) - Buscar por CRM ---
    public Medico buscarPorCRM(String crm) {
        String sql = "SELECT CRM, NOME_MEDICO, ESPECIALIDADE_MEDICO FROM MEDICO WHERE CRM = ?";

        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, crm);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Medico(
                            rs.getString("CRM"),
                            rs.getString("NOME_MEDICO"),
                            rs.getString("ESPECIALIDADE_MEDICO")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar médico: " + e.getMessage());
            throw new RuntimeException("Erro de persistência ao buscar Médico por CRM.", e);
        }
        return null;
    }
}