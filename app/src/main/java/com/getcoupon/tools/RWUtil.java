package com.getcoupon.tools;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class RWUtil {
    /**
     * 使用属性文件保存用户的信息
     *
     * @param context 上下文
     * @param alreadyLogin 已登录
     * @param autoLogin 自动登录
     * @param username 用户名
     * @param password  密码
     * @param relationID 关系ID
     */
    public static void setUserInfo(Context context, String alreadyLogin, String autoLogin, String username,
                                   String password, String relationID) {
        try {
            // 使用Android上下问获取当前项目的路径
            File file = new File(context.getFilesDir(), "info.properties");
            // 创建输出流对象
            FileOutputStream fos = new FileOutputStream(file);
            // 创建属性文件对象
            Properties pro = new Properties();
            // 设置用户名或密码
            pro.setProperty("alreadylogin", alreadyLogin);
            pro.setProperty("autologin", autoLogin);
            pro.setProperty("username", username);
            pro.setProperty("password", password);
            pro.setProperty("relationid", relationID);
            // 保存文件
            pro.store(fos, "info.properties");
            // 关闭输出流对象
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * 返回属性文件对象
     *
     * @param context 上下文
     * @return pro
     */
    public static Properties getUserInfo(Context context) {
        try {
            // 创建File对象
            File file = new File(context.getFilesDir(), "info.properties");
            // 创建FileIutputStream 对象
            FileInputStream fis = new FileInputStream(file);
            // 创建属性对象
            Properties pro = new Properties();
            // 加载文件
            pro.load(fis);
            // 关闭输入流对象
            fis.close();
            return pro;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void isExistAndCreate(Context context) {
        String path = context.getFilesDir().getPath();
        File file = new File(path);
        if (file.exists()) { // 存在
            Properties pro = getUserInfo(context);
            if(pro != null){
                if(pro.isEmpty()){
                    init(context,"pro为空");
                }
            } else {
                init(context,"pro为null");
            }
        } else { // 不存在
            if(file.mkdir()) {
                init(context,"目录不存在");
            }
        }
    }

    private static void init(Context context,String msg){
        setUserInfo(context,"false","false","null","null","null");
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
