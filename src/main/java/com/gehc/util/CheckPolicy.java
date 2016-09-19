///*
// * CheckPolicy.java
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
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
//import com.amazonaws.services.dynamodbv2.document.DynamoDB;
//import com.amazonaws.services.lambda.runtime.LambdaLogger;
//import com.gehc.dao.ResourceDAO;
//import com.gehc.dao.SubjectDAO;
//
//import java.util.Set;
//
///**
// * CheckPolicy derives all the eligible resources for the given Principal Id.
// */
//public class CheckPolicy {
//
//    private static CheckPolicy instance;
//    private static AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient().withEndpoint( "https://dynamodb.us-east-1.amazonaws.com" );
//    private static DynamoDB dynamoDB = new DynamoDB( dynamoDBClient );
//
//    public static CheckPolicy getInstance() {
//        if (instance == null) {
//            instance = new CheckPolicy();
//        }
//        return instance;
//    }
//
//    public Set<String> getResources( Set<String> subjectRoles, LambdaLogger logger ) {
//
//        Set<String> resourceRoles = null;
//        try {
//            ResourceDAO resourceDAO = ResourceDAO.getInstance( dynamoDB );
//            resourceRoles = resourceDAO.getResourceRoles( subjectRoles, logger );
//            logger.log( "\nResource Roles:" + resourceRoles );
//
//        } catch (Exception ex) {
//            logger.log( "\n Exception in getting Resources" + ex.getMessage() );
//        }
//
//        return resourceRoles;
//    }
//
//    public Set<String> getSubjectRoles( String principalId, LambdaLogger logger ) {
//
//        Set<String> subjectRoles = null;
//        try {
//            SubjectDAO subjectDAO = SubjectDAO.getInstance( dynamoDB );
//            subjectRoles = subjectDAO.getSubjectRoles( principalId, logger );
//            logger.log( "\nSubject Roles:" + subjectRoles );
//        } catch (Exception ex) {
//            logger.log( "\n Exception in getting Roles" + ex.getMessage() );
//        }
//        return subjectRoles;
//    }
//
//
//}
