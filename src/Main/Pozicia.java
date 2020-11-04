package Main;

public class Pozicia implements Cloneable{
    public int getStlpec() {
        return stlpec;
    }

    public void setStlpec(int stlpec) {
        this.stlpec = stlpec;
    }

    public int getRiadok() {
        return riadok;
    }

    public void setRiadok(int riadok) {
        this.riadok = riadok;
    }

    private int stlpec, riadok;

    @Override
    public Object clone() throws CloneNotSupportedException {
        Pozicia cloned = (Pozicia) super.clone();
        cloned.setRiadok(cloned.getRiadok());
        cloned.setStlpec(cloned.getStlpec());
        return cloned;
    }

}
