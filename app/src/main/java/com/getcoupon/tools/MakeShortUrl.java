package com.getcoupon.tools;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

public class MakeShortUrl {
    public String getShortUrlByHtml(String url) throws Exception {
        URL u = new URL("https://api.d5.nz/api/dwz/tcn.php?url=" + url);
        InputStream in = u.openStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte buf[] = new byte[1024];
            int read = 0;
            while ((read = in .read(buf)) > 0) {
                out.write(buf, 0, read);
            }
        } finally {
            if ( in != null) {
                in .close();
            }
        }
        byte b[] = out.toByteArray();
        return (new String(b, "utf-8"));
    }

    public String getShortUrlByJson(String url) throws Exception {
        String u = "https://api.d5.nz/api/dwz/tcn.php?url=";

        Json json = new Json();
        String sj = json.getJson( u + url);

        JSONObject jsonObject = new JSONObject(sj);
        String str = jsonObject.getString("url");

        return str;
    }
}
