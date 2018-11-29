package com.percent;

import com.percent.zookeeper.ZooKeeperBase;

import javax.swing.*;
import java.awt.*;

/**
 * @Description:
 * @Auther:史俊华
 * @Date:2018/11/2610
 */
public class Main{
   public Main(){
       String inputValue = JOptionPane.showInputDialog("请输入zookeeper的IP地址");
       ZooKeeperBase.setHost(inputValue);
//       ZooKeeperBase.setHost("172.20.54.121");
       //加载图片
       ImageIcon icon=new ImageIcon(getClass().getResource("/bg2.jpg"));
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
       JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
       BodyLeftPanel bodyLeftPanel = new BodyLeftPanel();
       BodyRightPanel bodyRightPanel = new BodyRightPanel();
       bodyLeftPanel.setTextPane(bodyRightPanel.getTextArea());
       jSplitPane.setLeftComponent(bodyLeftPanel);
       jSplitPane.setRightComponent(bodyRightPanel);
       jSplitPane.setOpaque(false);
       jSplitPane.setDividerSize(2);
       frame.add(headerPanel,BorderLayout.NORTH);
       frame.add(jSplitPane,BorderLayout.CENTER);
       frame.setSize(900,700);
       frame.setLocationRelativeTo(null);
       frame.setVisible(true);
       frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
       frame.setIconImage(new ImageIcon(getClass().getResource("/zookeeper.png")).getImage());
   }
    public static void main(String[] args) {
        new Main();
    }
}
