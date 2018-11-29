package com.percent;

import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * @Description:
 * @Auther:史俊华
 * @Date:2018/11/2613
 */
public class TextFieldBorder extends AbstractBorder {
    private static final Color BACKGROUND01= new Color(255,255,255);
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d=(Graphics2D) g;
        g2d.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(BACKGROUND01);
        g2d.drawRoundRect(0, 0, c.getWidth()-4,c.getHeight()-4, 20, 20);
    }

    public Insets getBorderInsets(Component c){
        return new Insets(0, 10, 0, 0);
    }
}
