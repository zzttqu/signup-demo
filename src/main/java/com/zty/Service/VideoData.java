package com.zty.Service;

import com.zty.Dao.Dynamic;
import com.zty.Dao.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.List;

@Component
public class VideoData//这个就是我的商品类
{
    private int flag=2;//首先进数据库查
    private List<Dynamic> videoList;//这个就是我的商品
    public synchronized void download(String msg){
        if (flag!=1){
            try
            {
                this.wait();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        System.out.println("下载状态:"+msg);
        this.notifyAll();
        this.flag=3;
    }
    public synchronized void mysql(List<Dynamic> videoList){
        if (flag!=2){
            try
            {
                this.wait();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        this.videoList=videoList;
        if (videoList.size() == 0){
            this.flag=1;
            System.out.println("未查询到该名字");
        }
        else {
            System.out.println("从数据库中获得"+videoList.get(0)+"的视频");
        }
        this.notifyAll();
        this.flag=3;

    }
    public synchronized List<Dynamic> upload(Integer mid){
        if (flag!=3){
            try
            {
                this.wait();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        System.out.println("通知上级取回"+videoList.get(0)+"的视频");
        this.notifyAll();
        this.flag=2;
        return this.videoList;
    }
}
