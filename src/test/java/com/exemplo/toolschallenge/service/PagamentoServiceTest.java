package com.exemplo.toolschallenge.service;

import com.exemplo.toolschallenge.exception.TransacaoNaoEncontradaException;
import com.exemplo.toolschallenge.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PagamentoServiceTest {

    private PagamentoService pagamentoService;

    @BeforeEach
    public void setup() {
        pagamentoService = new PagamentoService();
    }

    @Test
    public void testProcessarPagamento() {
        Descricao descricao = new Descricao(BigDecimal.valueOf(50.00), LocalDateTime.parse("2021-05-01T18:30:00"), "PetShop Mundo cão", null, null, null);
        FormaPagamento formaPagamento = new FormaPagamento(FormaPagamentoTipo.AVISTA, 1);
        Transacao transacao = new Transacao(1L, "4444555566661234", descricao, formaPagamento);

        Transacao resultado = pagamentoService.processarPagamento(transacao);

        assertNotNull(resultado.id());
        assertNotNull(resultado.descricao().nsu());
        assertNotNull(resultado.descricao().codigoAutorizacao());
        assertEquals(TransacaoStatus.AUTORIZADO, resultado.descricao().status());
    }

    @Test
    public void testConsultarPagamento() {
        Descricao descricao = new Descricao(BigDecimal.valueOf(50.00), LocalDateTime.parse("2021-05-01T18:30:00"), "PetShop Mundo cão", null, null, null);
        FormaPagamento formaPagamento = new FormaPagamento(FormaPagamentoTipo.AVISTA, 1);
        Transacao transacao = new Transacao(1L, "4444555566661234", descricao, formaPagamento);

        pagamentoService.processarPagamento(transacao);

        Optional<Transacao> resultado = pagamentoService.consultarPagamento(transacao.id());

        assertTrue(resultado.isPresent());
        assertEquals(transacao.id(), resultado.get().id());
    }

    @Test
    public void testProcessarEstorno() {
        Descricao descricao = new Descricao(BigDecimal.valueOf(50.00), LocalDateTime.parse("2021-05-01T18:30:00"), "PetShop Mundo cão", null, null, null);
        FormaPagamento formaPagamento = new FormaPagamento(FormaPagamentoTipo.AVISTA, 1);
        Transacao transacao = new Transacao(1L, "4444555566661234", descricao, formaPagamento);

        pagamentoService.processarPagamento(transacao);

        Optional<Transacao> estorno = pagamentoService.processarEstorno(transacao.id());

        assertTrue(estorno.isPresent());
        assertEquals(TransacaoStatus.CANCELADO, estorno.get().descricao().status());
    }

    @Test
    public void testIdDuplicadoDeveFalhar() {
        Descricao descricao = new Descricao(BigDecimal.valueOf(50.00), LocalDateTime.parse("2021-05-01T18:30:00"), "PetShop Mundo cão", null, null, null);
        FormaPagamento formaPagamento = new FormaPagamento(FormaPagamentoTipo.AVISTA, 1);
        Transacao transacao1 = new Transacao(1L, "4444555566661234", descricao, formaPagamento);
        Transacao transacao2 = new Transacao(1L, "5555444433332222", descricao, formaPagamento);

        pagamentoService.processarPagamento(transacao1);

        Exception exception = assertThrows(TransacaoNaoEncontradaException.class, () -> {
            pagamentoService.processarPagamento(transacao2);
        });

        String expectedMessage = "ID de transação já existe.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}