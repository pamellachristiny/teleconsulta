package br.com.fiap.teleconsulta.controller;

import br.com.fiap.teleconsulta.dominio.Paciente;
// [REMOVIDO] import br.com.fiap.teleconsulta.infra.dao.PacienteDAO;
import br.com.fiap.teleconsulta.service.PacienteService;
import br.com.fiap.teleconsulta.exececao.RecursoNaoEncontradoException; // [CORRIGIDO] Importação do pacote 'exececao'
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/pacientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PacienteController {

    // [CORRIGIDO] Apenas o Service deve ser injetado e usado.
    @Inject
    private PacienteService pacienteService;

    // O campo pacienteDAO foi removido.

    // --- C (CREATE) - Inserir ---
    @POST
    public Response inserir(Paciente paciente) {
        try {
            pacienteService.adicionar(paciente);
            return Response.status(Response.Status.CREATED).build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao cadastrar o paciente: " + e.getMessage())
                    .build();
        }
    }

    // --- R (READ) - Buscar Todos ---
    @GET
    public Response buscarTodos() {
        // [CORRIGIDO] Chamando o Service
        List<Paciente> pacientes = pacienteService.buscarTodos();

        if (pacientes == null || pacientes.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(pacientes).build();
    }

    // --- R (READ) - Buscar por CPF ---
    @GET
    @Path("/{cpf}")
    public Response buscarPorCpf(@PathParam("cpf") String cpf) {
        // [CORRIGIDO] Chamando o Service com String CPF.
        Paciente paciente = pacienteService.buscarPorCpf(cpf);

        if (paciente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(paciente).build();
    }

    // --- D (DELETE) - Deletar ---
    @DELETE
    @Path("/{cpf}")
    public Response deletarPaciente(@PathParam("cpf") String cpf) {
        try {
            // [CORRIGIDO] Chamando o Service. O Service lança a exceção.
            pacienteService.deletar(cpf);
            return Response.status(Response.Status.NO_CONTENT).build();

        } catch (RecursoNaoEncontradoException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    // --- U (UPDATE) - Atualizar ---
    @PUT
    public Response atualizar(Paciente paciente) {
        try {
            // [CORRIGIDO] Chamando o Service. O Service lança a exceção.
            Paciente pacienteAtualizado = pacienteService.atualizar(paciente);
            return Response.ok(pacienteAtualizado).build();

        } catch (RecursoNaoEncontradoException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();

        } catch (RuntimeException e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao atualizar o paciente: " + e.getMessage())
                    .build();
        }
    }
}