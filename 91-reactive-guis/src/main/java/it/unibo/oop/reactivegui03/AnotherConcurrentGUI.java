package it.unibo.oop.reactivegui03;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Third experiment with reactive gui.
 */
public final class AnotherConcurrentGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    private static final String DEFAULT = "0";
    private final JLabel display = new JLabel();
    private final JButton stop = new JButton("stop");
    private final JButton up = new JButton("up");
    private final JButton down  = new JButton("down");

    public AnotherConcurrentGUI() {
        super();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        final JPanel panel = new JPanel();
        panel.add(display);
        display.setText(DEFAULT);
        panel.add(up);
        panel.add(down);
        panel.add(stop);
        this.getContentPane().add(panel);
        this.setVisible(true);

        final Agent agent = new Agent();
        new Thread(agent).start();
        new Thread(() -> {
            try {
                Thread.sleep(10000);
              
                SwingUtilities.invokeAndWait(() -> agent.stopCounting());
            }
            catch (InvocationTargetException | InterruptedException e1) {
                e1.printStackTrace();
        }}).start();

        stop.addActionListener((e) -> agent.stopCounting());
        up.addActionListener((e) -> agent.increaseCounting());
        down.addActionListener((e) -> agent.decreaseCounting());


    }

    private class Agent implements Runnable {

        private volatile boolean isStopped;
        private volatile boolean isIncrementing = true;
        private int counter = Integer.parseInt(DEFAULT);

        @Override
        public void run() {
            try {
                while (!this.isStopped) {
                    
                    SwingUtilities.invokeAndWait(() ->display.setText(Integer.toString(counter)));

                    if(isIncrementing) {
                        counter++;
                    }
                    else {
                        counter--;
                    }
                    
                    Thread.sleep(100);    
                }
            }

            catch (InterruptedException e) {
                e.printStackTrace();
            } 
            catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            
        }

        public void stopCounting() {
            this.isStopped = true;
            up.setEnabled(false);
            down.setEnabled(false);
            stop.setEnabled(false);
        }

        public void increaseCounting() {
            this.isIncrementing = true;
        }

        public void decreaseCounting() {
            this.isIncrementing = false;
        }
        
    }

}
