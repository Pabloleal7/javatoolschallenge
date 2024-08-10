package com.exemplo.toolschallenge.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record SolicitacaoPagamento(
        @NotNull @Valid Transacao transacao) {
}