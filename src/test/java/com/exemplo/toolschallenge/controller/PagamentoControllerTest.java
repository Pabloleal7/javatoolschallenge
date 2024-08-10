package com.exemplo.toolschallenge.controller;

import com.exemplo.toolschallenge.model.Descricao;
import com.exemplo.toolschallenge.model.FormaPagamento;
import com.exemplo.toolschallenge.model.FormaPagamentoTipo;
import com.exemplo.toolschallenge.model.Transacao;
import com.exemplo.toolschallenge.service.PagamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PagamentoController.class)
public class PagamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PagamentoService pagamentoService;

    private Transacao transacao;

    @BeforeEach
    void setUp() {
        Descricao descricao = new Descricao(BigDecimal.valueOf(50.00), LocalDateTime.parse("2021-05-01T18:30:00"), "PetShop Mundo cão", null, null, null);
        FormaPagamento formaPagamento = new FormaPagamento(FormaPagamentoTipo.AVISTA, 1);
        transacao = new Transacao(1L, "4444555566661234", descricao, formaPagamento);

        when(pagamentoService.consultarPagamento(1L)).thenReturn(Optional.of(transacao));
        when(pagamentoService.consultarPagamento(2L)).thenReturn(Optional.empty());
    }

    @Test
    void realizarPagamentoDeveRetornarTransacao() throws Exception {
        when(pagamentoService.processarPagamento(Mockito.any(Transacao.class))).thenReturn(transacao);

        String jsonRequest = """
                    {
                        "transacao": {
                            "id": 1,
                            "cartao": "4444555566661234",
                            "descricao": {
                                "valor": 50.00,
                                "dataHora": "01/05/2021 18:30:00",
                                "estabelecimento": "PetShop Mundo cão"
                            },
                            "formaPagamento": {
                                "tipo": "AVISTA",
                                "parcelas": 1
                            }
                        }
                    }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao.id", is(1)))
                .andExpect(jsonPath("$.transacao.descricao.estabelecimento", is("PetShop Mundo cão")));
    }

    @Test
    void consultarPagamentoDeveRetornarTransacaoQuandoIdExistir() throws Exception {
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/api/pagamentos/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON));
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao.id", is(1)))
                .andExpect(jsonPath("$.transacao.descricao.estabelecimento", is("PetShop Mundo cão")));
    }

    @Test
    void consultarPagamentoDeveRetornar404QuandoIdNaoExistir() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/pagamentos/{id}", 2L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}