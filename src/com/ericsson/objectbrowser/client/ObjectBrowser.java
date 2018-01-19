/*
 * Smart GWT (GWT for SmartClient)
 * Copyright 2008 and beyond, Isomorphic Software, Inc.
 *
 * Smart GWT is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  Smart GWT is also
 * available under typical commercial license terms - see
 * http://smartclient.com/license
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package com.ericsson.objectbrowser.client;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.gwt.core.client.EntryPoint;

public class ObjectBrowser implements EntryPoint {
	
	private ObjectDescriptionRecordsServiceAsync objectDescriptionRecordsSvc = GWT.create(ObjectDescriptionRecordsService.class);
	
	private Map<String, ArrayList<String>> mainDataSource;
	
	private Map<String, String> detailsDataSource;

    public void onModuleLoad() {
    	readMainDataSource();

    }
    
    public void initMainView(){
    	
    	RecordList recordList = new RecordList();
    	
    	for(Map.Entry<String, ArrayList<String>> entry : mainDataSource.entrySet()) {
    	
    		for(String objectName : entry.getValue()){
    			
    			Record record = new Record();
        		record.setAttribute("name", objectName);
        		record.setAttribute("domain_id", entry.getKey());
        		
        		recordList.add(record);
        		
    		}
    	}
    	
    	Window mainWindow = new Window();
    	mainWindow.setHeight(400);
    	mainWindow.setWidth(300);
    	mainWindow.centerInPage();
    	mainWindow.setCanDragReposition(true);
    	mainWindow.setCanDragResize(true);
    	mainWindow.setTitle("OBJECT BROWSER");
    	mainWindow.draw();
    	
    	final ListGrid mainListGrid = new ListGrid(); 
        mainListGrid.setShowAllRecords(true);
        mainListGrid.setData(recordList);
        mainListGrid.setHeight100();
        mainListGrid.setWidth100();
        
        mainListGrid.setGroupStartOpen(GroupStartOpen.ALL);  
        mainListGrid.setGroupByField("domain_id");
        
        ListGridField nameListGridField = new ListGridField("name", "Name", mainListGrid.getWidth());  
        nameListGridField.setAlign(Alignment.CENTER);  
        nameListGridField.setType(ListGridFieldType.TEXT);
        nameListGridField.setCanEdit(false);

        ListGridField domainIdListGridField = new ListGridField("domain_id", "Domain ID", 0);
        domainIdListGridField.setAlign(Alignment.CENTER);  
        domainIdListGridField.setType(ListGridFieldType.TEXT);
        domainIdListGridField.setCanEdit(false);
        
        mainListGrid.setFields(new ListGridField[]{nameListGridField, domainIdListGridField});
        mainListGrid.hideFields(new ListGridField[]{domainIdListGridField}, true);
        
        mainListGrid.setShowHeader(false);
        //mainListGrid.setScrollbarSize(0);
       
        mainListGrid.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				
				Record selectedRecord = mainListGrid.getSelectedRecord();
				readDetailsDataSource(selectedRecord.getAttribute("name"));
				
			}
		});
       
        mainWindow.addMember(mainListGrid);
    	
    }
    
    public void initDetailsView(String name) {
    	
    	RecordList recordList = new RecordList();
    	
    	for(Map.Entry<String, String> entry : detailsDataSource.entrySet()){
    		
    		Record record = new Record();
    		record.setAttribute("key", entry.getKey());
    		record.setAttribute("value", entry.getValue());
    		
    		recordList.add(record);
    		
    	}
    	
    	
    	Window detailsWindow = new Window();
    	detailsWindow.setHeight(200);
    	detailsWindow.setWidth(260);
    	detailsWindow.centerInPage();
    	detailsWindow.setCanDragReposition(true);
    	detailsWindow.setCanDragResize(true);
    	detailsWindow.setTitle(name.toUpperCase());
    	detailsWindow.draw();
    	
    	final ListGrid detailsListGrid = new ListGrid(); 
    	detailsListGrid.setShowAllRecords(true);
    	detailsListGrid.setData(recordList);
    	detailsListGrid.setHeight100();
    	detailsListGrid.setWidth100();
        
    	//detailsListGrid.setGroupStartOpen(GroupStartOpen.ALL);  
    	//detailsListGrid.setGroupByField("domain_id");

        ListGridField keyListGridField = new ListGridField("key", "Key", 90);  
        keyListGridField.setAlign(Alignment.LEFT);  
        keyListGridField.setType(ListGridFieldType.TEXT);
        keyListGridField.setCanEdit(false);

        ListGridField valueListGridField = new ListGridField("value", "Value", 140);
        valueListGridField.setAlign(Alignment.LEFT);  
        valueListGridField.setType(ListGridFieldType.TEXT);
        valueListGridField.setCanEdit(false);
        
        detailsListGrid.setFields(new ListGridField[]{keyListGridField, valueListGridField});
        
        detailsListGrid.setShowHeader(false);
        
        detailsWindow.addMember(detailsListGrid);
    	
    }
    
    public void readMainDataSource(){
    	
    	if (objectDescriptionRecordsSvc == null) {
    		objectDescriptionRecordsSvc = GWT.create(ObjectDescriptionRecordsService.class);
    	    }
    	
    	AsyncCallback<Map<String, ArrayList<String>>> callback = new AsyncCallback <Map<String, ArrayList<String>>>() {
    	      public void onFailure(Throwable caught) {
    	    	  SC.confirm(caught.getMessage(), new BooleanCallback() {  
                      public void execute(Boolean value) {  
                      	
                      }
                          
                  }); 
    	      }

    	      public void onSuccess(Map<String, ArrayList<String>> resultMap) {
   		  
	    		  mainDataSource = new HashMap<String, ArrayList<String>> (resultMap);
    	    	  
    	    	  initMainView();
    	    	  
    	      }
    	};
        
    	objectDescriptionRecordsSvc.getDomainsWithObjects(callback);
    }
    
	public void readDetailsDataSource(final String objectValueName){
    	
    	if (objectDescriptionRecordsSvc == null) {
    		objectDescriptionRecordsSvc = GWT.create(ObjectDescriptionRecordsService.class);
    	    }
    	
    	AsyncCallback<Map<String, String>> callback = new AsyncCallback <Map<String, String>>() {
    	      public void onFailure(Throwable caught) {
    	    	  SC.confirm(caught.getMessage(), new BooleanCallback() {  
                      public void execute(Boolean value) {  
                      	
                      }
                          
                  }); 
    	      }

    	      public void onSuccess(Map<String, String> resultMap) {
   		  
	    		  detailsDataSource = new HashMap<String, String> (resultMap);
    	    	  
    	    	  initDetailsView(objectValueName);
    	    	  
    	      }
    	};
        
    	objectDescriptionRecordsSvc.getObjectWithAttributes(objectValueName, callback);
    }

}
