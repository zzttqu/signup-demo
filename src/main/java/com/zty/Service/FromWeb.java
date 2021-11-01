package com.zty.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zty.Dao.Dynamic;
import com.zty.Dao.Uploader;
import com.zty.Dao.Video;
import com.zty.mapper.MainMapper;
import lombok.Data;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Data
public class  FromWeb
{
    @Autowired
    MainMapper mainMapper;
    private String next = "0";

    private static Map<String, String> Header() {
        Map<String, String> header = new HashMap<String, String>();
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/91.0.4472.124 Safari/537.36");
        header.put("Accept", "  text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        header.put("Accept-Language", "zh-cn,zh;q=0.5");
        header.put("Accept-Charset", "UTF-8;q=0.7,*;q=0.7");
        header.put("Connection", "keep-alive");
        return header;
    }

    public void DynamicFromWeb(Integer mid) {
        for (int i = 0; i < 50; i++)
        {
            //这个是动态全部
            String url = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/space_history?visitor_uid=0&host_uid=" + mid + "&offset_dynamic_id=" + this.next + "&need_top=1&platform=web";
            Connection connection = Jsoup.connect(url);
            //这个是根据mid找up主粉丝信息 vmid后边是uid
            //https://api.bilibili.com/x/relation/stat?vmid=1&jsonp=jsonp
            //这个是根据mid找up主名字
            //http://api.bilibili.com/x/space/acc/info?mid=1
            //这个是根据bvid找视频
            //https://api.bilibili.com/x/web-interface/view?bvid=BV1Vg41157wz
            connection.headers(Header());
            Connection.Response a = null;
            try
            {
                a = connection.ignoreContentType(true).execute();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                Thread.sleep((int) (200 + Math.random() * (300 - 200 + 1)));
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            String b = a.body();
            JSONArray data = JSONObject.parseObject(b).
                    getJSONObject("data").getJSONArray("cards");
            if (data == null)
            {
                break;
            }
            next = JSONObject.parseObject(b).
                    getJSONObject("data").getString("next_offset");
            Dynamic dynamic = new Dynamic();
            Video video = new Video();
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
                    connection1.headers(Header());
                    String e = null;
                    try
                    {
                        e = connection1.ignoreContentType(true).execute().body();
                    } catch (IOException ioException)
                    {
                        ioException.printStackTrace();
                    }
                    JSONObject data1 = JSONObject.parseObject(e).
                            getJSONObject("data");
                    dynamic.setTitle(data1.getString("title"));
                    dynamic.setComment(Integer.parseInt(data1.getJSONObject("stat").getString("reply")));
                    dynamic.setLike(Integer.parseInt(data1.getJSONObject("stat").getString("like")));
                    dynamic.setView(Integer.parseInt(data1.getJSONObject("stat").getString("view")));
                    dynamic.setRepost(Integer.parseInt(data1.getJSONObject("stat").getString("share")));
                    video.setMid(dynamic.getUid());
                    video.setLike(dynamic.getLike());
                    video.setBvid(dynamic.getBvid());
                    video.setTitle(dynamic.getTitle());
                    video.setPlay(dynamic.getView());
                    video.setComment(dynamic.getComment());
                    video.setCreated(dynamic.getTimestamp());
                    mainMapper.addVideo(video);
                }
                else if (dynamic.getType() == 64)
                {
                    url = "https://api.bilibili.com/x/article/viewinfo?id=" + raw.getJSONObject("desc").getString("rid") + "&mobi_app=pc&from=web";
                    Connection connection2 = Jsoup.connect(url);

                    connection2.headers(Header());
                    String e = null;
                    try
                    {
                        e = connection2.ignoreContentType(true).execute().body();
                    } catch (IOException ioException)
                    {
                        ioException.printStackTrace();
                    }
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
                    connection3.headers(Header());
                    String e = null;
                    try
                    {
                        e = connection3.ignoreContentType(true).execute().body();
                    } catch (IOException ioException)
                    {
                        ioException.printStackTrace();
                    }
                    JSONObject data3 = JSONObject.parseObject(e).
                            getJSONObject("data");
                    dynamic.setTitle(data3.getString("title"));
                    dynamic.setComment(Integer.parseInt(data3.getJSONObject("statistic").getString("comment")));
                    dynamic.setView(Integer.parseInt(data3.getJSONObject("statistic").getString("play")));
                    dynamic.setRepost(Integer.parseInt(data3.getJSONObject("statistic").getString("share")));
                }
                System.out.println(dynamic);
                mainMapper.addDynamic(dynamic);
            }
            try
            {
                Thread.sleep((int) (100 + Math.random() * (300 - 100 + 1)));
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void UploaderFromWeb(Integer mid) {
        System.out.println(Thread.currentThread().getName() + "：开始DownloadMethod业务");
        long startTime = System.currentTimeMillis();
        //这个是根据mid找up主粉丝信息 vmid后边是uid
        //https://api.bilibili.com/x/relation/stat?vmid=1&jsonp=jsonp
        //这个是根据mid找up主名字
        //http://api.bilibili.com/x/space/acc/info?mid=1
        String url = "https://api.bilibili.com/x/relation/stat?vmid=" + mid + "&jsonp=jsonp";
        Connection connection = Jsoup.connect(url);
        connection.headers(Header());
        Connection.Response a = null;
        Uploader uploader = new Uploader();
        try
        {
            a = connection.ignoreContentType(true).execute();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        String b = a.body();
        uploader.setFollower(Integer.parseInt(JSONObject.parseObject(b).
                getJSONObject("data").getString("follower")));
        uploader.setMid(Integer.parseInt(JSONObject.parseObject(b).
                getJSONObject("data").getString("mid")));
        url = "https://api.bilibili.com/x/space/acc/info?mid=" + mid;
        Connection connection2 = Jsoup.connect(url);
        connection2.headers(Header());
        Connection.Response c = null;
        try
        {
            c = connection2.ignoreContentType(true).execute();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            Thread.sleep((int) (200 + Math.random() * (300 - 200 + 1)));
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        String d = c.body();
        uploader.setAuthor(JSONObject.parseObject(d).
                getJSONObject("data").getString("name"));
        mainMapper.addUploader(uploader);
        long endTime = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + "：DownloadMethod业务结束，耗时：" + (endTime - startTime));
    }

    public String VideoFromWeb(Integer mid) {
        System.out.println(Thread.currentThread().getName() + "：开始DownloadVideo业务");
        long startTime = System.currentTimeMillis();
        int pn = 1;
        do
        {
            String url = "https://api.bilibili.com/x/space/arc/search?mid=" + mid + "&ps=100&tid=0&pn=" + pn + "&keyword=&order=pubdate&jsonp=jsonp";
            Connection connection = Jsoup.connect(url);
            connection.headers(Header());
            String a = null;
            try
            {
                a = connection.ignoreContentType(true).execute().body();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            String c = JSONObject.parseObject(a).getJSONObject("data").getJSONObject("list").getString("vlist");
            if (c.equals("[]"))
            {
                if (pn==1){
                    return "noVideo";
                }
                break;
            }
            String object = JSONObject.parseObject(a).
                    getJSONObject("data").getJSONObject("list").getString("vlist");
            List<Video> abc = JSON.parseArray(object, Video.class);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Video video : abc)
            {
                String time0 = sdf.format(Long.parseLong(video.getCreated()) * 1000);
                video.setCreated(time0);
                video.setMid(mid);
            }
            pn++;
            for (Video video : abc)
            {
                mainMapper.addVideo(video);
            }
            try
            {
                Thread.sleep((int) (300 + Math.random() * (400 - 300 + 1)));
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        } while (true);
        long endTime = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + "：DownloadVideo业务结束，耗时：" + (endTime - startTime));
        return "OK";
    }

    public void HotFromWeb(){

    }
}
