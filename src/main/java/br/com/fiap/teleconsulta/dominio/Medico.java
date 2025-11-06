package br.com.fiap.teleconsulta.dominio;

public class Medico { private String crm;
    private String nome;
    private String especialidade;

    public Medico(String crm, String nome, String especialidade) {
        this.crm = crm;
        this.nome = nome;
        this.especialidade = especialidade;
    }

    public String getCrm() {
        return crm;
    }

    public String getNome() {              // use getNome()
        return nome;
    }

    public String getEspecialidade() {     // use getEspecialidade()
        return especialidade;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    @Override
    public String toString() {
        return "Medico{" +
                "crm='" + crm + '\'' +
                ", nome='" + nome + '\'' +
                ", especialidade='" + especialidade + '\'' +
                '}';
    }
}
