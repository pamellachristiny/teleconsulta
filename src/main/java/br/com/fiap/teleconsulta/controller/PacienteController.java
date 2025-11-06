package br.com.fiap.teleconsulta.controller;

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
}