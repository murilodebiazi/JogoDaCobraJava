import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class cobraGame extends JPanel
        implements ActionListener, KeyListener {

    // classe Tile (bloco)
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // configuração da janela
    int larguraJanela;
    int alturaJanela;
    int tamanhoTiles = 25;

    // cobra
    Tile cobraCabeca;
    ArrayList<Tile> cobraCorpo;

    // comida
    Tile comida;
    Random random;

    // lógica do jogo
    Timer loopJogo;
    int velocidadeX;
    int velocidadeY;
    boolean gameOver = false;

    // Construtor
    cobraGame(int larguraJanela, int alturaJanela) {

        // JFrame
        this.larguraJanela = larguraJanela;
        this.alturaJanela = alturaJanela;
        setPreferredSize(new Dimension(this.larguraJanela, this.alturaJanela));
        setBackground(Color.black);

        // Escutar o Teclado
        addKeyListener(this);
        setFocusable(true);

        // cobra
        cobraCabeca = new Tile(5, 5);
        cobraCorpo = new ArrayList<Tile>();

        // comida
        comida = new Tile(10, 10);

        random = new Random();
        colocarcomida();

        // Velocidade da Cobra
        velocidadeX = -1;
        velocidadeY = 0;

        // Processamento do Jogo
        loopJogo = new Timer(100, this);
        loopJogo.start();
    }

    // Componente de pintura
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        desenhar(g);
    }

    // Desenho
    public void desenhar(Graphics g) {

        // Grid
        // for (int i = 0; i < larguraJanela / tamanhoTiles; i++) {
        // // (x1, y1, x2, y2)
        // // g.desenharLine(i * tamanhoTiles, 0, i * tamanhoTiles, alturaJanela);
        // // g.desenharLine(0, i * tamanhoTiles, larguraJanela, i * tamanhoTiles);
        // }

        // comida
        g.setColor(Color.gray);
        g.fill3DRect(comida.x * tamanhoTiles, comida.y * tamanhoTiles, tamanhoTiles, tamanhoTiles, true);

        // Cabeça da Cobra
        g.setColor(Color.green);
        g.fill3DRect(cobraCabeca.x * tamanhoTiles, cobraCabeca.y * tamanhoTiles, tamanhoTiles, tamanhoTiles, true);

        // Corpo da Cobra
        for (int i = 0; i < cobraCorpo.size(); i++) {
            Tile cobraParte = cobraCorpo.get(i);
            g.fill3DRect(cobraParte.x * tamanhoTiles, cobraParte.y * tamanhoTiles, tamanhoTiles, tamanhoTiles, true);
        }

        // Pontuação
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(cobraCorpo.size()), tamanhoTiles - 16, tamanhoTiles);
        } else {
            g.drawString("Pontuação: " + String.valueOf(cobraCorpo.size()), tamanhoTiles - 16, tamanhoTiles);
        }
    }

    public void colocarcomida() {
        comida.x = random.nextInt(larguraJanela / tamanhoTiles); // 600/25 = 24
        comida.y = random.nextInt(alturaJanela / tamanhoTiles);
    }

    public boolean colisao(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void movimento() {

        // comer comida
        if (colisao(cobraCabeca, comida)) {
            cobraCorpo.add(new Tile(comida.x, comida.y));
            colocarcomida();
        }

        // Corpo da Cobra
        for (int i = cobraCorpo.size() - 1; i >= 0; i--) {
            Tile cobraParte = cobraCorpo.get(i);
            if (i == 0) {
                cobraParte.x = cobraCabeca.x;
                cobraParte.y = cobraCabeca.y;
            } else {
                Tile prevcobraParte = cobraCorpo.get(i - 1);
                cobraParte.x = prevcobraParte.x;
                cobraParte.y = prevcobraParte.y;
            }
        }

        // Cabeça da Cobra
        cobraCabeca.x += velocidadeX;
        cobraCabeca.y += velocidadeY;

        // Condições de game over

        for (int i = 0; i < cobraCorpo.size(); i++) {
            Tile cobraParte = cobraCorpo.get(i);

            // colisão com cabeça da cobra
            if (colisao(cobraCabeca, cobraParte)) {
                gameOver = true;
            }
        }

        // colisão com o final da tela
        if (cobraCabeca.x * tamanhoTiles < 0 || cobraCabeca.x * tamanhoTiles > larguraJanela ||
                cobraCabeca.y * tamanhoTiles < 0 || cobraCabeca.y * tamanhoTiles > alturaJanela) {
            gameOver = true;
        }
    }

    // Carregamento do jogo
    @Override
    public void actionPerformed(ActionEvent e) {
        movimento();
        repaint();
        if (gameOver == true) {
            loopJogo.stop();
        }
    }

    // Pegar o teclado
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocidadeY != 1) {
            velocidadeX = 0;
            velocidadeY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocidadeY != -1) {
            velocidadeX = 0;
            velocidadeY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocidadeX != 1) {
            velocidadeX = -1;
            velocidadeY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocidadeX != -1) {
            velocidadeX = 1;
            velocidadeY = 0;
        }
    }

    // Não precisa (interface)
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
