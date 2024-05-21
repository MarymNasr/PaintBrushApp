package paintbrushapp;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Integer.min;
import static java.lang.Math.abs;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Mario & Marymnasr
 */
public class MyPanel extends JPanel implements Runnable {

    // Paint Attributes
    static int x1, y1, x2, y2;
    static Color color = Color.RED;
    static Stroke stroke;
    protected static Vector<Shape> history;
    protected static int historyCounter;
    protected static int redoCounter;
    static BufferedImage imageBuffer = null;

    // Temp Variables
    Line tempLine;
    Oval tempOval;
    Rectangle tempRect;
    FreeHand tempFShape;
    Eraser tempEraser;

    // Flags
    static boolean isLineClicked;
    static boolean isRectClicked;
    static boolean isOvalClicked;
    static boolean isFreeHandClicked = true;
    static boolean isEraserClicked;
    static boolean isFilledChecked;
    static boolean isChanged;
    static boolean isSaved = false;
    static boolean isFileOpened = false;

    public MyPanel() {
        // Initializing Variables
        x1 = y1 = x2 = y2 = historyCounter = 0;
        history = new Vector();
        stroke = new BasicStroke(2f); // default stroke (Solid)
        int[] keybuffer = new int[2];

        // Setting mouse and Keyboarf Listeners 
        this.setBackground(Color.WHITE);
        this.setFocusable(true);   //make the default panel focus is false 
        MouseLis mLisObj = new MouseLis();   // Mouse Listener -> press & release
        this.addMouseListener(mLisObj);
        MouseMotLis mMLisObj = new MouseMotLis();  // Mouse Listener -> Drag 
        this.addMouseMotionListener(mMLisObj);
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                keybuffer[1] = e.getKeyCode();
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keybuffer[0] = e.getKeyCode();
                if (keybuffer[0] == KeyEvent.VK_CONTROL && keybuffer[1] == KeyEvent.VK_Z) {
                    MyPanel.undoAction();
                } else if (keybuffer[0] == KeyEvent.VK_CONTROL && keybuffer[1] == KeyEvent.VK_S) {
                    MyPanel.isSaved = true;
                } else if (keybuffer[0] == KeyEvent.VK_CONTROL && keybuffer[1] == KeyEvent.VK_O) {
                    MyPanel.openImage();
                }
            }
        });

        // Starting flag checker thread
        new Thread(this).start();
    }

    public class MouseLis implements MouseListener // Inner class to implement MouseListener  (press & release)
    {

        @Override
        public void mousePressed(MouseEvent e) {
            x1 = e.getX();
            y1 = e.getY();
            if (isFreeHandClicked) {
                tempFShape = new FreeHand(color, stroke);
            } else if (isEraserClicked) {
                tempEraser = new Eraser(new Color(255, 255, 255), stroke, Oval.TYPE);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (MyPanel.this.isFocusable()) {
                if (isLineClicked) {
                    x2 = e.getX();
                    y2 = e.getY();
                    history.add(historyCounter, new Line(x1, y1, x2, y2, color, stroke));
                    redoCounter = 0;
                    historyCounter++;
                    x1 = x2 = y1 = y2 = 0;

                } else if (isRectClicked) {
                    history.add(historyCounter, new Rectangle(min(x1, x2), min(y1, y2), abs(x1 - x2), abs(y1 - y2), color, stroke, isFilledChecked));
                    redoCounter = 0;
                    historyCounter++;
                    x1 = x2 = y1 = y2 = 0;

                } else if (isOvalClicked) {
                    history.add(historyCounter, new Oval(min(x1, x2), min(y1, y2), abs(x1 - x2), abs(y1 - y2), color, stroke, isFilledChecked));
                    redoCounter = 0;
                    historyCounter++;
                    x1 = x2 = y1 = y2 = 0;

                } else if (isFreeHandClicked) {
                    history.add(historyCounter, tempFShape);
                    redoCounter = 0;
                    historyCounter++;
                    x1 = x2 = y1 = y2 = 0;

                } else if (isEraserClicked) {
                    history.add(historyCounter, tempEraser);
                    redoCounter = 0;
                    historyCounter++;
                    x1 = x2 = y1 = y2 = 0;

                }

            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }
    }

    public class MouseMotLis implements MouseMotionListener // Inner class to implement MouseMotionListener  (drag)
    {

        @Override
        public void mouseDragged(MouseEvent e) {
            if (MyPanel.this.isFocusable()) {
                if ((isLineClicked || isRectClicked || isOvalClicked)) {
                    x2 = e.getX();
                    y2 = e.getY();
                    updateUI();

                } else if (isFreeHandClicked) {
                    Graphics g = getGraphics();
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setColor(color);
                    g2d.setStroke(stroke);
                    tempLine = new Line(x1, y1, e.getX(), e.getY(), color, stroke);
                    tempFShape.pushLine(tempLine);
                    g.drawLine(tempLine.getxStart(), tempLine.getyStart(), tempLine.getxEnd(), tempLine.getyEnd());
                    x1 = e.getX();
                    y1 = e.getY();

                } else if (isEraserClicked) //Eraser dragging
                {
                    Graphics g = getGraphics();
                    g.setColor(Color.WHITE);
                    tempOval = new Oval(e.getX(), e.getY(), 10, 10, Color.WHITE, stroke, true);
                    tempEraser.pushEraser(tempOval);
                    g.fillOval(tempOval.getX(), tempOval.getY(), tempOval.getWidth(), tempOval.getHeight());

                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }

    //*************** Paint Method  *****************\\
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        // Drawing the image Buffer opened from file
        if (imageBuffer != null) {
            g.drawImage(imageBuffer, 0, 0, this);
        }

        // Drawing the previously drawed objects from the history vector
        if (historyCounter > 0) {
            for (int i = 0; i < historyCounter; i++) {
                switch (history.elementAt(i).getType()) {
                    case Line.TYPE -> {
                        g2d.setStroke(history.elementAt(i).getStroke());
                        g2d.setColor(history.elementAt(i).getColor());
                        tempLine = (Line) history.elementAt(i); // TypeCasting!
                        g2d.drawLine(tempLine.getxStart(), tempLine.getyStart(), tempLine.getxEnd(), tempLine.getyEnd());
                    }

                    case Oval.TYPE -> {
                        g2d.setStroke(history.elementAt(i).getStroke());
                        g2d.setColor(history.elementAt(i).getColor());
                        tempOval = (Oval) history.elementAt(i); // TypeCasting!
                        if (tempOval.isIsFilled()) {
                            g2d.fillOval(tempOval.getX(), tempOval.getY(), tempOval.getWidth(), tempOval.getHeight());
                        } else {
                            g2d.drawOval(tempOval.getX(), tempOval.getY(), tempOval.getWidth(), tempOval.getHeight());
                        }
                    }

                    case Rectangle.TYPE -> {
                        g2d.setStroke(history.elementAt(i).getStroke());
                        g2d.setColor(history.elementAt(i).getColor());
                        tempRect = (Rectangle) history.elementAt(i); // TypeCasting!
                        if (tempRect.isIsFilled()) {
                            g2d.fillRect(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight());
                        } else {
                            g2d.drawRect(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight());
                        }
                    }

                    case FreeHand.TYPE -> {
                        g2d.setStroke(history.elementAt(i).getStroke());
                        g2d.setColor(history.elementAt(i).getColor());
                        tempFShape = (FreeHand) history.elementAt(i); // TypeCasting!

                        for (int j = 0; j < tempFShape.freeshape.size(); j++) {
                            tempLine = tempFShape.freeshape.elementAt(j);
                            g.drawLine(tempLine.getxStart(), tempLine.getyStart(), tempLine.getxEnd(), tempLine.getyEnd());
                        }
                    }
                    case Eraser.TYPE -> {
                        g2d.setStroke(history.elementAt(i).getStroke());
                        g2d.setColor(history.elementAt(i).getColor());
                        tempEraser = (Eraser) history.elementAt(i); // TypeCasting!

                        for (int j = 0; j < tempEraser.ovalEraser.size(); j++) {
                            tempOval = tempEraser.ovalEraser.elementAt(j);
                            g2d.fillOval(tempOval.getX(), tempOval.getY(), tempOval.getWidth(), tempOval.getHeight());
                        }
                    }
                }
            }
        }

        // Draw the object at the moment
        if (this.isFocusable() && (x1 > 0 && y1 > 0)) {
            g2d.setColor(color);
            g2d.setStroke(stroke);
            if (isLineClicked == true) //painting lines
            {
                g2d.drawLine(x1, y1, x2, y2);
            } else if (isOvalClicked) //painting Ovals
            {
                if (isFilledChecked) // if the current oval is checked to be filled, then fill it while drawing
                {
                    g2d.fillOval(min(x1, x2), min(y1, y2), abs(x1 - x2), abs(y1 - y2));
                } else {
                    g2d.drawOval(min(x1, x2), min(y1, y2), abs(x1 - x2), abs(y1 - y2));
                }

            } else if (isRectClicked) //painting Rectangles
            {
                if (isFilledChecked) // if the current rect is checked to be filled, then fill it while drawing
                {
                    g2d.fillRect(min(x1, x2), min(y1, y2), abs(x1 - x2), abs(y1 - y2));  //-> this way of drawing rect is to be able to draw the rec in all directions
                } else {
                    g2d.drawRect(min(x1, x2), min(y1, y2), abs(x1 - x2), abs(y1 - y2));
                }
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            if (isSaved == true) {
                this.saveImage();
                isSaved = false;
            }
            if (isFileOpened == true) {
                MyPanel.openImage();
                isFileOpened = false;
            }
            if (isChanged == true) {
                this.repaint();
                isChanged = false;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(MyPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void saveImage() {
        BufferedImage imageTemp = null;

        try {
            imageTemp = new Robot().createScreenCapture(this.bounds());
            Graphics2D graphics2D = imageTemp.createGraphics();
            this.paint(graphics2D);
        } catch (AWTException e1) {
            e1.printStackTrace();
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify location to save the paint");

        fileChooser.setAcceptAllFileFilterUsed(false);

        FileNameExtensionFilter extFilter = new FileNameExtensionFilter("JPEG file", "jpg", "jpeg");
        fileChooser.addChoosableFileFilter(extFilter);
        int userSelection = fileChooser.showSaveDialog(new JFrame());

        if (userSelection == JFileChooser.APPROVE_OPTION) {

            File fileToSave = null;

            if (!fileChooser.getSelectedFile().getName().endsWith(".jpg")) {
                fileToSave = new File(fileChooser.getSelectedFile() + ".jpg");
            } else {
                fileToSave = fileChooser.getSelectedFile();
            }

            try {
                ImageIO.write(imageTemp, "jpg", fileToSave);
                JOptionPane.showMessageDialog(null, "Image Saved Successfully!", "Saved", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Image Couldn't be Saved!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Image not SAVED! your worthless painting is in DANGER!", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public static void openImage() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose Image...");
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter extFilter = new FileNameExtensionFilter("JPEG file", "jpg", "jpeg");
        fileChooser.addChoosableFileFilter(extFilter);
        int userSelection = fileChooser.showOpenDialog(new JFrame());

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                imageBuffer = ImageIO.read(new File(fileChooser.getSelectedFile().toURI()));
                isChanged = true;
            } catch (IOException ex) {

            }
        }
    }

    public static void undoAction() {
        if (historyCounter > 0) {
            historyCounter--;
            redoCounter++;
            x1 = x2 = y1 = y2 = 0;
            isChanged = true;

        } else {
            JOptionPane.showInternalMessageDialog(null, "More undo pressing will not undo your choices in life!", "مفيش تانى!", JOptionPane.WARNING_MESSAGE);
        }

    }

    public static void redoAction() {

        if (MyPanel.historyCounter > -1 && MyPanel.redoCounter > 0) {
            MyPanel.historyCounter++;
            MyPanel.redoCounter--;
            MyPanel.x1 = MyPanel.x2 = MyPanel.y1 = MyPanel.y2 = 0;
            MyPanel.isChanged = true;
        } else if (MyPanel.historyCounter == 0) {
            JOptionPane.showInternalMessageDialog(null, "What Should i redo if didn't draw any thing!!", "ابدأ بنفسك!", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showInternalMessageDialog(null, "No more drawing are in the stack!", "خلاص عملنا كل حاجة", JOptionPane.WARNING_MESSAGE);
        }
    }

}
