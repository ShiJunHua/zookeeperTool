package com.percent;


import javax.swing.*;
import javax.swing.text.AttributeSet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @Description:
 * @Auther:史俊华
 * @Date:2018/11/2814
 */
public class BodyRightPanel extends JPanel {
    private JTextPane textPane;
    private BodyLeftPanel bodyLeftPanel;
    public BodyRightPanel(){
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        textPane = new JTextPane();
        textPane.setOpaque(false);
        textPane.setForeground(Color.WHITE);

        JPopupMenu menu = getMenu();
        textPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.isMetaDown()){
                    super.mouseClicked(e);
                    int x = e.getX();
                    int y = e.getY();
                    menu.show(textPane, x, y);
                }
            }
        });


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

    public void setBodyLeftPanel(BodyLeftPanel bodyLeftPanel) {
        this.bodyLeftPanel = bodyLeftPanel;
    }

    public JPopupMenu getMenu(){
        JPopupMenu menu=new JPopupMenu();		//创建菜单
        JMenuItem addMenuItem=new JMenuItem("保存",new ImageIcon(getClass().getResource("/images/menu-save.png")));//创建菜单项(点击菜单项相当于点击一个按钮)
        //菜单项绑定监听
        addMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(bodyLeftPanel!=null){
                    bodyLeftPanel.save(textPane.getText());
                }
            }
        });

        menu.add(addMenuItem);
        return menu;
    }
}
