package com.toyking.jiyou.model;

import java.io.Serializable;

public class SquareStatus implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6624434990315704635L;

	private int id;
	private String content;
	private String time;
	private String username;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
