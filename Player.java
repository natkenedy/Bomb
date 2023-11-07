
package finalbomber;

// classe que representa o player
public class Player {
    private int x;
    private int y;
    private int vida;
    private int score;

    public Player(int x, int y, int vida, int score) {
        this.x = x;
        this.y = y;
        this.vida = vida;
        this.score = score;
    }

    // Referencia para a instancia do jogo (Bomber)
    private Bomber gameInstance;

    // Getters para as coordenadas do jogador
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Getter para a vida do jogador
    public int getVida() {
        return vida;
    }

    // Getter para a pontuacao do jogador
    public int getScore() {
        return score;
    }
    
    // Método para mover o jogador para novas coordenadas
    public void mover(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    // Método para diminuir a vida do jogador
    public void perderVida(int quantidade) {
        this.vida -= quantidade;
        
        // Verifica se a vida do jogador chegou a zero ou menos
        if (this.vida <= 0) {
            this.vida = 0;
            
            // Para o jogo (define pararJogo como falso)
            this.gameInstance.pararJogo();
        }
    }
    
    // Método para adicionar pontuação ao jogador
    public void addScore(int quantidade) {
        this.score += quantidade;
    }

    // Método para adicionar vida ao jogador
    public void addVida(int quantidade) {
        // Incrementa a vida, mas limita ao valor maximo de 20
        this.vida = Math.min(this.vida + vida, 20);
    }
}
