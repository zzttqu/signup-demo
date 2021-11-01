package com.zty.controller;

import com.zty.Dao.Hot;
import com.zty.Dao.Uploader;
import com.zty.Service.FromWeb;
import com.zty.mapper.MainMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ScheduledTask
{
    private int id = 0;
    private String[] url = {"https://www.zhihu.com/hot", "https://s.weibo.com/top/summary", "https://www.bilibili.com/v/popular/rank/all"};

    private static Map<String, String> Header(int i) {
        if (i == 0)
        {
            Map<String, String> header = new HashMap<String, String>();
            header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/91.0.4472.124 Safari/537.36");
            header.put("Accept", "  text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            header.put("Accept-Language", "zh-cn,zh;q=0.5");
            header.put("Accept-Charset", "UTF-8;q=0.7,*;q=0.7");
            header.put("Connection", "keep-alive");
            return header;
        }
        else
        {
            Map<String, String> header = new HashMap<String, String>();
            header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/91.0.4472.124 Safari/537.36");
            header.put("Accept", "  text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            header.put("Accept-Language", "zh-cn,zh;q=0.5");
            header.put("Accept-Charset", "UTF-8;q=0.7,*;q=0.7");
            header.put("Connection", "keep-alive");
            header.put("cookie", "_zap=3a0acd5d-2403-469e-8808-a1edeead7f81; d_c0=\"AACVlzqPMBKPTmJPMO6NrrT24RIKhP1s4xo=|1605276417\"; _xsrf=5e2b1e5f-4f8a-4d1d-b922-424034b4187a; l_n_c=1; n_c=1; q_c1=8ff34b48d3794e8dac36290228acfa29|1619003028000|1605709039000; __utma=51854390.1292089515.1619003031.1619003031.1619003031.1; __utmz=51854390.1619003031.1.1.utmcsr=zhuanlan.zhihu.com|utmccn=(referral)|utmcmd=referral|utmcct=/p/205008885; __utmc=51854390; __utmv=51854390.000--|3=entry_date=20201118=1; _9755xjdesxxd_=32; YD00517437729195%3AWM_TID=9XarrwV5IYtFUVBQEAZ%2F0%2FrLySeH1OCk; __snaker__id=ZEpzIxRQafm6uJmh; gdxidpyhxdE=8SaJHxW0Rnd11IJ224fObZYuLRR%2FglUbrHO0pTJlV0DVUxC6D6RlmyIfkebotYGCU%2ByC%5CN76mCXKwDQY1npHJc0jJSBAwasDuz%5Cb9QMMNc5oGN53G%5CPVUm9ewO8j6Un4kTCxnQ%5CifaOc56ktcpNJcUS%2BzANERGq%2FqdaIGpnsaW%2BKAtAJ%3A1627389650461; YD00517437729195%3AWM_NI=hw8gw%2FP4Y%2BPvnm8IvwgdB8dRpMWcVxDN6tBSuOva2UrlD5QNS4Yx26%2FdIkP%2B%2B0tzJvHfHg9qb18m4Dt9rTC8Iopd4ldTDJXHe9AYpckq%2Bvc7tGYjPhEKvECq3DxUwkoeRk8%3D; YD00517437729195%3AWM_NIKE=9ca17ae2e6ffcda170e2e6eeb1f54583f0bab3cf6ea29a8aa7d54a878a8fbbae63a7b9be92ca6e8bebf9d3f12af0fea7c3b92aaca9f888f62594ec97ccd37eaeb3f8aaf57da5bc8ed9e75ab4939984d5748892fa95c159b791ba82b15fb2b48cd6d44691ec9799b13eedbc878cd321e9f0bc9bc97e8cba8888bc7ba2eff88eb750b8e7beb5c2348bb1c085b768a88c8cabdb44a7e78fbaf369a9afe597e23db899978bd3648795baa6c27bf19cb8b6f661adbb9e8cbb37e2a3; z_c0=\"2|1:0|10:1627388780|4:z_c0|92:Mi4xQ1IxakVRQUFBQUFBQUpXWE9vOHdFaVlBQUFCZ0FsVk5iRW50WVFDOGt6N29PVzc3ZXZjYTRySklnelFubDFVaEtR|6e52dc02ad55c889d273e64260f60b52879f5fd8dba4de9cdc9e9991cead4774\"; Hm_lvt_98beee57fd2ef70ccdd5ca52b9740c49=1630079675,1630211941,1630217739,1630232534; Hm_lpvt_98beee57fd2ef70ccdd5ca52b9740c49=1630232534; SESSIONID=5LDESS2bv1mKWu2oFSttBlycaMc3gI6aLt5EjDAPMDX; JOID=VVkXC07MAz2BDwI_T8OHrKoYmAtY_0xzyV5XDie4NFPAVXULIx9VEe8LBzZIIk0b9wKn8LeyuhX8iUD6VzboW-s=; osd=UlgQC0rLAjqBCwU-SMODq6sfmA9f_ktzzVlWCSe8M1LHVXEMIhhVFegKADZMJUwc9wag8bCyvhL9jkD-UDfvW-8=; tst=h; KLBRSID=d017ffedd50a8c265f0e648afe355952|1630232536|1630232530; tshl=");
            return header;
        }
    }
    /*LocalDateTime date=LocalDateTime.now();*/
    @Autowired
    private FromWeb fromWeb;
    @Autowired
    private MainMapper mainMapper;
    private Hot hot = new Hot();

    @Scheduled(cron = "0/100 * * * * ?")
    private void scheduledTask1() {
        System.err.println("执行更新视频列表任务，时间: " + LocalDateTime.now());
        List<Uploader> uploaders = mainMapper.queryAllUploader();
        for (Uploader uploader : uploaders)
        {
            try
            {
                Thread.sleep((int) (800 + Math.random() * (900 - 800 + 1)));
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            fromWeb.VideoFromWeb(uploader.getMid());
        }
    }

    @Scheduled(cron = "0/20 * * * * ?")
    private void scheduledTask2() {
        System.out.println(Thread.currentThread().getName() + "：开始热榜更新业务");
        long startTime = System.currentTimeMillis();
        try
        {
            Thread.sleep((int) (100 + Math.random() * (200 - 100 + 1)));
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        this.id = 0;
        zhiHu();
        weiBo();
        bilibili();
        long endTime = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + "：结束热榜更新业务，耗时：" + (endTime - startTime));
    }
    private void bilibili() {
        Connection connection = Jsoup.connect(url[2]);
        connection.headers(Header(0));
        Document document = null;
        try
        {
            document = connection.ignoreContentType(true).get();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        for (int i1 = 0; i1 < 100; i1++)
        {
            this.id++;
            //标题
            String element1 = document.select(".rank-list .rank-item .content .info .title").get(i1).text();
            //播放量
            String element2 = document.select(".rank-list .rank-item .content .info .detail .data-box").get(i1).text();
            //综合得分
            String element3 = document.select(".rank-list .rank-item .content .info .pts div").get(i1).text();
            hot.setId(this.id);
            hot.setTitle(element1);
            hot.setHotscore(Integer.parseInt(element3));
            hot.setWeb("哔哩哔哩");
            mainMapper.addHot(hot);
            Element element = document.getElementsByClass("rank-list").get(0).getElementsByClass("rank-item").get(i1).getElementsByTag("a").get(1);
            String bvid = element.attr("href").substring(25);
        }
        Element element = document.getElementsByClass("rank-list").get(0).getElementsByClass("rank-item").get(1).getElementsByTag("a").get(1);
        String bvid = element.attr("href").substring(25);
    }

    private void weiBo() {
        Connection connection = Jsoup.connect(url[1]);
        connection.headers(Header(0));
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
                this.id++;
                Element element = document.select(".wbs-hotrank .data td.td-02 a").get(i + 1);
                Element element1 = document.select(".wbs-hotrank .data td.td-02 span").get(i);
                hot.setTitle(element.text());
                hot.setHotscore(Integer.parseInt(element1.text()));
                hot.setWeb("微博");
                hot.setId(this.id);
                mainMapper.addHot(hot);
            }
        }
    }

    private void zhiHu() {
        Connection connection = Jsoup.connect(url[0]);
        connection.headers(Header(1));
        Document document = null;
        try
        {
            document = connection.ignoreContentType(true).get();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        Elements element = document.select(".HotItem-metrics");
        Elements element1 = document.select(".HotItem-title");
        for (int i = 0; i < 50; i++)
        {
            this.id++;
            String score = element.get(i).text();
            String score2 = "";
            if (score != null && !"".equals(score))
            {
                for (int j = 0; j < score.length(); j++)
                {
                    if (score.charAt(j) >= 48 && score.charAt(j) <= 57)
                    {
                        score2 += score.charAt(j);
                    }
                }
            }
            String title = element1.get(i).text();
            int score3 = Integer.parseInt(score2);
            hot.setHotscore(score3);
            hot.setTitle(title);
            hot.setId(this.id);
            hot.setWeb("知乎");
            mainMapper.addHot(hot);
        }

    }
}
