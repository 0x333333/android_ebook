package com.jesusjzp.entity;

public class Article {
	private String id;
	private String url;
	private String abstr;
	private String title;
	private String status;
	private String type;

	public Article(String id, String url, String title, String abstr,
			String status, String type) {
		super();
		this.id = id;
		this.url = url;
		this.abstr = abstr;
		this.title = title;
		this.status = status;
		this.type = type;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAbstr() {
		return abstr;
	}

	public void setAbstr(String abstr) {
		this.abstr = abstr;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
