import org.graalvm.compiler.graph.Graph;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class JavaApp48 extends  JFrame{

    JButton brushBut, lineBut, ellipseBut, rectBut, strokeBut, fillBut;

    int currentAction = 1;

    Color strokeColor = Color.BLACK, fillColor = Color.black;

    public static void main(String[] args){
        new JavaApp48();
    }

    public JavaApp48(){
        this.setSize(500, 500);
        this.setTitle("Java Paint Alex");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel buttonPanel = new JPanel();

        Box theBox = Box.createHorizontalBox();

        brushBut = makeMeButtons("./src/brush.png", 1);
        lineBut = makeMeButtons("./src/line.png", 2);
        ellipseBut = makeMeButtons("./src/Ellipse.png", 3);
        rectBut = makeMeButtons("./src/Rectangle.png", 4);

        strokeBut = makeMeColorButtons("./src/Stroke.png", 5, true);
        fillBut = makeMeColorButtons("./src/Fill.png", 6, false);

        theBox.add(brushBut);
        theBox.add(lineBut);
        theBox.add(ellipseBut);
        theBox.add(rectBut);
        theBox.add(strokeBut);
        theBox.add(fillBut);

        buttonPanel.add(theBox);

        this.add(buttonPanel, BorderLayout. SOUTH);

        this.add(new DrawingBoard(), BorderLayout. CENTER);

        this.setVisible(true);
    }

    public JButton makeMeButtons(String iconFile, final int actionNum){

        JButton theBut = new JButton();
        Icon butIcon = new ImageIcon(iconFile);
        theBut.setIcon(butIcon);

        theBut.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                currentAction = actionNum;
            }
        });
        return theBut;
    }

    public JButton makeMeColorButtons(String iconFile, final int actionNum, final boolean stroke){

        JButton theBut = new JButton();
        Icon butIcon = new ImageIcon(iconFile);
        theBut.setIcon(butIcon);

        theBut.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(stroke){
                    strokeColor = JColorChooser.showDialog(null, "Pick a stroke", Color.BLACK);
                } else{
                    fillColor = JColorChooser.showDialog(null, "Pick a Fill", Color.BLACK);
                }
            }
        });
        return theBut;
    }

    private class DrawingBoard extends JComponent{
        ArrayList<Shape> shapes = new ArrayList<Shape>();
        ArrayList<Color> shapeFill = new ArrayList<Color>();
        ArrayList<Color> shapeStroke = new ArrayList<Color>();
        Point drawStart, drawEnd;

        public DrawingBoard(){

            this.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e){
                    drawStart = new Point(e.getX(), e.getY());
                    drawEnd = drawStart;
                    repaint();
                }

                public void mouseReleased(MouseEvent e){
                    Shape aShape = drawRectangle(drawStart.x, drawStart.y, e.getX(), e.getY());

                    shapes.add(aShape);
                    shapeFill.add(fillColor);
                    shapeStroke.add(strokeColor);

                    drawStart = null;
                    drawEnd = null;
                    repaint();

                }
            });  // End of addMouseListener

            this.addMouseMotionListener(new MouseMotionAdapter(){
                public void mouseDragged(MouseEvent e){
                    drawEnd = new Point(e.getX(), e.getY());
                    repaint();
                }
            }); // End of addMouseMotionListenner
        }

        public void paint(Graphics g){
            Graphics2D graphSettings = (Graphics2D)g;

            graphSettings.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphSettings.setStroke(new BasicStroke(2));
            Iterator<Color> strokeCounter = shapeStroke.iterator();
            Iterator<Color> fillCounter = shapeFill.iterator();

            graphSettings.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

            for (Shape s: shapes){
                graphSettings.setPaint(strokeCounter.next());
                graphSettings.draw(s);
                graphSettings.setPaint(fillCounter.next());
                graphSettings.fill(s);
            }

            if(drawStart != null && drawEnd != null){
                graphSettings.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));

                graphSettings.setPaint(Color.GRAY);

                Shape aShape = drawRectangle(drawStart.x, drawStart.y, drawEnd.x, drawEnd.y);
            }
        }


        private Rectangle2D.Float drawRectangle(int x1, int y1, int x2, int y2){
            int x = Math.min(x1, x2);
            int y = Math.min(y1, y2);

            int width = Math.abs(x1-x2);
            int height = Math.abs(y1-y2);

            return new Rectangle2D.Float(x, y, width, height);

        }
    }

}
