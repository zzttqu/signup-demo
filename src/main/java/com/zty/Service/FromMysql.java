package com.zty.Service;

import com.zty.Dao.Hot;
import com.zty.Dao.Video;
import com.zty.mapper.MainMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class FromMysql
{
    @Autowired
    private MainMapper mainMapper;

    public List<Video> fromMysql(int mid){
        if (mid != 0)
        {
            List<Video> videoList = mainMapper.queryVideoByMid(mid);
            return videoList;
        }
        else {
            return null;
        }

    }
    public List<Hot> HotFromMysql(String web){
        List<Hot> hots=mainMapper.queryHot(web);
        return hots;
    }
}
