package com.zty.Dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dynamic
{
    private int uid;
    private String timestamp;
    private int type;
    private String dynamic_id;
    private int view;
    private int repost;
    private int comment;
    private int like;
    private String bvid;
    private String title;
}
