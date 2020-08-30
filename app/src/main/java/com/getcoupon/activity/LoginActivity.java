package com.getcoupon.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.getcoupon.bmob.User;
import com.getcoupon.tools.RWUtil;
import com.moyu.uigetcoupon.R;
import com.getcoupon.bmob.Person;

import java.util.List;
import java.util.Properties;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {

    final LoginActivity loginActivity = this;
    final User user = new User();

    EditText et_username;
    EditText et_password;
    CheckBox remember_username;
    CheckBox remember_password;
    CheckBox auto_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn_login = findViewById(R.id.btn_login);
        Button btn_back = findViewById(R.id.btn_back);
        TextView tv_register = findViewById(R.id.tv_register);
        TextView tv_forget = findViewById(R.id.tv_forget);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        remember_username = findViewById(R.id.remember_username);
        remember_password = findViewById(R.id.remember_password);
        auto_login = findViewById(R.id.auto_login);

        // 保存账号
        remember_username.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    remember_password.setChecked(false);
                }
            }
        });

        // 保存密码
        remember_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    remember_username.setChecked(true);
                } else {
                    auto_login.setChecked(false);
                }
            }
        });

        // 自动登录
        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    remember_password.setChecked(true);
                }
            }
        });

        // 初始化Bmob
        Bmob.initialize(this, "f72fba6a46d9b2e149c29d1ad6071c54");

        Properties pro = RWUtil.getUserInfo(this);
        if(pro != null){
            user.setAlreadylogin(pro.getProperty("alreadylogin"));
            user.setAutoLogin(pro.getProperty("autologin"));
            user.setUsername(pro.getProperty("username"));
            user.setPassword(pro.getProperty("password"));
            user.setRelationID(Long.valueOf(pro.getProperty("relationid")).longValue());
        }

        if(!"null".equals(user.getUsername())) {
            et_username.setText(user.getUsername());
            remember_username.setChecked(true);
        } else {
            remember_username.setChecked(false);
        }

        if(!"null".equals(user.getPassword())) {
            et_password.setText(user.getPassword());
            remember_password.setChecked(true);
        } else {
            remember_password.setChecked(false);
        }
        auto_login.setChecked("true".equals(user.getAutoLogin()));

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String username = et_username.getText().toString().trim();
                final String password = et_password.getText().toString();
                if(username.length() >= 6 && username.length() <= 12){
                    if("".equals(password)){
                        Toast.makeText(LoginActivity.this, "密码不能为空，请输入密码", Toast.LENGTH_SHORT).show();
                    } else if(password.length() >= 6 && password.length() <=15) {
                        BmobUser.loginByAccount(username, password, new LogInListener<Person>() {
                            @Override
                            public void done(Person person, BmobException e) {
                                if (e == null) {
                                    if(BmobUser.isLogin()){
                                        BmobQuery<Person> bmobQuery = new BmobQuery<>();
                                        bmobQuery.findObjects(new FindListener<Person>() {
                                            @Override
                                            public void done(List<Person> object, BmobException e) {
                                                if (e == null) {
                                                    for (int i = 0; i < object.size(); i++) {
                                                        if(username.equals(object.get(i).getUsername())){
                                                            RWUtil.setUserInfo(loginActivity,"true",auto_login.isChecked()?"true":"false",object.get(i).getUsername(),object.get(i).getPw(),object.get(i).getRelationId());
                                                            Toast.makeText(LoginActivity.this, "登录成功，欢迎你，" + object.get(i).getUsername(), Toast.LENGTH_SHORT).show();
                                                            finish();
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    Toast.makeText(LoginActivity.this, "查询数据失败" + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(loginActivity, "请先登录", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    RWUtil.setUserInfo(loginActivity,"false",auto_login.isChecked()?"true":"false","".equals(username)?"null":username,"".equals(password)?"null":password,"null");
                                    Toast.makeText(LoginActivity.this, "登录失败，账号或者密码错误，请确认！" + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(LoginActivity.this, "密码长度为6-15，当前长度为:" + password.length(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if ("".equals(username)) {
                        Toast.makeText(LoginActivity.this, "账号不能为空，请输入账号", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "账号长度为6-12，当前长度:" + username.length(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if("".equals(et_username.getText().toString())){
                    et_password.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        tv_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
                startActivity(intent);
            }
        });
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 0x11:
                    String s = (String) msg.obj;
                    et_username.setText(s);
                    break;
                case 0x12:
                    String ss = (String) msg.obj;
                    et_password.setText(ss);
                    break;
            }

        }
    };
}