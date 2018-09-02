package com.kasao.qintai.widget.photo;

import java.io.Serializable;

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/31 0031:15
 */

public class ImageItem implements Serializable {
    private static final long serialVersionUID = -8910838378685472854L;
    public String imageId;
    public String thumbnailPath;
    public String imagePath;
    public boolean isSelected = false;
}
