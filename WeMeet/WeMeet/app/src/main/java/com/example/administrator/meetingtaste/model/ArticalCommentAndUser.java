package com.example.administrator.meetingtaste.model;

/**
 * Created by Administrator on 2018/1/4.
 */


public class ArticalCommentAndUser
{
    final public static String ARTICAL_COMMENT_USER_TABLE = "artical_comment_user";

    public User user;
    public ArticalComment articalComment;

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public ArticalComment getArticalComment() {
        return articalComment;
    }
    public void setArticalComment(ArticalComment articalComment) {
        this.articalComment = articalComment;
    }


}
