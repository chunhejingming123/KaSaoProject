package com.kasao.qintai.model.domain;

import com.kasao.qintai.model.BrowseEntity;
import com.kasao.qintai.model.CommentEntity;
import com.kasao.qintai.model.RtnSuss;
import com.kasao.qintai.model.SNSEntity;
import com.kasao.qintai.model.ThumbModel;

import java.util.List;

/**
 * Created by Administrator on 2017/10/17.
 */

public class CommentLikedomain extends RtnSuss {
    public SNSEntity detail;
    public List<ThumbModel> likes;
    public List<CommentEntity> data;
    public List<BrowseEntity> browse;
    public String likescount;
    public String browsecount;
}
