# Exercício 1: Classificação de pedido

## 1. Blocos básicos e decisões

| Nó | Bloco básico / ação |
|---:|---|
| 1 | Início do método e inicialização: `desconto = 0`. |
| 2 | Decisão D1: `valor >= 500`. |
| 3 | Atribuição: `desconto = 10`. |
| 4 | Decisão D2: `clienteVip`. |
| 5 | Atribuição: `desconto += 5`. |
| 6 | Decisão D3: `!pagamentoAprovado`. |
| 7 | Retorno antecipado: `"PAGAMENTO RECUSADO"`. |
| 8 | Cálculo de `valorFinal` e retorno `"PEDIDO APROVADO: " + valorFinal`. |
| 9 | Fim do método. |

As três decisões são os nós 2, 4 e 6, em cada losango, a saída **T** significa condição verdadeira e **F**, falsa.

## 2. Grafo de Fluxo de Controle

![Grafo de Fluxo de Controle numerado do Exercício 1](imagens/cfg_exercicio_1.svg)

O nó 7 representa o encerramento antecipado, após o retorno de pagamento recusado, o fluxo segue ao fim do método e não executa o nó 8.

## 3. Nós, arestas e complexidade ciclomática

As arestas do grafo são:

`1 -> 2`, `2 -> 3`, `2 -> 4`, `3 -> 4`, `4 -> 5`, `4 -> 6`, `5 -> 6`, `6 -> 7`, `6 -> 8`, `7 -> 9` e `8 -> 9`

- Número de nós: **N = 9**.
- Número de arestas: **E = 11**.
- `V(G) = E - N + 2 = 11 - 9 + 2 = 4`.
- Há três decisões; portanto, `V(G) = 3 + 1 = 4`.

Os dois cálculos coincidem, a complexidade ciclomática é **4**.

## 4. Base de caminhos independentes e casos de teste

| Caminho | Sequência de nós | Entradas (`valor`, `clienteVip`, `pagamentoAprovado`) | Resultado esperado |
|---|---|---|---|
| P1 | `1 -> 2(F) -> 4(F) -> 6(T) -> 7 -> 9` | `400`, `false`, `false` | `PAGAMENTO RECUSADO` |
| P2 | `1 -> 2(T) -> 3 -> 4(F) -> 6(T) -> 7 -> 9` | `500`, `false`, `false` | `PAGAMENTO RECUSADO` |
| P3 | `1 -> 2(F) -> 4(T) -> 5 -> 6(T) -> 7 -> 9` | `400`, `true`, `false` | `PAGAMENTO RECUSADO` |
| P4 | `1 -> 2(F) -> 4(F) -> 6(F) -> 8 -> 9` | `400`, `false`, `true` | `PEDIDO APROVADO: 400.0` |

Cada caminho acrescenta pelo menos uma aresta ainda não coberta pelos anteriores, P2 cobre o desconto por valor, P3 cobre o desconto VIP e P4 cobre o fluxo aprovado com cálculo de `valorFinal`.

## 5. Questões para discussão

**Quantas combinações entre as três condições são possíveis?** Há `2³ = 8` combinações possíveis dos resultados das condições, `valor >= 500`, `clienteVip` e `pagamentoAprovado`.

**Esse número é igual a complexidade ciclomática?** Não, as oito combinações representam possibilidades de entrada/ramificação, a complexidade ciclomática é 4 e indica o número de caminhos linearmente independentes necessário para uma base de testes, ela não exige testar todas as combinações.

**Como o `return` na terceira condição altera o grafo?** Ele cria o nó 7 e uma aresta direta desse nó para o fim, quando `!pagamentoAprovado` é verdadeiro, o fluxo não se junta ao caminho de aprovação nem executa o cálculo final.

**É possível calcular `valorFinal` quando o pagamento não foi aprovado?** Não, nesse caso, a terceira decisão direciona o fluxo ao retorno `"PAGAMENTO RECUSADO"`, encerrando o método antes do nó 8.
