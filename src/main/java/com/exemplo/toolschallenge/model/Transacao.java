package com.exemplo.toolschallenge.model;

import jakarta.validation.constraints.NotNull;

public record Transacao(
        @NotNull Long id,
        @NotNull String cartao,
        @NotNull Descricao descricao,
        @NotNull FormaPagamento formaPagamento) {
}