package com.getcoupon.tools;

public class Item {

    private boolean has_coupon;
    private String item_url;
    private long item_id;
    private String coupon_click_url;
    private int category_id;
    private String tpwd;
    private String short_url;
    private String title;
    private int volume;
    private String pict_url;

    private String quanLimit;
    private String youHuiQuan;
    private String max_commission_rate;
    private int coupon_type;
    private String coupon_start_time;
    private int coupon_total_count;
    private int coupon_remain_count;
    private String coupon_info;
    private String coupon_end_time;
    private String zk_final_price;

    private String document;

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public void setTpwd(String tpwd) {
        this.tpwd = tpwd;
    }

    public void setZk_final_price(String zk_final_price) {
        this.zk_final_price = zk_final_price;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setShort_url(String short_url) {
        this.short_url = short_url;
    }

    public void setCoupon_click_url(String coupon_click_url) {
        this.coupon_click_url = coupon_click_url;
    }

    public void setPict_url(String pict_url) {
        this.pict_url = pict_url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHas_coupon(boolean has_coupon) {
        this.has_coupon = has_coupon;
    }

    public void setItem_id(long item_id) {
        this.item_id = item_id;
    }

    public void setItem_url(String item_url) {
        this.item_url = item_url;
    }

    public void setCoupon_end_time(String coupon_end_time) {
        this.coupon_end_time = coupon_end_time;
    }

    public void setCoupon_info(String coupon_info) {
        this.coupon_info = coupon_info;
    }

    public void setCoupon_remain_count(int coupon_remain_count) {
        this.coupon_remain_count = coupon_remain_count;
    }

    public void setCoupon_start_time(String coupon_start_time) {
        this.coupon_start_time = coupon_start_time;
    }

    public void setCoupon_total_count(int coupon_total_count) {
        this.coupon_total_count = coupon_total_count;
    }

    public void setCoupon_type(int coupon_type) {
        this.coupon_type = coupon_type;
    }

    public void setMax_commission_rate(String max_commission_rate) {
        this.max_commission_rate = max_commission_rate;
    }

    public void setQuanLimit(String quanLimit) {
        this.quanLimit = quanLimit;
    }

    public String getType() {
        switch(coupon_type) {
            case 1:
                return "公开券";
            case 2:
                return "私有券";
            default:
                return "妈妈券";
        }
    }

    public String getPict_url() {
        return pict_url;
    }

    public int getVolume() {
        return volume;
    }

    public String getZk_final_price() {
        return zk_final_price;
    }

    public String getTitle() {
        return title;
    }

    public void setYouHuiQuan(String youHuiQuan) {
        this.youHuiQuan = youHuiQuan;
    }

    public String getCoupon_end_time() {
        return coupon_end_time;
    }

    public int getCoupon_remain_count() {
        return coupon_remain_count;
    }

    public int getCoupon_total_count() {
        return coupon_total_count;
    }

    public int getCoupon_type() {
        return coupon_type;
    }

    public String getCoupon_info() {
        return coupon_info;
    }

    public String getCoupon_start_time() {
        return coupon_start_time;
    }

    public String getMax_commission_rate() {
        return max_commission_rate;
    }

    public String getQuanLimit() {
        return quanLimit;
    }

    public String getYouHuiQuan() {
        return youHuiQuan;
    }

    public int getCategory_id() {
        return category_id;
    }

    public String getTpwd() {
        return tpwd;
    }

    public String getShort_url() {
        return short_url;
    }

    public long getItem_id() {
        return item_id;
    }

    public String getCoupon_click_url() {
        return coupon_click_url;
    }

    public String getItem_url() {
        return item_url;
    }

    public boolean isHas_coupon() {
        return has_coupon;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getDocument() {
        return document;
    }
}
