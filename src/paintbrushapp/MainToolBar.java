package paintbrushapp;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;

/**
 *
 * @author Mario & Marymnasr
 */
public class MainToolBar extends JToolBar {

    static JButton colorButton;

    public MainToolBar() {
        setRollover(true);

        colorButton = new JButton("     ");     //Color button
        ColorButtonAction redObj = new ColorButtonAction();
        colorButton.addActionListener(redObj);
        colorButton.setBackground(MyPanel.color);
        this.add(colorButton);

        this.add(new Separator(new Dimension(20, 30)));

        JButton lineBtn = new JButton("Line");  // Draw line button
        LineButtonAction lineObj = new LineButtonAction();
        lineBtn.addActionListener(lineObj);
        this.add(lineBtn);

        JButton rectBtn = new JButton("Rectangle");   //Draw rectangle button
        RectButtonAction rectObj = new RectButtonAction();
        rectBtn.addActionListener(rectObj);
        this.add(rectBtn);

        JButton ovaltBtn = new JButton("Oval"); //Draw oval button
        OvalButtonAction ovalObj = new OvalButtonAction();
        ovaltBtn.addActionListener(ovalObj);
        this.add(ovaltBtn);

        JCheckBox dottedCB = new JCheckBox("Dotted");    //Dotted Checkbox
        DottedItemList dottedObj = new DottedItemList();
        dottedCB.addItemListener(dottedObj);
        this.add(dottedCB);

        JCheckBox filledCB = new JCheckBox("Filled");    //Filled Checkbox
        FilledItemList filledObj = new FilledItemList();
        filledCB.addItemListener(filledObj);
        this.add(filledCB);

        JButton freeBtn = new JButton("Free Hand");  // Free hand button
        FreeHandAction fbreeObj = new FreeHandAction();
        freeBtn.addActionListener(fbreeObj);
        this.add(freeBtn);

        JButton eraserBtn = new JButton("Eraser");    //Eraser button
        EraserAction eraserObj = new EraserAction();
        eraserBtn.addActionListener(eraserObj);
        this.add(eraserBtn);

        JButton clearBtn = new JButton(" Clear All ");  // Clear All button
        ClearAllAction clearObj = new ClearAllAction();
        clearBtn.addActionListener(clearObj);
        this.add(clearBtn);

        JButton undoBtn = new JButton(" Undo ");  // Undo button
        UndoAction undoObj = new UndoAction();
        undoBtn.addActionListener(undoObj);
        this.add(undoBtn);

        JButton redoBtn = new JButton(" Redo ");  // Undo button
        RedoAction redoObj = new RedoAction();
        redoBtn.addActionListener(redoObj);
        this.add(redoBtn);

        JButton saveBtn = new JButton(" Save ");  // Save button
        SaveAsJPGAction saveObj = new SaveAsJPGAction();
        saveBtn.addActionListener(saveObj);
        this.add(saveBtn);

        JButton openBtn = new JButton(" Open ");  // Open button
        OpenJPGAction openObj = new OpenJPGAction();
        openBtn.addActionListener(openObj);
        this.add(openBtn);

    }

    //*************** SHAPES BUTTONS  *****************\\
    public class LineButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            MyPanel.isLineClicked = true;
            MyPanel.isRectClicked = false;
            MyPanel.isOvalClicked = false;
            MyPanel.isFreeHandClicked = false;
            MyPanel.isEraserClicked = false;

        }
    }

    public class RectButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            MyPanel.isRectClicked = true;
            MyPanel.isLineClicked = false;
            MyPanel.isOvalClicked = false;
            MyPanel.isFreeHandClicked = false;
            MyPanel.isEraserClicked = false;
        }
    }

    public class OvalButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            MyPanel.isOvalClicked = true;
            MyPanel.isLineClicked = false;
            MyPanel.isRectClicked = false;
            MyPanel.isFreeHandClicked = false;
            MyPanel.isEraserClicked = false;
        }
    }
    //*************** Other BUTTONS  *****************\\

    public class FreeHandAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            MyPanel.isFreeHandClicked = true;
            MyPanel.isLineClicked = MyPanel.isRectClicked = MyPanel.isOvalClicked = false;

        }
    }

    public class EraserAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            MyPanel.isEraserClicked = true;
            MyPanel.isRectClicked = false;
            MyPanel.isLineClicked = false;
            MyPanel.isOvalClicked = false;
            MyPanel.isFreeHandClicked = false;

        }
    }

    public class ClearAllAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            MyPanel.history.clear();
            MyPanel.historyCounter = 0;
            MyPanel.x1 = 0;
            MyPanel.x2 = 0;
            MyPanel.y1 = 0;
            MyPanel.y2 = 0;
            MyPanel.isChanged = true;
            MyPanel.imageBuffer = null;
        }
    }

    public static class UndoAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            MyPanel.undoAction();
        }
    }

    public static class RedoAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            MyPanel.redoAction();
        }
    }

    public class SaveAsJPGAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            MyPanel.isSaved = true;
        }

    }

    public class OpenJPGAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            MyPanel.isFileOpened = true;
        }

    }

    //*************** Checklists  *****************\\
    public class DottedItemList implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) // if checkbox is checked
            {
                float[] dashingPattern1 = {2f, 2f};
                MyPanel.stroke = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dashingPattern1, 2.0f);
            } else // else if the checkbox in unchecked
            {
                MyPanel.stroke = new BasicStroke(2f);
            }

        }

    }

    public class FilledItemList implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                MyPanel.isFilledChecked = true;
            } else {
                MyPanel.isFilledChecked = false;
            }

        }

    }

    //*************** COLORS BUTTONS  *****************\\
    public class ColorButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JColorChooser colorChooser = new JColorChooser();
            JFrame colorFrame = new JFrame();
            Color color = colorChooser.showDialog(colorFrame, "Select a Color", MyPanel.color);
            colorButton.setBackground(color);
            MyPanel.color = color;
        }
    }
}
