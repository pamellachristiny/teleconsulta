package br.com.fiap.teleconsulta.controller;

import br.com.fiap.teleconsulta.dominio.Medico;
import br.com.fiap.teleconsulta.dominio.Paciente; // Assumindo que a classe Paciente está no pacote 'dominio'
import br.com.fiap.teleconsulta.infra.dao.PacienteDAO;
import br.com.fiap.teleconsulta.service.PacienteService; // Assumindo a existência de um Service
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/pacientes")
@Produces(MediaType.APPLICATION_JSON)
public class PacienteController {

    private PacienteDAO pacienteDAO;
    private PacienteService pacienteService;

    public PacienteController() {
        this.pacienteDAO = new PacienteDAO();
    }

    // --- GET 1: Buscar Todos os Pacientes ---
    @GET
    public Response buscarTodos() {
        List<Paciente> pacientes = pacienteDAO.listarTodos();
        Response.Status status = null;

        if (pacientes.isEmpty()) {
            status = Response.Status.NOT_FOUND;
        } else {
            status = Response.Status.OK;
        }

        return Response
                .status(status)
                .entity(pacientes)
                .build();
    }


    // --- GET 2: Buscar Paciente por ID ---
    // Ex: GET /pacientes/id/15
    @GET
    @Path("/id/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        Paciente paciente = pacienteDAO.buscarPorId(id);
        Response.Status status = null;

        if (paciente == null) {
            status = Response.Status.NOT_FOUND;
        } else {
            status = Response.Status.OK;
        }

        return Response
                .status(status)
                .entity(paciente)
                .build();
    }

    @POST
    public Response inserir(Paciente paciente) {
        try {
            pacienteService.adicionar(paciente);
            return Response.status(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
            // Ex: CRM já cadastrado (erro de regra de negócio)
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        } catch (RuntimeException e) {
            // Outros erros, incluindo falhas de persistência (RuntimeException relançada pelo DAO)
            System.err.println(e.getMessage());
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao cadastrar o paciente: " + e.getMessage())
                    .build();
        }
    }


    // --- D (DELETE)
    // DELETE /paciente/{id}
    @DELETE
    @Path("/{id}")
    public Response cancelar(@PathParam("id") int id) {
        boolean deletar = pacienteService.deletar(id);

        if (!deletar) {
            // Se o Service retornar false, o ID não foi encontrado
            return Response.status(Response.Status.NOT_FOUND).entity("Paciente com ID " + id + " não encontrada para cancelamento.").build();
        }

        // 204 No Content: Resposta padrão para deleção bem-sucedida sem corpo de resposta.
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    // --- U (UPDATE) - Atualizar Paciente ---
    // PUT /pacientes
    @PUT
    public Response atualizar(Paciente paciente) {
        try {
            // Chama o Service para realizar a atualização e verificar a existência
            Paciente pacienteAtualizado = pacienteService.atualizar(paciente);

            if (pacienteAtualizado == null) {
                // Se o Service retorna null, o recurso não existe. Retorna 404 Not Found.
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Paciente com ID " + paciente.getId() + " não encontrado para atualização.")
                        .build();
            }

            // 200 OK: Retorna o objeto atualizado no corpo.
            return Response.status(Response.Status.OK)
                    .entity(pacienteAtualizado)
                    .build();

        } catch (IllegalArgumentException e) {
            // Se houver alguma regra de negócio (ex: CPF já em uso)
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();

        } catch (RuntimeException e) {
            // Captura erros de persistência (RuntimeException relançada pelo DAO)
            System.err.println("Erro interno ao atualizar paciente: " + e.getMessage());
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao atualizar o paciente: " + e.getMessage())
                    .build();
        }
    }
}