package com.percent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.percent.zookeeper.ZooKeeperBase;
import com.percent.zookeeper.ZookeeperServer;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Description:
 * @Auther:史俊华
 * @Date:2018/11/2814
 */
public class BodyLeftPanel extends JPanel {
    private String rootNodeName = "zookeeper";
    private JTextPane textPane;
    private ZookeeperServer zookeeperServer;
    private String selectPath;
    private DefaultMutableTreeNode rootNode;
    private DefaultMutableTreeNode curSlectNode;
    private TreePath curSlectTreePath;
    private DefaultTreeModel dtm;
    private JTree tree;

    public BodyLeftPanel(){
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        initTree();
        List<String> zkips = PropertiesUtils.getZkIps();
        if(!zkips.isEmpty()){
            for (String zkip:zkips){
                addTree(zkip);
            }
        }
    }

    public JTree getTree() {
        return tree;
    }

    public void initTree(){
        rootNode = new DefaultMutableTreeNode(rootNodeName);
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

        JPopupMenu menu = getMenu();
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int x = e.getX();
                int y = e.getY();

                //menuItem.doClick(); //编程方式点击菜单项
                TreePath pathForLocation = tree.getPathForLocation(x, y);//获取右键点击所在树节点路径
                if(pathForLocation!=null){
                    tree.setSelectionPath(pathForLocation);
                    selectPath = getPaths(pathForLocation);
                    curSlectTreePath = pathForLocation;
                    curSlectNode = (DefaultMutableTreeNode)pathForLocation.getLastPathComponent();
                    if(e.getButton()==MouseEvent.BUTTON3){
                        menu.show(tree, x, y);
                    }
                    if(e.getClickCount()>=2 && !selectPath.equals(rootNodeName)) {
                        JTree node = (JTree) e.getSource();
                        DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) node.getLastSelectedPathComponent();
                        refreshNode(selectPath, selectNode, zookeeperServer);
                        if(selectNode.getChildCount()>0){
                            reloadTree();
                        }
                    }else{
                        setContent(selectPath);
                    }
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

    private int refreshNode(String nodePath, DefaultMutableTreeNode selectNode, ZookeeperServer zookeeperServer) {
        int nodeChildCount = 0;
        if(selectNode!=null){
            int childCount = selectNode.getChildCount();
            if(childCount>0){
                selectNode.removeAllChildren();
            }
            List<String> nodes = zookeeperServer.queryAll(nodePath);
            nodeChildCount = nodes.size();
            for(String zkNode : nodes){
                DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(zkNode);
                selectNode.add(treeNode);
            }
        }
        return nodeChildCount;
    }

    public void setTextPane(JTextPane textPane){
        this.textPane = textPane;
    }

    public JPopupMenu getMenu(){
        JPopupMenu menu=new JPopupMenu();		//创建菜单
        JMenuItem addMenuItem=new JMenuItem("新增",new ImageIcon(getClass().getResource("/images/menu-add.png")));//创建菜单项(点击菜单项相当于点击一个按钮)
        //菜单项绑定监听
        addMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(curSlectNode!=null && selectPath!=null && !"".equals(selectPath)){
                    String node = JOptionPane.showInputDialog("请输入节点名称");
                    String nodePath = "";
                    if(selectPath.length()==1 && "/".equalsIgnoreCase(selectPath)){
                        nodePath = "/"+node;
                    }else{
                        nodePath = selectPath+"/"+node;
                    }
                    zookeeperServer.create(nodePath,"");
                    refreshNode(selectPath, curSlectNode, zookeeperServer);
                    reloadTree();
                }
            }
        });

        JMenuItem delMenuItem=new JMenuItem("删除",new ImageIcon(getClass().getResource("/images/del.png")));//创建菜单项(点击菜单项相当于点击一个按钮)
        //菜单项绑定监听
        delMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delete();
            }
        });

        JMenuItem refreshMenuItem=new JMenuItem("刷新",new ImageIcon(getClass().getResource("/images/refresh.png")));//创建菜单项(点击菜单项相当于点击一个按钮)
        //菜单项绑定监听
        refreshMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(curSlectNode!=null && selectPath!=null && !"".equals(selectPath)){
                    int childCount = refreshNode(selectPath, curSlectNode, zookeeperServer);
                    if(childCount>0){
                        reloadTree();
                    }
                }
            }
        });

        menu.add(addMenuItem);
        menu.add(delMenuItem);
        menu.add(refreshMenuItem);
        return menu;
    }

    private void setContent(String nodePath){
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

    public void save(String str) {
        try {
            String content = new String(str.getBytes(), StandardCharsets.UTF_8);
            System.out.println(content);
            if(curSlectNode!=null && StringUtils.isNotBlank(selectPath)){
                if(StringUtils.isNotBlank(content)){
                    content = content.replaceAll("\n","").replaceAll("\\s*","");
                }
                zookeeperServer.update(selectPath,content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(){
        if(curSlectNode!=null && selectPath!=null && !"".equals(selectPath)){
            if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(
                    null, "真的要删除该节点吗？", "删除节点",JOptionPane.YES_NO_OPTION)) {
                TreePath parentTreePath = curSlectTreePath.getParentPath();
                if(selectPath.equals("/")){
                    String zkip = curSlectTreePath.getPath()[1].toString();
                    PropertiesUtils.delZkip(zkip);
                    dtm.removeNodeFromParent(curSlectNode);
                }else{
                    zookeeperServer.delAllNode(selectPath);
                    selectPath = getPaths(parentTreePath);
                    boolean hasChild = zookeeperServer.hasChild(selectPath);
                    if(hasChild){
                        curSlectTreePath = parentTreePath;
                        curSlectNode = (DefaultMutableTreeNode)parentTreePath.getLastPathComponent();
                    }else{
                        selectPath = getPaths(parentTreePath.getParentPath());
                        curSlectTreePath = parentTreePath.getParentPath();
                        curSlectNode = (DefaultMutableTreeNode)curSlectTreePath.getLastPathComponent();
                    }
                    refreshNode(selectPath, curSlectNode, zookeeperServer);
                    reloadTree();
                }
            }
        }
    }

    private void reloadTree(){
        dtm.reload();
        tree.expandPath(curSlectTreePath);
    }

    public void addTree(String ip){
        DefaultMutableTreeNode zkNode = new DefaultMutableTreeNode(ip);
        rootNode.add(zkNode);
        reloadTree();
    }

    private void resetZkServer(String ip){
        ZooKeeperBase.setHost(ip);
        zookeeperServer = new ZookeeperServer();
    }

    private String getPaths(TreePath treePath) {
        Object[] objects = treePath.getPath();
        String nodePath = "";
        String zkIp = "";
        if(objects.length==1){
            nodePath = rootNodeName;
        }else{
            int index = 0;
            for (Object object : objects){
                if(!object.toString().equals(rootNodeName)){
                    if(index==0){
                        zkIp = object.toString();
                    }else{
                        nodePath+="/"+object.toString();
                    }
                    index++;
                }
            }
            nodePath = "".equals(nodePath)?"/":nodePath;
        }
        if(!"".equals(zkIp)){
            resetZkServer(zkIp);
        }
        return nodePath;
    }
}
