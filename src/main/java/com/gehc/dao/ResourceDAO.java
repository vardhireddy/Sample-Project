///*
// * ResourceDAO.java
// *
// * Copyright (c) 2016 by General Electric Company. All rights reserved.
// *
// * The copyright to the computer software herein is the property of
// * General Electric Company. The software may be used and/or copied only
// * with the written permission of General Electric Company or in accordance
// * with the terms and conditions stipulated in the agreement/contract
// * under which the software has been supplied.
// 
//package com.gehc.dao;
//
//import com.amazonaws.services.dynamodbv2.document.*;
//import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
//import com.amazonaws.services.lambda.runtime.LambdaLogger;
//
//import java.util.*;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.util.stream.Stream;
//import java.util.stream.StreamSupport;
//
//public class ResourceDAO {
//
//    private DynamoDB dynamoDB = null;
//
//    private static Pattern methodArnPattern = Pattern.compile( ".*?/.*?/(.*)" );
//
//    private static String guidPattern ="[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";
//
//    private Matcher arnMatcher;
//
//    private Set<String> resources;
//
//    private static ResourceDAO instance;
//
//    public static ResourceDAO getInstance(DynamoDB dynamoDB) {
//        if (instance == null) {
//            instance = new ResourceDAO( dynamoDB );
//        }
//        return instance;
//    }
//
//    private ResourceDAO( DynamoDB dynamoDB ) {
//        this.dynamoDB = dynamoDB;
//    }
//
//    public Set<String> getResourceRoles( Set<String> roles, LambdaLogger logger ) {
//
//        resources = new HashSet<>();
//        logger.log( "Roles......." + roles );
//        roles.stream().forEach( role -> fetchResources( role, logger ) );
//
//        return resources;
//
//    }
//
//    private void fetchResources( String role, LambdaLogger logger ) {
//
//        try {
//            HashMap<String, String> nameMap = new HashMap<>();
//            nameMap.put( "#resourcerole", "resourcerole" );
//
//            HashMap<String, Object> valueMap = new HashMap<>();
//            valueMap.put( ":resourcerole", role );
//
//            QuerySpec querySpec = new QuerySpec()
//                    .withKeyConditionExpression( "#resourcerole = :resourcerole" )
//                    .withNameMap( nameMap )
//                    .withValueMap( valueMap );
//
//            Table resourceTable = dynamoDB.getTable( "HC_ROLE_RESOURCE_MAPPING" );
//            ItemCollection<QueryOutcome> items = resourceTable.query( querySpec );
//            Iterator iterator = items.iterator();
//            Stream<Item> stream = StreamSupport.stream( Spliterators.spliteratorUnknownSize( iterator, Spliterator.ORDERED ), false );
//            Optional<Item> itemOptional = stream.findFirst();
//            if (itemOptional.isPresent()) {
//                resources.addAll( (Set<String>)itemOptional.get().get( "resources" ) );
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//}
//*/