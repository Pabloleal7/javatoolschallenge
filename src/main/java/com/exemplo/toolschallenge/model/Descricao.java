package com.exemplo.toolschallenge.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Descricao(
        @NotNull BigDecimal valor,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
        @NotNull LocalDateTime dataHora,
        @NotNull String estabelecimento,
        String nsu,
        String codigoAutorizacao,
        TransacaoStatus status) {
}