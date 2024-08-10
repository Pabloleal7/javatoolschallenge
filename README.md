
# ToolsChallenge API

## Descrição

A `ToolsChallenge API` é uma API de pagamentos desenvolvida em Java com Spring Boot. Ela permite processar pagamentos, consultar transações e realizar estornos. Este projeto foi criado como parte de um desafio técnico, seguindo requisitos específicos e utilizando boas práticas de desenvolvimento.

## Funcionalidades

- **Processamento de Pagamento:** Realiza a autorização de um pagamento.
- **Consulta de Transações:** Permite consultar todas as transações ou uma específica pelo seu ID.
- **Estorno de Pagamentos:** Realiza o estorno de uma transação autorizada.

## Estrutura do Projeto

O projeto foi organizado em pacotes para manter a separação de responsabilidades:

- **controller:** Contém os controladores REST que lidam com as requisições HTTP.
- **service:** Contém a lógica de negócios.
- **model:** Contém as entidades e enums que representam os dados.
- **factory:** Contém a `TransacaoFactory`, responsável por criar instâncias de `Transacao`.
- **exception:** Contém as classes de exceção e o manipulador global de exceções.

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.3.2**
- **Dependências Relevantes:**
    - `spring-boot-starter-web`: Fornece o suporte necessário para criar APIs RESTful.
    - `spring-boot-starter-validation`: Facilita a validação de dados de entrada usando anotações como `@Valid` e `@NotNull`.

## Decisões de Design

### Encapsulamento em Objetos

Todas as chamadas e respostas da API estão encapsuladas em objetos como `SolicitacaoPagamento`, `RespostaPagamento`, e `RespostaEstorno`. Essa decisão foi tomada para seguir o exemplo fornecido no desafio, onde as transações eram representadas como objetos aninhados. Isso facilita a extensão futura da API e mantém o padrão de design consistente.

### Status da Transação

O desafio especificou que as transações podem ter os seguintes status: `"AUTORIZADO"`, `"NEGADO"`. Entretanto, o exemplo fornecido mostrava o status dentro do objeto `descricao`. Para manter a consistência com o exemplo, o status da transação foi mantido dentro do objeto `descricao`.

## Chamadas e Respostas da API

### Processamento de Pagamento

**Endpoint:** `POST /api/pagamentos`

**Requisição:**

```json
{
  "transacao": {
    "id": 1,
    "cartao": "4444555566661234",
    "descricao": {
      "valor": 500.50,
      "dataHora": "01/05/2021 18:30:00",
      "estabelecimento": "PetShop Mundo cão"
    },
    "formaPagamento": {
      "tipo": "AVISTA",
      "parcelas": 1
    }
  }
}
```

**Resposta:**

```json
{
  "transacao": {
    "id": 1,
    "cartao": "4444555566661234",
    "descricao": {
      "valor": 500.50,
      "dataHora": "01/05/2021 18:30:00",
      "estabelecimento": "PetShop Mundo cão",
      "nsu": "1234567890",
      "codigoAutorizacao": "147258369",
      "status": "AUTORIZADO"
    },
    "formaPagamento": {
      "tipo": "AVISTA",
      "parcelas": 1
    }
  }
}
```

### Consulta de Transações

**Endpoint:** `GET /api/pagamentos`

**Resposta:**

```json
{
  "transacoes": [
    {
      "id": 1,
      "cartao": "4444555566661234",
      "descricao": {
        "valor": 50.00,
        "dataHora": "01/05/2021 18:30:00",
        "estabelecimento": "PetShop Mundo cão",
        "nsu": "1723250200145",
        "codigoAutorizacao": "360627302",
        "status": "CANCELADO"
      },
      "formaPagamento": {
        "tipo": "AVISTA",
        "parcelas": 1
      }
    }
  ]
}
```

### Estorno de Pagamento

**Endpoint:** `POST /api/pagamentos/{id}/estorno`

**Resposta:**

```json
{
  "transacao": {
    "id": 1,
    "cartao": "4444555566661234",
    "descricao": {
      "valor": 50.00,
      "dataHora": "01/05/2021 18:30:00",
      "estabelecimento": "PetShop Mundo cão",
      "nsu": "1723250200145",
      "codigoAutorizacao": "360627302",
      "status": "CANCELADO"
    },
    "formaPagamento": {
      "tipo": "AVISTA",
      "parcelas": 1
    }
  }
}
```

## Executando o Projeto

### Pré-requisitos

- **Java 21**
- **Maven 3.6.3** ou superior

### Passos para Executar

1. Clone o repositório:

   ```bash
   git clone https://github.com/seu-usuario/ToolsChallenge.git
   ```

2. Navegue até o diretório do projeto:

   ```bash
   cd ToolsChallenge
   ```

3. Compile e execute o projeto usando Maven:

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. A API estará disponível em `http://localhost:8080/api/pagamentos`.

## Executando os Testes

Para executar os testes unitários e de integração, utilize o seguinte comando:

```bash
mvn test
```

Os testes verificam a funcionalidade completa da API, incluindo:

- Processamento de pagamentos
- Consulta de transações
- Estorno de pagamentos
- Tratamento de exceções

## Considerações Finais

Espero que este README forneça todas as informações necessárias para entender e executar o projeto. Se houver dúvidas adicionais ou sugestões, sinta-se à vontade para abrir uma *issue* no repositório.

Este projeto foi desenvolvido como parte de um desafio técnico, com foco em seguir boas práticas de desenvolvimento e garantir que todos os requisitos sejam atendidos. O uso de uma `TransacaoFactory` facilita a criação e manutenção de transações com diferentes características, mantendo o código modular e reutilizável.
