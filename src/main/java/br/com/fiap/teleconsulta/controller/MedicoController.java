package br.com.fiap.teleconsulta.controller;

import br.com.fiap.teleconsulta.dominio.Medico;
import br.com.fiap.teleconsulta.infra.dao.MedicoDAO;
import br.com.fiap.teleconsulta.service.MedicoService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/medicos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController() {
        MedicoDAO medicoDAO = new MedicoDAO();
        this.medicoService = new MedicoService(medicoDAO);
    }

    @POST
    public Response inserir(Medico medico) {
        try {
            medicoService.adicionar(medico);
            return Response.status(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Response.serverError().entity("Erro interno ao cadastrar: " + e.getMessage()).build();
        }
    }

    @GET
    public Response buscarTodos() {
        List<Medico> medicos = medicoService.listarTodos();
        return medicos.isEmpty()
                ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.ok(medicos).build();
    }

    @GET
    @Path("/{crm}")
    public Response buscarPorCrm(@PathParam("crm") String crm) {
        Medico medico = medicoService.buscarPorCRM(crm);
        return medico == null
                ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.ok(medico).build();
    }

    @DELETE
    @Path("/{crm}")
    public Response deletarMedico(@PathParam("crm") String crm) {
        boolean deletado = medicoService.deletar(crm);
        return deletado
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    public Response atualizar(Medico medico) {
        try {
            Medico atualizado = medicoService.atualizar(medico);
            return atualizado == null
                    ? Response.status(Response.Status.NOT_FOUND).entity("CRM não encontrado").build()
                    : Response.ok(atualizado).build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Response.serverError().entity("Erro interno: " + e.getMessage()).build();
        }
    }
}
