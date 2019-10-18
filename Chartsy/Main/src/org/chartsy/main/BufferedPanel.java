package org.chartsy.main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Transparency;
import java.awt.image.VolatileImage;
import java.io.Serializable;
import javax.swing.JPanel;
import org.chartsy.main.utils.SerialVersion;

/**
 * 缓冲面板
 *
 * @author viorel.gheba
 */
public abstract class BufferedPanel extends JPanel implements Serializable {

    private static final long serialVersionUID = SerialVersion.APPVERSION;

    // @see https://www.iteye.com/blog/regular-507416
    // Java 1.4在Java 2D的功能方面引入了对硬件加速的支持。毫无疑问，硬件加速非常有用——不过有效的使用java.awt.image.VolatileImage至少
    // 要比使用传统的“图像缓冲”机制要复杂一些。仅当你在自行实现复杂的Java 2D渲染的时候，使用低级的“硬件加速”功能才是的确很重要的。如果
    // 你只是在使用比方说Swing里预编译的控件的话，那么这个技巧的大部分都不太合适。但是对那些Java的2D游戏编程的人，或者那些操作大量图形，如
    // 图表、图解的人来说，就非常有用了。 
    protected VolatileImage volatileImage;

    public BufferedPanel() {
    }

    @Override
    public void paint(Graphics g) {
        createBackBuffer();
        do {
            GraphicsConfiguration graphicsConfiguration = getGraphicsConfiguration();
            int code = volatileImage.validate(graphicsConfiguration);

            if (code == VolatileImage.IMAGE_INCOMPATIBLE) {
                createBackBuffer();
            }

            Graphics graphics = volatileImage.getGraphics();
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.setColor(new Color(0, 0, 0, 0));
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OUT));
            graphics2D.fillRect(0, 0, volatileImage.getWidth(), volatileImage.getHeight());
            paintBufferedComponent(graphics);
            g.drawImage(volatileImage, 0, 0, this);
            graphics2D.dispose();
            graphics.dispose();
        } while (volatileImage.contentsLost());
    }

    protected abstract void paintBufferedComponent(Graphics g);

    protected void createBackBuffer() {
        GraphicsConfiguration graphicsConfiguration = getGraphicsConfiguration();
        volatileImage = graphicsConfiguration.createCompatibleVolatileImage(getWidth(), getHeight(), Transparency.TRANSLUCENT);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

}
