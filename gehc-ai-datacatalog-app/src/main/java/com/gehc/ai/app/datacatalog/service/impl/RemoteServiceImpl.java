package com.gehc.ai.app.datacatalog.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.gehc.ai.app.datacatalog.service.IRemoteService;

@Configuration
@Component
public class RemoteServiceImpl implements IRemoteService {

	private static Logger logger = LoggerFactory.getLogger(RemoteServiceImpl.class);

	@Value("${uom.user.me.url}")
	private String uomMeUrl;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public String getOrgIdBasedOnSessionToken(String authToken) throws Exception {
		String orgId = null;
		if (null != authToken) {
			HttpHeaders headers = new HttpHeaders();
			headers.set(HttpHeaders.AUTHORIZATION, authToken);
			headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<>(headers);
			ResponseEntity<Object> responseEntity = null;
			responseEntity = restTemplate.exchange(uomMeUrl, HttpMethod.GET, requestEntity, Object.class);
			Object userObject = "{}";
			if (null != responseEntity && responseEntity.hasBody()) {
				userObject = responseEntity.getBody();
				LinkedHashMap<String, Object> userData = (LinkedHashMap) userObject;
				Iterator<Entry<String, Object>> itr = userData.entrySet().iterator();
				while (itr.hasNext() && orgId == null) {
					Entry<String, Object> entry = itr.next();
					String key = entry.getKey();
					if (key.equalsIgnoreCase("role")) {
						ArrayList<Object> list = (ArrayList<Object>) entry.getValue();
						for (int i = 0; i < list.size(); i++) {
							LinkedHashMap<String, Object> roleValue = (LinkedHashMap) (list.get(i));
							Iterator<Entry<String, Object>> roleItr = roleValue.entrySet().iterator();
							while (roleItr.hasNext()) {
								Entry<String, Object> roleEntry = roleItr.next();
								String roleKey = roleEntry.getKey();
								if (roleKey.equalsIgnoreCase("scopingOrganization")) {
									Object valueInRole = (Object) roleEntry.getValue();
									Map<String, String> dataInRole = (HashMap<String, String>) valueInRole;
									if (dataInRole.get("reference").startsWith("organization")) {
										orgId = dataInRole.get("reference")
												.substring((dataInRole.get("reference").indexOf("/")) + 1);
										logger.info("*** In getOrgIdBasedOnSessionToken, Org id = " + orgId);
										break;
									}
								}
							}
						}
					}
				}
			} else {
				logger.info("!!! Response Entity User Object has no content");
			}
		}
		return orgId;
	}
}
