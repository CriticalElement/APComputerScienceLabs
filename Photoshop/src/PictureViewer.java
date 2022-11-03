import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.util.Arrays;

/**
 * A GUI to view a {@link Picture}
 * GUI contributed by Liberty student Ben Wyatt, 2017
 * with very minor visual tweaks by Mr. Bunn
 */
public class PictureViewer {

    private final JFrame frame;
    private final PictureView pictureView;
    private final JLabel label;

    /**
     * Creates a PictureViewer with a given {@link Picture}
     * @param picture The {@link Picture} to view
     */
    public PictureViewer(Picture picture) {
        picture = new Picture(picture);
        frame = new JFrame();
        frame.setLayout(new BorderLayout(0, 20));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        label = new JLabel("", SwingConstants.CENTER);
        frame.add(label, BorderLayout.NORTH);
        
        Point cursor = new Point(0, 0);

        pictureView = new PictureView(picture, cursor);
        JScrollPane p = new JScrollPane(pictureView);
        frame.add(p, BorderLayout.SOUTH);
        frame.pack();
        JMenuBar menu = new JMenuBar();
        JMenu m = new JMenu("Zoom");
        JMenuItem item = new JMenuItem("Original");
        item.addActionListener(e -> {pictureView.zoom = 1; pictureView.revalidate();});
        m.add(item);
        for (int i = 200; i<=1200; i+=200) {
            item = new JMenuItem(i+"%");
            int x = i;
            item.addActionListener(e -> {pictureView.zoom = x/100; pictureView.revalidate();});
            m.add(item);
        }
        menu.add(m);
        frame.setJMenuBar(menu);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Closes the PictureViewer
     */
    @SuppressWarnings("unused")
    public void close() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        frame.dispose();
    }

    public boolean isVisible() {
        int totalWindows = Arrays.stream(Frame.getWindows()).toArray().length;
        return Arrays.stream(Frame.getWindows()).filter(f -> !f.isVisible()).toArray().length != totalWindows;
    }

    private static String makeLabelText(Point cursor, Color color) {
        return String.format("<html><div style='text-align: center;'>Row: %d, Col: %d<br><font color=\"red\">Red</font> = %d, <font color=\"green\">Green</font> = %d, <font color=\"blue\">Blue</font> = %d</div></html>", cursor.y, cursor.x, color.getRed(), color.getGreen(), color.getBlue());
    }

    private class PictureView extends JPanel implements Scrollable {

        Dimension size;
        Picture picture;
        Point cursor;
        int zoom = 1;

        public PictureView(Picture picture, Point cursor) {
            this.picture = picture;
            this.size = new Dimension(picture.getWidth(), picture.getHeight());
            this.cursor = cursor;
            setLabelText();
            this.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    int x = e.getX()/zoom;
                    int y = e.getY()/zoom;
                    if (x<0 || x>=size.width || y<0 || y>=size.height) return;
                    cursor.x = x;
                    cursor.y = y;
                    repaint();
                    setLabelText();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });
            this.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    int x = e.getX()/zoom;
                    int y = e.getY()/zoom;
                    if (x<0 || x>=size.width || y<0 || y>=size.height) return;
                    cursor.x = x;
                    cursor.y = y;
                    repaint();
                    setLabelText();
                }

                @Override
                public void mouseMoved(MouseEvent e) {

                }
            });
        }

        private void setLabelText() {
            label.setText(makeLabelText(cursor, picture.getPixel(cursor.x, cursor.y).getColor()));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int y = 0; y<size.height; y++) {
                for (int x = 0; x<size.width; x++) {
                    g.setColor(picture.getPixel(x, y).getColor());
                    g.fillRect(x*zoom, y*zoom, zoom, zoom);
                }
            }
            g.setColor(Color.BLACK);
            for (int i = -5; i<=5; i++) {
                if (i==0) continue;
                Point p = new Point(cursor.x+i, cursor.y);
                if (p.x>=0 && p.x<size.width) g.fillRect(p.x*zoom, p.y*zoom, zoom, zoom);
                p = new Point(cursor.x, cursor.y+i);
                if (p.y>=0 && p.y<size.height) g.fillRect(p.x*zoom, p.y*zoom, zoom, zoom);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(size.width*zoom, size.height*zoom);
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return new Dimension(size.width, size.height);
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 100;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 100;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return false;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

}
