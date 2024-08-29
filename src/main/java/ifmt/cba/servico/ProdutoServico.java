package ifmt.cba.servico;

import java.util.List;

import ifmt.cba.dto.ProdutoDTO;
import ifmt.cba.negocio.ProdutoNegocio;
import ifmt.cba.persistencia.FabricaEntityManager;
import ifmt.cba.persistencia.PersistenciaException;
import ifmt.cba.persistencia.ProdutoDAO;
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

@Path("/produto")
public class ProdutoServico {

    private static ProdutoNegocio produtoNegocio;
    private static ProdutoDAO produtoDAO;

    static {
        try {
            produtoDAO = new ProdutoDAO(FabricaEntityManager.getEntityManagerProducao());
            produtoNegocio = new ProdutoNegocio(produtoDAO);
        } catch (PersistenciaException e) {
            e.printStackTrace();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response adicionar(ProdutoDTO produtoDTO) {
        ResponseBuilder resposta;
        try {
            produtoNegocio.inserir(produtoDTO);
            
            ProdutoDTO produtoDTOTemp = produtoNegocio.pesquisaParteNome(produtoDTO.getNome()).get(0);
            produtoDTOTemp.setLink("/produto/codigo/" + produtoDTOTemp.getCodigo());
            resposta = Response.ok();
            resposta.entity(produtoDTOTemp);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response alterar(ProdutoDTO produtoDTO) {
        ResponseBuilder resposta;
        try {
            produtoNegocio.alterar(produtoDTO);
            ProdutoDTO produtoDTOTemp = produtoNegocio.pesquisaCodigo(produtoDTO.getCodigo());
            produtoDTOTemp.setLink("/produto/codigo/" + produtoDTOTemp.getCodigo());
            resposta = Response.ok();
            resposta.entity(produtoDTOTemp);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @DELETE
    @Path("/{codigo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response excluir(@PathParam("codigo") int codigo) {
        ResponseBuilder resposta;
        try {
            produtoNegocio.excluir(codigo);
            resposta = Response.noContent();
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @GET
    @Path("/codigo/{codigo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarProdutoPorCodigo(@PathParam("codigo") int codigo) {
        ResponseBuilder resposta;
        try {
            ProdutoDTO produtoDTO = produtoNegocio.pesquisaCodigo(codigo);
            produtoDTO.setLink("/produto/codigo/" + produtoDTO.getCodigo());
            resposta = Response.ok();
            resposta.entity(produtoDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @GET
    @Path("/nome/{nome}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarProdutoPorNome(@PathParam("nome") String nome) {
        ResponseBuilder resposta;
        try {
            List<ProdutoDTO> listaProdutoDTO = produtoNegocio.pesquisaParteNome(nome);
            for (ProdutoDTO produtoDTO : listaProdutoDTO) {
                produtoDTO.setLink("/produto/codigo/" + produtoDTO.getCodigo());

            }
            resposta = Response.ok();
            resposta.entity(listaProdutoDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @GET
    @Path("/estoquebaixo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarProdutoAbaixoEstoqueMinimo() {
        ResponseBuilder resposta;
        try {
            List<ProdutoDTO> listaProdutoDTO = produtoNegocio.pesquisaProdutoAbaixoEstoqueMinimo();
            for (ProdutoDTO produtoDTO : listaProdutoDTO) {
                produtoDTO.setLink("/produto/codigo/" + produtoDTO.getCodigo());

            }
            resposta = Response.ok();
            resposta.entity(listaProdutoDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity("{\"erro\": \"" + ex.getMessage() + "\"}");
        }
        return resposta.build();
    }
}