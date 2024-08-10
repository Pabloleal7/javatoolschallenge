package com.exemplo.toolschallenge.factory;

import com.exemplo.toolschallenge.model.Descricao;
import com.exemplo.toolschallenge.model.FormaPagamento;
import com.exemplo.toolschallenge.model.Transacao;
import com.exemplo.toolschallenge.model.TransacaoStatus;

public class TransacaoFactory {

    public static Transacao criarTransacao(Long id, String cartao, Descricao descricao, FormaPagamento formaPagamento, TransacaoStatus status) {
        String nsu = String.valueOf(System.currentTimeMillis());
        String codigoAutorizacao = String.valueOf((int) (Math.random() * 1000000000));

        Descricao novaDescricao = new Descricao(
                descricao.valor(),
                descricao.dataHora(),
                descricao.estabelecimento(),
                nsu,
                codigoAutorizacao,
                status
        );

        return new Transacao(id, cartao, novaDescricao, formaPagamento);
    }

    public static Transacao criarTransacaoAutorizada(Long id, String cartao, Descricao descricao, FormaPagamento formaPagamento) {
        return criarTransacao(id, cartao, descricao, formaPagamento, TransacaoStatus.AUTORIZADO);
    }

    public static Transacao criarTransacaoCancelada(Long id, String cartao, Descricao descricao, FormaPagamento formaPagamento) {
        return criarTransacao(id, cartao, descricao, formaPagamento, TransacaoStatus.CANCELADO);
    }
}