package com.example.administrator.meetingtaste.model;

/**
 * Created by Administrator on 2018/1/3.
 */

public class ArticalAndUser
{
    final public static String ARTICAL_USER_TABLE = "artical_user";


    public Artical artical;
    public User user;

    public ArticalAndUser(){}

    public Artical getArtical() {
        return artical;
    }

    public void setArtical(Artical artical) {
        this.artical = artical;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
