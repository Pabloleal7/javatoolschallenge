package com.exemplo.toolschallenge.model;

import jakarta.validation.constraints.NotNull;

public record FormaPagamento(
        @NotNull FormaPagamentoTipo tipo,
        @NotNull int parcelas) {
}