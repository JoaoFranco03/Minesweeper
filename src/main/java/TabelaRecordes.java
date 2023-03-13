import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TabelaRecordes implements Serializable {
    private String playerName = "Anónimo";
    private long playerTime = 9999999;
    private transient ArrayList<TabelaRecordesListener> listeners;

    public TabelaRecordes() {
        listeners = new ArrayList<>();
    }

    public String getPlayerName() {
        return playerName;
    }

    public long getPlayerTime() {
        return playerTime;
    }

    public void setRecorde(String playerName, long playerTime) {
        if (playerTime < this.playerTime) {
            this.playerName = playerName;
            this.playerTime = playerTime;
            notifyRecordesActualizados();
        }
    }

    public void addTabelaRecordesListener(TabelaRecordesListener list) {
        if (listeners == null) listeners = new ArrayList<>();
        listeners.add(list);
    }

    public void removeTabelaRecordesListener(TabelaRecordesListener list) {
        if (listeners != null) listeners.remove(list);
    }

    private void notifyRecordesActualizados() {
        if (listeners != null) {
            for (TabelaRecordesListener list : listeners) list.recordesActualizados(this);
        }
    }
}
