import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyForm {
    private JPanel mainPanel;

    public static InputStream getResource (String name)
    {
        InputStream is = ClassLoader.getSystemResourceAsStream (name);
        if (is == null)
        {
            System.out.println ("could not load: "+name);
            return null;
        }
        return new BufferedInputStream(is);
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("MyForm");
        MyForm mf = new MyForm();
        frame.setContentPane(mf.mainPanel);

        Image img = ImageIO.read (Objects.requireNonNull (getResource ("JOKE.GIF")));
        Image img2 = ImageIO.read (Objects.requireNonNull (getResource ("DAYS.GIF")));
        Clock clk = new Clock (img, img2);
        mf.mainPanel.add (clk, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        // Tick the clock
        ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);
        sched.scheduleAtFixedRate(clk::Tick, 0, 1000, TimeUnit.MILLISECONDS);

        frame.setVisible(true);
    }
}
