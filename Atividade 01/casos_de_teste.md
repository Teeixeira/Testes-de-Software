# Casos de Teste - Sistema de Reserva de Laboratórios

---

### Caso de Teste 1 - Criação de Reserva e Restrições de Unidade/Horário (CT-01)
**Descrição:** Verificar se o sistema permite a criação de reservas em horários e unidades permitidas, impedindo agendamentos fora do horário de funcionamento ou em unidades não autorizadas ao perfil.  
**Pré-condições:** Usuário com perfil "Docente" autenticado no sistema e vinculado à "Unidade Centro". Salas cadastradas na base.  
**Passos:**
1. Acessar o módulo de agendamento de laboratórios.
2. Selecionar o laboratório desejado na lista de opções disponíveis.
3. Informar a turma e o intervalo de horário desejado.
4. Clicar no botão "Salvar Reserva".

#### Cenário 1 - Criação de reserva com sucesso (Fluxo Positivo):
- **Dados de Teste:**
  - Laboratório: "Lab 01 - Unidade Centro"
  - Turma: "Engenharia de Software (30 alunos)"
  - Horário: "10:00 às 12:00"
- **Resultado Esperado:**
  - A reserva é confirmada e exibida na grade de horários.
  - A trilha de auditoria grava o log de inserção (*Insert*) com os dados da transação (RNF-02).
  - Apenas salas vinculadas à "Unidade Centro" são listadas para o docente (RNF-03).

#### Cenário 2 - Tentativa de agendamento fora da janela permitida (Fluxo Negativo):
- **Dados de Teste:**
  - Laboratório: "Lab 01 - Unidade Centro"
  - Horário: "23:00 às 00:00" (fora da janela de 07h30 às 22h30)
- **Resultado Esperado:**
  - O sistema bloqueia a criação do agendamento.
  - Exibe mensagem de erro informando que o horário está fora do intervalo de funcionamento permitido (RF-05).
  - Nenhuma reserva é gravada no banco.

#### Cenário 3 - Tentativa de reserva em unidade não autorizada via manipulação de ID (Fluxo Negativo):
- **Dados de Teste:**
  - Laboratório: ID correspondente a "Lab Sul 01 - Unidade Sul"
  - Horário: "10:00 às 12:00"
- **Resultado Esperado:**
  - O sistema rejeita a solicitação emitindo código/alerta de Acesso Não Autorizado (Erro 403 / Proibido) (RNF-03).
  - A reserva não é criada e a tentativa irregular é registrada em log de segurança.

**Pós-condições:** O sistema mantém a integridade da grade de horários e exibe a mensagem de confirmação ou erro de acordo com o cenário testado.

---

### Caso de Teste 2 - Travas de Conflito de Horário, Capacidade e Manutenção (CT-02)
**Descrição:** Verificar se o sistema valida corretamente a disponibilidade do espaço, impedindo choque de horários, excesso de capacidade de alunos e alocação de salas em manutenção.  
**Pré-condições:** 
- "Sala A" com reserva existente das 08:00 às 10:00.
- "Sala B" com capacidade máxima de 30 alunos.
- "Sala C" com status "Em Manutenção".  
**Passos:**
1. Acessar a tela de nova reserva.
2. Selecionar a sala e preencher o tamanho da turma e o horário.
3. Confirmar a submissão no botão "Salvar".

#### Cenário 1 - Reserva válida respeitando horário e capacidade (Fluxo Positivo):
- **Dados de Teste:**
  - Sala: "Sala A" | Horário: "10:30 às 12:00" | Turma: 25 alunos
  - Sala: "Sala B" | Horário: "14:00 às 16:00" | Turma: 25 alunos (limite: 30)
- **Resultado Esperado:**
  - Ambas as reservas são processadas e salvas com sucesso no sistema.

#### Cenário 2 - Tentativa de agendamento em horário conflitante (Fluxo Negativo):
- **Dados de Teste:**
  - Sala: "Sala A"
  - Horário: "09:00 às 11:00" (conflita com reserva das 08:00 às 10:00)
- **Resultado Esperado:**
  - O sistema aborta o agendamento e emite mensagem de aviso indicando choque/sobreposição de horários (RF-02).

#### Cenário 3 - Tentativa de alocação de turma que excede a lotação (Fluxo Negativo):
- **Dados de Teste:**
  - Sala: "Sala B" (capacidade máxima: 30)
  - Turma: 35 alunos
- **Resultado Esperado:**
  - O sistema recusa o agendamento e apresenta alerta de superlotação/capacidade física excedida (RF-03).

#### Cenário 4 - Tentativa de agendamento de sala em manutenção (Fluxo Negativo):
- **Dados de Teste:**
  - Sala: "Sala C" (Status: Em Manutenção)
- **Resultado Esperado:**
  - O sistema bloqueia a seleção ou exibe alerta de que a sala está indisponível para uso devido a manutenção (RF-04).

**Pós-condições:** O sistema permanece consistente sem permitir registros inválidos ou sobrepostos na base de dados.

---

