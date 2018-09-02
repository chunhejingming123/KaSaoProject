package com.kasao.qintai.model.domain;

import com.kasao.qintai.model.CarDetailEntity;
import com.kasao.qintai.model.RtnSuss;

import java.util.List;

public class CarListdomain extends RtnSuss {
    public String list_count;
    public int page_num;
    public int page_count;
    public String list_num;
    public List<CarDetailEntity> data;
}
