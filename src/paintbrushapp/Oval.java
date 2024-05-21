package paintbrushapp;

import java.awt.Color;
import java.awt.Stroke;

/**
 *
 * @author Mario & Marymnasr
 */
public class Oval extends Shape {
    public static final int TYPE = 0;
    protected int width;
    protected int height;
    protected boolean isFilled;
    
    public Oval(int x, int y, int width, int height, Color color, Stroke stroke, boolean isFilled) {
        super(x, y, color, stroke,TYPE);
        this.width = width;
        this.height = height;
        this.isFilled=isFilled;
        
    }

    public boolean isIsFilled() {
        return isFilled;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
