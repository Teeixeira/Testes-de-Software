## Respostas dos Exercícios Teóricos

### 1. O que significa cada percentual de cobertura do JaCoCo

O relatório do JaCoCo mede o que foi efetivamente executado pelos testes. Alcançar 100% em cada métrica significa o seguinte:

- **Cobertura de Classes (Classes):** Indica se ao menos um método de uma determinada classe foi executado. Atingir 100% significa que os testes instanciaram e utilizaram todas as classes do projeto (neste caso, `Boletim` e `Participacao`).
- **Cobertura de Métodos (Methods):** Aponta se um método específico foi chamado durante os testes. 100% significa que todos os métodos criados no código de produção (`calcularMedia`, `verificarSituacao`, `contarAprovados` e `calcularPontos`) foram invocados.
- **Cobertura de Linhas (Lines):** Verifica se as instruções contidas em cada linha de código foram executadas. 100% garante que nenhuma linha escrita no código-fonte foi ignorada durante a execução dos testes.
- **Cobertura de Branches / Ramos (Branches):** Mede se todas as alternativas (verdadeira e falsa) de cada estrutura de decisão (como `if/else` e o laço `for`) foram exercitadas. Atingir 100% aqui significa que os testes validaram todos os desvios lógicos possíveis no código.

### 2. Entradas e caminhos do método `calcularPontos`

O método `calcularPontos` da classe `Participacao` avalia duas decisões independentes, gerando quatro combinações possíveis de entrada (verdadeiro/falso). As entradas correspondentes a cada caminho são:

- **Caminho 1 (Ambos verdadeiros):** `entregouAtividade = true` e `participouDaAula = true`. O código entra nos dois blocos `if`, somando primeiro 2 pontos e depois mais 1 ponto. **Resultado: 3 pontos**.
- **Caminho 2 (Somente atividade):** `entregouAtividade = true` e `participouDaAula = false`. O fluxo entra apenas no primeiro `if` e ignora o segundo. **Resultado: 2 pontos**.
- **Caminho 3 (Somente aula):** `entregouAtividade = false` e `participouDaAula = true`. O fluxo ignora o primeiro `if` e entra apenas no segundo. **Resultado: 1 ponto**.
- **Caminho 4 (Ambos falsos):** `entregouAtividade = false` e `participouDaAula = false`. O código não entra em nenhum dos blocos `if`, mantendo o valor inicial da variável (zero). **Resultado: 0 pontos**.
