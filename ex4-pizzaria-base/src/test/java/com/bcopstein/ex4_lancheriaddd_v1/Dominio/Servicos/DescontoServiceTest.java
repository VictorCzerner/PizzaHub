package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.DescontosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Desconto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public class DescontoServiceTest {

    @Mock
    private DescontosRepository descontosRepository;

    @Mock
    private DescontoConcretoI descontoClienteFrequente;

    @Mock
    private DescontoConcretoI descontoClienteGastador;

    private DescontoService descontoService;
    private Cliente cliente;
    private Pedido pedido;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Nomes devem bater com os que estão no banco / lógica da aplicação
        when(descontoClienteFrequente.getNome()).thenReturn("ClienteFrequente");
        when(descontoClienteGastador.getNome()).thenReturn("ClienteGastador");

        descontoService = new DescontoService(
            descontosRepository,
            List.of(descontoClienteFrequente, descontoClienteGastador)
        );

        cliente = mock(Cliente.class);
        pedido = mock(Pedido.class);
    }

    // ===========================================================
    // 1️ Cliente FREQUENTE (>=3 pedidos) → desconto 7%
    // ===========================================================
    @Test
    void deveAplicarDescontoClienteFrequente() {
        Desconto descontoAtivo = new Desconto(1, "ClienteFrequente", 0.07, true);
        when(descontosRepository.retornaDescontoAtivo()).thenReturn(descontoAtivo);
        when(descontoClienteFrequente.aplicarDesconto(cliente, pedido, 0.07)).thenReturn(93.0);

        double resultado = descontoService.aplicarDescontoAtivo(cliente, pedido);

        assertEquals(93.0, resultado, 0.001);
        verify(descontoClienteFrequente, times(1)).aplicarDesconto(cliente, pedido, 0.07);
    }

    // ===========================================================
    // 2️ Cliente GASTADOR (>=500 gasto) → desconto 15%
    // ===========================================================
    @Test
    void deveAplicarDescontoClienteGastador() {
        Desconto descontoAtivo = new Desconto(2, "ClienteGastador", 0.15, true);
        when(descontosRepository.retornaDescontoAtivo()).thenReturn(descontoAtivo);
        when(descontoClienteGastador.aplicarDesconto(cliente, pedido, 0.15)).thenReturn(85.0);

        double resultado = descontoService.aplicarDescontoAtivo(cliente, pedido);

        assertEquals(85.0, resultado, 0.001);
        verify(descontoClienteGastador, times(1)).aplicarDesconto(cliente, pedido, 0.15);
    }

    // ===========================================================
    // 3️ Nenhum desconto ativo → retorna valor cheio do pedido
    // ===========================================================
    @Test
    void deveRetornarValorOriginalQuandoNaoHaDescontoAtivo() {
        Desconto descontoPadrao = new Desconto(-1, "Nenhum", 0.0, false);
        when(descontosRepository.retornaDescontoAtivo()).thenReturn(descontoPadrao);
        when(pedido.getValor()).thenReturn(100.0);

        double resultado = descontoService.aplicarDescontoAtivo(cliente, pedido);

        assertEquals(100.0, resultado, 0.001);
        verify(descontosRepository, times(1)).retornaDescontoAtivo();
    }


    // ===========================================================
    // 4️ Verifica se lista de descontos está sendo carregada
    // ===========================================================
    @Test
    void deveRecuperarTodosOsDescontos() {
        List<Desconto> listaFake = List.of(
            new Desconto(1, "ClienteFrequente", 0.07, true),
            new Desconto(2, "ClienteGastador", 0.15, false)
        );

        when(descontosRepository.listaTodos()).thenReturn(listaFake);

        List<Desconto> resultado = descontoService.RecuperaTodosDescontos();

        assertEquals(2, resultado.size());
        assertEquals("ClienteFrequente", resultado.get(0).getNome());
        verify(descontosRepository, times(1)).listaTodos();
    }

    // ===========================================================
    // 5️ Percentual de desconto ativo
    // ===========================================================
    @Test
    void deveRetornarPercentualDescontoAtivo() {
        Desconto descontoAtivo = new Desconto(3, "ClienteGastador", 0.15, true);
        when(descontosRepository.retornaDescontoAtivo()).thenReturn(descontoAtivo);

        double percentual = descontoService.percentagemDesconto();

        assertEquals(0.15, percentual, 0.001);
    }

    // ===========================================================
    // 6️ Verifica se método decideDescontoAtivo() delega ao repositório
    // ===========================================================
    @Test
    void deveDecidirDescontoAtivoComBaseNoRepositorio() {
        when(descontosRepository.decideDescontoAtivo(1L)).thenReturn(true);

        boolean resultado = descontoService.decideDescontoAtivo(1L);

        assertTrue(resultado);
        verify(descontosRepository, times(1)).decideDescontoAtivo(1L);
    }
}
