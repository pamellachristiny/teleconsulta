package br.com.fiap.teleconsulta.controller;

import br.com.fiap.teleconsulta.dominio.Consulta; // Assumindo que a classe Consulta está no pacote 'dominio'
import br.com.fiap.teleconsulta.infra.dao.ConsultaDAO; // DAO necessário para injeção no Service
import br.com.fiap.teleconsulta.service.ConsultaService; // O Service que contém as regras
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/consultas")
@Produces(MediaType.APPLICATION_JSON) // Define o formato de resposta padrão como JSON
@Consumes(MediaType.APPLICATION_JSON) // Define o formato de entrada padrão como JSON
public class ConsultaController {

    private ConsultaService consultaService;

    public ConsultaController() {
        // 1. Instancia o DAO
        ConsultaDAO consultaDAO = new ConsultaDAO();
        // 2. Instancia o Service, injetando o DAO
        this.consultaService = new ConsultaService(consultaDAO);
    }

    // --- C (CREATE) - Agendar uma nova Consulta ---
    // POST /consultas
    @POST
    public Response agendar(Consulta consulta) {
        try {
            // A regra de negócio (verificação de horário) está no Service.
            consultaService.agendar(consulta);
            // 201 Created: Retorna sucesso sem corpo.
            return Response.status(Response.Status.CREATED).build();

        } catch (IllegalArgumentException e) {
            // Captura erros de regra de negócio (Ex: paciente/médico indisponível).
            // 409 Conflict: Indica que a requisição não pode ser concluída devido a um conflito.
            System.err.println("Conflito de agendamento: " + e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();

        } catch (RuntimeException e) {
            // Captura erros de persistência (RuntimeException relançada pelo DAO).
            System.err.println("Erro interno ao agendar: " + e.getMessage());
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao agendar a consulta: " + e.getMessage())
                    .build();
        }
    }

    // --- R (READ) - Listar todas as Consultas ---
    // GET /consultas
    @GET
    public Response buscarTodas() {
        List<Consulta> consultas = consultaService.buscarTodas();
        Response.Status status = consultas.isEmpty() ? Response.Status.NOT_FOUND : Response.Status.OK;

        return Response
                .status(status)
                .entity(consultas)
                .build();
    }

    // --- R (READ) - Buscar Consulta por ID ---
    // GET /consultas/{id}
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        Consulta consulta = consultaService.buscarPorId(id);
        Response.Status status = consulta == null ? Response.Status.NOT_FOUND : Response.Status.OK;

        return Response
                .status(status)
                .entity(consulta)
                .build();
    }

    // --- D (DELETE) - Cancelar Consulta por ID ---
    // DELETE /consultas/{id}
    @DELETE
    @Path("/{id}")
    public Response cancelar(@PathParam("id") int id) {
        boolean cancelada = consultaService.cancelar(id);

        if (!cancelada) {
            // Se o Service retornar false, o ID não foi encontrado
            return Response.status(Response.Status.NOT_FOUND).entity("Consulta com ID " + id + " não encontrada para cancelamento.").build();
        }

        // 204 No Content: Resposta padrão para deleção bem-sucedida sem corpo de resposta.
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    // O método PUT (Atualizar) seria adicionado aqui, seguindo a mesma estrutura do POST,
    // mas usando o método de atualização no Service.
}
