import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Minesweeper extends JFrame {
    private JPanel painelPrincipal;
    private JButton jogoFacilButton;
    private JButton jogoDificilButton;
    private JButton jogoMedioButton;
    private JButton btnSair;
    private JLabel lblNomeFacil;
    private JLabel lblTempoFacil;
    private JLabel lblNomeMedio;
    private JLabel lblTempoMedio;
    private JLabel lblTempoDificil;
    private JLabel lblNomeDificil;

    private TabelaRecordes recordesFacil;
    private TabelaRecordes recordesMedio;
    private TabelaRecordes recordesDificil;

    public Minesweeper(String title) {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(painelPrincipal);
        // Causes this Window to be sized to fit the preferred size and layoutsof its subcomponents.
        pack();
        btnSair.addActionListener(this::btnSairActionPerformed);
        jogoFacilButton.addActionListener(this::btnJogoFacilActionPerformed);
        jogoMedioButton.addActionListener(this::btnJogoMedioActionPerformed);
        jogoDificilButton.addActionListener(this::btnJogoDificilActionPerformed);

        recordesFacil = new TabelaRecordes();
        recordesMedio = new TabelaRecordes();
        recordesDificil = new TabelaRecordes();
        lerRecordesDoDisco();
        lblNomeFacil.setText(recordesFacil.getPlayerName());
        lblTempoFacil.setText(Long.toString(recordesFacil.getPlayerTime() / 1000));
        lblNomeMedio.setText(recordesMedio.getPlayerName());
        lblTempoMedio.setText(Long.toString(recordesMedio.getPlayerTime() / 1000));
        lblNomeDificil.setText(recordesDificil.getPlayerName());
        lblTempoDificil.setText(Long.toString(recordesDificil.getPlayerTime() / 1000));
        recordesFacil.addTabelaRecordesListener(new TabelaRecordesListener() {
            @Override
            public void recordesActualizados(TabelaRecordes recordes) {
                guardarRecordesDisco();
                recordesFacilActualizado(recordes);
            }
        });
        recordesMedio.addTabelaRecordesListener(new TabelaRecordesListener() {
            @Override
            public void recordesActualizados(TabelaRecordes recordes) {
                guardarRecordesDisco();
                recordesMedioActualizado(recordes);
            }
        });
        recordesDificil.addTabelaRecordesListener(new TabelaRecordesListener() {
            @Override
            public void recordesActualizados(TabelaRecordes recordes) {
                guardarRecordesDisco();
                recordesDificilActualizado(recordes);
            }
        });
    }

    private void btnSairActionPerformed(ActionEvent e) {
        System.exit(0);
    }

    private void btnJogoFacilActionPerformed(ActionEvent e) {
        JanelaDeJogo janela = new JanelaDeJogo(new CampoMinado(9, 9, 10), recordesFacil);
        janela.setVisible(true);
    }

    private void btnJogoMedioActionPerformed(ActionEvent e) {
        JanelaDeJogo janela = new JanelaDeJogo(new CampoMinado(16, 16, 40), recordesMedio);
        janela.setVisible(true);
    }

    private void btnJogoDificilActionPerformed(ActionEvent e) {
        JanelaDeJogo janela = new JanelaDeJogo(new CampoMinado(16, 30, 90), recordesDificil);
        janela.setVisible(true);
    }

    private void recordesFacilActualizado(TabelaRecordes recordes) {
        lblNomeFacil.setText(recordes.getPlayerName());
        lblTempoFacil.setText(Long.toString(recordes.getPlayerTime() / 1000));
    }

    private void recordesMedioActualizado(TabelaRecordes recordes) {
        lblNomeMedio.setText(recordes.getPlayerName());
        lblTempoMedio.setText(Long.toString(recordes.getPlayerTime() / 1000));
    }

    private void recordesDificilActualizado(TabelaRecordes recordes) {
        lblNomeDificil.setText(recordes.getPlayerName());
        lblTempoDificil.setText(Long.toString(recordes.getPlayerTime() / 1000));
    }

    private void guardarRecordesDisco() {
        ObjectOutputStream oos = null;
        try {
            File f = new File(System.getProperty("user.home") + File.separator + "minesfinder.recordes");
            oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(recordesFacil);
            oos.writeObject(recordesMedio);
            oos.writeObject(recordesDificil);
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(Minesweeper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void lerRecordesDoDisco() {
        ObjectInputStream ois = null;
        File f = new File(System.getProperty("user.home") + File.separator + "minesfinder.recordes");
        if (f.canRead()) {
            try {
                ois = new ObjectInputStream(new FileInputStream(f));
                recordesFacil = (TabelaRecordes) ois.readObject();
                recordesMedio = (TabelaRecordes) ois.readObject();
                recordesDificil = (TabelaRecordes) ois.readObject();
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(Minesweeper.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Minesweeper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
        new Minesweeper("Minesweeper").setVisible(true);
    }
}