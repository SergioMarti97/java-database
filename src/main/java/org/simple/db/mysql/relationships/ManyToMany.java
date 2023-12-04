package org.simple.db.mysql.relationships;

public class ManyToMany {

    protected int idLeft;

    protected int idRight;

    public ManyToMany(int idLeft, int idRight) {
        this.idLeft = idLeft;
        this.idRight = idRight;
    }

    public int getIdLeft() {
        return idLeft;
    }

    public void setIdLeft(int idLeft) {
        this.idLeft = idLeft;
    }

    public int getIdRight() {
        return idRight;
    }

    public void setIdRight(int idRight) {
        this.idRight = idRight;
    }

}
