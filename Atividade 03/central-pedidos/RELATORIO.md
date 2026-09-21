# Relatório do grupo

Integrantes: Leonardo Teixeira e Matheus Polizelli

## Grafos e complexidade

**Modelo adotado:**

- **Curto-circuito (`&&`, `||`):** Cada operando foi considerado um nó de decisão independente, pois a linguagem Java avalia da esquerda para a direita e interrompe a avaliação se o resultado já for garantido.
- **Exceções:** O lançamento de uma exceção (`throw`) é tratado como uma aresta direcional para um nó de saída de erro genérico (ou para o bloco `catch`), ramificando o fluxo normal.

| Método                      | Nós (N) | Arestas (E) | V(G) | Caminhos independentes | Restrições de viabilidade                                                                                                                                  |
| --------------------------- | ------- | ----------- | ---- | ---------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `AnaliseRisco.avaliar`      | 12      | 18          | 8    | 8                      | Valores negativos causam interrupção imediata. Caminhos com compras negativas não ocorrem na prática devido à validação no construtor do record `Cliente`. |
| `CalculadoraFrete.calcular` | 15      | 23          | 10   | 10                     | Impossível testar subtotal ativo nulo, pois a validação do `PedidoService` impede que o fluxo chegue aqui com total = 0.                                   |
| `PagamentoService.pagar`    | 9       | 13          | 6    | 6                      | Apenas a exceção `IllegalStateException` estende o fluxo do laço; demais exceções encerram a execução imediatamente.                                       |
| `PedidoService.fechar`      | 11      | 15          | 6    | 6                      | O fluxo depende inteiramente das retornos precoces (`BLOQUEADO`, `SEM_ESTOQUE`, `REVISAO`), impedindo caminhos paralelos simulados.                        |

## Matriz de testes

| ID / método JUnit                          | Unidade            | Entrada e estado do stub                          | Resultado esperado               | Caminho / aresta                                               | Critério atendido                            |
| ------------------------------------------ | ------------------ | ------------------------------------------------- | -------------------------------- | -------------------------------------------------------------- | -------------------------------------------- |
| `deveRejeitarSempreClienteBloqueado`       | `AnaliseRisco`     | `cliente.bloqueado() = true`                      | Retorno `"RECUSADO"`             | Avaliação direta no início do fluxo.                           | Branch de bloqueio.                          |
| `deveAplicarDezPorcentoParaVip`            | `PoliticaDesconto` | `vip = true`, `subtotal = 40_000`                 | Desconto de `4_000`              | Caminho do primeiro `if` (VIP) sem cupons.                     | Decisão principal verdadeira.                |
| `deveZerarBaseEmFreteGratis`               | `CalculadoraFrete` | PR, 3kg, Frágil, Não VIP, Total 30k               | Frete `500` (só taxa de frágil). | Anula switch de estado e while de peso.                        | Cobertura de laço omitido e branches falsas. |
| `deveEsgotarTentativasEmIndisponibilidade` | `PagamentoService` | `stub` lança `IllegalStateException` nas 3 vezes. | Retorno `false` após 3 laços.    | Passagem pelo `catch` e repetição máxima do `do/while`.        | Cobertura de laço e exceção.                 |
| `deveReterProcessamentoParaRevisao`        | `PedidoService`    | Pedido de 150k, cliente sem histórico.            | Status `"REVISAO"`.              | Retorno no bloco de risco; interface de pagamento não chamada. | Retorno antecipado (Short-circuit).          |

## Evolução da cobertura

| Etapa                        | Testes executados | Linhas | Branches | Métodos | Classes | Lacunas e justificativas                                                                            |
| ---------------------------- | ----------------- | ------ | -------- | ------- | ------- | --------------------------------------------------------------------------------------------------- |
| Inicial                      | 0                 | 0%     | 0%       | 0%      | 0%      | Código de produção intocado.                                                                        |
| Pós-Unidade                  | ~30               | ~85%   | ~80%     | ~90%    | ~100%   | Faltava integração do fluxo principal e verificação de passagens de estado nulo em `PedidoService`. |
| Integração (`PedidoService`) | ~40 (Total)       | 100%   | 100%     | 100%    | 100%    | Todas as 116 branches e 108 linhas foram validadas.                                                 |

## Análise crítica

- **Quais combinações faltavam mesmo com os ramos cobertos?**
  A cobertura de branches (100%) garante que o compilador passou por todos os `ifs`, mas não garante todas as combinações semânticas. Por exemplo: em `CalculadoraFrete`, cobrimos o ramo de cliente VIP, o ramo de pedido expresso e o ramo de item frágil, mas não necessariamente escrevemos um teste onde os _três_ sejam verdadeiros simultaneamente.
- **Quais condições não foram avaliadas devido ao curto-circuito?**
  Na regra de risco `if (total > 100_000 || expresso)`, quando enviamos um `total = 150_000`, a variável `expresso` nunca chegou a ser lida na execução por conta do curto-circuito do operador `||`. O mesmo ocorreu com o `&&` na validação de clientes com compras (`total > 500_000 && !cliente.vip()`).
- **Quais caminhos são inviáveis no serviço, mas viáveis na unidade?**
  Na classe `CalculadoraFrete`, o método consegue matematicamente lidar com subtotais de frete negativos ou vazios. No entanto, através do `PedidoService`, é impossível atingir esse caminho, pois o serviço lança `IllegalArgumentException("Pedido sem itens ativos")` antes de chamar a calculadora.
- **Como foram testadas exceções e quantidades de iterações?**
  O `while` de cálculo de peso no frete foi testado com iteração zero (pedido com 2kg), uma iteração exata (3kg) e iteração fracionada (3001g, exigindo duas rodadas do laço para zerar o peso). As exceções foram testadas através de stubs via interfaces funcionais (`ProcessadorPagamento`), lançando a `IllegalStateException` propositalmente nas duas primeiras tentativas e validando se o código recuperava.
- **Qual alteração proposital foi detectada por qual teste? A alteração foi desfeita?**
  (Exemplo para a prática): A alteração do operador `if (total > 500_000)` para `>= 500_000` em `AnaliseRisco` foi imediatamente detectada pelo teste `deveAvaliarClientesComComprasAnteriores`, que contava com um cenário-limite cobrando exatos 500.000 e esperando status `"APROVADO"`. Após confirmar a falha defensiva, a alteração foi desfeita para recuperar os 100%.
