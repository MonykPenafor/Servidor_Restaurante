package ifmt.cba.servico;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;


import ifmt.cba.dto.EstadoOrdemProducaoDTO;
import ifmt.cba.dto.ItemOrdemProducaoDTO;
import ifmt.cba.dto.OrdemProducaoDTO;
import ifmt.cba.dto.PreparoProdutoDTO;
import ifmt.cba.negocio.NegocioException;
import ifmt.cba.negocio.OrdemProducaoNegocio;
import ifmt.cba.persistencia.FabricaEntityManager;
import ifmt.cba.persistencia.ItemOrdemProducaoDAO;
import ifmt.cba.persistencia.OrdemProducaoDAO;
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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Path("/ordemproducao")
public class OrdemProducaoServico {

    private static OrdemProducaoNegocio ordemProducaoNegocio;
    private static OrdemProducaoDAO ordemProducaoDAO;
    private static ItemOrdemProducaoDAO itemOrdemProducaoDAO;

    static {
        try {
            ordemProducaoDAO = new OrdemProducaoDAO(FabricaEntityManager.getEntityManagerProducao());
            itemOrdemProducaoDAO = new ItemOrdemProducaoDAO(FabricaEntityManager.getEntityManagerProducao());
            ordemProducaoNegocio = new OrdemProducaoNegocio(ordemProducaoDAO, itemOrdemProducaoDAO);
        } catch (PersistenciaException | NegocioException e) {
            e.printStackTrace();
        }
    }
    
    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response adicionar(OrdemProducaoDTO ordemProducaoDTO) {
        ResponseBuilder resposta;
        try {
            int id = ordemProducaoNegocio.inserir(ordemProducaoDTO);
            OrdemProducaoDTO ordemProducaoDTOTemp = ordemProducaoNegocio.pesquisaCodigo(id);
            ordemProducaoDTOTemp.setLink("/ordemproducao/codigo/" + ordemProducaoDTOTemp.getCodigo());
            resposta = Response.ok();
            resposta.entity(ordemProducaoDTOTemp);

        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response alterar(OrdemProducaoDTO ordemProducaoDTO) {
        ResponseBuilder resposta;
        try {
            if(ordemProducaoDTO.getEstado() != EstadoOrdemProducaoDTO.PROCESSADA){

                ordemProducaoNegocio.alterar(ordemProducaoDTO);
                List<ItemOrdemProducaoDTO> listaItens = ordemProducaoDTO.getListaItens();

                for(ItemOrdemProducaoDTO item : listaItens){
                    ordemProducaoNegocio.alterarItemOrdem(item);
                    // item.setLink();
                }

                ordemProducaoDTO.setLink("/ordemproducao/codigo/" + ordemProducaoDTO.getCodigo());
            }
            resposta = Response.ok();
            resposta.entity(ordemProducaoDTO);
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
            ordemProducaoNegocio.excluir(ordemProducaoNegocio.pesquisaCodigo(codigo));
            resposta = Response.noContent();
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @PUT
    @Path("/item")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response alterarItemOrdem(ItemOrdemProducaoDTO itemOrdemProducaoDTO) {
        ResponseBuilder resposta;
        try {
            ordemProducaoNegocio.alterarItemOrdem(itemOrdemProducaoDTO);
            ItemOrdemProducaoDTO itemOrdemProducaoDTOTemp = ordemProducaoNegocio.pesquisaItemOrdemCodigo(itemOrdemProducaoDTO.getCodigo());
            itemOrdemProducaoDTOTemp.setLink("/ordemproducao/item/codigo/" + itemOrdemProducaoDTO.getCodigo());
            resposta = Response.ok();
            resposta.entity(itemOrdemProducaoDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @DELETE
    @Path("/item/{codigo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response excluirItemOrdem(@PathParam("codigo") int codigo) {
        ResponseBuilder resposta;
        try {
            ordemProducaoNegocio.excluirItemOrdem(ordemProducaoNegocio.pesquisaItemOrdemCodigo(codigo));
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
    public Response buscarPorCodigo(@PathParam("codigo") int codigo) {
        ResponseBuilder resposta;
        try {
            OrdemProducaoDTO ordemProducaoDTO = ordemProducaoNegocio.pesquisaCodigo(codigo);
            ordemProducaoDTO.setLink("/ordemproducao/codigo/" + ordemProducaoDTO.getCodigo());
            resposta = Response.ok();
            resposta.entity(ordemProducaoDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @GET
    @Path("/item/codigo/{codigo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarItemOrdemPorCodigo(@PathParam("codigo") int codigo) {
        ResponseBuilder resposta;
        try {
            ItemOrdemProducaoDTO itemOrdemProducaoDTO = ordemProducaoNegocio.pesquisaItemOrdemCodigo(codigo);
            itemOrdemProducaoDTO.setLink("/ordemproducao/item/codigo/" + itemOrdemProducaoDTO.getCodigo());
            resposta = Response.ok();
            resposta.entity(itemOrdemProducaoDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @GET
    @Path("/dataproducao")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorDataProducao(@QueryParam("dataInicial") String dataInicial,
            @QueryParam("dataFinal") String dataFinal) {
        ResponseBuilder resposta;
        try {
            List<OrdemProducaoDTO> listaOrdemProducaoDTO = ordemProducaoNegocio.pesquisaPorDataProducao(
                    LocalDate.parse(dataInicial, formato), LocalDate.parse(dataFinal, formato));
            for (OrdemProducaoDTO ordemProducaoDTO : listaOrdemProducaoDTO) {
                ordemProducaoDTO.setLink("/ordemproducao/codigo/" + ordemProducaoDTO.getCodigo());
            }
            resposta = Response.ok();
            resposta.entity(listaOrdemProducaoDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @GET
    @Path("/estado")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorEstado(@QueryParam("estado") EstadoOrdemProducaoDTO estado) {
        ResponseBuilder resposta;
        try {
            List<OrdemProducaoDTO> listaOrdemProducaoDTO = ordemProducaoNegocio.pesquisaPorEstado(estado);
            for (OrdemProducaoDTO ordemProducaoDTO : listaOrdemProducaoDTO) {
                ordemProducaoDTO.setLink("/ordemproducao/codigo/" + ordemProducaoDTO.getCodigo());
            }
            resposta = Response.ok();
            resposta.entity(listaOrdemProducaoDTO);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @GET
    @Path("/estadodata")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarProducoesPorPeriodoComCalculoDeValores(
        @QueryParam("dataInicial") String dataInicial,
        @QueryParam("dataFinal") String dataFinal, 
        @QueryParam("estado") EstadoOrdemProducaoDTO estado) {

        ResponseBuilder resposta;
        try {

            List<OrdemProducaoDTO> listaOrdemProducaoDTO = ordemProducaoNegocio.pesquisaPorEstadoEDataProducao(estado, LocalDate.parse(dataInicial, formato), LocalDate.parse(dataFinal, formato));

            Map<String, float[]> mapaItens = new HashMap<>();
            var valorTotalGeral = 0f;

            for (OrdemProducaoDTO ordemProducaoDTO : listaOrdemProducaoDTO) {
                for (ItemOrdemProducaoDTO item : ordemProducaoDTO.getListaItens()) {

                    PreparoProdutoDTO preparoProduto = item.getPreparoProduto();
                    String nomeItem = preparoProduto.getNome();
                    var valorPreparo = preparoProduto.getValorPreparo();
                    var custoUnidade = preparoProduto.getProduto().getCustoUnidade();
                    var quantidadePorcao = item.getQuantidadePorcao();

                    var valorProduto = custoUnidade * quantidadePorcao;
                    var valorTotal = valorProduto + valorPreparo;

                    valorTotalGeral += valorTotal;

                    mapaItens.compute(nomeItem, (key, dados) -> {
                        if (dados == null) {
                            return new float[] { quantidadePorcao, 1, valorPreparo, valorProduto, valorTotal };
                        } else {
                            dados[0] += quantidadePorcao;
                            dados[1] += 1;
                            dados[2] += valorPreparo;
                            dados[3] += valorProduto;
                            dados[4] += valorTotal;
                            return dados;
                        }
                    });
                }
            }

            Map<String, Map<String, Object>> mapaItensJson = new LinkedHashMap<>();

            for (Map.Entry<String, float[]> entry : mapaItens.entrySet()) {
                String nomeItem = entry.getKey();
                float[] dados = entry.getValue();

                Map<String, Object> itemData = new LinkedHashMap<>();
                itemData.put("quantidadeItensPedidos", (int) dados[1]);
                itemData.put("quantidadePorcoes", (int) dados[0]);
                itemData.put("valorPreparoTotal", (float) dados[2]);
                itemData.put("valorPorQuantidadeProduto", (float) dados[3]);
                itemData.put("valorTotal", (float) dados[4]);

                mapaItensJson.put(nomeItem, itemData);
            }

            Map<String, Object> relatorioFinal = new LinkedHashMap<>();
            relatorioFinal.put("dataInicial", dataInicial);
            relatorioFinal.put("dataFinal", dataFinal);
            relatorioFinal.put("valorTotalGeral", valorTotalGeral);
            relatorioFinal.put("itens", mapaItensJson);

            resposta = Response.ok();
            resposta.entity(relatorioFinal);

        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }
    
    @GET
    @Path("/itens")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarProdutosProduzidos(@QueryParam("dataInicial") String dataInicial,
            @QueryParam("dataFinal") String dataFinal) {
        ResponseBuilder resposta;

        try {
            EstadoOrdemProducaoDTO estado = EstadoOrdemProducaoDTO.PROCESSADA;
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            List<OrdemProducaoDTO> listaOrdemProducaoDTO = ordemProducaoNegocio.pesquisaPorEstadoEDataProducao(
                estado,
                LocalDate.parse(dataInicial, formato), 
                LocalDate.parse(dataFinal, formato));

            Map<String, Integer> itens = new HashMap<>();

            for (OrdemProducaoDTO ordemProducaoDTO : listaOrdemProducaoDTO) {
                for (ItemOrdemProducaoDTO itemOrdemProducaoDTO : ordemProducaoDTO.getListaItens()) {
                    
                    String produto = itemOrdemProducaoDTO.getPreparoProduto().getProduto().getNome();
                    var quantidadePorcao = itemOrdemProducaoDTO.getQuantidadePorcao();

                    itens.merge(produto, quantidadePorcao, Integer::sum);
                }
            }

            Map<String, Integer> itensOrdenados = itens.entrySet()
            .stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new));

            resposta = Response.ok();
            resposta.entity(itensOrdenados);

        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

    @PUT
    @Path("/processar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response processarOrdemProducao(OrdemProducaoDTO ordemProducaoDTO) {
        ResponseBuilder resposta;
        try {
            ordemProducaoNegocio.processarOrdemProducao(ordemProducaoDTO);
            OrdemProducaoDTO ordemProducaoDTOTemp = ordemProducaoNegocio.pesquisaCodigo(ordemProducaoDTO.getCodigo());
            ordemProducaoDTOTemp.setLink("/ordemproducao/codigo/" + ordemProducaoDTO.getCodigo());
            resposta = Response.ok();
            resposta.entity(ordemProducaoDTOTemp);
        } catch (Exception ex) {
            resposta = Response.status(400);
            resposta.entity(new MensagemErro(ex.getMessage()));
        }
        return resposta.build();
    }

}