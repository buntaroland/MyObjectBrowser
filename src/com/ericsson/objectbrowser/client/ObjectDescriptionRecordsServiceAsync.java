package com.ericsson.objectbrowser.client;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ObjectDescriptionRecordsServiceAsync {
	
	void getDomainsWithObjects(AsyncCallback<Map<String, ArrayList<String>>> callback);
	
	void getObjectWithAttributes(String objectValueName, AsyncCallback<Map<String, String>> callback);

}
