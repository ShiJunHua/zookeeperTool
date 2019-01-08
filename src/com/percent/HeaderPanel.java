package com.percent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * @Description:
 * @Auther:史俊华
 * @Date:2018/11/2814
 */
public class HeaderPanel extends JPanel {
    private JTree tree;
    private JTextPane textPane;
    private BodyLeftPanel bodyLeftPanel;

    public void setBodyLeftPanel(BodyLeftPanel bodyLeftPanel) {
        this.bodyLeftPanel = bodyLeftPanel;
    }

    public HeaderPanel(){
        JToolBar toolBar = new JToolBar("工具栏");
        JButton addZkBtn = new JButton("添加ZK",new ImageIcon(getClass().getResource("/images/add.png")));
        addZkBtn.setFocusPainted(false);
        addZkBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputValue = JOptionPane.showInputDialog("请输入zookeeper的IP地址");
                if(inputValue!=null && !"".equals(inputValue.trim())){
                    PropertiesUtils.setZkip(inputValue);
                    bodyLeftPanel.addTree(inputValue);
                }

            }
        });

        JButton fontPlusBtn = new JButton("字体",new ImageIcon(getClass().getResource("/images/font-plus.png")));
        fontPlusBtn.setFocusPainted(false);
        fontPlusBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Font font = GlobalFont.getFont();
                int size = font.getSize();
                GlobalFont.setFont(new Font("alias", Font.PLAIN, size+2));
                tree.setFont(GlobalFont.getFont());
                textPane.setFont(GlobalFont.getFont());
            }
        });
        JButton fontSmallBtn = new JButton("字体",new ImageIcon(getClass().getResource("/images/font-small.png")));
        fontSmallBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Font font = GlobalFont.getFont();
                int size = font.getSize();
                GlobalFont.setFont(new Font("alias", Font.PLAIN, size-2));
                tree.setFont(GlobalFont.getFont());
                textPane.setFont(GlobalFont.getFont());
            }
        });
        fontSmallBtn.setFocusPainted(false);
        toolBar.add(addZkBtn);
        toolBar.add(fontPlusBtn);
        toolBar.add(fontSmallBtn);
        this.setLayout(new BorderLayout());
        this.add(toolBar);
        this.setOpaque(false);
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
    }

    public void setTree(JTree tree) {
        this.tree = tree;
    }

    public void setTextPane(JTextPane textPane) {
        this.textPane = textPane;
    }
}
