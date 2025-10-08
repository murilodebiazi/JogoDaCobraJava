import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int larguraJanela = 600;
        int alturaJanela = larguraJanela;

        JFrame janela = new JFrame("Snake");
        janela.setVisible(true);
        janela.setSize(larguraJanela, alturaJanela);
        janela.setLocationRelativeTo(null);
        janela.setResizable(false);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cobraGame snakeGame = new cobraGame(larguraJanela, alturaJanela);
        janela.add(snakeGame);
        janela.pack();
        snakeGame.requestFocus();
    }
}
