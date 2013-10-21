package retrievers.flickr;

import java.util.Map;
import java.util.Map.Entry;
import com.google.common.collect.Maps;

import services.StedrConstants;

public abstract class FlickrQuery {
	
	protected abstract String getMethodName();
	
	protected static final String API_KEY = StedrConstants.FLICKR_API_KEY;
	protected static final String REST_API_URL = "http://api.flickr.com/services/rest/";
	
	private Map<String, Object> params = Maps.newConcurrentMap();
	
	protected void addParameter(String key, Object value) {
		params.put(key, value);
	}
	
	protected String makeRequestUrl() {
		StringBuffer sb = new StringBuffer();

		sb.append(REST_API_URL);
		sb.append("?method=").append(getMethodName());
		sb.append("&api_key=").append(API_KEY);
		sb.append("&format=rest");
		
		setAdditionalParams();
		
		for (Entry<String, Object> param : params.entrySet()) {
			sb.append("&").append(param.getKey()).append("=").append(param.getValue());
		}
		
		return sb.toString();
	}
	
	
	protected abstract void setAdditionalParams();

}
