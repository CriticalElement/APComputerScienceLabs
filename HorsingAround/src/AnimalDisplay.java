import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class AnimalDisplay extends JComponent implements ActionListener
{
    public static void test()
    {
        new AnimalDisplay();
    }
    
    public static void main(String[] args)
    {
        test();
    }

    private ArrayList<Animal> animals;

    public AnimalDisplay()
    {
        animals = randomList();

        JFrame frame = new JFrame("Animals");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 200));
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
        frame.getContentPane().add(this);

        JButton button = new JButton("randomize");
        button.setActionCommand("randomize");
        button.addActionListener(this);
        frame.getContentPane().add(button);

        button = new JButton("count your chickens");
        button.setActionCommand("count your chickens");
        button.addActionListener(this);
        frame.getContentPane().add(button);

        button = new JButton("inventory");
        button.setActionCommand("inventory");
        button.addActionListener(this);
        frame.getContentPane().add(button);

        button = new JButton("pest control");
        button.setActionCommand("pest control");
        button.addActionListener(this);
        frame.getContentPane().add(button);

        button = new JButton("horsing around");
        button.setActionCommand("horsing around");
        button.addActionListener(this);
        frame.getContentPane().add(button);

        button = new JButton("feeling sheepish");
        button.setActionCommand("feeling sheepish");
        button.addActionListener(this);
        frame.getContentPane().add(button);

        button = new JButton("selection sort");
        button.setActionCommand("selection sort");
        button.addActionListener(this);
        frame.getContentPane().add(button);

        frame.pack();
        frame.setVisible(true);
    }

    public void paintComponent(Graphics g)
    {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        int totalWidth = HorsingAround.totalWidth(animals);
        int maxHeight = HorsingAround.tallestAnimal(animals).getHeight();
        double factor = Math.min((double)getWidth() / totalWidth,
                (double)getHeight() / maxHeight);
        int x = 0;
        for (Animal a : animals) {
            int width = (int) (a.getWidth() * factor);
            int height = (int) (a.getHeight() * factor);
            int y = getHeight() - height;
            g.drawImage(a.getImage(), x, y, width, height, null);
            x += width;
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        switch (command) {
            case "randomize":
                animals = randomList();
                break;
            case "count your chickens":
                JOptionPane.showMessageDialog(this, HorsingAround.countYourChickens(animals) + " chickens");
                break;
            case "inventory":
                ArrayList<String> names = HorsingAround.inventory(animals);
                JOptionPane.showMessageDialog(this, names.toString());
                break;
            case "pest control":
                HorsingAround.pestControl(animals);
                break;
            case "horsing around":
                HorsingAround.horsingAround(animals);
                break;
            case "feeling sheepish":
                HorsingAround.feelingSheepish(animals);
                break;
            case "selection sort":
                HorsingAround.selectionSort(animals);
                break;
            default:
                throw new RuntimeException("invalid command:  " + command);
        }
        repaint();
    }

    public static String randomImage()
    {
        int n = (int)(Math.random() * 1000);
        if (n < 386)
            return "mouse";
        else if (n < 579)
            return "chicken";
        else if (n < 707)
            return "pig";
        else if (n < 803)
            return "sheep";
        else if (n < 881)
            return "duck";
        else if (n < 945)
            return "horse";
        else if (n < 999)
            return "cow";
        else
            return "other";
    }

    public static ArrayList<Animal> randomList()
    {
        ArrayList<Animal> list = new ArrayList<>();
        int n = (int)(Math.random() * 20) + 1;
        while (n > 0)
        {
            list.add(new Animal(randomImage()));
            n--;
        }
        return list;
    }
}