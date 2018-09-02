package com.kasao.qintai.widget.banner;

import java.util.List;

/**
 * 广告位
 *
 * @auth lipf on 2015/1/26.
 */
public class Banner {
    public int width;
    public int height;
    /**
     * 在单排流插入banner的位置index
     */
    public int position;
    public List<BannerNode> items;

    public String url;
    public String image;

}
