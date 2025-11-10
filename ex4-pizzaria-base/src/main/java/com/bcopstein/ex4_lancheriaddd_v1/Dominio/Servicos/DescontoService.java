package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

@Service
public class DescontoService {

    private static final double DESCONTO_FIDELIDADE = 0.07; // 7%

    public double aplicarDesconto(double valorBase, int quantidadePedidosRecentes) {
        if (valorBase < 0) {
            throw new IllegalArgumentException("Valor base inválido para cálculo de desconto.");
        }

        if (quantidadePedidosRecentes >= 3) {
            // aplica desconto de 7%
            return valorBase * (1 - DESCONTO_FIDELIDADE);
        }

        // sem desconto
        return valorBase;
    }

    public double getPercentualDesconto(int quantidadePedidosRecentes) {
        return quantidadePedidosRecentes >= 3 ? DESCONTO_FIDELIDADE : 0.0;
    }
}
