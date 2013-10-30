package utils;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Utility class implementing some convenience methods to work with HTTP.
 * 
 * @author Simon Stastny
 * 
 */
public class HttpUtils {

	/**
	 * Method for getting a document on a specified URL. 
	 * @param url url where the document is located at
	 * @return contents of the document
	 */
	public static String getDocument(String url) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}
			
			HttpEntity getResponseEntity = getResponse.getEntity();

			StringWriter writer = new StringWriter();
			IOUtils.copy(getResponseEntity.getContent(), writer);
			return writer.toString();

		} catch (IOException e) {
			getRequest.abort();
		}

		return null;
	}

}
