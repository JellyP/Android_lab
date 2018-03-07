package com.example.administrator.meetingtaste.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ArticalCollection
{
	/**
	 * artical_collection表 表名以及各列名称
	 */
	final public static String ARTICAL_COLLECTION_TABLE = "artical_collection";
	final public static String ARTICAL_COLLECTION_USER_ID = "artical_collection_user_id";
	final public static String ARTICAL_COLLECTION_ARTICAL_ID = "artical_collection_artical_id";
	final public static String ARTICAL_COLLECTION_DATE = "artical_collection_date";

	private String artical_collection_user_id;
	private long artical_collection_artical_id;
	private Timestamp artical_collection_date;

	public void setArticalCollection(ArticalCollection articalCollection)
    {
        artical_collection_user_id = articalCollection.getArtical_collection_user_id();
        artical_collection_artical_id = articalCollection.getArtical_collection_artical_id();
        artical_collection_date = articalCollection.getArtical_collection_date();
    }

	public ArticalCollection(ResultSet rs) throws SQLException
	{
		this.artical_collection_user_id = rs.getString(ARTICAL_COLLECTION_USER_ID);
		this.artical_collection_artical_id = rs.getLong(ARTICAL_COLLECTION_ARTICAL_ID);
		this.artical_collection_date = rs.getTimestamp(ARTICAL_COLLECTION_DATE);
	}

	public ArticalCollection(String artical_collection_user_id, long artical_collection_artical_id,
							 Timestamp artical_collection_date) {
		super();
		this.artical_collection_user_id = artical_collection_user_id;
		this.artical_collection_artical_id = artical_collection_artical_id;
		this.artical_collection_date = artical_collection_date;
	}
	public String getArtical_collection_user_id() {
		return artical_collection_user_id;
	}
	public void setArtical_collection_user_id(String artical_collection_user_id) {
		this.artical_collection_user_id = artical_collection_user_id;
	}
	public long getArtical_collection_artical_id() {
		return artical_collection_artical_id;
	}
	public void setArtical_collection_artical_id(long artical_collection_artical_id) {
		this.artical_collection_artical_id = artical_collection_artical_id;
	}
	public Timestamp getArtical_collection_date() {
		return artical_collection_date;
	}
	public void setArtical_collection_date(Timestamp artical_collection_date) {
		this.artical_collection_date = artical_collection_date;
	}


}