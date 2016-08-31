/*
 * CustomAuthRequestHandler.java
 *
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.gehc.model.AuthPolicy;
import com.gehc.model.TokenAuthorizerContext;
import com.gehc.util.CheckPolicy;
import com.gehc.util.Keyloader;
import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Request Handler to process all requests to the Custom authorizer from the AWS API Gateway
 */
public class CustomAuthRequestHandler implements RequestHandler<TokenAuthorizerContext, AuthPolicy> {

    private LambdaLogger logger;
    private String principalId;
    private static JSONArray keySet = Keyloader.loadKeys();
    private static Pattern pattern = Pattern.compile( "^(Bearer|bearer)[ ]{1}.+$" );
    private Matcher matcher;

    @Override
    public AuthPolicy handleRequest( TokenAuthorizerContext input, Context context ) {

        logger = context.getLogger();
        String token = input.getAuthorizationToken();
        AuthPolicy authPolicy = null;

        if (verifyJWT( keySet, token ) ) {
            authPolicy = getAuthPolicy( input );
        }
        return authPolicy;

    }

    /**
     * Generates runtime access policy for client requests.
     *
     * @param input
     * @return
     */
    private AuthPolicy getAuthPolicy( TokenAuthorizerContext input ) {

        boolean allowAccess = false;
        String methodArn = input.getMethodArn();
        logger.log( "Method Arn:" + methodArn );
        String[] arnPartials = methodArn.split( ":" );
        String region = arnPartials[3];
        String awsAccountId = arnPartials[4];
        String[] apiGatewayArnPartials = arnPartials[5].split( "/" );
        String restApiId = apiGatewayArnPartials[0];
        String stage = apiGatewayArnPartials[1];
        String httpMethodStr = apiGatewayArnPartials[2];
        AuthPolicy.HttpMethod httpMethod = Stream.of( AuthPolicy.HttpMethod.values() )
                .filter( item -> item.toString().equalsIgnoreCase( httpMethodStr ) )
                .findFirst().get();

        String resource = ""; // root resource

        for (int i = 3; i <= apiGatewayArnPartials.length - 1; i++) {
            resource += apiGatewayArnPartials[i] + "/";
        }
        resource = resource.substring( 0, resource.length() - 1 );
        logger.log( "\nResource:" + resource );

        if (principalId != null) {
            Set<String> subjectRoles = CheckPolicy.getInstance().getSubjectRoles( principalId, logger );

            if (subjectRoles != null && subjectRoles.contains( "geadmin" )) {
                return new AuthPolicy( principalId, AuthPolicy.PolicyDocument.getAllowAllPolicy( region, awsAccountId, restApiId, stage ) );
            }

            Set<String> resources = CheckPolicy.getInstance().getResources( subjectRoles, logger );
            return new AuthPolicy( principalId, AuthPolicy.PolicyDocument.getAllowMultiplePolicy( region, awsAccountId, restApiId, stage, resources ) );
        }
        return null;
    }

    /**
     * Verifies the authenticity and validity of the user JWT token.
     *
     * @param keySet
     * @param token
     * @return
     */
    private boolean verifyJWT( JSONArray keySet, String token ) {
        try {
            matcher = pattern.matcher( token );
            if (!matcher.matches()) {
                logger.log( "Invalid token format" );
                return false;
            }
            token = token.replace( matcher.group( 1 ), "" ).trim();
            String[] tokenParts = token.split( "\\." );

            Optional<JSONObject> optionalKey = checkIfValidKid( keySet, tokenParts[0] );

            if (optionalKey == null && optionalKey.get() == null) {
                logger.log( "Invalid kid supplied \n" );
                return false;
            }
            logger.log( "Valid Kid\n" );

            if (!checkIfValidSignature( optionalKey, token )) {
                logger.log( "Invalid Signature\n" );
                return false;
            }
            logger.log( "Valid Signature\n" );

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    private Optional<JSONObject> checkIfValidKid( JSONArray keySet, String tokenPart ) {
        try {
            JSONParser parser = new JSONParser();
            byte[] headerBytes = Base64.decodeBase64( tokenPart );
            Object header = parser.parse( new String( headerBytes ) );
            JSONObject jsonObject = (JSONObject) header;
            String kid = jsonObject.get( "kid" ).toString();

            return keySet.stream().filter( item -> ((JSONObject) item).get( "kid" ).toString().equalsIgnoreCase( kid ) ).findFirst();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private boolean checkIfValidSignature( Optional<JSONObject> optionalKey, String token ) {

        try {
            JSONObject key = optionalKey.get();
            KeyFactory keyFactory = KeyFactory.getInstance( "RSA" );
            String mod = key.get( "n" ).toString();
            String exp = key.get( "e" ).toString();

            BigInteger modulus = new BigInteger( 1, Base64.decodeBase64( mod.getBytes( Charset.defaultCharset() ) ) );
            BigInteger exponent = new BigInteger( 1, Base64.decodeBase64( exp.getBytes( Charset.defaultCharset() ) ) );

            RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec( modulus, exponent );
            PublicKey publicKey = keyFactory.generatePublic( pubKeySpec );

            Jwt<JwsHeader, Claims> jwt = Jwts.parser().setSigningKey( publicKey ).parseClaimsJws( token );
            principalId = jwt.getBody().getSubject();
            logger.log( "Principal Id:" + principalId + "\n" );
        } catch (SignatureException | InvalidKeySpecException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

}
