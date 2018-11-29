package com.percent;

import com.percent.zookeeper.ZooKeeperBase;

import javax.swing.*;
import java.awt.*;

/**
 * @Description:
 * @Auther:史俊华
 * @Date:2018/11/2814
 */
public class HeaderPanel extends JPanel {
    public HeaderPanel(){
        JLabel label=new JLabel("zookeeperIp:"+ZooKeeperBase.getHost(),JLabel.LEFT);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("",1,18));
        label.setOpaque(false);
        this.add(label);
        this.setSize(900,100);
        this.setOpaque(false);
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
    }
}
