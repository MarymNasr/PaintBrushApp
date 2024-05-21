package paintbrushapp;

import java.awt.Color;
import java.awt.Stroke;
import java.util.Vector;

/**
 *
 * @author Mario & Marymnasr
 */
public class FreeHand extends Shape {
    static final int TYPE = 888;
    protected Vector<Line> freeshape;

    
    public FreeHand(Color color,Stroke stroke) {
        super(color, stroke,TYPE);
        freeshape = new Vector();
    }
    
//   // public Vector getFShape(){
//    
//        return freeshape;
//    }
    
    public void pushLine(Line l){
    freeshape.add(l);
    }
    
}
