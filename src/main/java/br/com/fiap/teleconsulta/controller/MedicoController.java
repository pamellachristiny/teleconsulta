package br.com.fiap.teleconsulta.controller;

import br.com.fiap.teleconsulta.dominio.Medico;
import br.com.fiap.teleconsulta.service.MedicoService;
import br.com.fiap.teleconsulta.excecao.RecursoNaoEncontradoException; // Importado
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/medicos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MedicoController {

    @Inject // [CORREÇÃO CDI] Injeta a instância de MedicoService
    private MedicoService medicoService;

    // Construtor manual removido

    // --- C (CREATE) - Criar um novo Medico ---
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

    // --- R (READ) - Buscar todos os Medicos
    @GET
    public Response buscarTodos() {
        List<Medico> medicos = medicoService.buscarTodos();

        if (medicos == null || medicos.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response
                .status(Response.Status.OK)
                .entity(medicos)
                .build();
    }

    // --- R (READ) - Buscar Medico por CRM
    @GET
    @Path("/{crm}")
    public Response buscarPorCrm(@PathParam("crm") String crm) {
        Medico medico = medicoService.buscarPorCrm(crm);

        if (medico == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response
                .status(Response.Status.OK)
                .entity(medico)
                .build();
    }

    @DELETE
    @Path("/{crm}")
    public Response deletarMedico(@PathParam("crm") String crm) {
        boolean deletado = medicoService.deletar(crm);

        if (deletado) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // --- U (UPDATE) - Atualizar Medico ---
    @PUT
    public Response atualizar(Medico medico) {
        try {
            // [TRATAMENTO DE EXCEÇÃO] Chama o Service; se não encontrar, lança RecursoNaoEncontradoException.
            Medico medicoAtualizado = medicoService.atualizar(medico);

            // Se chegou aqui, deu certo. Retorna 200 OK.
            return Response.status(Response.Status.OK)
                    .entity(medicoAtualizado)
                    .build();

            // [CORREÇÃO] Captura a exceção de negócio e mapeia para 404 Not Found.
        } catch (RecursoNaoEncontradoException e) {
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