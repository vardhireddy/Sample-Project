package com.gehc.ai.app.dc.entity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AnnotationSet {
	@JsonProperty("schemaVersion")
	public String schemaVersion;
	
	public String getSchemaVersion() {
		return schemaVersion;
	}
	public void setSchemaVersion(String schemaVersion) {
		this.schemaVersion = schemaVersion;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public List<String> getImageSets() {
		return imageSets;
	}
	public void setImageSets(List<String> imageSets) {
		this.imageSets = imageSets;
	}
	public String getCreatorType() {
		return creatorType;
	}
	public void setCreatorType(String creatorType) {
		this.creatorType = creatorType;
	}
	public List<Object> getItems() {
		return items;
	}
	public void setItems(List<Object> items) {
		this.items = items;
	}
	
	@JsonProperty("id")
	public String id;
	
	@JsonProperty("orgId")
	public String orgId;
	
	@JsonProperty("orgName")
	public String orgName;
	
	@JsonProperty("imageSets")
	public List<String> imageSets;
	
	@JsonProperty("creatorType")
	public String creatorType;
	
	@JsonProperty("creatorId")
	public String creatorId;
	
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	
	public List<Object> items;
	
	public static void main(String [] args) throws IOException {
//		AnnotationSet as = AnnotationSet.createRandom();
//		ObjectMapper mapper = new ObjectMapper();
//		System.out.println(mapper.writeValueAsString(as));
		LineNumberReader lr = new LineNumberReader(
			new InputStreamReader(AnnotationSet.class.getResourceAsStream("SampleAnnotationSet")));
		StringBuffer buf = new StringBuffer();
		String line;
		while ((line = lr.readLine())!=null) {
			buf.append(line);
		}
		
		System.out.println("original json string = " + buf);
		AnnotationSet as = readFromJson(buf.toString());
		
		System.out.println(getJson(as));
		
	}
	
	public void set(AnnotationSet as) {
		this.id = as.id;
		this.creatorId = as.creatorId;
		this.creatorType = as.creatorType;
		this.schemaVersion = as.schemaVersion;
		this.imageSets = as.imageSets;
		this.orgId = as.orgId;
		this.orgName = as.orgName;
		this.items = as.items;
	}
	
	public static AnnotationSet createRandom() {
		AnnotationSet as = new AnnotationSet();
		as.id = "" + System.currentTimeMillis();
		as.creatorId = "1";
		as.schemaVersion = "v1";
		as.creatorType = "expert";
		as.orgId = "1";
		as.orgName = "University of Chicago";
		as.imageSets = new ArrayList<String>();
		as.imageSets.add("20160927163807");
		as.imageSets.add("20160927180917");
		as.items = new ArrayList<Object>();
		as.items.add(ObjectType1.createRandom());
		as.items.add(new Mask());
		return as;
	}
	
	public static AnnotationSet readFromJson(String jsonString) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return (AnnotationSet) mapper.readValue(jsonString, AnnotationSet.class);
	}
	public static String getJson(AnnotationSet as) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(as);
	}
}

class ObjectType1 {
	@JsonProperty ("objectId")
	public String objectId;
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public double getDiameter() {
		return diameter;
	}
	public void setDiameter(double diameter) {
		this.diameter = diameter;
	}
	public double[] getPoiData() {
		return poiData;
	}
	public void setPoiData(double[] poiData) {
		this.poiData = poiData;
	}

	@JsonProperty("objectName")
	public String objectName;
	@JsonProperty("objectType")
	public String objectType;
	@JsonProperty("diameter")
	public double diameter;
	@JsonProperty("poiData")
	double [] poiData;
	public static ObjectType1 createRandom() {
		ObjectType1 i = new ObjectType1();
		i.objectId = "1";
		i.objectName = "Nodule";
		i.objectType = "" + (int) (Math.random() * 5 + 1);
		i.diameter = 5 + Math.random() * 20;
		i.poiData = new double [] {Math.random()*10, Math.random()*10, Math.random() * 10};
		return i;
	}
}

class Mask {
	@JsonProperty ("mask")
	public Map<String, Map<String, String>> mask;
	
	public Mask() {
		mask = new HashMap();
		HashMap cont = new HashMap();
		cont.put("uri", "s3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/annot1");
		cont.put("type", "nrrd");
		mask.put("mask", cont);
	}
}
