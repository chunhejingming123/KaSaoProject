package com.kasao.qintai.widget.photo;

import java.io.Serializable;
import java.util.List;

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/31 0031:15
 */

public class ImageBucket implements Serializable {
    private static final long serialVersionUID = 5725207191529658858L;
    public int count = 0;
    public String bucketName;
    public List<ImageItem> imageList;
    public boolean isSelected = false;
}
