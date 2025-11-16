package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CardapioResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cardapio;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.CardapioService;

@Component
public class AtivarCardapioUC {

    private final CardapioService cardapioService;

    @Autowired
    public AtivarCardapioUC(CardapioService cardapioService) {
        this.cardapioService = cardapioService;
    }

    public CardapioResponse executar(long idCardapio) {
        Cardapio cardapio = cardapioService.recuperaCardapio(idCardapio);
        if (cardapio == null) {
            throw new IllegalArgumentException("Cardápio não encontrado com ID: " + idCardapio);
        }

        cardapioService.ativarCardapio(idCardapio);

        Cardapio ativo = cardapioService.recuperaCardapioAtivo();

        List<Produto> sugestoes = cardapioService.recuperaSugestoesDoChef();
        
        return new CardapioResponse(ativo, sugestoes);
    }
}