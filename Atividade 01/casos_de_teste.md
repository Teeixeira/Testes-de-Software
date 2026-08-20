# Casos de Teste - Sistema de Reserva de Laboratórios

## Caso de Teste 01 - Efetivação de Reserva Padrão (RF-01)
**Descrição:** Validar a funcionalidade básica de criação de reserva e suas validações de campos obrigatórios.
**Pré-condições:** Usuário logado. Laboratórios disponíveis no sistema.
*   **Cenário Positivo:**
    *   **Passos:** Preencher sala, data, horário e vincular uma turma válida. Clicar em "Salvar".
    *   **Resultado Esperado:** Sistema exibe mensagem de sucesso e confirma a reserva.
*   **Cenário Negativo:**
    *   **Passos:** Deixar o campo "Turma" ou "Horário" em branco e tentar salvar.
    *   **Resultado Esperado:** O sistema bloqueia a ação, destaca os campos não preenchidos em vermelho e exibe a mensagem "Campos obrigatórios ausentes". A reserva não é criada.

---

## Caso de Teste 02 - Prevenção de Dupla Ocupação (RF-02)
**Descrição:** Assegurar que o sistema recuse o agendamento de uma sala que já possui uma reserva confirmada para o mesmo dia e horário.
**Pré-condições:** O "Laboratório A" já possui uma reserva confirmada das 08:00 às 10:00 no dia 15/09.
*   **Cenário Positivo:**
    *   **Passos:** Tentar reservar o "Laboratório A" para o dia 15/09, no horário livre das 10:30 às 12:00.
    *   **Resultado Esperado:** Reserva efetuada com sucesso, pois não há conflito.
*   **Cenário Negativo:**
    *   **Passos:** Tentar reservar o mesmo "Laboratório A" para o dia 15/09, das 09:00 às 11:00 (gerando sobreposição).
    *   **Resultado Esperado:** O sistema interrompe o fluxo, exibe um aviso informando choque de horários e aborta a transação.

---

## Caso de Teste 03 - Validação de Limite de Alunos (RF-03)
**Descrição:** Confirmar se o sistema controla corretamente a relação entre tamanho da turma e capacidade física do laboratório.
**Pré-condições:** "Laboratório B" possui capacidade máxima configurada para 30 alunos.
*   **Cenário Positivo:**
    *   **Passos:** Selecionar o "Laboratório B" e vincular uma turma com 25 alunos. Clicar em salvar.
    *   **Resultado Esperado:** Reserva aprovada e registrada na grade.
*   **Cenário Negativo:**
    *   **Passos:** Selecionar o "Laboratório B" e vincular uma turma com 35 alunos.
    *   **Resultado Esperado:** A interface exibe alerta de incompatibilidade (capacidade excedida) e desabilita a opção de confirmação.

---

## Caso de Teste 04 - Trava de Manutenção de Laboratório (RF-04)
**Descrição:** Garantir que espaços em manutenção fiquem indisponíveis para novas reservas.
**Pré-condições:** O "Laboratório C" foi colocado no status "Em Manutenção". "Laboratório D" está "Livre".
*   **Cenário Positivo:**
    *   **Passos:** Buscar horários para o "Laboratório D" e confirmar agendamento.
    *   **Resultado Esperado:** Operação concluída sem restrições de status.
*   **Cenário Negativo:**
    *   **Passos:** Tentar forçar o agendamento no "Laboratório C" injetando o ID da sala diretamente na requisição (API) ou via URL direta.
    *   **Resultado Esperado:** O backend do sistema deve barrar a tentativa, retornando erro de indisponibilidade técnica da sala.

---

## Caso de Teste 05 - Regra da Janela de Horário (RF-05)
**Descrição:** Certificar que o sistema obedece ao horário de funcionamento da instituição (07h30 às 22h30).
**Pré-condições:** Usuário na tela de criação de agendamentos.
*   **Cenário Positivo:**
    *   **Passos:** Agendar uma reserva das 19:00 às 21:00.
    *   **Resultado Esperado:** Agendamento ocorre normalmente.
*   **Cenário Negativo:**
    *   **Passos:** Tentar inserir um horário de início para as 23:00 e término às 23:50.
    *   **Resultado Esperado:** Bloqueio imediato na interface com a mensagem alertando que o horário está fora do período de funcionamento.

---

## Caso de Teste 06 - Permissão de Edição por Hierarquia (RF-06)
**Descrição:** Validar as permissões de acesso sobre a modificação de agendamentos de terceiros.
**Pré-condições:** Reserva ativa sob titularidade do "Prof. João". "Prof. Pedro" logado no sistema.
*   **Cenário Positivo:**
    *   **Passos:** Um usuário com perfil "Coordenador" acessa o sistema, busca a reserva do "Prof. João" e altera a data.
    *   **Resultado Esperado:** Alteração salva com sucesso, pois o perfil possui o privilégio necessário.
