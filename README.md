# Ágata —
## Identificação do Projeto e Equipe

* **Projeto:** Ágata — Gerenciador de Teleconsultas
* **Instituição / Marca:** NEXUMTECH
* **Turmas:** 1TDSPY
* **Felipe Ribeiro Salles de Camargo - 565224
Pamella Christiny Chaves Brito - 565206**
* **Local e Ano:** São Paulo, 2025
---

## Objetivo e Escopo do Projeto

O **Ágata** é uma solução que facilita o acesso à telemedicina para pacientes com limitações motoras, cognitivas ou dificuldades no uso de tecnologia. Em vez de forçar o paciente a navegar por sistemas complexos, o Ágata oferece notificações e atalhos diretos para que o usuário entre rapidamente nas salas de teleconsulta ou salas de espera.

O foco principal é: reduzir barreiras, orientar o paciente com interações intuitivas (botões `ENTRAR`) e garantir que o fluxo de agendamento/entrada na teleconsulta seja simples, seguro e robusto.

### Escopo

* Gestão de pacientes, médicos e consultas.
* Agendamento de consultas com verificação de conflitos.
* Notificações para três tipos de interação: fixa/pontual, pré-consulta e consulta atrasada.
* Interface CLI para administração e um componente de simulação de teleconsulta via Swing.

---

## Descrição das Funções

Há três modos de notificação/interação:

1. **Processo de notificação fixa e pontual**

   * Notificação com botão **ENTRAR** que leva diretamente à sala virtual da teleconsulta.

2. **Processo de notificação pré-consulta**

   * Notificação com botão **ENTRAR** que leva para a **sala de espera virtual** (aguarda liberação pelo profissional).

3. **Processo de notificação de consulta atrasada**

   * Notificação com botão **ENTRAR** que leva para a sala virtual — usada quando a consulta já começou, mas o paciente está atrasado.

Cada fluxo tem regras de negócio específicas (válidações de horário, estado da consulta, duração e atualização de status).

---

## Endpoints (API REST)
https://teleconsultajava.onrender.com

A seguir, a tabela resumida com os recursos expostos pela API e os códigos de resposta esperados.

| Recurso  | Método HTTP | URI               | Descrição                                | Sucesso          | Erro / Regra de Negócio |
| -------- | ----------- | ----------------- | ---------------------------------------- | ---------------- | ----------------------- |
| Consulta | POST        | `/consultas`      | Agenda nova consulta (verifica conflito) | `201 Created`    | `409 Conflict`, `500`   |
| Consulta | GET         | `/consultas`      | Lista todas as consultas agendadas       | `200 OK`         | `404 Not Found`         |
| Consulta | DELETE      | `/consultas/{id}` | Cancela/deleta consulta por ID           | `204 No Content` | `404 Not Found`         |
| Paciente | POST        | `/pacientes`      | Adiciona novo paciente (CPF único)       | `201 Created`    | `409 Conflict`          |
| Paciente | GET         | `/pacientes/{id}` | Busca paciente por ID                    | `200 OK`         | `404 Not Found`         |
| Paciente | PUT         | `/pacientes/{id}` | Atualiza todos os dados do paciente      | `200 OK`         | `404 Not Found`         |
| Paciente | DELETE      | `/pacientes/{id}` | Remove paciente pelo ID                  | `204 No Content` | `404 Not Found`         |
| Médico   | POST        | `/medicos`        | Adiciona novo médico (CRM único)         | `201 Created`    | `409 Conflict`          |
| Médico   | GET         | `/medicos`        | Lista todos os médicos                   | `200 OK`         | `404 Not Found`         |
| Médico   | GET         | `/medicos/{crm}`  | Busca médico pelo CRM                    | `200 OK`         | `404 Not Found`         |
| Médico   | PUT         | `/medicos/{crm}`  | Atualiza dados do médico pelo CRM        | `200 OK`         | `404 Not Found`         |
| Médico   | DELETE      | `/medicos/{crm}`  | Remove médico pelo CRM                   | `204 No Content` | `404 Not Found`         |

