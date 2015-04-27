package com.toyking.jiyou.model;

public class PartyAttender {
	private int id;
	private int party_id;
	private String username;
	private String reason;
	private String time;
	private boolean ischeked;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParty_id() {
		return party_id;
	}

	public void setParty_id(int party_id) {
		this.party_id = party_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public boolean isIscheked() {
		return ischeked;
	}

	public void setIscheked(boolean ischeked) {
		this.ischeked = ischeked;
	}

}
