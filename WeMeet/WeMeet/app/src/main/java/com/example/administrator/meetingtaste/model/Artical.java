package com.example.administrator.meetingtaste.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Artical implements Serializable
{
	/**
	 * artical表 表名以及各列名称
	 */
	final public static String ARTICAL_TABLE = "artical";
	final public static String ARTICAL_ID = "artical_id";
	final public static String ARTICAL_URL = "artical_url";
	//	final public static String ARTICAL_COMMENT_ID = "artical_comment_id";
	final public static String ARTICAL_LIKE_COUNT = "artical_like_count";
	final public static String ARTICAL_DATE = "artical_date";
	final public static String ARTICAL_USER_ID = "artical_user_id";
	final public static String ARTICAL_FORWARD_STATE = "artical_forward_state";
	final public static String ARTICAL_FORWARD_SRC = "artical_forward_src";
	final public static String ARTICAL_ARTICAL_COLLECTION_COUNT = "artical_artical_collection_count";
	final public static String ARTICAL_ARTICAL_COMMENT_COUNT = "artical_artical_comment_count";
	final public static String ARTICAL_TAG = "artical_tag";
	final public static String ARTICAL_PICTURE_URL = "artical_picture_url";
	final public static String ARTICAL_TITLE = "artical_title";
	final public static String ARTICAL_DESCRIPTION = "artical_description";


	private long artical_id;
	private String artical_url;
	//	private long artical_comment_id;
	private long artical_like_count;
	private Timestamp artical_date;
	private String artical_user_id;
	private boolean artical_forward_state;
	private long artical_forward_src;
	private long artical_artical_collection_count;
	private long artical_artical_comment_count;
	private String artical_tag;
	private String artical_picture_url;
	private String artical_title;
	private String artical_description;

	public void setArtical(Artical artical)
	{
		artical_id = artical.getArtical_id();
		artical_url = artical.getArtical_url();
		artical_like_count = artical.getArtical_like_count();
		artical_date = artical.getArtical_date();
		artical_user_id = artical.getArtical_user_id();
		artical_forward_state = artical.isArtical_forward_state();
        artical_forward_src = artical.getArtical_forward_src();
        artical_artical_collection_count = artical.getArtical_artical_collection_count();
        artical_artical_comment_count = artical.getArtical_artical_comment_count();
        artical_tag = artical.getArtical_tag();
        artical_picture_url = artical.getArtical_picture_url();
        artical_title = artical.getArtical_title();
        artical_description = artical.getArtical_description();
	}

	public static String getArticalTable() {
		return ARTICAL_TABLE;
	}

	public String getArtical_picture_url() {
		return artical_picture_url;
	}

	public void setArtical_picture_url(String artical_picture_url) {
		this.artical_picture_url = artical_picture_url;
	}

	public String getArtical_title() {
		return artical_title;
	}

	public void setArtical_title(String artical_title) {
		this.artical_title = artical_title;
	}

	public String getArtical_description() {
		return artical_description;
	}

	public void setArtical_description(String artical_description) {
		this.artical_description = artical_description;
	}

	public Artical() {
		super();
	}

	public String getArtical_tag() {
		return artical_tag;
	}


	public void setArtical_tag(String artical_tag) {
		this.artical_tag = artical_tag;
	}


	public long getArtical_artical_collection_count() {
		return artical_artical_collection_count;
	}


	public void setArtical_artical_collection_count(long artical_artical_collection_count) {
		this.artical_artical_collection_count = artical_artical_collection_count;
	}


	public long getArtical_artical_comment_count() {
		return artical_artical_comment_count;
	}


	public void setArtical_artical_comment_count(long artical_artical_comment_count) {
		this.artical_artical_comment_count = artical_artical_comment_count;
	}


	public long getArtical_id() {
		return artical_id;
	}
	public void setArtical_id(long artical_id) {
		this.artical_id = artical_id;
	}
	public String getArtical_url() {
		return artical_url;
	}
	public void setArtical_url(String artical_url) {
		this.artical_url = artical_url;
	}
	public long getArtical_like_count() {
		return artical_like_count;
	}
	public void setArtical_like_count(long artical_like_count) {
		this.artical_like_count = artical_like_count;
	}
	public Timestamp getArtical_date() {
		return artical_date;
	}
	public void setArtical_date(Timestamp artical_date) {
		this.artical_date = artical_date;
	}
	public String getArtical_user_id() {
		return artical_user_id;
	}
	public void setArtical_user_id(String artical_user_id) {
		this.artical_user_id = artical_user_id;
	}
	public boolean isArtical_forward_state() {
		return artical_forward_state;
	}
	public void setArtical_forward_state(boolean artical_forward_state) {
		this.artical_forward_state = artical_forward_state;
	}
	public long getArtical_forward_src() {
		return artical_forward_src;
	}
	public void setArtical_forward_src(long artical_src) {
		this.artical_forward_src = artical_src;
	}


}