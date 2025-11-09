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

    public void agendar(Consulta consulta) {
        Date dataConsulta = Date.valueOf(consulta.getDataHora().toLocalDateTime().toLocalDate());

        if (consultaDAO.pacienteTemConsultaNoDia(consulta.getPaciente().getId(), dataConsulta)) {
            throw new IllegalArgumentException(
                    "Erro: O paciente já possui uma consulta agendada para o dia " + dataConsulta + "."
            );
        }
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
        consultaDAO.deletar(id);
        return true;
    }

    /**
     * Atualiza os dados de uma consulta existente.
     * @param consulta O objeto Consulta com os dados atualizados (o ID deve ser válido).
     * @return O objeto Consulta atualizado ou null se não for encontrado.
     */
    public Consulta atualizar(Consulta consulta) {
        // Verifica se o registro existe no banco de dados
        if (consultaDAO.buscarPorId(consulta.getId()) == null) {
            return null;
        }
        consultaDAO.atualizar(consulta);
        return consulta;
    }
}