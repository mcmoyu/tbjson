package com.getcoupon.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.moyu.uigetcoupon.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiyActivity extends AppCompatActivity implements View.OnClickListener {

    EditText con;
    String[][] location = new String[20][8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diy);

        // 返回
        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 编辑框
        con = findViewById(R.id.et_content);

        // 添加功能
        findViewById(R.id.btn_add_title).setOnClickListener(this); // 标题
        findViewById(R.id.btn_add_coupon).setOnClickListener(this); // 优惠券
        findViewById(R.id.btn_add_price).setOnClickListener(this); // 抢购价
        findViewById(R.id.btn_add_volume).setOnClickListener(this); // 销量
        findViewById(R.id.btn_add_tpwd).setOnClickListener(this); // 淘口令
        findViewById(R.id.btn_add_shorturl).setOnClickListener(this); // 短链接

        con.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(con.getSelectionStart() == start){
//                    autoDelete();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(con.getSelectionStart() == start){
                    autoDelete(0,start);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = "商品标题|优惠券|抢购价|月销量|淘口令|短链接";
                str = "[\\S\\s]{3,4}";
                String pattern = "\\{" + str + "\\}";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(s.toString());
                int i = 0;
                while (m.find()){
//                    Toast.makeText(DiyActivity.this, m.group(0) + "," + (m.start()) + "," + (m.end()-1), Toast.LENGTH_SHORT).show();
                    for (int j = 0; j < ((m.end())-m.start()); j++) {
                        location[i][j] = s.toString().substring(m.start()+j,m.start()+j+1);
                    }
                    i++;
                }
                show();
            }
        });
    }

    private void show() {
        TextView show = findViewById(R.id.tv_show);
        show.setText("");
        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[i].length; j++) {
                show.append(location[i][j]);
            }
            show.append("|" + location[i].length);
            show.append("\n");
        }
    }

    private void autoDelete(int start, int end) {
        String str = con.getEditableText().toString();//获取EditText的文本字符串
        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[i].length; j++) {

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_title:
                insertAtFocusedPosition("{商品标题}");
                break;
            case R.id.btn_add_coupon:
                insertAtFocusedPosition("{优惠券}");
                break;
            case R.id.btn_add_price:
                insertAtFocusedPosition("{抢购价}");
                break;
            case R.id.btn_add_volume:
                insertAtFocusedPosition("{月销量}");
                break;
            case R.id.btn_add_tpwd:
                insertAtFocusedPosition("{淘口令}");
                break;
            case R.id.btn_add_shorturl:
                insertAtFocusedPosition("{短链接}");
                break;
            default:
                Toast.makeText(this, "默认", Toast.LENGTH_SHORT).show();
        }
        con.setSelection(con.getSelectionEnd());
    }

    /**
     * 实现将扫描结果插入在EditText光标处
     * @param text 插入的文本
     */
    private void insertAtFocusedPosition(String text){
        int index = con.getSelectionStart();//获取光标所在位置
        Editable edit = con.getEditableText();//获取EditText的文字
        if (index < 0 || index >= edit.length()){
            edit.append(text);
        }else{
            edit.insert(index,text);//光标所在位置插入文字
        }
    }
}