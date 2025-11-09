package br.com.fiap.teleconsulta.controller;

import br.com.fiap.teleconsulta.dominio.Medico;
// import br.com.fiap.teleconsulta.infra.dao.MedicoDAO; // REMOVIDO: Controller não deve acessar o DAO
import br.com.fiap.teleconsulta.service.MedicoService;
import jakarta.inject.Inject; // ADICIONADO: Importação para Injeção de Dependência (CDI)
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/medicos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MedicoController {

    // private MedicoDAO medicoDAO; // REMOVIDO: Evitar que o Controller use o DAO

    @Inject // [CORREÇÃO CRUCIAL] O Quarkus/CDI injeta a instância de MedicoService
    private MedicoService medicoService;

    // [CORREÇÃO CRUCIAL] O construtor manual foi removido para permitir a injeção via @Inject
    /*
    public MedicoController() {
        // [ERRADO] new MedicoDAO();
        // [ERRADO] new MedicoService(medicoDAO);
    }
    */

    // --- C (CREATE) - Criar um novo Medico ---
    // POST /medicos
    @POST
    public Response inserir(Medico medico) {
        try {
            medicoService.adicionar(medico);
            return Response.status(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
            // Ex: CRM já cadastrado (erro de regra de negócio)
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
    // GET /medicos
    @GET
    public Response buscarTodos() {
        // [CORREÇÃO] Chamando o Service, que é a camada correta
        List<Medico> medicos = medicoService.buscarTodos();
        Response.Status status = null;

        if (medicos == null || medicos.isEmpty()) {
            status = Response.Status.NOT_FOUND;
        } else {
            status = Response.Status.OK;
        }

        return Response
                .status(status)
                .entity(medicos)
                .build();
    }

    // --- R (READ) - Buscar Medico por CRM
    // GET /medicos/{crm}
    @GET
    @Path("/{crm}")
    public Response buscarPorCrm(@PathParam("crm") String crm) {
        // [CORREÇÃO] Chamando o Service, que é a camada correta
        Medico medico = medicoService.buscarPorCrm(crm);
        Response.Status status = null;

        if (medico == null) {
            status = Response.Status.NOT_FOUND;
        } else {
            status = Response.Status.OK;
        }

        return Response
                .status(status)
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
    // PUT /medicos
    @PUT
    public Response atualizar(Medico medico) {
        try {
            Medico medicoAtualizado = medicoService.atualizar(medico);

            if (medicoAtualizado == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Médico com CRM " + medico.getCrm() + " não encontrado para atualização.")
                        .build();
            }

            return Response.status(Response.Status.OK)
                    .entity(medicoAtualizado)
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