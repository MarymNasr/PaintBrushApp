package paintbrushapp;

import java.awt.Color;
import java.awt.Stroke;

/**
 *
 * @author Mario & Marymnasr
 */
public class Line extends Shape {
    static final int TYPE = 1;
    protected final int xEnd;
    protected final int yEnd;

    
    public Line(int x, int y, int xEnd, int yEnd, Color color, Stroke stroke) {
        super(x, y, color, stroke,TYPE);
        this.xEnd = xEnd;
        this.yEnd = yEnd;
    }
    
    
    public Line(Line oldie) {
        super(oldie.x,oldie.y,oldie.color,oldie.stroke,oldie.typo);
        this.xEnd = oldie.xEnd;
        this.yEnd = oldie.yEnd;
    }
    

    public int getxStart() {
        return x;
    }

    public int getyStart() {
        return y;
    }

    public int getxEnd() {
        return xEnd;
    }

    public int getyEnd() {
        return yEnd;
    }
}
