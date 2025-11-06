package br.com.fiap.teleconsulta.controller;

import br.com.fiap.teleconsulta.dominio.Medico; // Assumindo que a classe Medico está no pacote 'dominio'
import br.com.fiap.teleconsulta.infra.dao.MedicoDAO; // Adapte o pacote conforme necessário
import br.com.fiap.teleconsulta.service.MedicoService; // Assumindo a existência do Service
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/medicos")
@Produces(MediaType.APPLICATION_JSON) // Define o formato de resposta padrão como JSON
@Consumes(MediaType.APPLICATION_JSON) // Define o formato de entrada padrão como JSON
public class MedicoController {

    private MedicoDAO medicoDAO; // Usado para buscas simples
    private MedicoService medicoService; // Usado para operações com regras de negócio (POST, PUT, DELETE)

    public MedicoController() {
        // 1. Instancia o DAO
        this.medicoDAO = new MedicoDAO();
        // 2. Instancia o Service, injetando o DAO (seguindo o padrão do AlunoService)
        // Nota: Assumimos que MedicoService precisa apenas do MedicoDAO, como PacienteService.
        this.medicoService = new MedicoService(medicoDAO);
    }

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
            // Outros erros, incluindo falhas de persistência (RuntimeException relançada pelo DAO)
            System.err.println(e.getMessage());
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao cadastrar o médico: " + e.getMessage())
                    .build();
        }
    }

    // --- R (READ) - Buscar todos os Medicos ---
    // GET /medicos
    @GET
    public Response buscarTodos() {
        List<Medico> medicos = medicoDAO.listarTodos(); // Chamada direta ao DAO para listar
        Response.Status status = null;

        if (medicos.isEmpty()) {
            status = Response.Status.NOT_FOUND;
        } else {
            status = Response.Status.OK;
        }

        return Response
                .status(status)
                .entity(medicos)
                .build();
    }

    // --- R (READ) - Buscar Medico por CRM ---
    // GET /medicos/{crm}
    @GET
    @Path("/{crm}")
    public Response buscarPorCrm(@PathParam("crm") String crm) {
        Medico medico = medicoDAO.buscarPorCRM(crm);
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
}



    // --- U (UPDATE) - Atualizar um Medico existente ---
    // PUT /medicos/{crm}

    // --- D (DELETE) - Deletar um Medico por CRM ---
    // DELETE /medicos/{crm}
    //@DELETE
    //@Path("/{crm}")
    //public Response deletar(@PathParam("crm") String crm) {
   //     boolean deletado = medicoService.deletar(crm);

  //      if (!deletado) {
   //         return Response.status(Response.Status.NOT_FOUND).entity("Médico com CRM " + crm + " não encontrado ou já deletado.").build();
   //     }

        // 204 No Content para deleção bem-sucedida
 //       return Response.status(Response.Status.NO_CONTENT).build();
   // }
//}