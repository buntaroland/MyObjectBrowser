package com.ericsson.objectbrowser.server;

import java.util.HashMap;
import java.util.Map;

public class ObjectDescription {
	
	private String id;
	
	private String name;
	
	private String description;
	
	private Map<String, String> others;
	
	public ObjectDescription(){
		id = "id_t";
		name = "name_t";
		description = "description_t";
		others = new HashMap<String, String>();
	}
	
	public ObjectDescription(String id, String name, String description, Map<String, String> others){
		this.id = id;
		this.name = name;
		this.description = description;
		this.others = others;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Map<String, String> getOthers() {
		return others;
	}

	public void setOthers(Map<String, String> others) {
		this.others = others;
	}
	
	public void addOther(String key, String value){
		others.put(key, value);
	}
}
