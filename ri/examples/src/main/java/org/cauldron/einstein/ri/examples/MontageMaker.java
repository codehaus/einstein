package org.cauldron.einstein.ri.examples;

import com.sun.media.jai.widget.DisplayJAI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.RenderedImage;


/**
 * @author Neil Ellis
 */
@org.contract4j5.contract.Contract
public class MontageMaker {
    private JFrame frame;
    private Container contentPane;
    private int count = 0;
    private int max;


    public MontageMaker() {
        start();

    }

    public synchronized void addImageToMosaic(RenderedImage image) {

        System.err.println("Received Image.:" + image);
        if (image == null) {
            return;
        }
        System.out.println("Displaying image number: " + count);

        if (contentPane.getComponentCount() > count) {
            Component component = contentPane.getComponent(count);
            ((DisplayJAI) component).set(image);
            contentPane.validate();
        } else {
            DisplayJAI dj = new DisplayJAI(image);
            dj.set(image);
            contentPane.add(dj);
            contentPane.validate();
        }
        frame.repaint();
        count++;
        if (count >= max) {
            count = 0;
            frame.pack();
        }

    }


    void start() {
        if (frame == null) {
            frame = new JFrame();
            contentPane = frame.getContentPane();

            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            //GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
            Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            frame.setSize(bounds.width, bounds.height);
            frame.setVisible(true);
            int w = contentPane.getWidth();
            int h = contentPane.getHeight();
            int cols = (int) (w / Constants.WIDTH);
            int rows = (int) (h / Constants.HEIGHT);
            max = cols * rows;
            contentPane.setLayout(new GridLayout(rows, cols));

        }

    }

    public void stop() {
        if (frame != null) {
            frame.dispose();
        }

        frame = null;
    }

}
