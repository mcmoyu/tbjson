package com.getcoupon.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getcoupon.bmob.User;
import com.getcoupon.tools.RWUtil;
import com.moyu.uigetcoupon.R;
import com.getcoupon.api.MiaoYouQuan;
import com.getcoupon.tools.Item;
import com.getcoupon.tools.Json;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class TurnActivity extends AppCompatActivity {

    MiaoYouQuan miaoYouQuan = new MiaoYouQuan();
    MiaoYouQuan miaoYouQuan2 = new MiaoYouQuan();
    Json json = new Json();
    Item item = new Item();
    User user = new User();
    Message message = new Message();
    TextView tv_text;
    TextView tv_tips;
    ImageView iv_pic;
    long relationID;
    Boolean ok = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn);

        final EditText et_content = findViewById(R.id.et_content);
        final Button btn_turn = findViewById(R.id.btn_turn);
        final Button btn_copy_all = findViewById(R.id.btn_copy_all);
        final Button btn_copy_url = findViewById(R.id.btn_copy_url);
        final Button btn_copy_tpwd = findViewById(R.id.btn_copy_tpwd);
        final Button btn_claer = findViewById(R.id.btn_clear);
        final Button btn_diy = findViewById(R.id.btn_diy);
        Button btn_open_tb = findViewById(R.id.btn_open_tb);
        Button btn_open_qq = findViewById(R.id.btn_open_qq);
        Button btn_open_vx = findViewById(R.id.btn_open_vx);
        ImageButton btn_back = findViewById(R.id.btn_back);
        tv_text = findViewById(R.id.tv_text);
        tv_tips = findViewById(R.id.tv_tips);
        iv_pic = findViewById(R.id.iv_pic);

        Properties pro = RWUtil.getUserInfo(this);
        if(pro != null){
            user.setAlreadylogin(pro.getProperty("alreadylogin","false"));
            user.setUsername(pro.getProperty("username"));
            user.setRelationID(Long.valueOf(pro.getProperty("relationid","0")).longValue());
        }
        if(user.getAlreadylogin().equals("true")){
            relationID = user.getRelationID();
            tv_tips.setVisibility(View.GONE);
        } else {
            tv_tips.setVisibility(View.VISIBLE);
        }

        // 返回
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 打开QQ
        btn_open_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage("com.tencent.mobileqq");
                startActivity(intent);
                Toast.makeText(TurnActivity.this, "正在打开手机ＱＱ", Toast.LENGTH_SHORT).show();
            }
        });

        // 打开微信
        btn_open_vx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage("com.tencent.mm");
                startActivity(intent);
                Toast.makeText(TurnActivity.this, "正在打开手机微信", Toast.LENGTH_SHORT).show();
            }
        });

        // 打开淘宝
        btn_open_tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage("com.taobao.taobao");
                startActivity(intent);
                Toast.makeText(TurnActivity.this, "正在打开手机淘宝", Toast.LENGTH_SHORT).show();
            }
        });

        // 仅复制链接
        btn_copy_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setDocument(item.getShort_url());
                if (copy()) {
                    Toast.makeText(TurnActivity.this, "复制链接成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TurnActivity.this, "复制链接失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 仅复制淘口令
        btn_copy_tpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setDocument(item.getTpwd());
                if (copy()) {
                    Toast.makeText(TurnActivity.this, "复制淘口令成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TurnActivity.this, "复制淘口令失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 复制全部文案
        btn_copy_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setDocument(tv_text.getText().toString());
                if (copy()) {
                    Toast.makeText(TurnActivity.this, "复制全部文案成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TurnActivity.this, "复制全部文案失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 清空
        btn_claer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_content.setText("");
                btn_turn.setEnabled(true);
                btn_turn.setText("转   链");
                iv_pic.setImageBitmap(null);
                tv_text.setText("");
            }
        });

        // 转链
        btn_turn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    public void run() {
                        String content = et_content.getText().toString().trim();
                        if (content.isEmpty()) {
                            setToast("请输入淘宝-淘口令/链接/商品ID");
                        } else {
                            String url, jsonStr;
                            miaoYouQuan.setContent(content);
                            miaoYouQuan.setTbname("mc陌宇");
                            miaoYouQuan.setPid("mm_129779402_46770368_109694000456");
                            miaoYouQuan.setKey("c90dbafa-95f2-e430-8b58-a085740c9e5c");
                            // 校验ID是否可用
                            miaoYouQuan.setRelationID(checkRelationID(relationID));
                            miaoYouQuan.setTpwd("1"); // 获取淘口令
                            miaoYouQuan.setExtsearch("1"); // 如果没有优惠券，从第三方查找
                            miaoYouQuan.setShorturl("1"); // 获取短链接
                            url = miaoYouQuan.getUrl_all_purpose();
                            try {
                                jsonStr = json.getJson(url);
                                anyJson(jsonStr);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
        });
        //  匹配淘口令：String regex = "([\\p{Sc}]?)(\\w{8,12})([\\p{Sc}]?)";

        // 自定义文案
        btn_diy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TurnActivity.this,DiyActivity.class);
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private long checkRelationID(long ID) {
        miaoYouQuan2.setKey("c90dbafa-95f2-e430-8b58-a085740c9e5c");
        miaoYouQuan2.setTbname("mc陌宇");
        try {
            String jsonStr = json.getJson(miaoYouQuan2.getUrl_agent_info());
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArr = jsonObject.getJSONObject("data").getJSONObject("inviter_list").getJSONArray("map_data");
            int n = 0;
            for (int i = 0; i < jsonArr.length(); i++) {
                String id = jsonArr.getJSONObject(i).getString("relation_id");
                if((ID + "").equals(id)){
                    ok = true;
                    break;
                } else {
                    n++;
                }
            }
            if(n == jsonArr.length()) {
                if (ID != 0) {
                    ok = false;
                } else {
                    ok = true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return ID;
    }

    private void setToast(String str) {
        Looper.prepare();
        Toast.makeText(TurnActivity.this, str, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    private boolean copy() {
        String str = item.getDocument();
        if(str == null) {
            return false;
        } else {
            try {
                //获取剪贴板管理器
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", str);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void anyJson(String jsonStr) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonStr);

        String code = jsonObject.getString("code"); // 返回代码
        String msg = jsonObject.getString("msg"); // 返回信息

        if(Integer.parseInt(code)!=200) {
            setToast(msg);
        } else {
            String data = jsonObject.getString("data"); // 获取数据
            JSONObject jsonData = new JSONObject(data);
            item.setHas_coupon(Boolean.parseBoolean(jsonData.getString("has_coupon"))); // 是否有优惠券，true:有；false:没有
            item.setItem_url(jsonData.getString("item_url")); // 商品淘客链接，同样为高佣，has_coupon为false时，建议使用此链接
            item.setItem_id(Long.parseLong(jsonData.getString("item_id"))); // 商品ID
            item.setCoupon_click_url(jsonData.getString("coupon_click_url")); // 二合一链接
            item.setCategory_id(Integer.parseInt(jsonData.getString("category_id"))); // 商品分类ID
            item.setTpwd(jsonData.getString("tpwd")); // 淘口令
            item.setShort_url(jsonData.getString("short_url")); // 短链接
            item.setTitle(jsonData.getJSONObject("item_info").getString("title")); // 标题
            item.setVolume(jsonData.getJSONObject("item_info").getInt("volume")); // 销量
            item.setZk_final_price(jsonData.getJSONObject("item_info").getString("zk_final_price"));
            item.setPict_url(jsonData.getJSONObject("item_info").getString("pict_url")); // 主图链接

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bmp = getURLimage(item.getPict_url());
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = bmp;
                    handle.sendMessage(msg);
                }
            }).start();

            String url = "";
            if(item.isHas_coupon()) {
                item.setQuanLimit(jsonData.getString("quanlimit")); // 优惠券使用限制条件，即满xx元使用
                item.setYouHuiQuan(jsonData.getString("youhuiquan")); // 优惠券面额，没有优惠券则不显示此字段
                item.setMax_commission_rate(jsonData.getString("max_commission_rate")); // 高佣金比例，计划中的最高佣金，如果阿里妈妈帐户为初级，则会走通用佣金
                item.setCoupon_type(Integer.parseInt(jsonData.getString("coupon_type"))); // 优惠券类型，1 公开券，2 私有券，3 妈妈券
                item.setCoupon_start_time(jsonData.getString("coupon_start_time")); // 优惠券开始时间，没有优惠券，则此项不显示
                item.setCoupon_total_count(Integer.parseInt(jsonData.getString("coupon_total_count"))); // 优惠券总量，没有优惠券，则此项不显示
                item.setCoupon_remain_count(Integer.parseInt(jsonData.getString("coupon_remain_count"))); // 优惠券剩余量，没有优惠券，则此项不显示
                item.setCoupon_info(jsonData.getString("coupon_info")); // 优惠券信息，没有优惠券，则此项不显示
                item.setCoupon_end_time(jsonData.getString("coupon_end_time")); // 	优惠券有效期截止日期，没有优惠券，则此项不显示
                url = item.getCoupon_click_url();
            } else {
                url = item.getItem_url();
            }

            if(item.isHas_coupon()) {
                float a = Float.parseFloat(item.getZk_final_price());
                float b = Float.parseFloat(item.getYouHuiQuan());
                float c = Float.parseFloat(item.getQuanLimit());
                float d;
                if(a >= c){
                    d = a-b;
                } else {
                    d = a;
                }
                DecimalFormat df = new DecimalFormat("#.00");
                String price = df.format(d);
                message.obj = "商品标题：" + item.getTitle()
                        + "\n优惠券：" + item.getYouHuiQuan() + " 元"
                        + "\n抢购价：" + price + " 元"
                        + "\n月销量：" + item.getVolume()
                        + "\n淘口令：" + item.getTpwd()
                        + "\n抢购点：" + item.getShort_url();
            } else {
                message.obj = "商品标题：" + item.getTitle()
                        + "\n抢购价：" + item.getZk_final_price() + " 元"
                        + "\n月销量：" + item.getVolume()
                        + "\n淘口令：" + item.getTpwd()
                        + "\n抢购点：" + item.getShort_url();
            }
            message.what = 0;
            handle.sendMessage(message);
        }
    }

    //加载图片
    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);//读取图像数据
            //读取文本数据
            //byte[] buffer = new byte[100];
            //inputStream.read(buffer);
            //text = new String(buffer);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    private Handler handle = new Handler() {
        public void handleMessage(@org.jetbrains.annotations.NotNull Message msg) {
            this.obtainMessage();
            tv_text.setMovementMethod(ScrollingMovementMethod.getInstance());
            switch (msg.what) {
                case 0:
                    tv_text.setText(msg.obj.toString());
                    Button btn_turn = findViewById(R.id.btn_turn);
                    btn_turn.setEnabled(false);
                    btn_turn.setText("←请点击");
                    if(ok){
                        Toast.makeText(TurnActivity.this, "转链完成！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TurnActivity.this, "推广ID有误，请重新设置，此次转链未使用推广ID，将不能获得佣金！", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 1:
                    iv_pic.setImageBitmap((Bitmap) msg.obj);
                    break;
            }
        };
    };
}
