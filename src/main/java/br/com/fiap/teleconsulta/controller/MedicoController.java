package br.com.fiap.teleconsulta.controller;

import br.com.fiap.teleconsulta.dominio.Medico;
import br.com.fiap.teleconsulta.service.MedicoService;
import br.com.fiap.teleconsulta.exececao.RecursoNaoEncontradoException; // [CORRIGIDO] Importação do pacote 'exececao'
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/medicos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MedicoController {

    @Inject
    private MedicoService medicoService;

    // Métodos Inserir/POST

    // [MÉTODO ANTERIORMENTE CORRIGIDO]
    @POST
    public Response inserir(Medico medico) {
        try {
            medicoService.adicionar(medico);
            return Response.status(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao cadastrar o médico: " + e.getMessage())
                    .build();
        }
    }

    // Métodos GET/Buscar

    @GET
    public Response buscarTodos() {
        List<Medico> medicos = medicoService.buscarTodos();

        if (medicos == null || medicos.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(medicos).build();
    }

    @GET
    @Path("/{crm}")
    public Response buscarPorCrm(@PathParam("crm") String crm) {
        Medico medico = medicoService.buscarPorCrm(crm);

        if (medico == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(medico).build();
    }

    // Métodos DELETE/Deletar

    @DELETE
    @Path("/{crm}")
    public Response deletarMedico(@PathParam("crm") String crm) {
        try {
            // Se o Service não encontrou o médico, ele lançará a exceção.
            medicoService.deletar(crm);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (RecursoNaoEncontradoException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    // Métodos PUT/Atualizar

    @PUT
    public Response atualizar(Medico medico) {
        try {
            Medico medicoAtualizado = medicoService.atualizar(medico);
            return Response.ok(medicoAtualizado).build();
        } catch (RecursoNaoEncontradoException e) {
            // [LINHA DO ERRO 97] O try-catch agora reconhece a exceção importada
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (RuntimeException e) {
            System.err.println("Erro interno ao atualizar médico: " + e.getMessage());
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao atualizar o médico: " + e.getMessage())
                    .build();
        }
    }
}