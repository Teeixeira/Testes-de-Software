# Planejamento de Qualidade - Sistema de Reserva de Laboratórios

## 1. Propósito
O objetivo deste documento é orientar a execução da validação do Sistema de Reserva de Laboratórios, englobando os requisitos funcionais e não funcionais estabelecidos. O foco principal é assegurar que falhas críticas — como agendamentos simultâneos no mesmo espaço, superlotação de turmas, edições sem a devida autorização e ausência de comunicação aos usuários — sejam evitadas.

## 2. Abrangência da Validação

### 2.1. O que será testado (Incluso)
*   Fluxo de criação e pesquisa de agendamentos laboratoriais.
*   Travas sistêmicas para evitar choque de horários e extrapolação do limite de alunos.
*   Inviabilização de reservas para espaços em período de conserto/manutenção.
*   Conformidade com a janela de funcionamento permitida (das 07:30 às 22:30).
*   Hierarquia de acessos, garantindo que edições de terceiros sejam exclusivas da coordenação.
*   Processo de exclusão de agendamentos, liberação da agenda e gravação na trilha de histórico.
*   Sistema de disparo de alertas (notificações) após mudanças ou cancelamentos.
*   Agilidade no retorno das pesquisas (limite de 2 segundos).
*   Verificação de rastreabilidade (auditoria) das ações dos usuários.
*   Isolamento de acessos de acordo com as unidades permitidas para cada perfil.

### 2.2. O que não será testado (Excluso)
*   Ensaios de estresse ou carga extrema (além do previsto no tempo de resposta).
*   Garantia de entrega nos servidores externos (e-mails/SMS); focar-se-á apenas na emissão sistêmica da notificação.
*   Avaliações aprofundadas de usabilidade, design ou experiência do usuário (UX).

## 3. Componentes Analisados
Os testes incidirão sobre as seguintes áreas na sua versão candidata a lançamento:
*   Gestão de Agendamentos
*   Controle de Ambientes/Salas
*   Administração de Identidades e Acessos
*   Central de Emissão de Alertas
*   Módulo de Rastreabilidade e Logs

## 4. Relação de Requisitos e Ameaças

| Cód | Especificação Reescrita | Classificação | Ameaça Vinculada |
| :--- | :--- | :--- | :--- |
| RF-01 | Efetivar reserva de laboratório vago para turma adequada | Funcional | Média |
| RF-02 | Bloquear agendamento sobreposto no mesmo local/hora | Funcional | Crítica |
| RF-03 | Recusar alocação de turmas que excedam a capacidade física | Funcional | Crítica |
| RF-04 | Vetar uso de laboratórios sinalizados em manutenção | Funcional | Alta |
| RF-05 | Restringir marcações fora da janela de 07h30 até 22h30 | Funcional | Alta |
| RF-06 | Permitir alteração de agendamentos alheios apenas à coordenação | Funcional | Crítica |
| RF-07 | Desmarcar reserva, restaurar disponibilidade e gerar log | Funcional | Média |
| RF-08 | Emitir alertas comunicando edições ou desmarcações | Funcional | Alta |
| RNF-01 | Consultas de disponibilidade devem concluir em até 2s | Desempenho | Média |
| RNF-02 | Toda ação no sistema deve alimentar a trilha de auditoria | Segurança/Log | Alta |
| RNF-03 | Acesso segmentado apenas às unidades sob a alçada do usuário | Segurança | Alta |

## 5. Metodologia de Execução
A validação mesclará táticas de caixa-branca (unitários), testes de integração e testes de ponta a ponta. 
*   **Técnicas Empregadas:** Análise de valor limite e particionamento de equivalência (para as janelas de horários e limites de vagas), tabelas de decisão (cruzando perfis com o que podem editar) e validação de fluxo negativo (forçar falhas nas travas do sistema).

## 6. Infraestrutura e Preparação
*   **Ambiente:** Servidor de homologação isolado, contendo banco de dados populado com massa de testes.
*   **Massa de Dados:** É mandatório possuir perfis de "Docente" e "Coordenador". O banco deve conter salas ativas, salas em manutenção, e turmas com número variado de alunos para forçar os limites.
*   **Ferramental:** Frameworks de automação (ex: JUnit), softwares de monitoramento de latência e acesso direto às tabelas de log.

## 7. Diretrizes e Cronograma
*   **Organização:** Tarefa conduzida em duplas. O prazo final para a modelagem e validação destes cenários é o dia 20 de agosto. Recomenda-se a revisão cruzada dos códigos de teste.
*   **Critérios para Iniciar:** Compreensão total dos requisitos (RF-01 a RF-08, RNF-01 a RNF-03) e ambiente de testes devidamente no ar com a massa injetada.
*   **Critérios para Encerrar:** Aprovação de 100% dos cenários vinculados a riscos Críticos e Altos. Zero inconformidades graves abertas sem justificativa aprovada.
