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

    private final ConsultaService consultaService;

    public ConsultaController() {
        ConsultaDAO consultaDAO = new ConsultaDAO();
        this.consultaService = new ConsultaService(consultaDAO);
    }

    @POST
    public Response agendar(Consulta consulta) {
        try {
            consultaService.agendar(consulta);
            return Response.status(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Response.serverError().entity("Erro interno ao agendar: " + e.getMessage()).build();
        }
    }

    @GET
    public Response buscarTodas() {
        List<Consulta> consultas = consultaService.buscarTodas();
        if (consultas.isEmpty())
            return Response.status(Response.Status.NOT_FOUND).entity("Nenhuma consulta encontrada").build();
        return Response.ok(consultas).build();
    }

    @DELETE
    @Path("/{id}")
    public Response cancelar(@PathParam("id") int id) {
        boolean cancelada = consultaService.cancelar(id);
        return cancelada
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).entity("Consulta não encontrada").build();
    }

    @PUT
    public Response atualizar(Consulta consulta) {
        try {
            Consulta atualizada = consultaService.atualizar(consulta);
            if (atualizada == null)
                return Response.status(Response.Status.NOT_FOUND).entity("Consulta não encontrada").build();
            return Response.ok(atualizada).build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Response.serverError().entity("Erro interno: " + e.getMessage()).build();
        }
    }
}