> Observação: ajuste as URIs caso a aplicação esteja servida em contexto (ex.: `/api/v1/consultas`).

---

## Modelo de Dados (MER) e UML

* **MER (Diagrama de Entidade-Relacionamento):** contempla as tabelas `PACIENTE`, `MEDICO` e `CONSULTA` com as chaves estrangeiras e restrições indicadas no script SQL.
* **UML (Diagrama de Classes):** inclua classes como `Paciente`, `Medico`, `Consulta`, DAOs (`PacienteDAO`, `MedicoDAO`, `ConsultaDAO`), `ConsultaService` (regras de negócio) e `ConnectionFactory`.

> **Observação:** aqui no repositório adicione as imagens/exportações dos diagramas (por exemplo, `docs/mer.png` e `docs/uml.png`).

---

## Ferramentas, Linguagens e Bibliotecas

**Linguagens & Plataformas**

* Java (principal linguagem)
* Oracle Database (base relacional)

**Bibliotecas / APIs**

* JDBC (conexão com Oracle)
* Java Swing (simulação de interface para iniciar consulta)
* `java.time` (manipulação de datas/horas)
* `java.sql.Timestamp` (interação com tipos SQL)

**IDE / Ferramenta de Desenvolvimento**

* IntelliJ IDEA (recomendado), Eclipse ou VS Code com suporte Java.

---

## Guia de Execução (Passo a Passo)

### 1) Pré-requisitos

* JDK 22 instalado e `JAVA_HOME` / `PATH` configurados.
* Oracle Database acessível.
* Driver JDBC (ojdbc*.jar) adicionado ao classpath do projeto.
* Criar usuário/schema no Oracle e executar os scripts SQL abaixo.

### 2) Configurar conexão

Abra `banco/ConnectionFactory.java` e configure:

```java
String urlDeConexao = "jdbc:oracle:thin:@<host>:<porta>:<SID>";
String login = "<seu_usuario>";
String senha = "<sua_senha>";
```

### 3) Importar projeto na IDE

1. File > Open > selecione a pasta raiz do projeto (Agata).
2. Verifique se `src` está marcado como Source Root.
3. Adicione o JAR do driver JDBC às bibliotecas do projeto.

### 4) Rodar a aplicação

* Abra `app.Main.java` e execute a classe principal (Run).
* A interação principal é via console (menu-driven). Siga as opções apresentadas.

---

## Scripts SQL (Schema)

Copie e execute os scripts abaixo no seu schema Oracle:

```sql
-- SEQUÊNCIAS
CREATE SEQUENCE SEQ_PACIENTE START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE SEQ_CONSULTA START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- TABELAS
CREATE TABLE PACIENTE (
    ID NUMBER PRIMARY KEY,
    NOME VARCHAR2(100) NOT NULL,
    CPF VARCHAR2(14) UNIQUE NOT NULL
);

CREATE TABLE MEDICO (
    CRM VARCHAR2(20) PRIMARY KEY,
    NOME_MEDICO VARCHAR2(100) NOT NULL,
    ESPECIALIDADE_MEDICO VARCHAR2(50) NOT NULL
);

CREATE TABLE CONSULTA (
    ID NUMBER PRIMARY KEY,
    ID_PACIENTE NUMBER NOT NULL,
    CRM_MEDICO VARCHAR2(20) NOT NULL,
    DATA_HORA_CONSULTA TIMESTAMP NOT NULL,
    STATUS VARCHAR2(20) NOT NULL,
    DURACAO NUMBER NOT NULL,
    CONSTRAINT FK_CONSULTA_PACIENTE FOREIGN KEY (ID_PACIENTE) REFERENCES PACIENTE(ID),
    CONSTRAINT FK_CONSULTA_MEDICO FOREIGN KEY (CRM_MEDICO) REFERENCES MEDICO(CRM)
);


*Arquivo gerado automaticamente — versão README atualizada para o projeto Ágata.*
