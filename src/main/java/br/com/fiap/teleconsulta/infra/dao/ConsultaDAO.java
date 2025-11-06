package br.com.fiap.teleconsulta.infra.dao;

import br.com.fiap.teleconsulta.dominio.Consulta;
import br.com.fiap.teleconsulta.dominio.Paciente;
import br.com.fiap.teleconsulta.dominio.Medico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO {
    public ConsultaDAO() {

    }

    // --- C (CREATE) - Inserir nova consulta ---
    public void inserir(Consulta c) {
        String sql = "INSERT INTO CONSULTA (ID_PACIENTE, CRM_MEDICO, DATA_HORA_CONSULTA, STATUS, DURACAO) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, c.getPaciente().getId());
            ps.setString(2, c.getMedico().getCrm());
            ps.setTimestamp(3, c.getDataHora());
            ps.setString(4, c.getStatus());
            ps.setInt(5, c.getDuracao());
            ps.executeUpdate();
            System.out.println("Consulta inserida!");

        } catch (SQLException e) {
            System.err.println("Erro ao inserir consulta: " + e.getMessage());
            throw new RuntimeException("Erro de persistência ao inserir Consulta.", e);
        }
    }


    // --- R (READ) - Listar todas as consultas ---
    public List<Consulta> listarTodos() {
        List<Consulta> consultas = new ArrayList<>();
        String sql = "SELECT c.ID, c.ID_PACIENTE, c.CRM_MEDICO, c.DATA_HORA_CONSULTA, c.STATUS, c.DURACAO, " +
                "p.NOME AS NOME_PACIENTE, m.NOME_MEDICO, m.ESPECIALIDADE_MEDICO " +
                "FROM CONSULTA c " +
                "JOIN PACIENTE p ON c.ID_PACIENTE = p.ID " +
                "JOIN MEDICO m ON c.CRM_MEDICO = m.CRM";

        // Usando PreparedStatement para garantir que todos os recursos AutoCloseable sejam fechados
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Consulta c = new Consulta(
                        new Paciente(rs.getInt("ID_PACIENTE"), rs.getString("NOME_PACIENTE"), null),
                        new Medico(rs.getString("CRM_MEDICO"), rs.getString("NOME_MEDICO"), rs.getString("ESPECIALIDADE_MEDICO")),
                        rs.getTimestamp("DATA_HORA_CONSULTA"),
                        rs.getString("STATUS"),
                        rs.getInt("DURACAO")
                );
                c.setId(rs.getInt("ID"));
                consultas.add(c);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar consultas: " + e.getMessage());
            throw new RuntimeException("Erro de persistência ao listar Consultas.", e);
        }

        return consultas;
    }


    // --- D (DELETE) - Deletar consulta pelo ID ---
    public void deletar(int id) {
        String sql = "DELETE FROM CONSULTA WHERE ID = ?";

        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Consulta deletada com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao deletar consulta: " + e.getMessage());
            throw new RuntimeException("Erro de persistência ao deletar Consulta.", e);
        }
    }

    // --- R (READ) - Buscar consulta por ID ---
    public Consulta buscarPorId(int id) {
        String sql = "SELECT c.ID, c.ID_PACIENTE, c.CRM_MEDICO, c.DATA_HORA_CONSULTA, c.STATUS, c.DURACAO, " +
                "p.NOME AS NOME_PACIENTE, m.NOME_MEDICO, m.ESPECIALIDADE_MEDICO " +
                "FROM CONSULTA c " +
                "JOIN PACIENTE p ON c.ID_PACIENTE = p.ID " +
                "JOIN MEDICO m ON c.CRM_MEDICO = m.CRM " +
                "WHERE c.ID = ?";

        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Consulta c = new Consulta(
                            new Paciente(rs.getInt("ID_PACIENTE"), rs.getString("NOME_PACIENTE"), null),
                            new Medico(rs.getString("CRM_MEDICO"), rs.getString("NOME_MEDICO"), rs.getString("ESPECIALIDADE_MEDICO")),
                            rs.getTimestamp("DATA_HORA_CONSULTA"),
                            rs.getString("STATUS"),
                            rs.getInt("DURACAO")
                    );
                    c.setId(rs.getInt("ID"));
                    return c;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar consulta: " + e.getMessage());
            throw new RuntimeException("Erro de persistência ao buscar Consulta por ID.", e);
        }
        return null;
    }


    // --- Verificações de Agendamento ---

    // Verifica se o paciente tem consulta no dia
    public boolean pacienteTemConsultaNoDia(int idPaciente, Date data) {
        // TRUNC() é específico para Oracle/Postgres. Se estiver usando MySQL, use DATE().
        String sql = "SELECT COUNT(*) FROM CONSULTA WHERE ID_PACIENTE = ? AND TRUNC(DATA_HORA_CONSULTA) = ?";

        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPaciente);
            stmt.setDate(2, data);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar consulta do paciente: " + e.getMessage());
            throw new RuntimeException("Erro de persistência ao verificar agendamento.", e);
        }
        return false;
    }

    // Verifica se o médico tem consulta no horário exato
    public boolean medicoTemConsultaNoHorario(String crm, Timestamp dataHora) {
        String sql = "SELECT COUNT(*) FROM CONSULTA WHERE CRM_MEDICO = ? AND DATA_HORA_CONSULTA = ?";

        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, crm);
            stmt.setTimestamp(2, dataHora);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar consulta do médico: " + e.getMessage());
            throw new RuntimeException("Erro de persistência ao verificar agendamento.", e);
        }
        return false;
    }
}