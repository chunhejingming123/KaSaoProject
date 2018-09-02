package com.kasao.qintai.model.domain;

import com.kasao.qintai.model.BrowseEntity;
import com.kasao.qintai.model.RtnSuss;

import java.util.List;

/**
 * Created by Administrator on 2017/11/25.
 * 浏览信息
 */

public class Browsedomain extends RtnSuss {
    public String list_num;
    public List<BrowseEntity> data;
}
