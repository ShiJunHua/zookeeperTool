package com.percent;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * @Description:
 * @Auther:史俊华
 * @Date:2018/12/717
 */
public class PropertiesUtils {

    public static List<String> getZkIps(){
        List<String> zkIps = new ArrayList<>();
        Properties prop = new Properties();
        InputStream in = null;
        try {
            in =  new FileInputStream(getPath());
            prop.load(in);     ///加载属性列表
            Iterator<String> it=prop.stringPropertyNames().iterator();
            while(it.hasNext()){
                String key=it.next();
                zkIps.add(prop.getProperty(key));
            }
        }catch (Exception e){
//            e.printStackTrace();
        }finally {
            if(in!=null){
                try{
                    in.close();
                }catch (Exception e){

                }
            }
        }
        return zkIps;
    }

    public static void setZkip(String ip){
        FileOutputStream oFile = null;
        try {
            List<String> zkips = getZkIps();
            if(!zkips.contains(ip)){
                String path = getPath();
                oFile = new FileOutputStream(path,true);//true表示追加打开
                Properties prop = new Properties();
                prop.setProperty(ip, ip);
                prop.store(oFile, "zookeeperIp");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(oFile!=null){
                try{
                    oFile.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void delZkip(String ip){
        FileOutputStream oFile = null;
        try {
            List<String> zkips = getZkIps();
            zkips.remove(ip);

            oFile = new FileOutputStream(getPath());//true表示追加打开
            Properties prop = new Properties();
            for (String zkip:zkips) {
                prop.setProperty(zkip, zkip);
            }
            prop.store(oFile, "zookeeperIp");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(oFile!=null){
                try{
                    oFile.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getPath(){
        String path = System.getProperty("user.dir")+"/zookeeper.properties";
        return path;
    }
}
