package com.getcoupon.api;

import android.widget.Toast;

public class MiaoYouQuan {

    private final String url_all_purpose = "http://api.uiuk.cn/taobao/highTurnByAll.php";
    private final String url_build_tkl = "http://api.uiuk.cn/taobao/createTaoPwd.php";
    private final String url_get_info = "http://api.uiuk.cn/taobao/getItemInfo.php";
    private final String url_get_agent_info = "http://api.uiuk.cn/taobao/getAgentInfo.php";
    private String key = "c90dbafa-95f2-e430-8b58-a085740c9e5c";
    private String tkl = "";
    private String pid = "mm_129779402_46770368_109694000456";
    private String tbname = "mc陌宇";
    private String content = "";
    private long relationID = 0;
    private String itemUrl = "";
    private String title = "";
    private String picUrl = "";
    private String tpwd = "";
    private String extsearch = "";
    private String shorturl = "";
    private long itemId = 0l;

    public MiaoYouQuan() {}

    public MiaoYouQuan(String key) {
        this.key = key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTkl(String tkl) {
        this.tkl = tkl;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTbname(String tbname) {
        this.tbname = tbname;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTpwd(String tpwd) {
        this.tpwd = tpwd;
    }

    public void setExtsearch(String extsearch) {
        this.extsearch = extsearch;
    }

    public void setShorturl(String shorturl) {
        this.shorturl = shorturl;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public void setRelationID(long relationID) {
        this.relationID = relationID;
    }

    public String getUrl_all_purpose() {
        String url = url_all_purpose + "?apkey=" + key + "&tbname=" + tbname + "&pid=" + pid + "&content=" + content;
        if("1".equals(tpwd)){
            url = url + "&tpwd=1";
        }
        if("1".equals(extsearch)){
            url = url + "&extsearch=1";
        }
        if("1".equals(shorturl)){
            url = url + "&shorturl=1";
        }
        if(relationID != 0){
            url = url + "&relationid=" + relationID;
        }
        return url;
    }

    public String getUrl_agent_info() {
        String url = url_get_agent_info + "?apkey=" + key + "&tbname=" + tbname + "&info_type=1&relation_app=common";
        return url;
    }

    public long getRelationID() {
        return relationID;
    }

    public String getUrl_build_tkl() {
        return url_build_tkl + "?apkey=" + key + "&url=" + itemUrl + "&title=" + title + "&pic=" + picUrl;
    }

    public String getUrl_build_tkl_ignore_pic() {
        return url_build_tkl + "?apkey=" + key + "&url=" + itemUrl + "&title=" + title;
    }

    public String getUrl_get_info() {
        return url_get_info + "?apkey=" + key + "&itemid=" + itemId;
    }
}