### Caso de Teste 3 - Permissões de Edição e Notificações (CT-03)
**Descrição:** Validar as regras de controle de acesso para alteração de agendamentos por terceiros e o respectivo disparo de alertas de comunicação.  
**Pré-condições:** 
- Usuário "Prof. João" (Docente) e usuária "Coord. Maria" (Coordenação) ativos no sistema.
- Reserva existente criada pelo usuário "Prof. João".  
**Passos:**
1. Acessar a tela de consulta/detalhes da reserva existente.
2. Aplicar as alterações necessárias nos campos de data/horário.
3. Clicar em "Atualizar Reserva".

#### Cenário 1 - Edição de reserva realizada pela coordenação (Fluxo Positivo):
- **Dados de Teste:**
  - Usuário logado: "Coord. Maria"
  - Ação: Alterar horário da reserva de "João" de 08:00 para 09:00
- **Resultado Esperado:**
  - A alteração é salva com sucesso (RF-06).
  - O sistema dispara automaticamente um alerta/notificação para o "Prof. João" comunicando a mudança (RF-08).
  - A auditoria grava o log de atualização (*Update*) com identificação do autor da edição (RNF-02).

#### Cenário 2 - Tentativa de edição por outro docente sem permissão (Fluxo Negativo):
- **Dados de Teste:**
  - Usuário logado: "Prof. Carlos" (Docente sem perfil de coordenação)
  - Ação: Tentar editar a reserva de "João" via URL direta ou interface
- **Resultado Esperado:**
  - O botão de edição permanece oculto ou o sistema exibe mensagem de "Acesso Negado" (RF-06).
  - Nenhuma alteração é refletida no agendamento.

#### Cenário 3 - Submissão de formulário sem alterações efetivas (Fluxo Negativo):
- **Dados de Teste:**
  - Usuário logado: "Coord. Maria"
  - Ação: Abrir modal de edição da reserva de "João" e salvar sem modificar dados
- **Resultado Esperado:**
  - O sistema identifica a ausência de mudanças e não dispara notificações duplicadas ou desnecessárias aos envolvidos.

**Pós-condições:** O agendamento mantém os dados autorizados e os logs refletem com precisão as tentativas de manipulação.

---

### Caso de Teste 4 - Cancelamento, Liberação de Agenda e Histórico (CT-04)
**Descrição:** Verificar se o processo de cancelamento libera o espaço na grade, mantém histórico em log e impede cancelamentos retroativos de reservas já realizadas.  
**Pré-condições:** 
- Reserva 1: Agendamento ativo em data futura cadastrado pelo usuário.
- Reserva 2: Agendamento já concluído em data passada cadastrado pelo usuário.  
**Passos:**
1. Acessar o painel "Meus Agendamentos".
2. Localizar o agendamento desejado.
3. Acionar a opção "Cancelar Reserva" e confirmar a operação no modal.

#### Cenário 1 - Cancelamento de reserva futura com sucesso (Fluxo Positivo):
- **Dados de Teste:**
  - Reserva Selecionada: Reserva 1 (Futura)
- **Resultado Esperado:**
  - O horário é liberado imediatamente, retornando ao estado "Livre" na grade de disponibilidade (RF-07).
  - O sistema emite notificação confirmando a desmarcação (RF-08).
  - A trilha de auditoria registra a exclusão/cancelamento (*Delete/Update*) (RNF-02).

#### Cenário 2 - Tentativa de cancelamento de reserva passada/retroativa (Fluxo Negativo):
- **Dados de Teste:**
  - Reserva Selecionada: Reserva 2 (Passada/Concluída)
- **Resultado Esperado:**
  - A opção de cancelamento fica desabilitada/oculta na interface.
  - Caso forçada via requisição direta, o sistema rejeita a operação informando que eventos passados não podem ser desmarcados.

**Pós-condições:** A grade de agendamentos reflete a disponibilidade atualizada e o histórico permanece íntegro para auditoria.

---

### Caso de Teste 5 - Desempenho e Filtragem no Motor de Busca (CT-05)
**Descrição:** Avaliar a agilidade na resposta de consultas de disponibilidade e o comportamento do sistema diante de parâmetros de entrada inválidos.  
**Pré-condições:** Base de dados de homologação populada com volume representativo de registros para simular produção. Usuário autenticado.  
**Passos:**
1. Acessar a tela de pesquisa de disponibilidade de laboratórios.
2. Inserir os critérios de busca (data e filtros de unidade).
3. Clicar no botão "Buscar Salas".

#### Cenário 1 - Consulta de disponibilidade com parâmetros válidos (Fluxo Positivo):
- **Dados de Teste:**
  - Data: Data futura válida no calendário
  - Filtro: Unidade vinculada ao perfil do usuário
- **Resultado Esperado:**
  - A listagem de salas disponíveis é retornada e renderizada em menos de 2.000 ms (RNF-01).
  - Apenas as salas das unidades autorizadas ao usuário são exibidas (RNF-03).

#### Cenário 2 - Consulta com data logicamente inválida (Fluxo Negativo):
- **Dados de Teste:**
  - Data: "31/02/2026" (Data inexistente/inválida)
- **Resultado Esperado:**
  - O sistema valida o dado na camada de interface/entrada antes da consulta pesada no banco.
  - A operação responde rapidamente (< 2s) exibindo mensagem de "Data Inválida", sem impactar o desempenho do servidor.

**Pós-condições:** O sistema permanece estável e com tempos de latência dentro do limite acordado.
