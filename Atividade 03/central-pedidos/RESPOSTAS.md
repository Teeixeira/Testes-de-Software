# Respostas Exercícios teóricos

## 1. Cobertura de Ramos vs. Cobertura de Caminhos

A **cobertura de ramos (branch coverage)** assegura que cada desvio lógico individual — ou seja, as alternativas verdadeiras e falsas de cada estrutura condicional (como `if` e `switch`) — foi exercitado pelo menos uma vez durante os testes. No entanto, ela **falha em garantir que todas as combinações sequenciais (caminhos completos) dessas decisões foram testadas em conjunto**.

**Exemplo prático no sistema:**
No método `calcular` da classe `CalculadoraFrete`, existem múltiplas decisões em sequência que avaliam isenção, status VIP, entrega expressa e itens frágeis. É matematicamente possível atingir 100% de cobertura de ramos com apenas dois testes diametralmente opostos (um onde todas as condições são simultaneamente verdadeiras e outro onde todas são falsas).

Isso deixa um vasto número de **caminhos intermediários descobertos**. Por exemplo: um cenário de um cliente VIP (condição verdadeira), mas sem pedido expresso (condição falsa), contendo um item frágil (condição verdadeira). Defeitos ou interações indesejadas ocultas em combinações específicas de regras podem passar facilmente para a produção, mesmo com o JaCoCo relatando 100% de cobertura de branches.

## 2. Exceção Não Representada no Contador de Branches

O JaCoCo baseia sua contagem de ramos na análise do fluxo de controle estrutural, que é gerado na compilação de estruturas lógicas convencionais, como `if/else`, `switch`, `for` e `while`. Devido à forma como o bytecode do Java é instrumentado e lido pela ferramenta, **blocos de tratamento de exceção (`try/catch`) não incrementam os contadores de branches visíveis como "losangos" lógicos.**

**Exemplo prático no sistema:**
No método `pagar` da classe `PagamentoService`, a invocação `processador.autorizar(total)` fica dentro de um bloco `try`. Ela pode ser executada normalmente ou lançar uma exceção `IllegalStateException`.

Essa transição excepcional, que interrompe o fluxo normal e desvia a execução para o bloco `catch`, não é desenhada como uma decisão condicional pelo JaCoCo. Consequentemente, os contadores de ramos gerais podem sinalizar 100% de cobertura mesmo que o cenário de indisponibilidade (e a consequente repetição do laço do-while pelo `catch`) nunca seja provocado pelos testes unitários. É por isso que é fundamental testar as exceções propositalmente, além de apenas confiar na métrica de cobertura.
