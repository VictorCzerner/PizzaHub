package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.CabecalhoCardapioPresenter;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.CardapioPresenter;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.AtivarCardapioUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.RecuperaCardapioAtivoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.RecuperaListaCardapiosUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.RecuperarCardapioUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CardapioResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cardapio;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

@RestController
@RequestMapping("/cardapio")
public class CardapioController {
    private RecuperarCardapioUC recuperaCardapioUC;
    private RecuperaListaCardapiosUC recuperaListaCardapioUC;
    private AtivarCardapioUC ativarCardapioUC;
    private RecuperaCardapioAtivoUC recuperaCardapioAtivoUC;

    public CardapioController(RecuperarCardapioUC recuperaCardapioUC,
                              RecuperaListaCardapiosUC recuperaListaCardapioUC, AtivarCardapioUC ativarCardapioUC, RecuperaCardapioAtivoUC recuperaCardapioAtivoUC) {
        this.recuperaCardapioUC = recuperaCardapioUC;
        this.recuperaListaCardapioUC = recuperaListaCardapioUC;
        this.ativarCardapioUC = ativarCardapioUC;
        this.recuperaCardapioAtivoUC = recuperaCardapioAtivoUC;
    }                   
    
    @GetMapping("/recuperaAtivo")
    @CrossOrigin("*")
    public CardapioPresenter recuperaCardapioAtivo() {
        CardapioResponse cardapioResponse = recuperaCardapioAtivoUC.executar();

        Cardapio cardapioAtivo = cardapioResponse.getCardapio();
        List<Produto> sugestoes = cardapioResponse.getSugestoesDoChef();

        Set<Long> conjIdSugestoes = new HashSet<>(sugestoes.stream()
            .map(Produto::getId)
            .toList());

        CardapioPresenter presenter = new CardapioPresenter(cardapioAtivo.getTitulo());

        for (Produto produto : cardapioAtivo.getProdutos()) {
            boolean sugestao = conjIdSugestoes.contains(produto.getId());
            presenter.insereItem(produto.getId(), produto.getDescricao(), produto.getPreco(), sugestao);
        }

        return presenter;
    }

    @PutMapping("/atualizaAtivo/{id}")
    @CrossOrigin("*")
    public CardapioPresenter atualizaCardapioAtivo(@PathVariable long id) {
        CardapioResponse cardapioResponse = ativarCardapioUC.executar(id);

        Cardapio cardapioAtivo = cardapioResponse.getCardapio();
        List<Produto> sugestoes = cardapioResponse.getSugestoesDoChef();

        Set<Long> conjIdSugestoes = new HashSet<>(sugestoes.stream()
            .map(Produto::getId)
            .toList());

        CardapioPresenter presenter = new CardapioPresenter(cardapioAtivo.getTitulo());

        for (Produto produto : cardapioAtivo.getProdutos()) {
            boolean sugestao = conjIdSugestoes.contains(produto.getId());
            presenter.insereItem(produto.getId(), produto.getDescricao(), produto.getPreco(), sugestao);
        }

        return presenter;
    }

    @GetMapping("buscaPorId/{id}")
    @CrossOrigin("*")
    public CardapioPresenter recuperaCardapio(@PathVariable(value="id")long id){
        CardapioResponse cardapioResponse = recuperaCardapioUC.run(id);
        Set<Long> conjIdSugestoes = new HashSet<>(cardapioResponse.getSugestoesDoChef().stream()
            .map(produto->produto.getId())
            .toList());
        CardapioPresenter cardapioPresenter = new CardapioPresenter(cardapioResponse.getCardapio().getTitulo());
        for(Produto produto:cardapioResponse.getCardapio().getProdutos()){
            boolean sugestao = conjIdSugestoes.contains(produto.getId());
            cardapioPresenter.insereItem(produto.getId(), produto.getDescricao(), produto.getPreco(), sugestao);
        }
        return cardapioPresenter;
    }

    @GetMapping("/lista")
    @CrossOrigin("*")
    public List<CabecalhoCardapioPresenter> recuperaListaCardapios(){
         List<CabecalhoCardapioPresenter> lstCardapios = 
            recuperaListaCardapioUC.run().cabecalhos().stream()
            .map(cabCar -> new CabecalhoCardapioPresenter(cabCar.id(),cabCar.titulo()))
            .toList();
         return lstCardapios;
    }
}
