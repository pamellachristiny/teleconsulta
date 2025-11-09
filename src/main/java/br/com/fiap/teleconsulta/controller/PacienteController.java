package br.com.fiap.teleconsulta.controller;

import br.com.fiap.teleconsulta.dominio.Paciente;
import br.com.fiap.teleconsulta.service.PacienteService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/pacientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PacienteController {

    private final PacienteService pacienteService = new PacienteService();

    @GET
    public Response buscarTodos() {
        List<Paciente> pacientes = pacienteService.buscarTodos();
        return pacientes.isEmpty()
                ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.ok(pacientes).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        Paciente paciente = pacienteService.buscarPorId(id);
        return paciente == null
                ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.ok(paciente).build();
    }

    @POST
    public Response inserir(Paciente paciente) {
        try {
            pacienteService.cadastrar(paciente); // ✅ corrigido nome do método
            return Response.status(Response.Status.CREATED).build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Response.serverError().entity("Erro interno: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") int id) {
        boolean deletado = pacienteService.deletar(id);
        return deletado
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    public Response atualizar(Paciente paciente) {
        try {
            Paciente atualizado = pacienteService.atualizar(paciente);
            return atualizado == null
                    ? Response.status(Response.Status.NOT_FOUND).entity("Paciente não encontrado").build()
                    : Response.ok(atualizado).build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Response.serverError().entity("Erro interno: " + e.getMessage()).build();
        }
    }
}
