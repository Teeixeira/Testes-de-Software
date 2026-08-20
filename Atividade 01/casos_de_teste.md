# Casos de Teste - Sistema de Reserva de Laboratórios

## Caso de Teste 01 - Prevenção de Dupla Ocupação (RF-02)
**Descrição:** Assegurar que o sistema recuse o agendamento de uma sala que já possui uma reserva confirmada para o mesmo dia e horário.
**Pré-condições:** O sistema está online. Existe uma reserva confirmada para o "Laboratório A" no dia 15/09, das 08:00 às 10:00. O usuário possui permissão para agendar.
**Passos:**
1. Acessar o módulo de nova reserva.
2. Selecionar o "Laboratório A".
3. Inserir a data 15/09 e o horário das 08:00 às 10:00.
4. Preencher os demais dados e clicar em "Confirmar Reserva".
*   **Resultado Esperado:** O sistema deve interromper a operação e exibir um aviso claro informando que o laboratório já se encontra ocupado neste período. A reserva não deve ser salva.

---

## Caso de Teste 02 - Validação de Limite de Alunos (RF-03)
**Descrição:** Confirmar se o sistema bloqueia a alocação de uma turma cujo número de integrantes é superior à capacidade cadastrada do laboratório.
**Pré-condições:** O "Laboratório B" possui capacidade máxima configurada para 30 alunos.
**Passos:**
1. Acessar o módulo de nova reserva.
2. Selecionar o "Laboratório B" em um horário disponível.
3. Vincular uma turma que possui 35 alunos matriculados.
4. Clicar em "Confirmar Reserva".
*   **Resultado Esperado:** A interface deve gerar uma mensagem de erro alertando sobre a incompatibilidade de capacidade (Turma: 35 / Sala: 30). O agendamento deve ser abortado.

---

## Caso de Teste 03 - Trava de Manutenção de Laboratório (RF-04)
**Descrição:** Garantir que espaços sinalizados como "Em Manutenção" fiquem indisponíveis para qualquer nova reserva.
**Pré-condições:** O "Laboratório C" teve seu status alterado para "Em Manutenção" pela administração.
**Passos:**
1. Acessar a tela de pesquisa de disponibilidade.
2. Tentar buscar horários vagos para o "Laboratório C".
*   **Cenário 1 (Busca):** O laboratório não deve aparecer como selecionável ou deve apresentar um indicativo visual bloqueado ("Em Manutenção").
*   **Cenário 2 (Tentativa Direta):** Caso tente forçar a reserva por um atalho, o sistema deve bloquear o salvamento e emitir um alerta de indisponibilidade técnica.

---

## Caso de Teste 04 - Regra da Janela de Horário (RF-05)
**Descrição:** Certificar que o sistema aceite apenas reservas contidas no intervalo permitido da instituição (07h30 às 22h30).
**Pré-condições:** Usuário autenticado na tela de criação de agendamentos.
**Passos:**
1. Selecionar um laboratório livre.
2. Preencher um horário de início às 06:30 e término às 07:20.
3. Clicar em "Confirmar Reserva".
*   **Resultado Esperado:** A plataforma deve emitir um alerta apontando que o horário está fora do período comercial autorizado da unidade.

---

## Caso de Teste 05 - Permissão de Edição por Hierarquia (RF-06)
**Descrição:** Verificar se professores comuns são impedidos de alterar horários de terceiros, enquanto coordenadores conseguem fazê-lo.
**Pré-condições:** O "Professor João" tem uma reserva ativa. Dois usuários de teste prontos: "Professor Pedro" (Perfil Docente) e "Maria" (Perfil Coordenador).
**Passos:**
*   **Cenário 1 (Acesso Não Autorizado):**
    1. Logar como "Professor Pedro".
    2. Acessar a reserva do "Professor João" e tentar modificar o horário.
    *   *Resultado Esperado:* O botão de edição deve estar oculto ou bloqueado, e tentativas diretas de salvar devem retornar erro de "Permissão Negada".
*   **Cenário 2 (Acesso Autorizado):**
    1. Logar como "Maria" (Coordenadora).
    2. Acessar a reserva do "Professor João", alterar o horário para 1 hora mais tarde e salvar.
    *   *Resultado Esperado:* O sistema deve permitir a alteração e salvar o novo horário com sucesso, disparando a rotina de notificação.
