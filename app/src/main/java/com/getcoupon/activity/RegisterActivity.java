package com.getcoupon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.moyu.uigetcoupon.R;
import com.getcoupon.bmob.Person;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class RegisterActivity extends AppCompatActivity {

    EditText et_username;
    EditText et_password;
    EditText et_repassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView tv_back_login = findViewById(R.id.tv_back_login);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_repassword = findViewById(R.id.et_repassword);
        Button btn_register = findViewById(R.id.btn_register);

        // 初始化Bmob
        Bmob.initialize(this, "f72fba6a46d9b2e149c29d1ad6071c54");

        tv_back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Person person = new Person();
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                String rePassword = et_repassword.getText().toString();
                if(username.length() >= 6 && username.length() <= 12){
                    if("".equals(password)){
                        Toast.makeText(RegisterActivity.this, "密码不能为空，请输入密码", Toast.LENGTH_SHORT).show();
                    } else if(password.length() >= 6 && password.length() <=15) {
                        person.setUsername(et_username.getText().toString().trim());
                        if(!et_password.getText().toString().equals(rePassword)){
                            Toast.makeText(RegisterActivity.this, "两次密码不一致，请重新输入密码", Toast.LENGTH_SHORT).show();
                            et_password.setText("");
                            et_repassword.setText("");
                        } else {
                            person.setPassword(et_password.getText().toString());
                            person.setPw(et_repassword.getText().toString());
                            person.signUp(new SaveListener<Person>() {
                                @Override
                                public void done(Person person, BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(RegisterActivity.this, "注册成功，请登录！", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "注册失败，" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "密码长度为6-15，当前长度为:" + password.length(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if ("".equals(username)) {
                        Toast.makeText(RegisterActivity.this, "账号不能为空，请输入账号", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "账号长度为6-12，当前长度:" + username.length(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}