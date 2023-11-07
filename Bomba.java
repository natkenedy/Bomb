
package finalbomber;

import java.util.Timer;
import java.util.TimerTask;


// classe que representa a bomba
public class Bomba {
    private int x;
    private int y;
    private int dano;
    private int aumentaScore = 10;


    public Bomba(int x, int y, int dano, Bomber aThis) {
        this.x = x;
        this.y = y;
        this.dano = dano;
    }

    public void colocarBomba(int[][] tabuleiro, Player jogador1, Player jogador2, long atraso) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    // Atraso de 1 segundo para mostrar a bomba e a área de explosao
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int acertos = explodir(tabuleiro, jogador1, jogador2);
                // Retorna a quantidade de acertos
            }
        }, atraso);
    }

    private int explodir(int[][] tabuleiro, Player jogador1, Player jogador2) {
        int acertos = 0; // Inicializa contador de acertos

        // Blocos acima e abaixo
        for (int i = x - 2; i <= x + 2; i++) {
            if (i >= 0 && i < tabuleiro.length) {
                if (tabuleiro[i][y] == Bomber.PAREDE_INDESTRUTIVEL) {
                    break; // Interrompe a explosao se encontrar uma parede indestrutivel
                }
                if (tabuleiro[i][y] == Bomber.PAREDE_DESTRUTIVEL) {
                    tabuleiro[i][y] = 0;
                }
                if (verificarAcertoJogador(jogador1, i, y)) {
                    acertos++; // Incrementa o contador de acertos se o jogador1 for atingido
                    jogador2.addScore(aumentaScore); // Incrementa a pontuacao do jogador2
                }
                if (verificarAcertoJogador(jogador2, i, y)) {
                    acertos++; // Incrementa o contador de acertos se o jogador2 for atingido
                    jogador1.addScore(aumentaScore); // Incrementa a pontuacao do jogador1
                }
            } else {
                break; // Interrompe a explosao se sair dos limites verticalmente
            }
        }

        // Blocos à esquerda e a direita
        for (int j = y - 2; j <= y + 2; j++) {
            if (j >= 0 && j < tabuleiro[0].length) {
                if (tabuleiro[x][j] == Bomber.PAREDE_INDESTRUTIVEL) {
                    break; // Interrompe a explosao se encontrar uma parede indestrutivel
                }
                if (tabuleiro[x][j] == Bomber.PAREDE_DESTRUTIVEL) {
                    tabuleiro[x][j] = 0;
                }
                if (verificarAcertoJogador(jogador1, x, j)) {
                    acertos++; // Incrementa o contador de acertos se o jogador1 for atingido
                    jogador2.addScore(aumentaScore); // Incrementa a pontuacao do jogador2
                }
                if (verificarAcertoJogador(jogador2, x, j)) {
                    acertos++; // Incrementa o contador de acertos se o jogador2 for atingido
                    jogador1.addScore(aumentaScore); // Incrementa a pontuacao do jogador1
                }
            } else {
                break; // Interrompe a explosao se sair dos limites horizontalmente
            }
        }

        tabuleiro[x][y] = 0;
        return acertos; // Retorna o numero de acertos
    }

    private boolean verificarAcertoJogador(Player jogador, int x, int y) {
        if (jogador.getX() == x && jogador.getY() == y) {
            jogador.perderVida(dano);
            if (jogador.getVida() <= 0) {
                Bomber.gameRunning = false;
            }
            return true; // Retorna true se o jogador foi atingido
        }
        return false; // Retorna false se o jogador nao foi atingido
    }
}
