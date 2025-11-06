package br.com.fiap.teleconsulta.infra.dao;

import br.com.fiap.teleconsulta.dominio.Paciente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    // A ConnectionFactory não precisa de uma classe 'dominio' para ser importada,
    // assumindo que ela está no mesmo pacote ou em um pacote de infra.
    // Você precisa garantir que a classe ConnectionFactory esteja acessível.
    // Usaremos a ConnectionFactory diretamente nos métodos.

    // Removemos 'private Connection conn;' pois a conexão será local em cada método.
    // Removemos o construtor problemático.
    public PacienteDAO() {
        // O construtor está vazio, pois não há estado persistente (conexão)
        // a ser mantido aqui. O código JAX-RS (Controller) não terá mais o problema
        // de NullPointerException ao instanciar o DAO.
    }

    // --- C (CREATE) - Inserir paciente ---
    public void inserir(Paciente paciente) {
        // Assume que SEQ_PACIENTE.NEXTVAL gera o ID
        String sql = "INSERT INTO PACIENTE (ID, NOME, CPF) VALUES (SEQ_PACIENTE.NEXTVAL, ?, ?)";

        // Usando try-with-resources para garantir que a Conexão e o PreparedStatement sejam fechados.
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getCpf());
            stmt.executeUpdate();

        } catch (SQLException e) {
            // Em DAOs, é comum lançar uma RuntimeException para a camada Service tratar.
            System.err.println("Erro ao inserir paciente: " + e.getMessage());
            throw new RuntimeException("Erro de persistência ao inserir Paciente.", e);
        }
    }


    // --- R (READ) - Verifica se CPF já existe ---
    public boolean cpfExiste(String cpf) {
        String sql = "SELECT COUNT(*) FROM PACIENTE WHERE CPF = ?";
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar CPF: " + e.getMessage());
            // Não lançamos RuntimeException aqui, pois o método retorna boolean e o erro é tratado
        }
        return false;
    }

    // --- R (READ) - Listar todos ---
    public List<Paciente> listarTodos() {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT ID, NOME, CPF FROM PACIENTE";
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) { // ResultSet também é AutoCloseable

            while (rs.next()) {
                lista.add(new Paciente(rs.getInt("ID"), rs.getString("NOME"), rs.getString("CPF")));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar pacientes: " + e.getMessage());
            throw new RuntimeException("Erro de persistência ao listar Pacientes.", e);
        }
        return lista;
    }

    // --- R (READ) - Buscar por ID ---
    public Paciente buscarPorId(int id) {
        String sql = "SELECT ID, NOME, CPF FROM PACIENTE WHERE ID = ?";
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Paciente(rs.getInt("ID"), rs.getString("NOME"), rs.getString("CPF"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar paciente por ID: " + e.getMessage());
            throw new RuntimeException("Erro de persistência ao buscar Paciente por ID.", e);
        }
        return null;
    }

    // --- U (UPDATE) - Atualizar paciente ---
    public void atualizar(Paciente paciente) {
        String sql = "UPDATE PACIENTE SET NOME = ?, CPF = ? WHERE ID = ?";
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getCpf());
            stmt.setInt(3, paciente.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar paciente: " + e.getMessage());
            throw new RuntimeException("Erro de persistência ao atualizar Paciente.", e);
        }
    }

    // --- D (DELETE) - Deletar paciente ---
    public void deletar(int id) {
        String sql = "DELETE FROM PACIENTE WHERE ID = ?";
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao deletar paciente: " + e.getMessage());
            throw new RuntimeException("Erro de persistência ao deletar Paciente.", e);
        }
    }
}
