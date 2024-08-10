package com.exemplo.toolschallenge.controller;

import com.exemplo.toolschallenge.model.*;
import com.exemplo.toolschallenge.service.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping
    public ResponseEntity<?> realizarPagamento(@Valid @RequestBody SolicitacaoPagamento solicitacao) {
        Transacao transacaoRealizada = pagamentoService.processarPagamento(solicitacao.transacao());
        return ResponseEntity.ok(new RespostaPagamento(transacaoRealizada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> consultarPagamento(@PathVariable Long id) {
        Optional<Transacao> transacao = pagamentoService.consultarPagamento(id);
        return transacao.map(t -> ResponseEntity.ok(new RespostaPagamento(t)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/estorno")
    public ResponseEntity<?> realizarEstorno(@PathVariable Long id) {
        Optional<Transacao> estorno = pagamentoService.processarEstorno(id);
        return estorno.map(t -> ResponseEntity.ok(new RespostaEstorno(t)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<RespostaTransacoes> consultarTodosPagamentos() {
        List<Transacao> transacoes = pagamentoService.consultarTodasTransacoes();
        return ResponseEntity.ok(new RespostaTransacoes(transacoes));
    }
}