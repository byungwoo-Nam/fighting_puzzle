package util.api;

import util.config.CodeConfig;
import util.config.URLConnector;
import lombok.Data;
import lombok.Setter;

import org.json.simple.JSONObject;

import lombok.AccessLevel;

@Data
public class Kakao {
	
	private CodeConfig codeConfig = new CodeConfig();
	private URLConnector urlConnector = new URLConnector();
	
	private String oauthURL = "https://kauth.kakao.com/oauth/authorize";
	private String profileURL = "https://kapi.kakao.com/v1";
	private String accessTokenURL = "https://kauth.kakao.com/oauth/token";
	private String userIdURL = profileURL + "/user/access_token_info";
	private String userProfileURL = profileURL + "/api/talk/profile";
	private String client_id = "fece329b4ac35e6729cbbe12dd0de042";
	private String grant_type = "authorization_code";
	private String redirectURL = codeConfig.getDomain() + "loginCallback.do";
	private String loginAPI = oauthURL + "?state=kakao&client_id=" + client_id + "&redirect_uri=" + redirectURL + "&response_type=code";
	
	public JSONObject getUser(String code){
		JSONObject userObject = null;
		JSONObject userIdObject = null;
		JSONObject paramObject = new JSONObject();
		if(code!=null){
			paramObject.put("client_id", client_id);
			paramObject.put("redirect_uri", redirectURL);
			paramObject.put("grant_type", grant_type);
			paramObject.put("code", code);
			JSONObject jsonObject = this.urlConnector.getData(accessTokenURL, paramObject);
			paramObject.clear();

			if(jsonObject.containsKey("access_token")){
				paramObject.put("access_token", jsonObject.get("access_token"));
				userIdObject = this.urlConnector.getData(userIdURL, paramObject);
				userObject = this.urlConnector.getData(userProfileURL, paramObject);
				userObject.put("id", userIdObject.get("id"));
			}
		}
		return userObject;
	}
}