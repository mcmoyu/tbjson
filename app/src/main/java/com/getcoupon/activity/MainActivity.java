package com.getcoupon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.getcoupon.bmob.Person;
import com.getcoupon.bmob.User;
import com.moyu.uigetcoupon.R;
import com.getcoupon.tools.RWUtil;

import java.util.List;
import java.util.Properties;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;


public class MainActivity extends AppCompatActivity {

    final MainActivity mainActivity = this;
    final User user = new User();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bmob.initialize(this, "f72fba6a46d9b2e149c29d1ad6071c54");

        Button btn_turn = findViewById(R.id.btn_turn);
        Button btn_login = findViewById(R.id.btn_login);
        Button btn_owner = findViewById(R.id.btn_owner);

        RWUtil.isExistAndCreate(this);

        Properties pro = RWUtil.getUserInfo(this);
        if(pro != null){
            user.setAlreadylogin(pro.getProperty("alreadylogin"));
            user.setAutoLogin(pro.getProperty("autologin"));
            user.setUsername(pro.getProperty("username"));
            user.setPassword(pro.getProperty("password"));
            user.setRelationID(Long.valueOf(pro.getProperty("relationid")).longValue());
        }

        if("true".equals(user.getAutoLogin())){ // 自动登录
            BmobUser.loginByAccount(user.getUsername(), user.getPassword(), new LogInListener<Person>() {
                @Override
                public void done(Person Person, BmobException e) {
                    if (e == null) {
                        if(BmobUser.isLogin()){
                            BmobQuery<Person> bmobQuery = new BmobQuery<>();
                            bmobQuery.findObjects(new FindListener<Person>() {
                                @Override
                                public void done(List<Person> object, BmobException e) {
                                    if (e == null) {
                                        for (int i = 0; i < object.size(); i++) {
                                            if(user.getUsername().equals(object.get(i).getUsername())){
                                                RWUtil.setUserInfo(mainActivity,"true",user.getAutoLogin(),object.get(i).getUsername(),object.get(i).getPw(),object.get(i).getRelationId());
                                                Toast.makeText(MainActivity.this, "自动登录成功，欢迎你：" + object.get(i).getUsername(), Toast.LENGTH_SHORT).show();
                                                break;
                                            }
                                        }
                                    } else {
                                        Toast.makeText(MainActivity.this, "查询数据失败" + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(mainActivity, "请先登录", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(mainActivity, "登录失败：" + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


            /*
            final Person person = new Person();
            person.setUsername(user.getUsername());
            person.setPassword(user.getPassword());
            person.login(new SaveListener<Person>() {
                @Override
                public void done(Person bmobPerson, BmobException e) {
                    if(e == null) {
                        Toast.makeText(MainActivity.this, "自动登录成功，欢迎您：" + person.getUsername(), Toast.LENGTH_SHORT).show();
                        if (BmobUser.isLogin()) {
                            // Person per = BmobUser.getCurrentUser(Person.class);
                            String username = (String) BmobUser.getObjectByKey("username");
                            String relationID = (String) BmobUser.getObjectByKey("relationId");
                            RWUtil.setUserInfo(mainActivity,"true","true",username,user.getPassword(),relationID);
                            Toast.makeText(MainActivity.this, "登录成功Username：" + username + "，RelationID：" + relationID, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "登录失败，" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });*/
        } else if("false".equals(user.getAutoLogin())) {
            // RWUtil.setUserInfo(mainActivity,"false","false",user.getUsername(),user.getPassword(),user.getRelationID());
        }

        // 打开转链
        btn_turn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,TurnActivity.class);
                startActivity(intent);
            }
        });

        // 我的界面
        btn_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties pro = RWUtil.getUserInfo(mainActivity);
                if(pro != null) {
                    if ("true".equals(pro.getProperty("alreadylogin"))) {
                        Intent intent = new Intent(MainActivity.this, OwnerActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(mainActivity, "您还未登录，请先登录您的账号", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // 登录界面
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties pro = RWUtil.getUserInfo(mainActivity);
                if(pro != null){
                    if("true".equals(pro.getProperty("alreadylogin"))) {
                        Toast.makeText(mainActivity, "您已登录，如需注销当前账号请在【我的】点击【注销当前账号】", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    };
}
