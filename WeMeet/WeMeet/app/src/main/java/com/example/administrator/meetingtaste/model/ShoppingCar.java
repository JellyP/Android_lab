package com.example.administrator.meetingtaste.model;

import java.sql.Timestamp;

public class ShoppingCar 
{
	/**
	 * shoppingcar表 表明以及各列名称
	 */
	final public static String SHOPPINGCAR_TABLE = "shoppingcar";
	final public static String SHOPPINGCAR_USER_ID = "shoppingcar_user_id";
	final public static String SHOPPINGCAR_ITEM_ID = "shoppingcar_item_id";
	final public static String SHOPPINGCAR_COUNT = "shoppingcar_count";
	final public static String SHOPPINGCAR_DATE = "shoppingcar_date";
	
	
	
	private String shoppingcar_user_id;
	private long shoppingcar_item_id;
	private long shoppingcar_count;
	private Timestamp shoppingcar_date;
	
	
	
	public ShoppingCar() {
		super();
	}

	public ShoppingCar(String shoppingcar_user_id, long shoppingcar_item_id, long shoppingcar_count) {
		this.shoppingcar_user_id = shoppingcar_user_id;
		this.shoppingcar_item_id = shoppingcar_item_id;
		this.shoppingcar_count = shoppingcar_count;
	}

	public ShoppingCar(String shoppingcar_user_id, long shoppingcar_item_id, long shoppingcar_count,
					   Timestamp shoppingcar_date) {
		super();
		this.shoppingcar_user_id = shoppingcar_user_id;
		this.shoppingcar_item_id = shoppingcar_item_id;
		this.shoppingcar_count = shoppingcar_count;
		this.shoppingcar_date = shoppingcar_date;
	}


	public String getShoppingcar_user_id() {
		return shoppingcar_user_id;
	}


	public void setShoppingcar_user_id(String shoppingcar_user_id) {
		this.shoppingcar_user_id = shoppingcar_user_id;
	}


	public long getShoppingcar_item_id() {
		return shoppingcar_item_id;
	}


	public void setShoppingcar_item_id(long shoppingcar_item_id) {
		this.shoppingcar_item_id = shoppingcar_item_id;
	}


	public long getShoppingcar_count() {
		return shoppingcar_count;
	}


	public void setShoppingcar_count(long shoppingcar_count) {
		this.shoppingcar_count = shoppingcar_count;
	}


	public Timestamp getShoppingcar_date() {
		return shoppingcar_date;
	}


	public void setShoppingcar_date(Timestamp shoppingcar_date) {
		this.shoppingcar_date = shoppingcar_date;
	}



	
	 

	
	
	
}
