package com.zty.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zty.Dao.Dynamic;
import com.zty.Dao.Hot;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class testController
{
    public static void main(String[] args) throws IOException {
        String next = "0";
        int mid = 33540542;
        String comment;
        for (int i = 0; i < 10; i++)
        {
            //这个是动态全部
            String url = "https://www.bilibili.com/v/popular/rank/all";
            Connection connection = Jsoup.connect(url);
            //这个是根据mid找up主粉丝信息 vmid后边是uid
            //https://api.bilibili.com/x/relation/stat?vmid=1&jsonp=jsonp
            //这个是根据mid找up主名字
            //http://api.bilibili.com/x/space/acc/info?mid=1
            //这个是根据bvid找视频
            //https://api.bilibili.com/x/web-interface/view?bvid=BV1Vg41157wz
            Map<String, String> header = new HashMap<String, String>();
            header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/91.0.4472.124 Safari/537.36");
            header.put("Accept", "  text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            header.put("Accept-Language", "zh-cn,zh;q=0.5");
            header.put("Accept-Charset", "UTF-8;q=0.7,*;q=0.7");
            header.put("Connection", "keep-alive");
            connection.headers(header);
            Connection.Response a = null;
            a = connection.ignoreContentType(true).execute();
            try
            {
                Thread.sleep((int) (500 + Math.random() * (600 - 500 + 1)));
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            Document document = connection.ignoreContentType(true).get();
            Element element = document.getElementsByClass("rank-list").get(0).getElementsByClass("rank-item").get(1).getElementsByTag("a").get(1);
            String p = element.attr("href").substring(25);
            String b = a.body();
            JSONArray data = JSONObject.parseObject(b).
                    getJSONObject("data").getJSONArray("cards");
            if (data == null)
            {
                break;
            }
            System.out.println(data.size());
            next = JSONObject.parseObject(b).
                    getJSONObject("data").getString("next_offset");
            Dynamic dynamic = new Dynamic();
            int c = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (int j = 0; j < data.size(); j++)
            {
                JSONObject raw = data.getJSONObject(c);
                dynamic = data.getJSONObject(c).getObject("desc", Dynamic.class);
                String time0 = sdf.format(Long.parseLong(dynamic.getTimestamp()) * 1000);
                dynamic.setTimestamp(time0);
                c++;
                String bvid = dynamic.getBvid();
                if (dynamic.getType() == 8)
                {
                    url = "https://api.bilibili.com/x/web-interface/view?bvid=" + bvid;
                    Connection connection1 = Jsoup.connect(url);
                    Map<String, String> header1 = new HashMap<String, String>();
                    header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/91.0.4472.124 Safari/537.36");
                    header.put("Accept", "  text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                    header.put("Accept-Language", "zh-cn,zh;q=0.5");
                    header.put("Accept-Charset", "UTF-8;q=0.7,*;q=0.7");
                    header.put("Connection", "keep-alive");
                    connection1.headers(header1);
                    String e = connection1.ignoreContentType(true).execute().body();
                    JSONObject data1 = JSONObject.parseObject(e).
                            getJSONObject("data");
                    dynamic.setTitle(data1.getString("title"));
                    dynamic.setComment(Integer.parseInt(data1.getJSONObject("stat").getString("reply")));
                    dynamic.setLike(Integer.parseInt(data1.getJSONObject("stat").getString("like")));
                    dynamic.setView(Integer.parseInt(data1.getJSONObject("stat").getString("view")));
                    dynamic.setRepost(Integer.parseInt(data1.getJSONObject("stat").getString("share")));
                }
                else if (dynamic.getType() == 64)
                {
                    url = "https://api.bilibili.com/x/article/viewinfo?id=" + raw.getJSONObject("desc").getString("rid") + "&mobi_app=pc&from=web";
                    Connection connection2 = Jsoup.connect(url);
                    Map<String, String> header1 = new HashMap<String, String>();
                    header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/91.0.4472.124 Safari/537.36");
                    header.put("Accept", "  text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                    header.put("Accept-Language", "zh-cn,zh;q=0.5");
                    header.put("Accept-Charset", "UTF-8;q=0.7,*;q=0.7");
                    header.put("Connection", "keep-alive");
                    connection2.headers(header1);
                    String e = connection2.ignoreContentType(true).execute().body();
                    JSONObject data2 = JSONObject.parseObject(e).
                            getJSONObject("data");
                    dynamic.setTitle(data2.getString("title"));
                    dynamic.setComment(Integer.parseInt(data2.getJSONObject("stats").getString("reply")));
                    dynamic.setLike(Integer.parseInt(data2.getJSONObject("stats").getString("like")));
                    dynamic.setView(Integer.parseInt(data2.getJSONObject("stats").getString("view")));
                    dynamic.setRepost(Integer.parseInt(data2.getJSONObject("stats").getString("share")));
                }
                else if (dynamic.getType() == 256)
                {
                    url = "https://www.bilibili.com/audio/music-service-c/web/song/info?sid=" + raw.getJSONObject("desc").getString("rid") + "&mobi_app=pc&from=web";
                    Connection connection3 = Jsoup.connect(url);
                    Map<String, String> header1 = new HashMap<String, String>();
                    header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/91.0.4472.124 Safari/537.36");
                    header.put("Accept", "  text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                    header.put("Accept-Language", "zh-cn,zh;q=0.5");
                    header.put("Accept-Charset", "UTF-8;q=0.7,*;q=0.7");
                    header.put("Connection", "keep-alive");
                    connection3.headers(header1);
                    String e = connection3.ignoreContentType(true).execute().body();
                    JSONObject data3 = JSONObject.parseObject(e).
                            getJSONObject("data");
                    dynamic.setTitle(data3.getString("title"));
                    dynamic.setComment(Integer.parseInt(data3.getJSONObject("statistic").getString("comment")));
                    dynamic.setView(Integer.parseInt(data3.getJSONObject("statistic").getString("play")));
                    dynamic.setRepost(Integer.parseInt(data3.getJSONObject("statistic").getString("share")));
                }
                System.out.println(dynamic);
            }
        }
    }

}

class bilibili
{
    public static void main(String[] args) {
        String url = "https://static-data.eol.cn/www/2.0/school/"
                + 31 +
                "/info.json";
        Connection connection1 = Jsoup.connect(url);
        Map<String, String> header = new HashMap<String, String>();
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/91.0.4472.124 Safari/537.36");
        header.put("Accept", "  text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        header.put("Accept-Language", "zh-cn,zh;q=0.5");
        header.put("Accept-Charset", "UTF-8;q=0.7,*;q=0.7");
        header.put("Connection", "keep-alive");
        connection1.headers(header);
        String e = null;
        try
        {
            e = connection1.ignoreContentType(true).execute().body();
        } catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
        JSONObject data = JSONObject.parseObject(e).
                getJSONObject("data");
        System.out.println(data);
    }
}

class zhihu
{
    public static void main(String[] args) {
        String url = "https://www.zhihu.com/hot";
        Connection connection = Jsoup.connect(url);
        Map<String, String> header = new HashMap<String, String>();
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/91.0.4472.124 Safari/537.36");
        header.put("Accept", "  text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        header.put("Accept-Language", "zh-cn,zh;q=0.5");
        header.put("Accept-Charset", "UTF-8;q=0.7,*;q=0.7");
        header.put("Connection", "keep-alive");
        header.put("cookie", "_zap=3a0acd5d-2403-469e-8808-a1edeead7f81; d_c0=\"AACVlzqPMBKPTmJPMO6NrrT24RIKhP1s4xo=|1605276417\"; _xsrf=5e2b1e5f-4f8a-4d1d-b922-424034b4187a; l_n_c=1; n_c=1; q_c1=8ff34b48d3794e8dac36290228acfa29|1619003028000|1605709039000; __utma=51854390.1292089515.1619003031.1619003031.1619003031.1; __utmz=51854390.1619003031.1.1.utmcsr=zhuanlan.zhihu.com|utmccn=(referral)|utmcmd=referral|utmcct=/p/205008885; __utmc=51854390; __utmv=51854390.000--|3=entry_date=20201118=1; _9755xjdesxxd_=32; YD00517437729195%3AWM_TID=9XarrwV5IYtFUVBQEAZ%2F0%2FrLySeH1OCk; __snaker__id=ZEpzIxRQafm6uJmh; gdxidpyhxdE=8SaJHxW0Rnd11IJ224fObZYuLRR%2FglUbrHO0pTJlV0DVUxC6D6RlmyIfkebotYGCU%2ByC%5CN76mCXKwDQY1npHJc0jJSBAwasDuz%5Cb9QMMNc5oGN53G%5CPVUm9ewO8j6Un4kTCxnQ%5CifaOc56ktcpNJcUS%2BzANERGq%2FqdaIGpnsaW%2BKAtAJ%3A1627389650461; YD00517437729195%3AWM_NI=hw8gw%2FP4Y%2BPvnm8IvwgdB8dRpMWcVxDN6tBSuOva2UrlD5QNS4Yx26%2FdIkP%2B%2B0tzJvHfHg9qb18m4Dt9rTC8Iopd4ldTDJXHe9AYpckq%2Bvc7tGYjPhEKvECq3DxUwkoeRk8%3D; YD00517437729195%3AWM_NIKE=9ca17ae2e6ffcda170e2e6eeb1f54583f0bab3cf6ea29a8aa7d54a878a8fbbae63a7b9be92ca6e8bebf9d3f12af0fea7c3b92aaca9f888f62594ec97ccd37eaeb3f8aaf57da5bc8ed9e75ab4939984d5748892fa95c159b791ba82b15fb2b48cd6d44691ec9799b13eedbc878cd321e9f0bc9bc97e8cba8888bc7ba2eff88eb750b8e7beb5c2348bb1c085b768a88c8cabdb44a7e78fbaf369a9afe597e23db899978bd3648795baa6c27bf19cb8b6f661adbb9e8cbb37e2a3; z_c0=\"2|1:0|10:1627388780|4:z_c0|92:Mi4xQ1IxakVRQUFBQUFBQUpXWE9vOHdFaVlBQUFCZ0FsVk5iRW50WVFDOGt6N29PVzc3ZXZjYTRySklnelFubDFVaEtR|6e52dc02ad55c889d273e64260f60b52879f5fd8dba4de9cdc9e9991cead4774\"; Hm_lvt_98beee57fd2ef70ccdd5ca52b9740c49=1630079675,1630211941,1630217739,1630232534; Hm_lpvt_98beee57fd2ef70ccdd5ca52b9740c49=1630232534; SESSIONID=5LDESS2bv1mKWu2oFSttBlycaMc3gI6aLt5EjDAPMDX; JOID=VVkXC07MAz2BDwI_T8OHrKoYmAtY_0xzyV5XDie4NFPAVXULIx9VEe8LBzZIIk0b9wKn8LeyuhX8iUD6VzboW-s=; osd=UlgQC0rLAjqBCwU-SMODq6sfmA9f_ktzzVlWCSe8M1LHVXEMIhhVFegKADZMJUwc9wag8bCyvhL9jkD-UDfvW-8=; tst=h; KLBRSID=d017ffedd50a8c265f0e648afe355952|1630232536|1630232530; tshl=");
        connection.headers(header);
        Connection.Response a = null;
        Document document = null;
        try
        {
            document = connection.ignoreContentType(true).get();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        Elements element = document.select(".HotItem-metrics");
        Element element0 = document.select(".HotItem-metrics").get(0);
        Elements element1 = document.select(".HotItem-title");
        for (Element title : element)
        {
            String str = title.text();
            String str2 = "";
            if (str != null && !"".equals(str))
            {
                for (int i = 0; i < str.length(); i++)
                {
                    if (str.charAt(i) >= 48 && str.charAt(i) <= 57)
                    {
                        str2 += str.charAt(i);
                    }
                }
            }
            int str3 = Integer.parseInt(str2);
            System.out.println(str3);
        }
    }
}

class weibo
{
    public static void main(String[] args) {
        String url = "https://s.weibo.com/top/summary";
        Connection connection = Jsoup.connect(url);
        Map<String, String> header = new HashMap<String, String>();
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/91.0.4472.124 Safari/537.36");
        header.put("Accept", "  text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        header.put("Accept-Language", "zh-cn,zh;q=0.5");
        header.put("Accept-Charset", "UTF-8;q=0.7,*;q=0.7");
        header.put("Connection", "keep-alive");
        connection.headers(header);
        Connection.Response a = null;
        Document document = null;
        try
        {
            document = connection.ignoreContentType(true).get();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        for (int i = 0; i < 50; i++)
        {
            if (!document.select(".wbs-hotrank .data td.td-02 span").get(i).text().equals(""))
            {
                Element element = document.select(".wbs-hotrank .data td.td-02 a").get(i + 1);
                Element element1 = document.select(".wbs-hotrank .data td.td-02 span").get(i);
                System.out.println(element.text() + " " + element1.text());
                Hot hot = new Hot();
            }
        }

    }
}