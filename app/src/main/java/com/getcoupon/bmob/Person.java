package com.getcoupon.bmob;

import cn.bmob.v3.BmobUser;

public class Person extends BmobUser {
    private String relationId; // 关系id
    private String commission; // 余额
    private String pw; // 备用密码

    // Setter方法

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public void setRemain(String remain) {
        this.commission = commission;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    // Getter方法

    public String getRelationId() {
        return relationId;
    }

    public String getRemain() {
        return commission;
    }

    public String getPw() {
        return pw;
    }
}
