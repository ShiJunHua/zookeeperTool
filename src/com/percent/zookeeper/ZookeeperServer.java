package com.percent.zookeeper;


import java.util.ArrayList;
import java.util.List;

public class ZookeeperServer{

    /**
     * 创建节点
     * @param path 节点路径
     * @param content 节点内容
     * @return 创建节点
     */
    public boolean create(String path,String content) {
        ZooKeeperBase zooKeeperBase = ZooKeeperBase.getInstance();
        try{
            zooKeeperBase.connect();
            return zooKeeperBase.createNode(path, content.getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            zooKeeperBase.close();
        }
        return false;
    }

    /**
     * 更新节点
     * @param path 节点路径
     * @param content 节点内容
     * @return 更新service配置
     */
    public boolean update(String path,String content){
        ZooKeeperBase zooKeeperBase = ZooKeeperBase.getInstance();
        try{
            zooKeeperBase.connect();
            return zooKeeperBase.setData(path, content.getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            zooKeeperBase.close();
        }
        return false;
    }

    /**
     * @return 查询所有节点
     */
    public List<String> queryAll(String path){
        ZooKeeperBase zooKeeperBase = ZooKeeperBase.getInstance();
        try{
            zooKeeperBase.connect();
            List<String> list = zooKeeperBase.getChildren(path);
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            zooKeeperBase.close();
        }
        return new ArrayList<>();
    }

    /**
     * 获取单个对象
     * @param path
     * @return 根据serviceID查询配置信息
     */
    public String get(String path){
        ZooKeeperBase zooKeeperBase = ZooKeeperBase.getInstance();
        try{
            zooKeeperBase.connect();
            String config = zooKeeperBase.getDate(path);
            return config;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            zooKeeperBase.close();
        }
        return "";
    }

    /**
     * 删除节点
     * @param path
     * @return
     * @throws Exception
     */
    public boolean delNode(String path){
        ZooKeeperBase zooKeeperBase = ZooKeeperBase.getInstance();
        try{
            zooKeeperBase.connect();
            return zooKeeperBase.delNode(path);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            zooKeeperBase.close();
        }
        return false;
    }
}