*   **Cenário Negativo:**
    *   **Passos:** "Prof. Pedro" tenta enviar um payload malicioso via DevTools (alterando o ID da sua própria reserva para o ID da reserva do João) na tentativa de modificá-la.
    *   **Resultado Esperado:** O servidor recusa a modificação com o código HTTP 403 (Acesso Negado), detectando que ele não é dono da reserva nem coordenador.

---

## Caso de Teste 07 - Cancelamento, Liberação e Histórico (RF-07)
**Descrição:** Avaliar as regras de negócio em torno do cancelamento de reservas.
**Pré-condições:** Duas reservas do usuário logado: uma futura e uma que ocorreu na semana passada.
*   **Cenário Positivo:**
    *   **Passos:** Clicar em "Cancelar" na reserva futura.
    *   **Resultado Esperado:** Horário é liberado na grade imediatamente e a ação fica gravada no histórico.
*   **Cenário Negativo:**
    *   **Passos:** Tentar acessar a reserva que ocorreu na semana passada e acionar o botão/rota de cancelamento.
    *   **Resultado Esperado:** O sistema deve impedir o cancelamento de eventos retroativos (já concluídos), emitindo um aviso de que a ação é inválida para o período.

---

## Caso de Teste 08 - Disparo de Notificações (RF-08)
**Descrição:** Validar o envio de notificações após eventos de alteração ou exclusão.
**Pré-condições:** Reserva ativa. Sistema de mensagens em funcionamento.
*   **Cenário Positivo:**
    *   **Passos:** Coordenador altera o horário de uma reserva e salva.
    *   **Resultado Esperado:** O sistema gera um evento de alerta na caixa de notificações do professor responsável avisando da alteração.
*   **Cenário Negativo:**
    *   **Passos:** Usuário clica em "Editar" na sua própria reserva, não faz nenhuma mudança nos campos e clica em "Salvar".
    *   **Resultado Esperado:** O sistema identifica que não houve mudança real nos dados e **não** gera disparo de notificação falsa/redundante.

---

## Caso de Teste 09 - Desempenho da Busca (RNF-01)
**Descrição:** Avaliar a estabilidade e o tempo de resposta do módulo de busca.
**Pré-condições:** Banco de dados populado. Ferramentas de medição de rede ativas.
*   **Cenário Positivo:**
    *   **Passos:** Executar uma busca por laboratórios vagos com filtros convencionais (data e unidade).
    *   **Resultado Esperado:** O servidor deve retornar e a tela deve renderizar a listagem em um tempo total inferior a 2 segundos.
*   **Cenário Negativo:**
    *   **Passos:** Inserir dados corrompidos ou inválidos no filtro de datas (ex: 31/02/2026 ou formatos inesperados) e acionar a busca.
    *   **Resultado Esperado:** O sistema não deve tentar fazer uma varredura pesada no banco. A validação deve ocorrer rapidamente, devolvendo a mensagem de "Data inválida" e mantendo o tempo de resposta muito abaixo do teto de 2 segundos.

---

## Caso de Teste 10 - Registro na Trilha de Auditoria (RNF-02)
**Descrição:** Garantir a inviolabilidade e a geração da trilha de auditoria.
**Pré-condições:** Diversas operações (inserts, updates e deletes) foram realizadas recentemente.
*   **Cenário Positivo:**
    *   **Passos:** Um administrador do sistema acessa o painel de logs de auditoria.
    *   **Resultado Esperado:** Todos os registros das ações constam na tabela com usuário, data/hora e metadados corretos.
*   **Cenário Negativo:**
    *   **Passos:** Um usuário comum descobre a URL do painel de auditoria (`/relatorios/auditoria`) e tenta acessá-la para visualizar ou excluir rastros.
    *   **Resultado Esperado:** O sistema barra o acesso imediatamente por falta de privilégios de segurança (retorno 401 ou 403), mantendo o sigilo dos logs.

---

## Caso de Teste 11 - Restrição de Acesso por Unidade (RNF-03)
**Descrição:** Confirmar o isolamento lógico das unidades dentro da plataforma.
**Pré-condições:** Usuário logado pertence apenas à "Unidade Centro".
*   **Cenário Positivo:**
    *   **Passos:** Acessar a listagem geral de laboratórios e pesquisar horários vagos.
    *   **Resultado Esperado:** Apenas salas pertencentes à "Unidade Centro" são listadas e disponibilizadas para reserva.
*   **Cenário Negativo:**
    *   **Passos:** O usuário intercepta o envio do formulário de reserva e troca manualmente o `id_sala` para uma sala pertencente à "Unidade Sul".
    *   **Resultado Esperado:** O sistema identifica a incompatibilidade de escopo no backend e rejeita a transação por falha de autorização corporativa, não criando a reserva.
