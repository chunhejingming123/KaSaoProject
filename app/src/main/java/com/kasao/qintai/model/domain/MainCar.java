package com.kasao.qintai.model.domain;

import com.kasao.qintai.model.BannerEntity;
import com.kasao.qintai.model.CarBrand;
import com.kasao.qintai.model.CarDetailEntity;
import com.kasao.qintai.model.Car_Class;

import java.util.List;

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/21 0021:15
 */

public class MainCar {
    public List<BannerEntity> carbanner;
    public List<CarBrand> brand;
    public List<CarDetailEntity> cargoods;
    public List<Car_Class> car_class;
    public List<CarDetailEntity> rank;
    public List<String> carphone;
}
