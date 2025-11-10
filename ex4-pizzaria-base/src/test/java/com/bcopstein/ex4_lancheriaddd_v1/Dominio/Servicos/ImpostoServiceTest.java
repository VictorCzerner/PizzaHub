/**
 * Testes unitários da classe ImpostoService
 *
 * Casos de teste:
 *
 * 1. Valor base 100.0 → imposto 10% → resultado esperado 10.0
 * 2. Valor base 0.0 → imposto 0% → resultado esperado 0.0
 * 3. Valor base negativo (-50.0) → deve lançar IllegalArgumentException
 */

package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ImpostoServiceTest {

    @Test
    public void deveCalcularDezPorCentoDeImposto() {
        ImpostoService service = new ImpostoService();
        double imposto = service.calcularImpostos(100.0);
        assertEquals(10.0, imposto, 0.001);
    }

    @Test
    public void deveRetornarZeroQuandoValorBaseZero() {
        ImpostoService service = new ImpostoService();
        double imposto = service.calcularImpostos(0.0);
        assertEquals(0.0, imposto, 0.001);
    }

    @Test
    public void deveLancarErroQuandoValorNegativo() {
        ImpostoService service = new ImpostoService();
        assertThrows(IllegalArgumentException.class, () -> service.calcularImpostos(-50.0));
    }
}
