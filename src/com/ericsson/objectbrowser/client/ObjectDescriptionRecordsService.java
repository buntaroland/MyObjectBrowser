package com.ericsson.objectbrowser.client;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("objectDescriptionRecords")
public interface ObjectDescriptionRecordsService extends RemoteService{
	
	Map<String, ArrayList<String>> getDomainsWithObjects();
	
	Map<String, String> getObjectWithAttributes(String objectValueName);
}
