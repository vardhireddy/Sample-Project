///*
// * SubjectDAO.java
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
//import java.util.stream.Stream;
//import java.util.stream.StreamSupport;
//
//public class SubjectDAO {
//
//    private DynamoDB dynamoDB = null;
//    private static SubjectDAO instance;
//
//    public static SubjectDAO getInstance(DynamoDB dynamoDB) {
//        if (instance == null) {
//            instance = new SubjectDAO( dynamoDB );
//        }
//        return instance;
//    }
//
//    private SubjectDAO( DynamoDB dynamoDB ) {
//        this.dynamoDB = dynamoDB;
//    }
//
//    public Set<String> getSubjectRoles( String principalId, LambdaLogger logger ) {
//
//        Set<String> roles = null;
//        Table subjectTable = dynamoDB.getTable( "HC_SUBJECT_ROLE_MAPPING" );
//
//        HashMap<String, String> nameMap = new HashMap<>();
//        nameMap.put( "#subjectIdentifier", "subjectIdentifier" );
//
//        HashMap<String, Object> valueMap = new HashMap<>();
//        valueMap.put( ":subjectIdentifier", principalId );
//
//        QuerySpec querySpec = new QuerySpec()
//                .withKeyConditionExpression( "#subjectIdentifier = :subjectIdentifier" )
//                .withNameMap( nameMap )
//                .withValueMap( valueMap );
//
//        ItemCollection<QueryOutcome> items = subjectTable.query( querySpec );
//        Iterator iterator = items.iterator();
//        Stream<Item> stream = StreamSupport.stream( Spliterators.spliteratorUnknownSize( iterator, Spliterator.ORDERED ), false );
//        Optional<Item> itemOptional = stream.findFirst();
//        if (itemOptional.isPresent()) {
//            roles = (LinkedHashSet)((LinkedHashMap)itemOptional.get().get( "attributes" )).get( "subjectRoles" );
//            System.out.println(roles);
//        } else {
//            logger.log( "Item missing" );
//        }
//        return roles;
//    }
//}
//*/