package paintbrushapp;

import java.awt.Color;
import java.awt.Stroke;

/**
 *
 * @author Mario
 */
public class Shape {

    public final int typo;
    protected int x;
    protected int y;
    protected Color color;
    protected Stroke stroke;

    protected Shape(Color color, Stroke stroke, int typo) {
        
        this.x = 0;
        this.y = 0;
        this.color = color;
        this.stroke = stroke;
        this.typo = typo;
    }

    public Shape(int x, int y, Color color, Stroke stroke, int typo) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.stroke = stroke;
        this.typo = typo;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    public Stroke getStroke() {
        return stroke;
    }

    public int getType() {
        return typo;
    }
}
