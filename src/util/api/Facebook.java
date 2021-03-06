package util.api;

import util.config.CodeConfig;
import util.config.URLConnector;
import lombok.Data;
import lombok.Setter;

import org.json.simple.JSONObject;

import lombok.AccessLevel;

@Data
public class Facebook {
	
	private CodeConfig codeConfig = new CodeConfig();
	private URLConnector urlConnector = new URLConnector();
	
	private String oauthURL = "https://www.facebook.com/dialog/oauth";
	private String profileURL = "https://graph.facebook.com";
	private String accessTokenURL = "https://graph.facebook.com/oauth/access_token";
	private String userProfileURL = profileURL + "/me";
	private String client_id = "1709422206050349";
	private String client_secret = "36a6e720d504d76d5fcff75d453d10ca";
	private String redirectURL = codeConfig.getDomain() + "loginCallback.do";
	private String loginAPI = oauthURL + "?state=facebook&client_id=" + client_id + "&redirect_uri=" + redirectURL;
	
	public JSONObject getUser(String code){
		JSONObject userObject = null;
		JSONObject paramObject = new JSONObject();
		if(code!=null){
			paramObject.put("client_id", client_id);
			paramObject.put("redirect_uri", redirectURL);
			paramObject.put("client_secret", client_secret);
			paramObject.put("code", code);
			JSONObject jsonObject = this.urlConnector.getData(accessTokenURL, paramObject);
			paramObject.clear();
			
			if(jsonObject.containsKey("access_token")){
				paramObject.put("access_token", jsonObject.get("access_token"));
				userObject = this.urlConnector.getData(userProfileURL, paramObject);
			}
		}
		return userObject;
	}
}