///*
// * Keyloader.java
// *
// * Copyright (c) 2016 by General Electric Company. All rights reserved.
// *
// * The copyright to the computer software herein is the property of
// * General Electric Company. The software may be used and/or copied only
// * with the written permission of General Electric Company or in accordance
// * with the terms and conditions stipulated in the agreement/contract
// * under which the software has been supplied.
// */
//package com.gehc.util;
//
//import com.amazonaws.util.IOUtils;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//
//import java.io.InputStream;
//
///**
// * Keyloader to load JWT public keys.
// */
//public class Keyloader {
//
//    public static JSONArray loadKeys() {
//        System.out.println("\n Keyloader invoked...\n");
//        JSONArray keySet = null;
//        try {
//            InputStream inputStream = Keyloader.class.getResourceAsStream( "/cognito-sp-keys.json" );
//            JSONParser parser = new JSONParser();
//            Object object = parser.parse( IOUtils.toString( inputStream ));
//            JSONObject jsonObject = (JSONObject) object;
//            keySet = (JSONArray)jsonObject.get( "keys" );
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return keySet;
//    }
//}
