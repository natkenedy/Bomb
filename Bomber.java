
    package finalbomber;

    /**
     * Classe que representa o jogo Bomber.
     */
    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.awt.event.KeyEvent;
    import java.util.ArrayList;
    import java.util.List;

    public class Bomber extends JPanel implements ActionListener {
        private static final int TAMANHO_CELULA = 30;
        private static final int LARGURA_TABULEIRO = 15;
        private static final int ALTURA_TABULEIRO = 18;
        private static final int QUANTIDADE_PAREDES = 120;
        private static final int POSICAO_INICIAL_JOGADOR1_X = 1;
        private static final int POSICAO_INICIAL_JOGADOR1_Y = 1;
        private static final int POSICAO_INICIAL_JOGADOR2_X = LARGURA_TABULEIRO - 2;
        private static final int POSICAO_INICIAL_JOGADOR2_Y = ALTURA_TABULEIRO - 2;
        static final int PAREDE_DESTRUTIVEL = 1;
        static final int PAREDE_INDESTRUTIVEL = 2;
        private static final int BOOSTER_PONTUACAO = 6;
        private static final int BOOSTER_VIDA = 7;
        private static final int VIDAS_INICIAIS_JOGADOR = 20;
        private static final int COR_JOGADOR1 = Color.RED.getRGB();
        private static final int COR_JOGADOR2 = Color.BLUE.getRGB();
        private static final int COR_PAREDE = Color.GRAY.getRGB();
        private static final int COR_PAREDE_DESTRUTIVEL = Color.LIGHT_GRAY.getRGB();
        private static final int COR_BOMBA = Color.BLACK.getRGB();
        private static final int COR_BOOSTER_VIDA = Color.GREEN.getRGB();
        private static final int COR_BOOSTER_PONTUACAO = Color.YELLOW.getRGB();
        private static final int DANO_BOMBA = 5; 
        public static final int JOGADOR1 = 3;
        public static final int JOGADOR2 = 4;
        public static final int BOMBA = 5;
        private final int score_parede = 2;
        private int scoreBooster = 10;
        private int[][] bombaCoordenadas = new int[LARGURA_TABULEIRO][ALTURA_TABULEIRO];
        public static Player jogador1; 
        public static Player jogador2;
        private List<Bomba> bombas = new ArrayList<>();
        private int[][] tabuleiro;
        private int ultimoMovimentoJogador1 = KeyEvent.VK_DOWN; // Inicializa com um valor padrão
        private int ultimoMovimentoJogador2 = KeyEvent.VK_W; // Inicializa com um valor padrão

        static boolean gameRunning;

        private JButton botaoIniciar;

        /**
         * Construtor da classe Bomber.
         */
        public Bomber() {
            setLayout(null);

            botaoIniciar = new JButton("Iniciar");
            botaoIniciar.setBounds(getWidth() + 290, ALTURA_TABULEIRO * TAMANHO_CELULA + 30, 80, 30);
            botaoIniciar.addActionListener(this);
            add(botaoIniciar);

            setFocusable(true);
            addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    if (gameRunning) {
                        processarEntradaJogador(evt);
                    }
                }
            });

            inicializarJogo();
            this.bombas = bombas;
        }

        /**
         * Inicializa o jogo.
         */
        private void inicializarJogo() {
            tabuleiro = new int[LARGURA_TABULEIRO][ALTURA_TABULEIRO];
            jogador1 = new Player(POSICAO_INICIAL_JOGADOR1_X, POSICAO_INICIAL_JOGADOR1_Y, VIDAS_INICIAIS_JOGADOR, 0);
            jogador2 = new Player(POSICAO_INICIAL_JOGADOR2_X, POSICAO_INICIAL_JOGADOR2_Y, VIDAS_INICIAIS_JOGADOR, 0);
            gameRunning = false;
            bombas = new ArrayList<>();
            colocarParedes();
            colocarBoosters();
        }

        /**
         * Para o jogo.
         */
        public static void pararJogo() {
            gameRunning = false;
        }

        /**
         * Processa a entrada do jogador.
         */
        private void processarEntradaJogador(KeyEvent evt) {
            int keyCode = evt.getKeyCode();
            int jogador1X = jogador1.getX();
            int jogador1Y = jogador1.getY();
            int jogador2X = jogador2.getX();
            int jogador2Y = jogador2.getY();

            if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN ||
                evt.getKeyCode() == KeyEvent.VK_LEFT || evt.getKeyCode() == KeyEvent.VK_RIGHT) {
                // Atualiza o último movimento do jogador1
                ultimoMovimentoJogador1 = keyCode;
            }
            
            if (evt.getKeyCode() == KeyEvent.VK_W || evt.getKeyCode() == KeyEvent.VK_S ||
                evt.getKeyCode() == KeyEvent.VK_A || evt.getKeyCode() == KeyEvent.VK_D) {
                // Atualiza o último movimento do jogador2
                ultimoMovimentoJogador2 = keyCode;
            }

            if (keyCode == KeyEvent.VK_UP) {
                moverJogador(jogador1, jogador1X, jogador1Y - 1);
            } else if (keyCode == KeyEvent.VK_DOWN) {
                moverJogador(jogador1, jogador1X, jogador1Y + 1);
            } else if (keyCode == KeyEvent.VK_LEFT) {
                moverJogador(jogador1, jogador1X - 1, jogador1Y);
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                moverJogador(jogador1, jogador1X + 1, jogador1Y);
            } else if (keyCode == KeyEvent.VK_W) {
                moverJogador(jogador2, jogador2X, jogador2Y - 1);
            } else if (keyCode == KeyEvent.VK_S) {
                moverJogador(jogador2, jogador2X, jogador2Y + 1);
            } else if (keyCode == KeyEvent.VK_A) {
                moverJogador(jogador2, jogador2X - 1, jogador2Y);
            } else if (keyCode == KeyEvent.VK_D) {
                moverJogador(jogador2, jogador2X + 1, jogador2Y);
            } else if (keyCode == KeyEvent.VK_SPACE) {
                colocarBomba(jogador1, DANO_BOMBA);
            } else if (keyCode == KeyEvent.VK_ENTER) {
                colocarBomba(jogador2, DANO_BOMBA);
            }
        }

        /**
         * Coloca uma bomba no tabuleiro.
         */
        private void colocarBomba(Player jogador, int dano) {
        int x = jogador.getX();
        int y = jogador.getY();

        // Verifica se as coordenadas (x, y) são válidas e se a célula é vazia
        if (ehValido(x, y) && tabuleiro[x][y] == 0) {
            Bomba bomba = new Bomba(x, y, dano, this);
            bombas.add(bomba);
            tabuleiro[x][y] = BOMBA;
            bombaCoordenadas[x][y] = 1;

            repaint();
            bomba.colocarBomba(tabuleiro, jogador1, jogador2, 3000);
        }
    }

            
        /**
        *Verifica se as coordenadas (x, y) são válidas dentro dos limites do tabuleiro.
        */
        private boolean ehValido(int x, int y) {
            return x >= 0 && x < LARGURA_TABULEIRO && y >= 0 && y < ALTURA_TABULEIRO;
        }


        /**
         * Move um jogador para uma nova posicao.
         */
        private void moverJogador(Player jogador, int novoX, int novoY) {
            if (movimentoValido(novoX, novoY)) {
                int xAtual = jogador.getX();
                int yAtual = jogador.getY();
                if (bombaCoordenadas[xAtual][yAtual] == 1) {
                    tabuleiro[xAtual][yAtual] = PAREDE_DESTRUTIVEL; // substitui a célula por uma parede
                    bombaCoordenadas[xAtual][yAtual] = 0; // limpa a informação de que uma bomba foi colocada aqui
                }
                int valorCelula = tabuleiro[novoX][novoY];
                if (valorCelula == BOOSTER_PONTUACAO) {
                    jogador.addScore(scoreBooster);
                } else if (valorCelula == BOOSTER_VIDA) {
                    jogador.addVida(10);
                }
                tabuleiro[xAtual][yAtual] = 0;
                jogador.mover(novoX, novoY);
                tabuleiro[novoX][novoY] = (jogador == jogador1) ? JOGADOR1 : JOGADOR2;
                repaint();
            }
        }

        /**
         * Verifica se um movimento eh valido.
         */
        private boolean movimentoValido(int x, int y) {
            if (x >= 0 && x < LARGURA_TABULEIRO && y >= 0 && y < ALTURA_TABULEIRO) {
                int valorCelula = tabuleiro[x][y];
                return valorCelula == 0 || valorCelula == BOOSTER_PONTUACAO || valorCelula == BOOSTER_VIDA;
            }
            return false;
        }

        /**
         * Coloca as paredes no tabuleiro.
         */
        private void colocarParedes() {
            for (int i = 0; i < QUANTIDADE_PAREDES; i++) {
                int x = (int) (Math.random() * LARGURA_TABULEIRO);
                int y = (int) (Math.random() * ALTURA_TABULEIRO);
                int tipoParede = (int) (Math.random() * 2) + 1;

                if (tabuleiro[x][y] == 0) {
                    if (tipoParede == PAREDE_DESTRUTIVEL) {
                        tabuleiro[x][y] = PAREDE_DESTRUTIVEL;
                    } else if (tipoParede == PAREDE_INDESTRUTIVEL) {
                        tabuleiro[x][y] = PAREDE_INDESTRUTIVEL;
                    }
                } else {
                    i--; // Tentar novamente colocar a parede
                }
            }
        }

        /**
         * Coloca os boosters no tabuleiro.
         */
        private void colocarBoosters() {
            int boostersPontuacao = 3;
            int boostersVida = 2;

            while (boostersPontuacao > 0 || boostersVida > 0) {
                int x = (int) (Math.random() * LARGURA_TABULEIRO);
                int y = (int) (Math.random() * ALTURA_TABULEIRO);

                if (tabuleiro[x][y] == 0) {
                    if (boostersPontuacao > 0) {
                        tabuleiro[x][y] = BOOSTER_PONTUACAO;
                        boostersPontuacao--;
                    } else if (boostersVida > 0) {
                        tabuleiro[x][y] = BOOSTER_VIDA;
                        boostersVida--;
                    }
                }
            }
        }

        /**
         * Desenha o tabuleiro e os elementos do jogo.
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Color corCelula = Color.white;

            for (int x = 0; x < LARGURA_TABULEIRO; x++) {
                for (int y = 0; y < ALTURA_TABULEIRO; y++) {
                    int valorCelula = tabuleiro[x][y];

                    switch (valorCelula) {
                        case PAREDE_DESTRUTIVEL:
                            corCelula = new Color(COR_PAREDE_DESTRUTIVEL);
                            break;
                        case PAREDE_INDESTRUTIVEL:
                            corCelula = new Color(COR_PAREDE);
                            break;
                        case JOGADOR1:
                            corCelula = new Color(COR_JOGADOR1);
                            break;
                        case JOGADOR2:
                            corCelula = new Color(COR_JOGADOR2);
                            break;
                        case BOMBA:
                            corCelula = new Color(COR_BOMBA);
                            break;
                        case BOOSTER_PONTUACAO:
                            corCelula = new Color(COR_BOOSTER_PONTUACAO);
                            break;
                        case BOOSTER_VIDA:
                            corCelula = new Color(COR_BOOSTER_VIDA);
                            break;
                        default:
                            corCelula = Color.white;
                    }

                    g.setColor(corCelula);
                    g.fillRect(x * TAMANHO_CELULA, y * TAMANHO_CELULA, TAMANHO_CELULA, TAMANHO_CELULA);
                }
            }

            // Desenhar jogador1
            g.setColor(new Color(COR_JOGADOR1));
            g.fillRect(jogador1.getX() * TAMANHO_CELULA, jogador1.getY() * TAMANHO_CELULA, TAMANHO_CELULA, TAMANHO_CELULA);

            // Desenhar jogador2
            g.setColor(new Color(COR_JOGADOR2));
            g.fillRect(jogador2.getX() * TAMANHO_CELULA, jogador2.getY() * TAMANHO_CELULA, TAMANHO_CELULA, TAMANHO_CELULA);

            // Desenhar bombas
            g.setColor(new Color(COR_BOMBA));
            for (int u = 0; u < LARGURA_TABULEIRO; u++) {
                for (int p = 0; p < ALTURA_TABULEIRO; p++) {
                    if (tabuleiro[u][p] == BOMBA) {
                        g.fillRect(u * TAMANHO_CELULA, p * TAMANHO_CELULA, TAMANHO_CELULA, TAMANHO_CELULA);
                    }
                }
            }

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Pontos Red: " + jogador1.getScore(), 10, ALTURA_TABULEIRO * TAMANHO_CELULA + 40);
            g.drawString("Pontos Blue: " + jogador2.getScore(), 10, ALTURA_TABULEIRO * TAMANHO_CELULA + 60);
            g.drawString("Vida Red: " + jogador1.getVida(), 150, ALTURA_TABULEIRO * TAMANHO_CELULA + 40);
            g.drawString("Vida Azul: " + jogador2.getVida(), 150, ALTURA_TABULEIRO * TAMANHO_CELULA + 60);

            // Verifica se um jogador ganhou o jogo
            if (jogador1.getVida() <= 0 || jogador2.getVida() <= 0 || jogador1.getScore() >= 100 || jogador2.getScore() >= 100) {
                gameRunning = false;

                String winner = "";
                if (jogador1.getVida() <= 0) {
                    winner = "Jogador Blue";
                } else if (jogador2.getVida() <= 0) {
                    winner = "Jogador Red";
                } else if (jogador1.getScore() >= 100) {
                    winner = "Jogador Red";
                } else if (jogador2.getScore() >= 100) {
                    winner = "Jogador Blue";
                }
                g.drawString("O " + winner + " ganhou!", 300, ALTURA_TABULEIRO * TAMANHO_CELULA + 10);
            }
        }

        /**
         * Trata os eventos do jogo.
         */
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == botaoIniciar) {
                botaoIniciar.setEnabled(false);
                botaoIniciar.setText("Rodando...");
                gameRunning = true;
                requestFocus();

                Timer timer = new Timer(10, new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        repaint();
                    }
                });
                timer.start();
            }
        }

        /**
         * Método principal que inicia o jogo.
         */
        public static void main(String[] args) {
            JFrame frame = new JFrame("Jogo Bomberman");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(LARGURA_TABULEIRO * TAMANHO_CELULA + 40, ALTURA_TABULEIRO * TAMANHO_CELULA + 100);
            frame.getContentPane().add(new Bomber());
            frame.setVisible(true);
        }
}
    
