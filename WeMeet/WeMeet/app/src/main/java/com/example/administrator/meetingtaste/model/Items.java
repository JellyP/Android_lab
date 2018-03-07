package com.example.administrator.meetingtaste.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Items
{
	/**
	 * items表 表名以及各列名称
	 */
	final public static String ITEMS_TABLE = "items";
	final public static String ITEM_ID = "item_id";
	final public static String ITEM_PICTURE_URL = "item_picture_url";
	final public static String ITEM_NAME = "item_name";
	final public static String ITEM_DESCRIPTION = "item_description";
	final public static String ITEM_PRICE = "item_price";
	//	final public static String ITEM_COMMENT_ID = "item_comment_id";
	final public static String ITEM_CREATE_DATE = "item_create_date";
	//	final public static String ITEM_CREATOR_USER_ID = "item_creator_user_id";
	final public static String ITEM_TAG = "item_tag";

	private long item_id;
	private String item_picture_url;
	private String item_name;
	private String item_description;
	private BigDecimal item_price;
	//	private long item_comment_id;
	private Timestamp item_create_date;
	//	private String item_creator_user_id;
	private String item_tag;


	public Items() {
		super();
	}


	public void setItems(Items items)
	{
        item_id = items.getItem_id();
        item_picture_url = items.getItem_picture_url();
        item_name = items.getItem_name();
        item_description = items.getItem_description();
        item_price = items.getItem_price();
        item_create_date = items.getItem_create_date();
        item_tag = items.getItem_tag();
	}

	public Items(long item_id, String item_picture_url, String item_name, String item_description,
				 BigDecimal item_price, Timestamp item_create_date, String item_tag) {
		super();
		this.item_id = item_id;
		this.item_picture_url = item_picture_url;
		this.item_name = item_name;
		this.item_description = item_description;
		this.item_price = item_price;
		this.item_create_date = item_create_date;
		this.item_tag = item_tag;
	}
	public String getItem_tag() {
		return item_tag;
	}
	public void setItem_tag(String item_tag) {
		this.item_tag = item_tag;
	}
	public long getItem_id() {
		return item_id;
	}
	public void setItem_id(long item_id) {
		this.item_id = item_id;
	}
	public String getItem_picture_url() {
		return item_picture_url;
	}
	public void setItem_picture_url(String item_picture_url) {
		this.item_picture_url = item_picture_url;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public String getItem_description() {
		return item_description;
	}
	public void setItem_description(String item_description) {
		this.item_description = item_description;
	}
	public BigDecimal getItem_price() {
		return item_price;
	}
	public void setItem_price(BigDecimal item_price) {
		this.item_price = item_price;
	}
	public Timestamp getItem_create_date() {
		return item_create_date;
	}
	public void setItem_create_date(Timestamp item_create_date) {
		this.item_create_date = item_create_date;
	}
}