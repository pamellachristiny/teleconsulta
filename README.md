
# 🩺 API RESTful da Teleconsulta - NEXUMTECH

Este projeto consiste em uma API RESTful desenvolvida em **Java** utilizando a concepção **JAX-RS (Jakarta RESTful Web Services)**. O objetivo é fornecer uma camada de serviços para o sistema de agendamento de consultas médicas NEXUMTECH, gerenciando recursos como Consultas, Pacientes e Médicos.

Pamella Christiny | rm565206 | 1TDSPY
Felipe Ribeiro | rm565224 | 1TDSPY


A arquitetura adota o padrão de **Três Camadas** (Controller, Service, DAO) para garantir a separação de responsabilidades e a aplicação das regras de negócio (como a verificação de conflito de horário).

---

## 🌳 Informações do Repositório

| Detalhe | Configuração |
| :--- | :--- |
| **Branch Principal** | `mestre` (ou `master`) |
| **Plataforma de Deploy** | Render |
| **Tipo de Serviço** | Docker (Build Multi-Stage) |

---

## 🚀 Tecnologias Utilizadas

| Tecnologia | Versão/Tipo | Descrição |
| :--- | :--- | :--- |
| Linguagem | Java (JDK 17+) | Desenvolvimento principal da aplicação. |
| Framework Web | JAX-RS (Jakarta RESTful Web Services) | Construção da API RESTful (Camada Controller). |
| Gerente de Dependências | Maven | Gestão de bibliotecas e automatização do build. |
| Banco de Dados | Oracle (via JDBC) | Persistência dos dados (Configurações FIAP). |

---

## 🏗️ Arquitetura e Camadas

| Camada | Responsabilidade |
| :--- | :--- |
| **Controlador** | Recebe requisições HTTP, trata a serialização JSON e retorna as respostas. (Ex: `ConsultaController`) |
| **Serviço** | Implementa as Regras de Negócio (ex: lógica de agendamento, verificação de conflitos) e coordena a operação. (Ex: `ConsultaService`) |
| **DAO/Infra** | Gerencia o acesso ao banco de dados (JDBC) e executa o CRUD (SQL). (Ex: `ConsultaDAO`, `ConnectionFactory`) |
| **Domínio** | Entidades de negócio (POJO). (Ex: `Consulta`, `Paciente`, `Medico`) |

---

## ⚙️ Pontos de Extremidade da API

A API é acessível através dos seguintes URIs base:

| Recurso | Método HTTP | URI | Descrição |
| :--- | :--- | :--- | :--- |
| Consulta | `POST` | `/consultas` | Agenda uma nova consulta, aplicando regras de negócio (conflito de horário). |
| Consulta | `GET` | `/consultas` | Lista todas as consultas agendadas. |
| Consulta | `PUT` | `/consultas/{id}` | Atualiza uma consulta existente. |
| Consulta | `DELETE` | `/consultas/{id}` | Cancela uma consulta pelo ID. |
| Paciente | `POST` | `/pacientes` | Cadastra um novo paciente. |
| Médico | `POST` | `/medicos` | Cadastra um novo médico. |
| Médico | `GET` | `/medicos` | Cadastra um novo médico. |

---

## ☁️ Configuração para Deploy (Render)

O serviço está configurado no Render como **Docker Web Service** utilizando um Build Multi-Stage (conforme o `Dockerfile`) para compilar o projeto Maven e rodar o JAR.
https://teleconsultajava.onrender.com


**Start Command (Comando de Inicialização):**
```bash
java -jar teleconsulta.jar
````

### Variáveis de Ambiente Necessárias

As seguintes variáveis **devem ser definidas** no painel do Render para o acesso ao Oracle:

| Variável | Descrição |
| :--- | :--- |
| `DB_URL_ORACLE` | URL de conexão (`jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL`) |
| `DB_USER_ORACLE` | Nome de usuário do BD (`[SEU RM]`) |
| `DB_PASSWORD_ORACLE` | Senha do BD (`[SUA SENHA]`) |
| `PORT` | Porta onde a aplicação será iniciada (`8080`) |

```
