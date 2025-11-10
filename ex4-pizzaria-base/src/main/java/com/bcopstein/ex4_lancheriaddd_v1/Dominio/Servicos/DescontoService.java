package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.TipoDesconto;

@Service
public class DescontoService {

    private static final double DESCONTO_FIDELIDADE = 0.07; // 7%
    private static final double DESCONTO_VALOR_GASTO = 0.15; // 15%

    public double aplicarDesconto(double valorBase, int quantidadePedidosRecentes, double valorGastoUltimos30Dias, TipoDesconto tipoDesconto) {
        if (valorBase < 0) {
            throw new IllegalArgumentException("Valor base inválido para cálculo de desconto.");
        }

        // Talvez algum tipo de construtor seja mais recomendável
        if (tipoDesconto == TipoDesconto.CLIENTE_FREQUENTE){
            if (quantidadePedidosRecentes >= 3) {
                // aplica desconto de 7%
                return valorBase * (1 - DESCONTO_FIDELIDADE);
            }
        }
        else if (tipoDesconto == TipoDesconto.CLIENTE_GASTADOR){
            if (valorGastoUltimos30Dias >= 500){
                return valorBase * (1 - DESCONTO_VALOR_GASTO);
            }
        }

        // sem desconto
        return valorBase;
    }

    public double getPercentualDesconto(int quantidadePedidosRecentes, double valorGastoUltimos30Dias, TipoDesconto tipoDesconto) {
        if (tipoDesconto == TipoDesconto.CLIENTE_FREQUENTE){
            return quantidadePedidosRecentes >= 3 ? DESCONTO_FIDELIDADE : 0.0;
        }
        else if (tipoDesconto == TipoDesconto.CLIENTE_GASTADOR){
            return valorGastoUltimos30Dias >= 500 ? DESCONTO_VALOR_GASTO : 0.0;
        }
        // Retorno default
        return 0.0;
    }
}

