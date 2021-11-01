package com.zty.Dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hot
{
    private int id;
    private String web;
    private String title;
    private int hotscore;
}