package com.zty.Service;

import com.zty.Dao.Dynamic;
import com.zty.Dao.Video;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

@Component
@Data
public class Search
{

    private List<Dynamic> videoList;
    private Integer mid;
    private String author;
    @Autowired
    private FromMysql fromMysql;
    @Autowired
    private FromWeb fromWeb;
    @Autowired
    private VideoData videoData;
    @Async
    public Future<List<Dynamic>> search(){
        this.videoList=this.videoData.upload(this.mid);
        return new AsyncResult<>(this.videoList);
    }
}
