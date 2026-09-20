package br.edu.ifpr.boletim;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ParticipacaoTest {
    @Test
    void deveTerZeroDeParticipacao() {
        Participacao participacao = new Participacao();

        boolean entregouAtividade = false;
        boolean participouDaAula = false;

        int pontos = participacao.calcularPontos(entregouAtividade, participouDaAula);

        assertEquals(0, pontos);
    }

    @Test
    void deveTerUmDeParticipacao() {
        Participacao participacao = new Participacao();

        boolean entregouAtividade = false;
        boolean participouDaAula = true;

        int pontos = participacao.calcularPontos(entregouAtividade, participouDaAula);

        assertEquals(1, pontos);
    }

    @Test
    void deveTerDoisDeParticipacao() {
        Participacao participacao = new Participacao();

        boolean entregouAtividade = true;
        boolean participouDaAula = false;

        int pontos = participacao.calcularPontos(entregouAtividade, participouDaAula);

        assertEquals(2, pontos);
    }

    @Test
    void deveTerTresDeParticipacao() {
        Participacao participacao = new Participacao();

        boolean entregouAtividade = true;
        boolean participouDaAula = true;

        int pontos = participacao.calcularPontos(entregouAtividade, participouDaAula);

        assertEquals(3, pontos);
    }
}
