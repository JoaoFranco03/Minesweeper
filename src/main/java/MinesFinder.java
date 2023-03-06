import javax.swing.*;
import java.awt.event.ActionEvent;

public class MinesFinder extends JFrame {
    private JPanel painelPrincipal;
    private JButton jogoFacilButton;
    private JButton jogoDificilButton;
    private JButton jogoMedioButton;
    private JButton btnSair;
    private JLabel nomefacil;
    private JLabel valorFacil;
    private JLabel nomeMedio;
    private JLabel valorMedio;
    private JLabel valorDificil;
    private JLabel nomeDificil;

    public MinesFinder(String title) {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(painelPrincipal);
        // Causes this Window to be sized to fit the preferred size and layoutsof its subcomponents.
        pack();
        btnSair.addActionListener(this::btnSairActionPerformed);
        jogoFacilButton.addActionListener(this::btnJogoFacilActionPerformed);
        jogoMedioButton.addActionListener(this::btnJogoMedioActionPerformed);
        jogoDificilButton.addActionListener(this::btnJogoDificilActionPerformed);
    }
    private void btnSairActionPerformed(ActionEvent e) {
        System.exit(0);
    }
    private void btnJogoFacilActionPerformed(ActionEvent e) {
        JanelaDeJogo janela = new JanelaDeJogo(new CampoMinado(9, 9, 10));
        janela.setVisible(true);
    }
    private void btnJogoMedioActionPerformed(ActionEvent e) {
        JanelaDeJogo janela = new JanelaDeJogo(new CampoMinado(16, 16, 40));
        janela.setVisible(true);
    }
    private void btnJogoDificilActionPerformed(ActionEvent e) {
        JanelaDeJogo janela = new JanelaDeJogo(new CampoMinado(16, 30, 90));
        janela.setVisible(true);
    }

    public static void main(String[] args) {
        new MinesFinder("Mines Finder").setVisible(true);
    }
}