package com.ericsson.objectbrowser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ericsson.objectbrowser.server.ObjectDescriptionRecordsImpl;

public class ObjectBrowserTest {
	
	@Test
	public void propertyReadingWorks(){
	
		ObjectDescriptionRecordsImpl objectRecords = new ObjectDescriptionRecordsImpl();
		
		objectRecords.writeProperties("./files.txt", "seaThings", "domain", "objects", "object");
		
		objectRecords.setSettingsPath("war/WEB-INF/settings/");
		objectRecords.readFilesIn();
		
		assertEquals("./files.txt", objectRecords.getProperties().get("objectFiles"));
		
	}
	
	@Test
	public void fileReadingWorks(){
		
		ObjectDescriptionRecordsImpl objectRecords = new ObjectDescriptionRecordsImpl();
		
		objectRecords.setSettingsPath("war/WEB-INF/settings/");
		objectRecords.readFilesIn();
		
		String firstXmlName = objectRecords.getXmlFiles().get(0).getName();
		
		assertEquals("sharks.xml", firstXmlName);
		
	}
	
	@Test
	public void xmlParserTestDomainNameIsRight(){
		
		ObjectDescriptionRecordsImpl objectRecords = new ObjectDescriptionRecordsImpl();
		
		objectRecords.setSettingsPath("war/WEB-INF/settings/");
		objectRecords.readFilesIn();
		
		objectRecords.parseXmlFiles();
		
		String domainName = objectRecords.getObjectDomains().get(0).getName();
		
		assertEquals("living", domainName);
		
	}
	
	@Test
	public void xmlParserTestObjectNameIsRight(){
		
		ObjectDescriptionRecordsImpl objectRecords = new ObjectDescriptionRecordsImpl();
		
		objectRecords.setSettingsPath("war/WEB-INF/settings/");
		objectRecords.readFilesIn();
		
		objectRecords.parseXmlFiles();
		
		String objectName = objectRecords.getObjectDomains().get(0).getObjects().get(0).getName();
		
		assertEquals("Tiger shark", objectName);
		
	}
	
	@Test
	public void getDomainsWithObjectsReturnsExpectedValues(){
		
		ObjectDescriptionRecordsImpl objectRecords = new ObjectDescriptionRecordsImpl();
		
		objectRecords.setSettingsPath("war/WEB-INF/settings/");
		
		String objectName = objectRecords.getDomainsWithObjects().get("living").get(1);
		
		assertEquals("Blue shark", objectName);
		
	}
	
	@Test
	public void getObjectWithAttributesReturnsExpectedValues(){
		
		ObjectDescriptionRecordsImpl objectRecords = new ObjectDescriptionRecordsImpl();
		
		objectRecords.setSettingsPath("war/WEB-INF/settings/");
				
		objectRecords.getObjectDescriptionRecords();
		
		String objectId = objectRecords.getObjectWithAttributes("RMS Titanic").get("id");
		
		assertEquals("titanic", objectId);
		
	}
	
}
