package ifmt.cba.servico;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import ifmt.cba.persistencia.FabricaEntityManager;
import ifmt.cba.persistencia.PersistenciaException;
import ifmt.cba.servico.util.MensagemErro;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import ifmt.cba.dto.GrupoAlimentarDTO;
import ifmt.cba.dto.ProdutoDTO;
import ifmt.cba.dto.RegistroEstoqueDTO;

import ifmt.cba.negocio.RegistroEstoqueNegocio;

import ifmt.cba.persistencia.RegistroEstoqueDAO;
import ifmt.cba.persistencia.ProdutoDAO;

@Path("/estoque")
public class EstoqueServico {

    private static RegistroEstoqueNegocio registroEstoqueNegocio;
    private static RegistroEstoqueDAO registroEstoqueDAO;
    private static ProdutoDAO produtoDAO;
    static {
        try {
            registroEstoqueDAO = new RegistroEstoqueDAO(FabricaEntityManager.getEntityManagerProducao());
            produtoDAO = new ProdutoDAO(FabricaEntityManager.getEntityManagerProducao());
            registroEstoqueNegocio = new RegistroEstoqueNegocio(registroEstoqueDAO, produtoDAO);
        } catch (PersistenciaException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Path("/codigo/{codigo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarEstoquePorCodigo(@PathParam("codigo") int codigo) {
        ResponseBuilder resposta;
        try {
            RegistroEstoqueDTO estoqueDTO = registroEstoqueNegocio.pesquisaCodigo(codigo);
            estoqueDTO.setLink("/estoque/codigo/" + estoqueDTO.getCodigo());
            resposta = Response.ok();
            resposta.entity(estoqueDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response adicionar(RegistroEstoqueDTO registroEstoqueDTO) {
        ResponseBuilder resposta;
        try {
            registroEstoqueNegocio.inserir(registroEstoqueDTO);
            
            RegistroEstoqueDTO registroEstoqueDTOTemp = registroEstoqueNegocio.pesquisaCodigo(registroEstoqueDTO.getCodigo());
            registroEstoqueDTOTemp.setLink("/codigo" + registroEstoqueDTOTemp.getCodigo());
            resposta = Response.ok();
            resposta.entity(registroEstoqueDTOTemp);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @GET
    @Path("/descarte")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorDescartadosEData(@PathParam("dataInicial") String dataInicial, @PathParam("dataFinal") String dataFinal) {
        ResponseBuilder resposta;
        try {
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            List<RegistroEstoqueDTO> listaEstoqueDTO = registroEstoqueNegocio.buscarPorDescartadosEData(LocalDate.parse(dataInicial, formato), LocalDate.parse(dataFinal, formato));
            for (RegistroEstoqueDTO estoqueDTO : listaEstoqueDTO) {
                estoqueDTO.setLink("/estoque/codigo/" + estoqueDTO.getCodigo());
            }
            resposta = Response.ok();
            resposta.entity(listaEstoqueDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @DELETE
    @Path("/{codigo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response excluir(RegistroEstoqueDTO registroEstoqueDTO) {
        ResponseBuilder resposta;
        try {
            registroEstoqueNegocio.excluir(registroEstoqueDTO);
            resposta = Response.noContent();
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }
}