package br.com.fiap.teleconsulta.service;

import br.com.fiap.teleconsulta.infra.dao.ConsultaDAO; // Assumindo que o ConsultaDAO está no pacote 'dao'
import br.com.fiap.teleconsulta.dominio.Consulta; // Assumindo que a classe Consulta está no pacote 'dominio'
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class ConsultaService {

    private ConsultaDAO consultaDAO;

    // Você pode precisar de outros DAOs para buscar Paciente e Medico,
    // mas vamos simplificar focando apenas nas regras de Consulta
    // private PacienteDAO pacienteDAO;
    // private MedicoDAO medicoDAO;

    public ConsultaService(ConsultaDAO consultaDAO) {
        this.consultaDAO = consultaDAO;
        // this.pacienteDAO = pacienteDAO;
        // this.medicoDAO = medicoDAO;
    }

    /**
     * Adiciona uma nova consulta após aplicar as regras de negócio (disponibilidade).
     * @param consulta O objeto Consulta a ser agendado.
     * @throws IllegalArgumentException se o horário estiver indisponível.
     */
    public void agendar(Consulta consulta) {

        // --- 1. Regra de Negócio: Verificação de Conflito com Paciente ---

        // Converte a data de agendamento para java.sql.Date, ignorando a hora (como no seu DAO)
        Date dataConsulta = Date.valueOf(consulta.getDataHora().toLocalDateTime().toLocalDate());

        if (consultaDAO.pacienteTemConsultaNoDia(consulta.getPaciente().getId(), dataConsulta)) {
            throw new IllegalArgumentException(
                    "Erro: O paciente já possui uma consulta agendada para o dia " + dataConsulta + "."
            );
        }

        // --- 2. Regra de Negócio: Verificação de Conflito com Médico ---

        // Verifica o horário exato (Timestamp)
        Timestamp dataHoraConsulta = consulta.getDataHora();

        if (consultaDAO.medicoTemConsultaNoHorario(consulta.getMedico().getCrm(), dataHoraConsulta)) {
            throw new IllegalArgumentException(
                    "Erro: O médico " + consulta.getMedico().getCrm() + " já possui uma consulta agendada neste horário exato."
            );
        }

        // --- 3. Persistência ---
        // Se as validações passarem, insere no banco.
        consultaDAO.inserir(consulta);
    }

    // --- Outras Operações CRUD (Simples) ---

    public List<Consulta> buscarTodas() {
        return consultaDAO.listarTodos();
    }

    public Consulta buscarPorId(int id) {
        return consultaDAO.buscarPorId(id);
    }

    /**
     * Deleta uma consulta por ID.
     * @param id O ID da consulta a ser deletada.
     * @return true se deletada, false se não encontrada.
     */
    public boolean cancelar(int id) {
        // Regra de Negócio: Poderia haver uma verificação de antecedência mínima para cancelar
        Consulta consulta = consultaDAO.buscarPorId(id);

        if (consulta == null) {
            return false;
        }

        // Se passar nas regras de negócio, repassa para o DAO
        consultaDAO.deletar(id);
        return true;
    }

    // O método 'atualizar' seria implementado aqui, com validações de horário semelhantes ao 'agendar'.
}