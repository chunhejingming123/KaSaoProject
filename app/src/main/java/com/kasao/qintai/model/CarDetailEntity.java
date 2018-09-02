package com.kasao.qintai.model;

import java.io.Serializable;
import java.util.List;

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/21 0021:15
 */

public class CarDetailEntity implements Serializable {
    public String goods_id; //257",
    public String brand_id;
    public String name; //奥迪Q5",
    public String goods_img; //",
    public String goods_jingle; //了解了",
    public String goods_price; //490000.00",
    public String goods_market_price; //500000.00",
    public String storage; //100",
    public String goods_body;
    public String goods_state;
    public String goods_status;
    public String created_at;
    public String updated_at;
    public String deleted_at;
    public String fxh;
    public String ry;
    public String kt;
    public String inphone;
    public boolean fav_type;// 是否搜藏过
    public boolean browse_type;// 是否浏览过
    public List<CarImage> img_list;
    public List<CarImage> surface;
    public List<CarImage> chassis;
    public List<CarDetailEntity> recommend_list;
    public String drive;
    public String purpose;
    public String notice;
    public String subscribecount;//预订人数
    public List<CarParmeterKeyValue> target;

    public String tuijian; //1",
    public String moto; //CA6SM2-35E5N",
    public String tank; //",
    public String gearbox; //CA12TA160M",
    public String wheelbase; //4300+1350",
    public String frontaxle; //",
    public String rearaxle; //",
    public String frame; //",
    public String tyre; //12R22.5",
    public String cab; //",
    public String cid; //5",
    public String other; //",
    public String gps; //",
    public String conditing; //自动",
    public String ggxh; //CA3250P66L2T1E24M5",
    public String lx; //",
    public String qdxs; //6X4",
    public String xcje; //",
    public String fdj; //CA6SM2-35E5N",
    public String bsx; //CA12TA160M",
    public String cscd; //9.05米",
    public String cskd; //2.5米",
    public String csgd; //3.3米",
    public String lj; //",
    public String zczl; //12.43吨",
    public String edzz; //12.375吨",
    public String zzl; //25吨",
    public String zgcs; //",
    public String cd; //吉林长春",
    public String dwjb; //重卡",
    public String jjj; //24\/20",
    public String xb; //",
    public String qhx; //",
    public String fdjsc; //",
    public String qgs; //6",
    public String rlzl; //液化天然气(LNG)",
    public String qgplxs; //",
    public String pl; //11.04L",
    public String pfbz; //国五\/欧五",
    public String zdscgl; //261kW",
    public String zdml; //350马力",
    public String zvrs; //",
    public String zwps; //",
    public String hdfs; //",
    public String dds; //2个",
    public String lts; //10个",
    public String qqxxzh; //",
    public String cjgd; //",
    public String hqxxzh; //18000kg",
    public String thps; //",
    public String bid; //5",
    public String bname; //自卸车
    public List<CarImage> images;
    public String priceblock;
}
