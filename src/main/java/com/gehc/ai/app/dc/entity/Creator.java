package com.gehc.ai.app.dc.entity;

public class Creator {
    private String id;
    public String getId() {
		return (id==null)?id:id.replaceAll("^\"|\"$", "");
	}
	public void setId(String id) {
		this.id = id;
	}
	private String name;
	public String getName() {
		return (name==null)?name:name.replaceAll("^\"|\"$", "");
	}
	public void setName(String name) {
		this.name = name;
	}
	public Creator(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public Creator() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Creator [id=" + id + ", name=" + name + "]";
	}	
}
