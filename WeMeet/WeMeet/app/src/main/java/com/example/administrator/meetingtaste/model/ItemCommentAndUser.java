package com.example.administrator.meetingtaste.model;

/**
 * Created by Administrator on 2018/1/4.
 */


public class ItemCommentAndUser
{
    final public static String ITEM_COMMENT_USER_TABLE = "item_comment_user";

    public ItemComment itemComment;
    public User user;



    public ItemComment getItemComment() {
        return itemComment;
    }

    public void setItemComment(ItemComment itemComment) {
        this.itemComment = itemComment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}

