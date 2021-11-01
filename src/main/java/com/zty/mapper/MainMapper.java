package com.zty.mapper;

import com.zty.Dao.Dynamic;
import com.zty.Dao.Hot;
import com.zty.Dao.Uploader;
import com.zty.Dao.Video;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository//表示在spring里边的
public interface MainMapper
{
    List<Dynamic> queryDynamicByMid(int mid);
    List<Video> queryVideoByMid(int mid);
    List<Uploader> queryAllUploader();
    List<Hot> queryHot(String web);
    Uploader queryUploaderByMid(int mid);
    void addDynamic(Dynamic dynamic);
    void addUploader(Uploader uploader);
    void addVideo(Video video);
    void addSuggestion(String s);
    void addHot(Hot hot);
}
