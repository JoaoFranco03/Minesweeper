import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JanelaDeJogo extends JFrame {
    private JPanel painelJogo; // painel do jogo. O nome é definido no mod Design, em "field name"
    private BotaoCampoMinado[][] botoes;
    private CampoMinado campoMinado;
    private TabelaRecordes recordes = new TabelaRecordes();

    public JanelaDeJogo(CampoMinado campoMinado, TabelaRecordes tabela) {
        String osName = System.getProperty("os.name");
        this.campoMinado = campoMinado;
        this.recordes = tabela;
        var nrLinhas = campoMinado.getNrLinhas();
        var nrColunas = campoMinado.getNrColunas();
        this.botoes = new BotaoCampoMinado[nrLinhas][nrColunas];
        painelJogo.setLayout(new GridLayout(nrLinhas, nrColunas));

        FocusListener focusListner = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                var botao = (BotaoCampoMinado) e.getSource();
                botao.setBackground(Color.WHITE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                actualizarEstadoBotoes();
            }
        };

        MouseListener mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON3) {
                    return;
                }
                var botao = (BotaoCampoMinado) e.getSource();
                var x = botao.getColuna();
                var y = botao.getLinha();
                var estadoQuadricula = campoMinado.getEstadoQuadricula(x, y);
                if (estadoQuadricula == CampoMinado.TAPADO) {
                    campoMinado.marcarComoTendoMina(y, x);
                } else if (estadoQuadricula == CampoMinado.MARCADO) {
                    campoMinado.marcarComoSuspeita(y, x);
                } else if (estadoQuadricula == CampoMinado.DUVIDA) {
                    campoMinado.desmarcarQuadricula(y, x);
                }
                actualizarEstadoBotoes();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                var btn = (BotaoCampoMinado) e.getSource();
                btn.requestFocus();
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };

        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                var botao = (BotaoCampoMinado) e.getSource();
                var x = botao.getLinha(); // ou var linha = botao.getLinha();
                var y = botao.getColuna(); // ou var coluna = botao.getColuna();
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> botoes[--x < 0 ? nrLinhas - 1 : x][y].requestFocus();
                    case KeyEvent.VK_DOWN -> botoes[(x + 1) % nrLinhas][y].requestFocus();
                    case KeyEvent.VK_LEFT -> botoes[x][--y < 0 ? nrColunas - 1 : y].requestFocus();
                    case KeyEvent.VK_RIGHT -> botoes[x][(y + 1) % nrColunas].requestFocus();
                    case KeyEvent.VK_M -> {
                        switch (campoMinado.getEstadoQuadricula(x, y)) {
                            case CampoMinado.TAPADO -> campoMinado.marcarComoTendoMina(x, y);
                            case CampoMinado.MARCADO -> campoMinado.marcarComoSuspeita(x, y);
                            case CampoMinado.DUVIDA -> campoMinado.desmarcarQuadricula(x, y);
                        }
                        actualizarEstadoBotoes();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };

        // Criar e adicionar os botões à janela
        for (int linha = 0; linha < nrLinhas; ++linha) {
            for (int coluna = 0; coluna < nrColunas; ++coluna) {
                botoes[linha][coluna] = new BotaoCampoMinado(linha, coluna);
                botoes[linha][coluna].addActionListener(this::acaoBotaoCampoMinado);
                painelJogo.add(botoes[linha][coluna]);
                botoes[linha][coluna].addActionListener(this::btnCampoMinadoActionPerformed);
                if (!osName.startsWith("Mac")) {
                    botoes[linha][coluna].addFocusListener(focusListner);
                }
                botoes[linha][coluna].addMouseListener(mouseListener);
                botoes[linha][coluna].addKeyListener(keyListener);
            }
        }
        setContentPane(painelJogo);
        // Destrói esta janela, removendo-a completamente da memória.
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // Causes this Window to be sized to fit the preferred size and layout of its subcomponents.
        pack();
        setVisible(true);
    }

    private void acaoBotaoCampoMinado(ActionEvent actionEvent) {
        BotaoCampoMinado botao = (BotaoCampoMinado) actionEvent.getSource();
        this.campoMinado.revelarQuadricula(botao.getLinha(), botao.getColuna());
        actualizarEstadoBotoes();
        if (campoMinado.isJogoTerminado()) {
            if (campoMinado.isJogadorDerrotado()) {
                JOptionPane.showMessageDialog(null, "Oh, rebentou uma mina", "Perdeu!", JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "Parabéns. Conseguiu descobrir todas as minas em " + (campoMinado.getDuracaoJogo() / 1000) + " segundos", "Vitória", JOptionPane.INFORMATION_MESSAGE);
                boolean novoRecorde = campoMinado.getDuracaoJogo() < recordes.getPlayerTime();
                if (novoRecorde) {
                    String nome = JOptionPane.showInputDialog("Introduza o seu nome");
                    recordes.setRecorde(nome, campoMinado.getDuracaoJogo());
                }
                setVisible(false);
            }
        }
    }

    private void actualizarEstadoBotoes() {
        for (int x = 0; x < campoMinado.getNrLinhas(); x++) {
            for (int y = 0; y < campoMinado.getNrColunas(); y++) {
                botoes[x][y].setEstado(campoMinado.getEstadoQuadricula(x, y));
            }
        }
    }

    public void btnCampoMinadoActionPerformed(ActionEvent e) {
        var botao = (BotaoCampoMinado) e.getSource();
        int x = botao.getLinha();
        int y = botao.getColuna();
        campoMinado.revelarQuadricula(x, y);
    }
}
