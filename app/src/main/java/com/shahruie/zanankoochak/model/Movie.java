package com.shahruie.zanankoochak.model;


public class Movie {
	private String title, thumbnailUrl,frame,time;
int visited;

	public Movie() {
	}

	public Movie(String name, String thumbnailUrl,String frame,String time,int visited) {
		this.title = name;
		this.thumbnailUrl = thumbnailUrl;
		this.frame = frame;
		this.time = time;
		this.visited = visited;

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}

public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
public int getVisited() {
		return visited;
	}

	public void setVisited(int visited) {
		this.visited = visited;
	}

	public String getFrame() {
		return frame;
	}

	public void setFrame(String frame) {
		this.frame = frame;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}



}
