package br.edu.ifpr.boletim;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BoletimTest {

    @Test
    void deveAprovarAlunoComMediaOito() {
        // Preparar: criar o objeto que será testado.
        Boletim boletim = new Boletim();

        // Executar: chamar um único método com uma entrada conhecida.
        String resultado = boletim.verificarSituacao(8);

        // Verificar: comparar o resultado esperado com o resultado obtido.
        assertEquals("APROVADO", resultado);
    }

    @Test
    void deveAprovarAlunoNoLimiteSete() {
        Boletim boletim = new Boletim();
        String resultado = boletim.verificarSituacao(7);
        assertEquals("APROVADO", resultado);
    }

    @Test
    void deveRecuperarNotaAlunoComMediaQuatro() {
        Boletim boletim = new Boletim();

        // Executar: chamar um único método com uma entrada conhecida.
        String resultado = boletim.verificarSituacao(4);

        // Verificar: comparar o resultado esperado com o resultado obtido.
        assertEquals("RECUPERACAO", resultado);
    }

    @Test
    void deveReprovarAlunoComMediaDois() {
        Boletim boletim = new Boletim();

        // Executar: chamar um único método com uma entrada conhecida.
        String resultado = boletim.verificarSituacao(2);

        // Verificar: comparar o resultado esperado com o resultado obtido.
        assertEquals("REPROVADO", resultado);
    }

    @Test
    void deveCalcularMediaIgualCinco() {
        Boletim boletim = new Boletim();

        double resultado = boletim.calcularMedia(5,5);

        assertEquals(5,resultado);

    }

    @Test
    void deveCalcularMediaComParteDecimal() {
        Boletim boletim = new Boletim();

        double resultado = boletim.calcularMedia(7.5, 8.0);

        assertEquals(7.75, resultado, 0.0001);
    }

    @Test
    void deveContarDoisAprovados() {
        Boletim boletim = new Boletim();

        double[] medias = new double[] {1, 4, 7, 6.99, 8};

        int aprovados = boletim.contarAprovados(medias);

        assertEquals(2, aprovados);
    }

    @Test
    void deveContarZeroAprovados() {
        Boletim boletim = new Boletim();

        double[] medias = new double[] {1, 4, 6, 6.99, 0};

        int aprovados = boletim.contarAprovados(medias);

        assertEquals(0, aprovados);
    }

    @Test
    void deveContarZeroAprovadosComArrayVazio() {
        Boletim boletim = new Boletim();

        double[] medias = new double[] {};

        int aprovados = boletim.contarAprovados(medias);

        assertEquals(0, aprovados);
    }

    @Test
    void deveContarAprovadosComUmElementoNoArray() {
        Boletim boletim = new Boletim();

        double[] medias = new double[] {8.5};

        int aprovados = boletim.contarAprovados(medias);

        assertEquals(1, aprovados);
    }
}
