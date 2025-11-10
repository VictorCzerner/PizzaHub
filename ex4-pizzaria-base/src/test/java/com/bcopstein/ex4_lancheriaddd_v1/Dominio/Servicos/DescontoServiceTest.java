package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.TipoDesconto;

public class DescontoServiceTest {

    /**
     * Classe: DescontoService
     * ------------------------------------------------------------
     * Casos de teste:
     * 1. Cliente FREQUENTE com >=3 pedidos → aplica 7%
     * 2. Cliente FREQUENTE com <3 pedidos → sem desconto
     * 3. Cliente GASTADOR com >=500 de gasto → aplica 15%
     * 4. Cliente GASTADOR com <500 → sem desconto
     * 5. Valor base negativo → lança exceção
     */
    @Test
    public void deveAplicarDescontoClienteFrequente() {
        DescontoService service = new DescontoService();

        double valorBase = 100.0;
        double resultado = service.aplicarDesconto(valorBase, 3, 0.0, TipoDesconto.CLIENTE_FREQUENTE);

        assertEquals(93.0, resultado, 0.001); // 7% de desconto
        assertEquals(0.07, service.getPercentualDesconto(3, 0.0, TipoDesconto.CLIENTE_FREQUENTE), 0.001);
    }

    @Test
    public void naoDeveAplicarDescontoClienteFrequenteComPoucosPedidos() {
        DescontoService service = new DescontoService();

        double valorBase = 100.0;
        double resultado = service.aplicarDesconto(valorBase, 2, 0.0, TipoDesconto.CLIENTE_FREQUENTE);

        assertEquals(100.0, resultado, 0.001);
        assertEquals(0.0, service.getPercentualDesconto(2, 0.0, TipoDesconto.CLIENTE_FREQUENTE), 0.001);
    }

    @Test
    public void deveAplicarDescontoClienteGastador() {
        DescontoService service = new DescontoService();

        double valorBase = 100.0;
        double resultado = service.aplicarDesconto(valorBase, 0, 600.0, TipoDesconto.CLIENTE_GASTADOR);

        assertEquals(85.0, resultado, 0.001); // 15% de desconto
        assertEquals(0.15, service.getPercentualDesconto(0, 600.0, TipoDesconto.CLIENTE_GASTADOR), 0.001);
    }

    @Test
    public void naoDeveAplicarDescontoClienteGastadorComPoucoGasto() {
        DescontoService service = new DescontoService();

        double valorBase = 100.0;
        double resultado = service.aplicarDesconto(valorBase, 0, 200.0, TipoDesconto.CLIENTE_GASTADOR);

        assertEquals(100.0, resultado, 0.001);
        assertEquals(0.0, service.getPercentualDesconto(0, 200.0, TipoDesconto.CLIENTE_GASTADOR), 0.001);
    }

    @Test
    public void deveLancarErroQuandoValorBaseNegativo() {
        DescontoService service = new DescontoService();

        assertThrows(IllegalArgumentException.class, () ->
            service.aplicarDesconto(-50.0, 5, 1000.0, TipoDesconto.CLIENTE_FREQUENTE)
        );
    }

}
