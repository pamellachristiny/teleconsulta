# 🩺 Teleconsulta API RESTful - NEXUMTECH

## 📝 Visão Geral do Projeto

Este projeto consiste em uma API RESTful desenvolvida em **Java** utilizando a especificação **JAX-RS (Jakarta RESTful Web Services)**. O objetivo é fornecer a camada de serviços para o sistema de agendamento de consultas médicas **NEXUMTECH**, gerenciando recursos como Consultas, Pacientes e Médicos.

A arquitetura adota o padrão de **Três Camadas** (Controller, Service, DAO) para garantir a separação de responsabilidades e a aplicação das regras de negócio (como a verificação de conflito de horário).

## 🚀 Tecnologias Utilizadas

| Tecnologia | Versão/Tipo | Função |
| :--- | :--- | :--- |
| **Linguagem** | Java (JDK 17+) | Desenvolvimento principal da aplicação. |
| **Framework Web** | JAX-RS (Jakarta RESTful Web Services) | Construção da API RESTful (Camada Controller). |
| **Gerenciador de Dependências**| Maven | Gestão de bibliotecas e automatização do build. |
| **Banco de Dados** | Oracle Database (Via JDBC) | Persistência dos dados. |
| **Implantação (Deploy)**| Render | Plataforma Cloud para hospedagem contínua (CI/CD). |

## 🏗️ Arquitetura e Camadas

| Camada | Classes Principais | Responsabilidade |
| :--- | :--- | :--- |
| **Controller** | `ConsultaController`, `PacienteController`, `MedicoController` | Recebe requisições HTTP, trata a serialização JSON e retorna as respostas (códigos HTTP). |
| **Service** | `ConsultaService`, `PacienteService`, `MedicoService` | Implementa as **Regras de Negócio** (ex: lógica de agendamento, verificação de conflitos) e coordena a operação. |
| **DAO/Infra** | `ConsultaDAO`, `ConnectionFactory`, etc. | Gerencia o acesso ao banco de dados (JDBC) e executa o CRUD (SQL). Utiliza `try-with-resources` para fechar conexões. |
| **Domínio** | `Consulta`, `Paciente`, `Medico` | Entidades de negócio (POJOs). |

## ⚙️ Endpoints da API

A API é acessível através dos seguintes URIs:

| Recurso | Método HTTP | URI | Descrição |
| :--- | :--- | :--- | :--- |
| **Consulta** | `POST` | `/consultas` | Agenda uma nova consulta, aplicando regras de negócio (conflito de horário). |
| **Consulta** | `GET` | `/consultas` | Lista todas as consultas agendadas. |
| **Consulta** | `PUT` | `/consultas/{id}` | Atualiza uma consulta existente. |
| **Consulta** | `DELETE` | `/consultas/{id}` | Cancela uma consulta pelo ID. |
| **Paciente** | `POST` | `/pacientes` | Cadastra um novo paciente. |
| **Médico** | `POST` | `/medicos` | Cadastra um novo médico. |

## ☁️ Configuração para Deploy (Render)

O projeto está configurado para ler as credenciais do banco de dados (Oracle) a partir de variáveis de ambiente.

1.  **Variáveis de Ambiente:** No painel do Render, as seguintes variáveis devem ser configuradas para o acesso ao Oracle.
    * `DB_URL_ORACLE`: `jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL`
    * `DB_USER_ORACLE`: `[SEU RM]`
    * `DB_PASSWORD_ORACLE`: `[SUA SENHA]`
    * `PORT`: `8080`

2.  **Start Command:** O Render inicia a aplicação com o comando:
    ```bash
    java -jar target/teleconsulta-1.0-SNAPSHOT.jar
    ```

***Atenção:*** *Devido a restrições de rede, a conexão com o Oracle da FIAP pode falhar no ambiente Render, exigindo a migração para um banco de dados na nuvem (como PostgreSQL).*
