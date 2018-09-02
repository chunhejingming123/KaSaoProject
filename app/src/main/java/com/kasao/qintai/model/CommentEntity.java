package com.kasao.qintai.model;

import java.util.List;

public class CommentEntity {
    public String id;
    public String title;
    public String create_time;
    public String u_id;
    public String nickname;
    public String user_img;
    public String parent_id;
    public String user_mobile;
    public String rm_id;
    public List<CommentEntity> child;
    public boolean isMine;
    //评论类型 1 问题  2 回复(评论);
    public int type;

    public boolean isComment() {
        if (2 == type) {
            return true;
        }
        return false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

}
