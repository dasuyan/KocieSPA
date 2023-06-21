package pja.kociespa.model;

import jakarta.persistence.*;

@Entity
public class Toy extends PersonalBelonging {
    @Column(nullable = false)
    private String color;
    @Column(nullable = false)
    private boolean isLoud;

    public Toy(String name, String color, boolean isLoud) {
        super(name);
        this.color = color;
        this.isLoud = isLoud;
    }
    protected Toy() {}

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isLoud() {
        return isLoud;
    }

    public void setLoud(boolean loud) {
        isLoud = loud;
    }
}
