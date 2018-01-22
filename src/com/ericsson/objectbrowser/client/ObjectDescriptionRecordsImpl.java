package com.ericsson.objectbrowser.server;

import com.ericsson.objectbrowser.client.ObjectDescriptionRecordsService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

@SuppressWarnings("serial")
public class ObjectDescriptionRecordsImpl extends RemoteServiceServlet implements ObjectDescriptionRecordsService{
	
	private Properties prop;
	
	private List<File> xmlFiles;
	
	private List<ObjectDomain> domains;
	
	private String settingsPath = "WEB-INF/settings/";
	
	public Properties getProperties(){		
		return prop;		
	}
	
	public List<File> getXmlFiles(){		
		return xmlFiles;
	}
	
	public List<ObjectDomain> getObjectDomains(){
		return domains;
	}
	
	public void setSettingsPath(String path){
		
		settingsPath = path;
		
	}
	
	public void getObjectDescriptionRecords(){
			
			readFilesIn();
			
			parseXmlFiles();
			
		}
	
	public Map<String, ArrayList<String>> getDomainsWithObjects(){		
		
		getObjectDescriptionRecords();
		
		Map<String, ArrayList<String>> resultMap = new HashMap<String, ArrayList<String>>();
		
		for(ObjectDomain domain : domains){
			
			ArrayList<String> objectNameList = new ArrayList<String>();
			
			for(ObjectDescription object : domain.getObjects()){
				
				objectNameList.add(object.getName());
				
			}
			
			resultMap.put(domain.getName(), objectNameList);
			
		}
		
		return resultMap;
		
	}
	
	public Map<String, String> getObjectWithAttributes(String objectValueName){

		Map<String, String> resultMap = new HashMap<String, String>();
		
		for(ObjectDomain domain : domains){
			
			for(ObjectDescription object : domain.getObjects()){
				
				if(object.getName().equals(objectValueName)){
					
					resultMap.put("id", object.getId());
					resultMap.put("name", object.getName());
					resultMap.put("description", object.getDescription());
					
					resultMap.putAll(object.getOthers());
					
					return resultMap;
				}
			}
		}
		
		return resultMap;
		
	}
	
