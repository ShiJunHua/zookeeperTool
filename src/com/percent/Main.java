package com.percent;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

/**
 * @Description:
 * @Auther:史俊华
 * @Date:2018/11/2610
 */
public class Main{
   public Main(){
       Dimension screeSize = Toolkit.getDefaultToolkit().getScreenSize();
       int screenWidth = screeSize.width;
       int screenHeight = screeSize.height;
       //加载图片
       ImageIcon icon=new ImageIcon(getClass().getResource("/images/bg2.jpg"));
       //Image im=new Image(icon);
       //将图片放入label中
       JLabel label=new JLabel(icon);
       //设置label的大小
       label.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());
       JFrame frame=new JFrame("Zookeeper");
       //获取窗口的第二层，将label放入
       frame.getLayeredPane().add(label,new Integer(Integer.MIN_VALUE));
       //获取frame的顶层容器,并设置为透明
       JPanel j=(JPanel)frame.getContentPane();
       j.setOpaque(false);

       HeaderPanel headerPanel = new HeaderPanel();
       BodyLeftPanel bodyLeftPanel = new BodyLeftPanel();
       BodyRightPanel bodyRightPanel = new BodyRightPanel();
       bodyLeftPanel.setTextPane(bodyRightPanel.getTextArea());
       headerPanel.setTree(bodyLeftPanel.getTree());
       headerPanel.setTextPane(bodyRightPanel.getTextArea());
       headerPanel.setBodyLeftPanel(bodyLeftPanel);
       JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
       jSplitPane.setLeftComponent(bodyLeftPanel);
       jSplitPane.setRightComponent(bodyRightPanel);
       jSplitPane.setOpaque(false);
       jSplitPane.setDividerSize(2);
       jSplitPane.setDividerLocation(Double.valueOf(screenWidth*0.8*0.3).intValue());

       frame.add(headerPanel,BorderLayout.NORTH);
       frame.add(jSplitPane,BorderLayout.CENTER);
       frame.setSize(Double.valueOf(screenWidth*0.8).intValue(),Double.valueOf(screenHeight*0.7).intValue());
       frame.setLocationRelativeTo(null);
       frame.setVisible(true);
       frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
       frame.setIconImage(new ImageIcon(getClass().getResource("/images/zookeeper.png")).getImage());
   }
    public static void main(String[] args) {
        InitGlobalFont(GlobalFont.getFont());
        new Main();
    }

    private static void InitGlobalFont(Font font) {
        FontUIResource fontRes = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys();
             keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }
}
