package com.kasao.qintai.model;

import java.io.Serializable;

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/27 0027:09
 */

public class SNSEntity implements Serializable {
    public String id;
    public String img;
    public String title;
    public String nackname;
    public String u_img;
    public String likes_count;
    public String review_count;
    public String create_time;
    public String type="1";
    public String u_id;
    public String browse_count;
    public String new_create_time;
    public  boolean browse_type;

    private int lookcount;
}
