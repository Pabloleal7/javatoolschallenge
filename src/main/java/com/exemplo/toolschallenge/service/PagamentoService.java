package com.exemplo.toolschallenge.service;

import com.exemplo.toolschallenge.exception.TransacaoNaoEncontradaException;
import com.exemplo.toolschallenge.factory.TransacaoFactory;
import com.exemplo.toolschallenge.model.Transacao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PagamentoService {

    private final List<Transacao> transacoes = new ArrayList<>();

    public Transacao processarPagamento(Transacao transacao) {
        if (transacoes.stream().anyMatch(t -> t.id().equals(transacao.id()))) {
            throw new TransacaoNaoEncontradaException("ID de transação já existe.");
        }

        Transacao novaTransacao = TransacaoFactory.criarTransacaoAutorizada(
                transacao.id(),
                transacao.cartao(),
                transacao.descricao(),
                transacao.formaPagamento()
        );

        transacoes.add(novaTransacao);
        return novaTransacao;
    }

    public Optional<Transacao> consultarPagamento(Long id) {
        return transacoes.stream()
                .filter(transacao -> transacao.id().equals(id))
                .findFirst();
    }

    public Optional<Transacao> processarEstorno(Long id) {
        Optional<Transacao> transacaoExistente = consultarPagamento(id);

        if (transacaoExistente.isEmpty()) {
            throw new TransacaoNaoEncontradaException("Transação com ID não encontrado.");
        }

        Transacao estorno = TransacaoFactory.criarTransacaoCancelada(
                transacaoExistente.get().id(),
                transacaoExistente.get().cartao(),
                transacaoExistente.get().descricao(),
                transacaoExistente.get().formaPagamento()
        );

        transacoes.remove(transacaoExistente.get());
        transacoes.add(estorno);
        return Optional.of(estorno);
    }

    public List<Transacao> consultarTodasTransacoes() {
        return new ArrayList<>(transacoes);
    }
}