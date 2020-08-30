package com.getcoupon.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.getcoupon.bmob.Person;
import com.getcoupon.bmob.User;
import com.getcoupon.tools.RWUtil;
import com.moyu.uigetcoupon.R;

import java.util.List;
import java.util.Properties;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class OwnerActivity extends AppCompatActivity {

    OwnerActivity ownerActivity = this;
    User user = new User();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        ImageButton btn_back = findViewById(R.id.btn_back);
        final EditText et_relationid = findViewById(R.id.et_relationid);
        final Button btn_bind = findViewById(R.id.btn_bind_relationid);
        final TextView tv_remain = findViewById(R.id.tv_remain);
        Button btn_offline = findViewById(R.id.btn_offline);

        final Properties pro = RWUtil.getUserInfo(ownerActivity);
        if(pro != null){
            user.setAlreadylogin(pro.getProperty("alreadylogin"));
            user.setAutoLogin(pro.getProperty("autologin"));
            user.setUsername(pro.getProperty("username"));
            user.setPassword(pro.getProperty("password"));
            user.setRelationID(Long.valueOf(pro.getProperty("relationid")).longValue());
        }

        BmobQuery<Person> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<Person>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void done(List<Person> object, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < object.size(); i++) {
                        if(user.getUsername().equals(object.get(i).getUsername())){
                            String relationID = object.get(i).getRelationId();
                            String remain = object.get(i).getRemain();
                            if(relationID == null || "".equals(relationID)){
                                Toast.makeText(ownerActivity, "您还未绑定推广ID", Toast.LENGTH_SHORT).show();
                                et_relationid.setEnabled(true);
                                et_relationid.setHint("请输入您的推广ID");
                                btn_bind.setText("绑定推广ID到当前的账号");
                            } else {
                                Toast.makeText(ownerActivity, "您已绑定推广ID：" + relationID, Toast.LENGTH_SHORT).show();
                                et_relationid.setEnabled(false);
                                et_relationid.setHint(relationID);
                                btn_bind.setText("如需更改推广ID请长按此按钮");
                            }
                            if(!"".equals(remain) && !"null".equals(remain) && remain!=null){
                                tv_remain.setText("￥" + remain);
                            } else {
                                tv_remain.setText("￥0.00");
                            }
                        }
                    }
                } else {
                    Toast.makeText(ownerActivity, "查询信息错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(btn_bind.getText().toString().equals("绑定推广ID到当前的账号")){
                    if("".equals(et_relationid.getText().toString())){
                        Toast.makeText(ownerActivity, "请输入推广ID", Toast.LENGTH_SHORT).show();
                    } else {
                        final Person person = BmobUser.getCurrentUser(Person.class);
                        person.setRelationId(et_relationid.getText().toString());
                        person.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    RWUtil.setUserInfo(ownerActivity,user.getAlreadylogin(),user.getAutoLogin(),user.getUsername(),user.getPassword(),person.getRelationId());
                                    Toast.makeText(ownerActivity, "绑定推广ID成功：" + person.getRelationId(), Toast.LENGTH_LONG).show();
                                    et_relationid.setHint(person.getRelationId());
                                    et_relationid.setEnabled(false);
                                    btn_bind.setText("如需更改推广ID请长按此按钮");
                                } else {
                                    Toast.makeText(ownerActivity, "绑定推广ID失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });

        btn_bind.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(btn_bind.getText().toString().equals("如需更改推广ID请长按此按钮")){
                    et_relationid.setEnabled(true);
                    et_relationid.setText("");
                    et_relationid.setHint("请输入您的推广ID");
                    btn_bind.setText("绑定推广ID到当前的账号");
                    return true;
                } else {
                    return false;
                }
            }
        });

        btn_offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.logOut();
                RWUtil.setUserInfo(ownerActivity,"false",user.getAutoLogin(),user.getUsername(),user.getPassword(),user.getRelationID() + "");
                Toast.makeText(ownerActivity, "已注销当前账户", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OwnerActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}