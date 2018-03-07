package com.example.administrator.meetingtaste.model;


import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User implements Serializable
{
	/**
	 * user_info表 表名以及各列名称
	 */
	final public static String USER_TABLE = "user";
	final public static String USER_ID = "user_id";
	final public static String USER_ICON = "user_icon";
	final public static String USER_NAME = "user_name";
	final public static String USER_PASSWORD = "user_password";
	final public static String USER_GENDER = "user_gender";
	final public static String USER_PHONE = "user_phone";
	final public static String USER_ADDRESS = "user_address";
	final public static String USER_ARTICAL_COUNT = "user_artical_count";
	final public static String USER_ARTICAL_COLLECTION_COUNT = "user_artical_collection_count";
	final public static String USER_FOLLOW_COUNT = "user_follow_count";
	final public static String USER_FANS_COUNT = "user_fans_count";
	final public static String USER_CITY = "user_city";
	final public static String USER_DESCRIPTION = "user_description";
	final public static String USER_ACCOUNT = "user_account";


	private String user_id;
	private String user_icon;
	private String user_name;
	private String user_password;
	private boolean user_gender;//true为男 false为女
	private String user_phone;
//	private String user_address;
	private Long user_artical_count;
	private Long user_artical_collection_count;
	private Long user_fans_count;
	private Long user_follow_count;
	private String user_city;
	private String user_description;
	private BigDecimal user_account;


	public User(ResultSet rs) throws SQLException
	{
		this.user_id = rs.getString(USER_ID);
		this.user_icon = rs.getString(USER_ICON);
		this.user_name = rs.getString(USER_NAME);
		//this.user_password = rs.getString();
		this.user_gender = rs.getBoolean(USER_GENDER);
		this.user_phone = rs.getString(USER_PHONE);
//		this.user_address = rs.getString(USER_ADDRESS);
		this.user_artical_count = rs.getLong(USER_ARTICAL_COUNT);
		this.user_artical_collection_count = rs.getLong(USER_ARTICAL_COLLECTION_COUNT);
		this.user_fans_count = rs.getLong(USER_FANS_COUNT);
		this.user_follow_count = rs.getLong(USER_FOLLOW_COUNT);
		this.user_city = rs.getString(USER_CITY);
		this.user_description = rs.getString(USER_DESCRIPTION);
		this.user_account = rs.getBigDecimal(USER_ACCOUNT);
	}

	public void setUser(User user)
	{
		user_id = user.getUser_id();
		user_icon = user.getUser_icon();
		user_name = user.getUser_name();
		user_gender = user.isUser_gender();
		user_phone = user.getUser_phone();
//		user_address = user.getUser_address();
		user_artical_count = user.getUser_artical_count();
		user_artical_collection_count = user.getUser_artical_collection_count();
		user_fans_count = user.getUser_fans_count();
		user_follow_count = user.getUser_follow_count();
        user_city = user.getUser_city();
		user_description = user.getUser_description();
		user_account = user.getUser_account();
	}

	public String getUser_city() {
		return user_city;
	}


	public void setUser_city(String user_city) {
		this.user_city = user_city;
	}


	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}

	public User() {
		super();
	}

	public Long getUser_artical_count() {
		return user_artical_count;
	}
	public void setUser_artical_count(Long user_artical_count) {
		this.user_artical_count = user_artical_count;
	}
	public Long getUser_artical_collection_count() {
		return user_artical_collection_count;
	}
	public void setUser_artical_collection_count(Long user_artical_collection_count) {
		this.user_artical_collection_count = user_artical_collection_count;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_icon() {
		return user_icon;
	}
	public void setUser_icon(String user_icon) {
		this.user_icon = user_icon;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public boolean isUser_gender() {
		return user_gender;
	}
	public void setUser_gender(boolean user_gender) {
		this.user_gender = user_gender;
	}
	public String getUser_phone() {
		return user_phone;
	}
	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}
//	public String getUser_address() {
//		return user_address;
////	}
//	public void setUser_address(String user_address) {
//		this.user_address = user_address;
//	}


	public Long getUser_fans_count() {
		return user_fans_count;
	}


	public void setUser_fans_count(Long user_fans_count) {
		this.user_fans_count = user_fans_count;
	}


	public Long getUser_follow_count() {
		return user_follow_count;
	}

	public void setUser_follow_count(Long user_follow_count) {
		this.user_follow_count = user_follow_count;
	}



	public String getUser_description() {
		return user_description;
	}



	public void setUser_description(String user_description) {
		this.user_description = user_description;
	}



	public BigDecimal getUser_account() {
		return user_account;
	}



	public void setUser_account(BigDecimal user_account) {
		this.user_account = user_account;
	}



}
