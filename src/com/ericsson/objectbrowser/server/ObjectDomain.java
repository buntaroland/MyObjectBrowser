package com.ericsson.objectbrowser.server;

import java.util.ArrayList;
import java.util.List;

public class ObjectDomain {
	
	private String name;
	
	private List<ObjectDescription> objects;
	
	public ObjectDomain(){
		
		name = "domain_name_t";
		objects = new ArrayList<ObjectDescription>();
		
	}
	
	public ObjectDomain(String name, List<ObjectDescription> objects){
		
		this.name = name;
		this.objects = objects;
		
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public List<ObjectDescription> getObjects(){
		return objects;
	}
	
	public void setObjects(List<ObjectDescription> objects){
		this.objects = objects;
	}
	
	public void addObject(ObjectDescription object){
		objects.add(object);
	}
	
}
