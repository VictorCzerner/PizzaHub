package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

@Service
public class ImpostoService {
    private static final double TAXA_PADRAO = 0.10; // 10%

    public double calcularImpostos(double valorBase) {
        if (valorBase < 0) {
            throw new IllegalArgumentException("Valor base inválido para cálculo de impostos.");
        }
        return valorBase * TAXA_PADRAO;
    }
}
