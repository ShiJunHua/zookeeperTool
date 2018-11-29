package com.percent;

import javax.swing.*;
import java.awt.*;

/**
 * @Description:
 * @Auther:史俊华
 * @Date:2018/11/2814
 */
public class BodyRightPanel extends JPanel {
    private JTextPane textPane;
    public BodyRightPanel(){
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        textPane = new JTextPane();
        textPane.setSize(500,600);
        textPane.setOpaque(false);
        textPane.setForeground(Color.WHITE);
        int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
        int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
        JScrollPane jsp = new JScrollPane(textPane,v,h);
        jsp.getHorizontalScrollBar().setUI(new ScrollBarUI());
        jsp.getVerticalScrollBar().setUI(new ScrollBarUI());
        jsp.setOpaque(false);
        jsp.getViewport().setOpaque(false);
        this.add(jsp);
    }

    public JTextPane getTextArea(){
        return textPane;
    }
}
