package com.gmodelo.cyclicinventories.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogInve implements Comparable<LogInve>{
	
	private String type;
	private String title;
	private String description;
	private String subtitle;
	private String userId;
	private String date;
	protected SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	public LogInve(){
		
		super();
	}

	public LogInve(String type, String title, String description, String subtitle, String userId, String date) {
		super();
		this.type = type;
		this.title = title;
		this.description = description;
		this.subtitle = subtitle;
		this.userId = userId;
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	private Date convertDate(String date){
		
		Date dateAux = null;
		
		try {
			dateAux = formatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dateAux;
	}

	@Override
	public String toString() {
		return "LogInve [type=" + type + ", title=" + title + ", description=" + description + ", subtitle=" + subtitle
				+ ", userId=" + userId + ", date=" + date + "]";
	}

	@Override
	public int compareTo(LogInve o) {
		// TODO Auto-generated method stub
		return convertDate(this.date).compareTo(convertDate(o.date));
	}
		
}
