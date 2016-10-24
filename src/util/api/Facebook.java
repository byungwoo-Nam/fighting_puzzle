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
	private String graphURL = "https://graph.facebook.com";
	private String accessTokenURL = "https://graph.facebook.com/oauth/access_token";
	private String userGraphURL = graphURL + "/me";
	private String client_id = "1709422206050349";
	private String client_secret = "36a6e720d504d76d5fcff75d453d10ca";
	private String redirectURL = codeConfig.getDomain() + "loginCallback.do";
	private String loginAPI = oauthURL + "?state=facebook&client_id=" + client_id + "&redirect_uri=" + redirectURL;
	private String accessTokenAPI = accessTokenURL + "?client_id=" + client_id + "&redirect_uri=" + redirectURL + "&client_secret=" + client_secret + "&code=";
	private String userGraphAPI = userGraphURL + "?access_token=";
	
	public JSONObject getUser(String code){
		JSONObject jsonObject = this.urlConnector.getData(accessTokenAPI+code);
		JSONObject userObject = null;
		if(jsonObject.containsKey("access_token")){
			userObject = this.urlConnector.getData(userGraphAPI+jsonObject.get("access_token"));
		}
		return userObject;
	}
}