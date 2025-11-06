package br.com.fiap.teleconsulta.service;

import br.com.fiap.teleconsulta.infra.dao.ConsultaDAO; // Assumindo que o ConsultaDAO está no pacote 'dao'
import br.com.fiap.teleconsulta.dominio.Consulta; // Assumindo que a classe Consulta está no pacote 'dominio'
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class ConsultaService {

    private ConsultaDAO consultaDAO;
    public ConsultaService(ConsultaDAO consultaDAO) {
        this.consultaDAO = consultaDAO;
    }

    /**
     * Adiciona uma nova consulta após aplicar as regras de negócio (disponibilidade).
     * @param consulta O objeto Consulta a ser agendado.
     * @throws IllegalArgumentException se o horário estiver indisponível.
     */
    public void agendar(Consulta consulta) {

        //1. Regra de Negócio: Verificação de Conflito com Paciente
        Date dataConsulta = Date.valueOf(consulta.getDataHora().toLocalDateTime().toLocalDate());

        if (consultaDAO.pacienteTemConsultaNoDia(consulta.getPaciente().getId(), dataConsulta)) {
            throw new IllegalArgumentException(
                    "Erro: O paciente já possui uma consulta agendada para o dia " + dataConsulta + "."
            );
        }

        // 2. Regra de Negócio: Verificação de Conflito com Médico
        Timestamp dataHoraConsulta = consulta.getDataHora();

        if (consultaDAO.medicoTemConsultaNoHorario(consulta.getMedico().getCrm(), dataHoraConsulta)) {
            throw new IllegalArgumentException(
                    "Erro: O médico " + consulta.getMedico().getCrm() + " já possui uma consulta agendada neste horário exato."
            );
        }
        consultaDAO.inserir(consulta);
    }

    public List<Consulta> buscarTodas() {
        return consultaDAO.listarTodos();
    }

    public Consulta buscarPorId(int id) {
        return consultaDAO.buscarPorId(id);
    }

    public boolean cancelar(int id) {
        Consulta consulta = consultaDAO.buscarPorId(id);
        if (consulta == null) {
            return false;
        }

        // Se passar nas regras de negócio, repassa para o DAO
        consultaDAO.deletar(id);
        return true;
    }
}