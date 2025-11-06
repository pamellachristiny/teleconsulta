package br.com.fiap.teleconsulta.dominio;

import java.sql.Timestamp;

public class Consulta {
    private int id;
    private Paciente paciente;
    private Medico medico;
    private Timestamp dataHora;
    private String status;
    private int duracao; // duração em minutos, por exemplo

    // Construtor completo
    public Consulta(Paciente paciente, Medico medico, Timestamp dataHora, String status, int duracao) {
        this.paciente = paciente;
        this.medico = medico;
        this.dataHora = dataHora;
        this.status = status;
        this.duracao = duracao;
    }

    // Construtor com id (para consultas já cadastradas)
    public Consulta(int id, Paciente paciente, Medico medico, Timestamp dataHora, String status, int duracao) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.dataHora = dataHora;
        this.status = status;
        this.duracao = duracao;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public Timestamp getDataHora() { return dataHora; }
    public void setDataHora(Timestamp dataHora) { this.dataHora = dataHora; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getDuracao() { return duracao; }
    public void setDuracao(int duracao) { this.duracao = duracao; }

    @Override
    public String toString() {
        return "Consulta{" +
                "id=" + id +
                ", paciente=" + (paciente != null ? paciente.getNome() : "N/A") +
                ", medico=" + (medico != null ? medico.getNome() : "N/A") +
                ", dataHora=" + dataHora +
                ", status='" + status + '\'' +
                ", duracao=" + duracao +
                '}';
    }
}
