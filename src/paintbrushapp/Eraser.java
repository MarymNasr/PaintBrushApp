package paintbrushapp;

import java.awt.Color;
import java.awt.Stroke;
import java.util.Vector;

/**
 *
 * @author Mario & Marymnasr
 */
public class Eraser extends Shape {
    static final int TYPE = 1000;
    protected Vector<Line> lineEraser; 
    protected Vector<Oval> ovalEraser;
    
    public Eraser(Color color,Stroke stroke,int t) {
        super(color, stroke,TYPE);
        switch (t) {
            case Line.TYPE -> lineEraser = new Vector();
            case Oval.TYPE -> ovalEraser = new Vector();
            default -> System.out.println("WRONG ERASER TYPE!!!!");
        }
    }
   
    public void pushEraser(Line l){
    lineEraser.add(l);
    }
    
    public void pushEraser(Oval o){
    ovalEraser.add(o);
    }
    
}
