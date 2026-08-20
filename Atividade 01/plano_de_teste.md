# Plano de Teste - Sistema de Reserva de Laboratórios

## 1. Introdução
Este plano de teste descreve a abordagem para testar o Sistema de Reserva de Laboratórios, que tem como objetivo gerenciar a alocação e o agendamento de espaços laboratoriais de forma segura e controlada. O objetivo deste plano é garantir a qualidade e a confiabilidade do software antes do lançamento, prevenindo falhas críticas de operação.

## 2. Objetivos
Os objetivos do teste são:
* Validar o atendimento integral aos requisitos funcionais (RF-01 a RF-08) e não funcionais (RNF-01 a RNF-03) especificados.
* Garantir o funcionamento das travas de integridade (prevenção de conflitos de horário, controle de lotação máxima, restrição de horários de funcionamento e bloqueio de salas em manutenção).
* Assegurar o isolamento de acessos por unidade e a exclusividade de privilégios de edição por terceiros para o perfil de coordenação.
* Validar a rastreabilidade integral de ações via trilha de auditoria e a resposta de consultas em até 2 segundos.
* Identificar e corrigir defeitos antes da liberação da versão de lançamento.

## 3. Escopo
O teste abrangerá as funcionalidades principais do sistema:
* Criação, consulta e cancelamento de agendamentos laboratoriais.
* Validação de restrições físicas e operacionais (limite de capacidade, horários entre 07h30 e 22h30, salas ativas vs. em manutenção).
* Controle de permissões e edição entre perfis (Docente vs. Coordenador).
* Disparo de notificações de alterações e cancelamentos de reservas.
* Auditoria de logs e tempo de resposta nas buscas de disponibilidade.

**Itens Fora de Escopo:**
* Ensaios de estresse ou carga extrema fora da métrica de latência de 2 segundos.
* Garantia de entrega em gateways externos (foco restrito à emissão interna da notificação).
* Avaliações aprofundadas de UX/UI.

## 4. Estratégia de Teste
A estratégia de teste incluirá:
* **Testes Unitários:** Execução de técnicas de caixa-branca para validar regras de negócio isoladas.
* **Testes de Integração:** Validação da comunicação entre módulos de agendamento, controle de ambientes, auditoria e emissão de alertas.
* **Técnicas de Modelagem:** Análise de valor limite, particionamento de equivalência, tabelas de decisão e validação de fluxos negativos (para forçar falhas nas travas).
* **Testes de Sistema:** Verificação de ponta a ponta em ambiente controlado de homologação.

## 5. Casos de Teste
Serão modelados casos de teste cobrindo fluxos positivos e negativos para cada requisito identificado.

**Exemplo de Caso de Teste 1 - Bloqueio de Agendamento Sobreposto (RF-02)**
* **Descrição:** Verificar se o sistema impede a reserva simultânea de um mesmo laboratório no mesmo intervalo de horário.
* **Pré-condições:** Laboratório "Lab 01" já reservado para o intervalo das 08:00 às 10:00.
* **Passos:**
  1. Acessar o módulo de agendamento.
  2. Selecionar o "Lab 01" e o intervalo das 08:30 às 09:30.
  3. Preencher os dados da turma e confirmar o agendamento.
* **Resultado Esperado:** O sistema bloqueia a ação, exibe mensagem de conflito de horário e não registra a reserva.

## 6. Ambiente de Teste
* **Infraestrutura:** Servidor de homologação isolado, espelhando a infraestrutura de produção.
* **Ferramental:** Frameworks de automação (JUnit), ferramentas de medição de latência e clientes de banco de dados para inspeção direta de logs.

## 7. Recursos
* **Equipe de Teste:** Execução conduzida em duplas de analistas/desenvolvedores.
* **Massa de Dados:** Base populada contendo perfis de "Docente" e "Coordenador", salas ativas, salas marcadas em manutenção e turmas configuradas com quantidades variáveis de alunos.

## 8. Cronograma
O ciclo de validação terá como prazo final de entrega a data de 20 de agosto, estruturado nas seguintes etapas:
* **Etapa 1:** Modelagem de cenários, injeção de massa de dados e execução de testes unitários/integração.
* **Etapa 2:** Execução dos testes de sistema/segurança, revisão cruzada de código e homologação final.

## 9. Critérios de Aceitação
O sistema será considerado aceito para lançamento mediante:
* 100% de aprovação nos cenários vinculados a riscos Críticos e Altos (RF-02, RF-03, RF-04, RF-05, RF-06, RF-08, RNF-02, RNF-03).
* Zero defeitos de severidade grave abertos sem justificativa e aprovação técnica.

## 10. Riscos
* Indisponibilidade ou instabilidade da massa de dados no ambiente de homologação.
* Conflitos na sincronização de horários de testes concorrentes em duplas.
* Atrasos na correção de falhas de segurança/permissão de acesso.

## 11. Responsabilidades
* **Equipe de Teste (Duplas):** Modelar, codificar e executar os testes, validar logs de auditoria e emitir apontamentos de não conformidade.
* **Equipe de Desenvolvimento:** Corrigir defeitos identificados e realizar a revisão cruzada do código de teste.

## 12. Comunicação
Registros de status de execução e relatórios de métricas de cobertura serão compartilhados com os envolvidos após cada rodada de teste.

## 13. Aprovação
Este plano será validado antes do início da execução dos testes formais. Qualquer ajuste de escopo exigirá alinhamento prévio.

## 14. Considerações Finais
Este plano consolida as diretrizes operacionais para mitigar os riscos associados ao agendamento de laboratórios, assegurando o cumprimento dos tempos de resposta, a integridade dos dados e a segurança das autorizações.
