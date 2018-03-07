package com.example.administrator.meetingtaste.model;

import java.sql.Timestamp;

public class ItemComment 
{
	/**
	 * item_comment
	 */
	final public static String ITEM_COMMENT_TABLE = "item_comment";
	final public static String ITEM_COMMENT_ITEM_ID = "item_comment_item_id";
	final public static String ITEM_COMMENT_USER_ID = "item_comment_user_id";
	final public static String ITEM_COMMENT_CONTENT = "item_comment_content";
	final public static String ITEM_COMMENT_DATE = "item_comment_date";
	final public static String ITEM_COMMENT_ID = "item_comment_id";
	
	private long item_comment_item_id;
	private String item_comment_user_id;
	private String item_comment_content;
	private long item_comment_id;
	

	public ItemComment(long item_id,String user_id,String comment_content)
	{
		this.item_comment_item_id=item_id;
		this.item_comment_user_id=user_id;
		this.item_comment_content=comment_content;
	}

	public void setItemComment(ItemComment itemComment)
    {
        item_comment_item_id = itemComment.getItem_comment_item_id();
        item_comment_user_id = itemComment.getItem_comment_user_id();
        item_comment_content = itemComment.getItem_comment_content();
        item_comment_id = itemComment.getItem_comment_id();
    }
	
//	public ItemComment(ResultSet rs) throws SQLException
//	{
//		this.item_comment_item_id = rs.getLong(ITEM_COMMENT_ITEM_ID);
//		this.item_comment_user_id = rs.getString(ITEM_COMMENT_USER_ID);
//		this.item_comment_content = rs.getString(ITEM_COMMENT_CONTENT);
//		this.item_comment_date = rs.getTimestamp(ITEM_COMMENT_DATE);
//		this.item_comment_id = rs.getLong(ITEM_COMMENT_ID);
//	}
	
	public ItemComment(long item_comment_item_id, String item_comment_user_id, String item_comment_content,
			Timestamp item_comment_date) {
		super();
		this.item_comment_item_id = item_comment_item_id;
		this.item_comment_user_id = item_comment_user_id;
		this.item_comment_content = item_comment_content;
	}
	
	
	public long getItem_comment_id() {
		return item_comment_id;
	}

	public void setItem_comment_id(long item_comment_id) {
		this.item_comment_id = item_comment_id;
	}

	public long getItem_comment_item_id() {
		return item_comment_item_id;
	}
	public void setItem_comment_item_id(long item_comment_item_id) {
		this.item_comment_item_id = item_comment_item_id;
	}
	public String getItem_comment_user_id() {
		return item_comment_user_id;
	}
	public void setItem_comment_user_id(String item_comment_user_id) {
		this.item_comment_user_id = item_comment_user_id;
	}
	public String getItem_comment_content() {
		return item_comment_content;
	}
	public void setItem_comment_content(String item_comment_content) {
		this.item_comment_content = item_comment_content;
	}

	
	
}
