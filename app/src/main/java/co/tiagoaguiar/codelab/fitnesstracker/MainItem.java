package co.tiagoaguiar.codelab.fitnesstracker;

public class MainItem {

    private int id;
    private int desenhavelId;
    private int textStringId;
    private int cor;

    public MainItem(int id,
                    int desenhavelId,
                    int textStringId,
                    int cor) {
        this.id = id;
        this.desenhavelId = desenhavelId;
        this.textStringId = textStringId;
        this.cor = cor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDesenhavelId() {
        return desenhavelId;
    }

    public void setDesenhavelId(int desenhavelId) {
        this.desenhavelId = desenhavelId;
    }

    public int getTextStringId() {
        return textStringId;
    }

    public void setTextStringId(int textStringId) {
        this.textStringId = textStringId;
    }

    public int getCor() {
        return cor;
    }

    public void setCor(int cor) {
        this.cor = cor;
    }
}
