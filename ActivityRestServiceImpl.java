package com.rest.activities.service.impl;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.rest.activities.model.Activity;
import com.rest.activities.model.Notification;
import com.rest.activities.model.Reference;
import com.rest.activities.service.ActivityRestService;
import com.rest.core.service.PB360Rest;
import com.rest.core.service.impl.PB360RestImpl;

@SuppressWarnings("unchecked")
public class ActivityRestServiceImpl implements ActivityRestService {

	private String activitiesResourceUrl;
	private static final String ACTIVITIES_RESOURCE = "v1/activities";
	private static final String REFERENCES_RESOURCE = "/references";
	private static final String MEMBER_RESOURCE = "v1/members";
	private static final String NOTIFICATION_RESOURCE = "/notifications";
	
	//this is comments
	private HttpHeaders getProperties() {
		InputStream fis;
		Properties restUrlProperties = new Properties();
		String restHeaders;
		final String REST_URL_FILE = "restUrl.properties";
		final String PB360_ACTIVITIES_URL = "pb360.activities.url";
		final String REST_HEADERS = "rest.headers";
		final String COMMA_SEPARATOR = ",";
		final String COLON_RESOURCE = ":";
		HttpHeaders httpHeaders = null;
		URL url = ActivityRestServiceImpl.class.getClassLoader().getResource(REST_URL_FILE);
		try {
			fis = url.openConnection().getInputStream();
			restUrlProperties.load(fis);
			activitiesResourceUrl = restUrlProperties.getProperty(PB360_ACTIVITIES_URL);
			restHeaders = restUrlProperties.getProperty(REST_HEADERS);
			httpHeaders = new HttpHeaders();
			if (restHeaders != null && !restHeaders.isEmpty()) {
				String headersList[] = restHeaders.split(COMMA_SEPARATOR);
				for (int index = 0; index < headersList.length; index++) {
					String header[] = headersList[index].split(COLON_RESOURCE);
					if (header.length > 1) {
						httpHeaders.add(header[0], header[1]);
					}
				}
			}
		} catch (Exception e) {

		}
		return httpHeaders;
	}

	@Override
	public Activity save(Activity activity) {
		HttpHeaders httpHeaders = getProperties();
		PB360Rest pB360Rest = new PB360RestImpl();
		pB360Rest.setHeaders(httpHeaders);
		pB360Rest.setURL(activitiesResourceUrl + ACTIVITIES_RESOURCE);
		ResponseEntity<Activity> postResponse = (ResponseEntity<Activity>) pB360Rest.save(activity, Activity.class);
		return postResponse.getBody();
	}

	@Override
	public Reference save(Reference reference, Activity activity) {
		HttpHeaders httpHeaders = getProperties();
		PB360Rest pB360Rest = new PB360RestImpl();
		pB360Rest.setHeaders(httpHeaders);
		pB360Rest.setURL(activitiesResourceUrl + ACTIVITIES_RESOURCE + "/" + activity.getActivityId()
				+ REFERENCES_RESOURCE);
		ResponseEntity<Reference> postResponse = (ResponseEntity<Reference>) pB360Rest.save(reference, Reference.class);
		return postResponse.getBody();
	}

	@Override
	public Notification save(Notification notification){
		HttpHeaders httpHeaders = getProperties();
		PB360Rest pB360Rest = new PB360RestImpl();
		pB360Rest.setHeaders(httpHeaders);
		pB360Rest.setURL(activitiesResourceUrl + MEMBER_RESOURCE + "/" + notification.getMemberId()
				+ NOTIFICATION_RESOURCE);
		ResponseEntity<Notification> postResponse = (ResponseEntity<Notification>) pB360Rest.save(notification, Notification.class);
		return postResponse.getBody();
	}
}
