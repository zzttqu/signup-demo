package com.zty.Dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    private int mid;
    private String bvid;
    private String title;
    private int like;
    private int play;
    private int comment;
    private String created;
}