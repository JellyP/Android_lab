package com.example.administrator.meetingtaste.model;
import java.sql.Timestamp;

public class ArticalComment 
{
	/**
	 * artical_comment表 表名以及各列名称
	 */
	final public static String ARTICAL_COMMENT_TABLE = "artical_comment";
	final public static String ARTICAL_COMMENT_ARTICAL_ID = "artical_comment_artical_id";
	final public static String ARTICAL_COMMENT_USER_ID = "artical_comment_user_id";
	final public static String ARTICAL_COMMENT_CONTENT = "artical_comment_content";
	final public static String ARTICAL_COMMENT_DATE = "artical_comment_date";
	final public static String ARTICAL_COMMENT_ID = "artical_comment_id";
	
	private long artical_comment_artical_id;
	private String artical_comment_user_id;
	private String artical_comment_content;
	private Timestamp artical_comment_date;
	private long artical_comment_id;
	
	public ArticalComment() {}

	public ArticalComment(long artical_comment_artical_id, String artical_comment_user_id, String artical_comment_content) {
		this.artical_comment_artical_id = artical_comment_artical_id;
		this.artical_comment_user_id = artical_comment_user_id;
		this.artical_comment_content = artical_comment_content;
	}

	public void setArticalComment(ArticalComment articalComment)
    {
        artical_comment_artical_id = articalComment.getArtical_comment_artical_id();
        artical_comment_user_id = articalComment.getArtical_comment_user_id();
        artical_comment_content = articalComment.getArtical_comment_content();
        artical_comment_date = articalComment.getArtical_comment_date();
        artical_comment_id = articalComment.getArtical_comment_id();
    }
	
//	public ArticalComment(ResultSet rs) throws SQLException
//	{
//		this.artical_comment_artical_id = rs.getLong(ARTICAL_COMMENT_ARTICAL_ID);
//		this.artical_comment_user_id = rs.getString(ARTICAL_COMMENT_USER_ID);
//		this.artical_comment_content = rs.getString(ARTICAL_COMMENT_CONTENT);
//		this.artical_comment_date = rs.getTimestamp(ARTICAL_COMMENT_DATE);
//		this.artical_comment_id = rs.getLong(ARTICAL_COMMENT_ID);
//	}
	
	public ArticalComment(long artical_comment_artical_id, String artical_comment_user_id,
			String artical_comment_content, Timestamp artical_comment_date) {
		super();
		this.artical_comment_artical_id = artical_comment_artical_id;
		this.artical_comment_user_id = artical_comment_user_id;
		this.artical_comment_content = artical_comment_content;
		this.artical_comment_date = artical_comment_date;
	}
	
	
	public long getArtical_comment_id() {
		return artical_comment_id;
	}

	public void setArtical_comment_id(long artical_comment_id) {
		this.artical_comment_id = artical_comment_id;
	}

	public long getArtical_comment_artical_id() {
		return artical_comment_artical_id;
	}
	public void setArtical_comment_artical_id(long artical_comment_artical_id) {
		this.artical_comment_artical_id = artical_comment_artical_id;
	}
	public String getArtical_comment_user_id() {
		return artical_comment_user_id;
	}
	public void setArtical_comment_user_id(String artical_comment_user_id) {
		this.artical_comment_user_id = artical_comment_user_id;
	}
	public String getArtical_comment_content() {
		return artical_comment_content;
	}
	public void setArtical_comment_content(String artical_comment_content) {
		this.artical_comment_content = artical_comment_content;
	}
	public Timestamp getArtical_comment_date() {
		return artical_comment_date;
	}
	public void setArtical_comment_date(Timestamp artical_comment_date) {
		this.artical_comment_date = artical_comment_date;
	}
	
	
}
