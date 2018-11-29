package com.percent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.percent.zookeeper.ZookeeperServer;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @Description:
 * @Auther:史俊华
 * @Date:2018/11/2814
 */
public class BodyLeftPanel extends JPanel {
    private JTextPane textPane;
    private ZookeeperServer zookeeperServer;
    private String selectPath;
    private DefaultMutableTreeNode curSlectNode;
    private TreePath curSlectTreePath;
    private DefaultTreeModel dtm;
    private JTree tree;
    public BodyLeftPanel(){
        this.setLayout(new BorderLayout());
        this.setSize(400,500);
        this.setOpaque(false);
        initTree();
    }

    public void initTree(){
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("/");
        zookeeperServer = new ZookeeperServer();
        List<String> nodes = zookeeperServer.queryAll("/");
        for(String node : nodes){
            DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(node);
            rootNode.add(treeNode);
        }
        dtm = new DefaultTreeModel(rootNode);
        tree = new JTree(dtm);
        //默认连线
        tree.putClientProperty("JTree.lineStyle" , "Angeled");
        //设置是否显示根节点的“展开/折叠”图标,默认是false
        tree.setShowsRootHandles(true);

        //将节点中的背景色设置为透明
        DefaultTreeCellRenderer cellRenderer = new DefaultTreeCellRenderer();
        cellRenderer.setBackgroundNonSelectionColor(new Color(0, 0, 0, 0));
        cellRenderer.setBackgroundSelectionColor(new Color(0, 0, 0, 0));
        cellRenderer.setForeground(Color.WHITE);
        cellRenderer.setTextNonSelectionColor(Color.WHITE);
        cellRenderer.setTextSelectionColor(Color.WHITE);
        cellRenderer.setBackgroundSelectionColor(Color.GRAY);

        tree.setCellRenderer(cellRenderer);
        tree.setOpaque(false);
        tree.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//设置按钮鼠标手势
        tree.setFocusable(false);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                TreePath treePath = e.getPath();
                String nodePath = getPaths(treePath);
                JTree node = (JTree)e.getSource();
                DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode)node.getLastSelectedPathComponent();
                refreshNode(nodePath, selectNode, zookeeperServer);
            }
        });

        JPopupMenu menu = getMenu();
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int x = e.getX();
                int y = e.getY();
                if(e.getButton()==MouseEvent.BUTTON3){
                    //menuItem.doClick(); //编程方式点击菜单项
                    TreePath pathForLocation = tree.getPathForLocation(x, y);//获取右键点击所在树节点路径
                    tree.setSelectionPath(pathForLocation);
                    selectPath = getPaths(pathForLocation);
                    curSlectTreePath = pathForLocation;
                    curSlectNode = (DefaultMutableTreeNode)pathForLocation.getLastPathComponent();
                    menu.show(tree, x, y);
                }
            }
        });

        int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
        int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
        JScrollPane jsp = new JScrollPane(tree,v,h);
        jsp.getHorizontalScrollBar().setUI(new ScrollBarUI());
        jsp.getVerticalScrollBar().setUI(new ScrollBarUI());
        jsp.setOpaque(false);
        jsp.getViewport().setOpaque(false);
        this.add(jsp);
    }

    private void refreshNode(String nodePath, DefaultMutableTreeNode selectNode, ZookeeperServer zookeeperServer) {
        if(selectNode!=null){
            int childCount = selectNode.getChildCount();
            if(childCount>0){
                selectNode.removeAllChildren();
            }
            List<String> nodes = zookeeperServer.queryAll(nodePath);
            for(String zkNode : nodes){
                DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(zkNode);
                selectNode.add(treeNode);
            }
        }

        String content = zookeeperServer.get(nodePath);
        if(content==null || content.equals("")){
            textPane.setText("");
        }else{
            try{
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = jsonParser.parse(content).getAsJsonObject();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String convertJson = gson.toJson(jsonObject);
                textPane.setText(convertJson);
            }catch (Exception e){
                textPane.setText(content);
            }

        }
    }

    private String getPaths(TreePath treePath) {
        Object[] objects = treePath.getPath();
        String nodePath = "";
        if(objects.length==1){
            nodePath = "/";
        }else{
            for (Object object : objects){
                if(!object.toString().equals("/")){
                    nodePath+="/"+object.toString();
                }
            }
        }

        return nodePath;
    }

    public void setTextPane(JTextPane textPane){
        this.textPane = textPane;
    }

    public JPopupMenu getMenu(){
        JPopupMenu menu=new JPopupMenu();		//创建菜单
        JMenuItem addMenuItem=new JMenuItem("新增",new ImageIcon(getClass().getResource("/add.png")));//创建菜单项(点击菜单项相当于点击一个按钮)
        //菜单项绑定监听
        addMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(curSlectNode!=null && selectPath!=null && !"".equals(selectPath)){
                    String node = JOptionPane.showInputDialog("请输入节点名称");
                    zookeeperServer.create(selectPath+"/"+node,"");
                    refreshNode(selectPath, curSlectNode, zookeeperServer);
                    reloadTree();
                }
            }
        });

        JMenuItem updMenuItem=new JMenuItem("修改",new ImageIcon(getClass().getResource("/upd.png")));//创建菜单项(点击菜单项相当于点击一个按钮)
        //菜单项绑定监听
        updMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(curSlectNode!=null && selectPath!=null && !"".equals(selectPath)){
                    String content = textPane.getText();
                    boolean result = zookeeperServer.update(selectPath,content);
                }
            }
        });

        JMenuItem delMenuItem=new JMenuItem("删除",new ImageIcon(getClass().getResource("/del.png")));//创建菜单项(点击菜单项相当于点击一个按钮)
        //菜单项绑定监听
        delMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(curSlectNode!=null && selectPath!=null && !"".equals(selectPath)){
                    if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(
                            null, "真的要删除该节点吗？", "删除节点",JOptionPane.YES_NO_OPTION)) {
                       zookeeperServer.delNode(selectPath);
                    }
                    TreePath parentTreePath = curSlectTreePath.getParentPath();
                    selectPath = getPaths(parentTreePath);
                    curSlectTreePath = parentTreePath;
                    curSlectNode = (DefaultMutableTreeNode)parentTreePath.getLastPathComponent();
                    refreshNode(selectPath, curSlectNode, zookeeperServer);
                    reloadTree();
                }
            }
        });

        JMenuItem refreshMenuItem=new JMenuItem("刷新",new ImageIcon(getClass().getResource("/refresh.png")));//创建菜单项(点击菜单项相当于点击一个按钮)
        //菜单项绑定监听
        refreshMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(curSlectNode!=null && selectPath!=null && !"".equals(selectPath)){
                    refreshNode(selectPath, curSlectNode, zookeeperServer);
                    reloadTree();
                }
            }
        });

        menu.add(addMenuItem);
        menu.add(updMenuItem);
        menu.add(delMenuItem);
        menu.add(refreshMenuItem);
        return menu;
    }

    private void reloadTree(){
        dtm.reload();
        tree.expandPath(curSlectTreePath);
    }
}