	public void writeProperties(String objectFiles, String rootTag, String domainIdTag, String objectDescriptorBlockTag, String objectDescriptorTag){
		
		try {
			prop = new Properties();
			prop.setProperty("objectFiles", objectFiles);
			prop.setProperty("rootTag", rootTag);
			prop.setProperty("domainIdTag", domainIdTag);
			prop.setProperty("objectDescriptorBlockTag", objectDescriptorBlockTag);
			prop.setProperty("objectDescriptorTag", objectDescriptorTag);

			File file = new File("war/WEB-INF/settings/config.properties");
			FileOutputStream fileOut = new FileOutputStream(file);
			prop.store(fileOut, "Config properties");
			fileOut.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}
	
	public void readFilesIn(){
		
		prop = new Properties();
		InputStream input = null;
		BufferedReader reader = null;
		
		try {

			input = new FileInputStream(settingsPath + "config.properties");

			prop.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		try {
			
			input = new FileInputStream(settingsPath + prop.getProperty("objectFiles").substring(2));

			reader = new BufferedReader(new InputStreamReader(input));
			
			String xmlPath;
			
			xmlFiles = new ArrayList<File>();
			
	         while ((xmlPath = reader.readLine()) != null) {
	            xmlFiles.add(new File(settingsPath + xmlPath.substring(2)));
	         }       
	      } catch(Exception e) {
	         e.printStackTrace();
	      }
	}
	
	public void parseXmlFiles(){
		domains = new ArrayList<ObjectDomain>();
		
		for(File xmlFile : xmlFiles) {
			
			try {
		         SAXParserFactory factory = SAXParserFactory.newInstance();
		         SAXParser saxParser = factory.newSAXParser();
		         UserHandler userhandler = new UserHandler();
		         saxParser.parse(xmlFile, userhandler);
		         
		      } catch (Exception e) {
		         e.printStackTrace();
		      }
			
		}
		
	}
	
	public class UserHandler extends DefaultHandler {

		   boolean atDomain = false;
		   boolean atObjects = false;
		   boolean atObject = false;
		   boolean atId = false;
		   boolean atName = false;
		   boolean atDescription = false;
		   boolean atOtherParams = false;
		   
		   boolean idFound = false;
		   boolean nameFound = false;
		   boolean descriptionFound = false;
		   boolean domainFound = false;
		   boolean objectNameGiven = false;
		   
		   boolean objectsEnded = false;
		   
		   ObjectDescription object_data;
		   List<ObjectDescription> objects;
		   
		   String domainName;
		   String objectName;
		   int key_value = 0;
		   
		   String otherParamName;

		   @Override
		   public void startElement(String uri, 
		   String localName, String qName, Attributes attributes) throws SAXException {

		      if (qName.equalsIgnoreCase(prop.getProperty("domainIdTag"))) {
		    	 atDomain = true;
		      } else if (qName.equalsIgnoreCase(prop.getProperty("objectDescriptorBlockTag"))) {
		         atObjects = true;
		         objects = new ArrayList<ObjectDescription>();
		      } else if (qName.equalsIgnoreCase(prop.getProperty("objectDescriptorTag"))) {
		    	  atObject = true;
		      } else if (atObject && qName.equalsIgnoreCase("id") && !idFound) {
		    	  atId = true;
		      } else if (atObject && qName.equalsIgnoreCase("name") && !nameFound) {
		    	  atName = true;
		      } else if (atObject && qName.equalsIgnoreCase("description") && !descriptionFound) {
		    	  atDescription = true;
		      } else if (atObject && !qName.equalsIgnoreCase("id") && !qName.equalsIgnoreCase("name") && !qName.equalsIgnoreCase("description")) {
		    	  atOtherParams = true;
		    	  otherParamName = qName;
		      }
		   }

		   @Override
		   public void endElement(String uri, 
		   String localName, String qName) throws SAXException {
		      if (qName.equalsIgnoreCase(prop.getProperty("objectDescriptorBlockTag"))) {
		    
			         ObjectDescriptionRecordsImpl.this.domains.add(new ObjectDomain(domainName, objects));   
			       			         
			         atObjects = false;
			         
			         objectsEnded = true;
			         
		      } else if (qName.equalsIgnoreCase(prop.getProperty("objectDescriptorTag")) && !objectsEnded ) {
			         
			         atObject = false;
			         objectNameGiven = false;
			         idFound = false;
			         nameFound = false;
			         descriptionFound = false;
			         
			         objects.add(object_data);
			         
			         }
		   }

		   @Override
		   public void characters(char ch[], int start, int length) throws SAXException {
			   
			  if (atDomain) {
				  
				  domainName = new String(ch, start, length);
				  
				  atDomain = false;
				  
				  domainFound = true;
				  
			  } else if (domainFound && atObjects) {
				  if (atObject) {
					  
					  if(!objectNameGiven){
						  objectName = "object_" + key_value;
						  key_value++;
						  object_data = new ObjectDescription();
						  
						  objectNameGiven = true;
					  }
					  
					  if(atId) {
						  object_data.setId(new String(ch, start, length));
						  						  
						  atId = false;
						  idFound = true;
						  
					  } else if (atName){
						  object_data.setName(new String(ch, start, length));
						  						  
						  atName = false;
						  nameFound = true;
						  
					  } else if (atDescription){
						  object_data.setDescription(new String(ch, start, length));
						  						  
						  atDescription = false;
						  descriptionFound = true;
						  
					  } else if (atOtherParams){
						  object_data.addOther(otherParamName, new String(ch, start, length));
										  						  
						  atOtherParams = false;
										  
					  }
				  }
			  }
		   }
	}

}
