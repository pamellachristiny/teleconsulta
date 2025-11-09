package br.com.fiap.teleconsulta.controller;

import br.com.fiap.teleconsulta.dominio.Consulta;
import br.com.fiap.teleconsulta.infra.dao.ConsultaDAO;
import br.com.fiap.teleconsulta.service.ConsultaService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/consultas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class ConsultaController {

    private ConsultaService consultaService;

    public ConsultaController() {
        ConsultaDAO consultaDAO = new ConsultaDAO();
        this.consultaService = new ConsultaService(consultaDAO);
    }

    @POST
    public Response agendar(Consulta consulta) {
        try {
            // A regra de negócio (verificação de horário) está no Service.
            consultaService.agendar(consulta);
            // 201 Created
            return Response.status(Response.Status.CREATED).build();

        } catch (IllegalArgumentException e) {
            // 409: não pode ser concluída devido a um conflito.
            System.err.println("Conflito de agendamento: " + e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();

        } catch (RuntimeException e) {
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

        // 204 No Content
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    // --- U (UPDATE) - Atualizar Consulta ---
    // PUT /consultas
    @PUT
    public Response atualizar(Consulta consulta) {
        try {
            Consulta consultaAtualizada = consultaService.atualizar(consulta);

            if (consultaAtualizada == null) {
                // 404 Not Found
                return Response.status(Response.Status.NOT_FOUND).entity("Consulta com ID " + consulta.getId() + " não encontrada para atualização.").build();
            }

            // 200 OK
            return Response.ok(consultaAtualizada).build();

        } catch (IllegalArgumentException e) {
            System.err.println("Erro de atualização: " + e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();

        } catch (RuntimeException e) {
            System.err.println("Erro interno ao atualizar: " + e.getMessage());
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao atualizar a consulta: " + e.getMessage())
                    .build();
        }
    }
}
