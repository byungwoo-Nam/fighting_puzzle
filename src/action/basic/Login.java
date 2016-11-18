package action.basic;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONObject;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import dao.FightingPuzzleDAO;
import dto.UserDTO;
import lombok.Data;
import util.api.Facebook;
import util.api.Kakao;
import util.config.CodeConfig;

@Data
public class Login extends ActionSupport  {
	private Map session;
	private ActionContext context;
	private WebApplicationContext wac;
	private Facebook facebook;
	private Kakao kakao;
	private FightingPuzzleDAO fightingPuzzleDAO;
	
	private JSONObject paramJson = new JSONObject();
	private JSONObject whereJson = new JSONObject();
	
	private String apiURL;	//	api 호출 주소
	private String code;	//	callback 파라미터
	private String state;		//	callback 종류(페이스북or카카오)
	
	private String nextActionNamespace;
	private String nextActionName;
	private String nextQueryString;
	
	public Login() {
		ServletContext servletContext = ServletActionContext.getServletContext();
	    this.wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	}

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	//	요소 초기화 및 세팅
	public void init(){
		this.fightingPuzzleDAO = (FightingPuzzleDAO)this.wac.getBean("user");
		this.context = ActionContext.getContext();	//session을 생성하기 위해
		this.session = this.context.getSession();		// Map 사용시
	}
	
	//	sns api 호출 세팅
	public String callAPI() throws Exception {
		if(this.state.equals("facebook")){
			this.facebook = new Facebook();
			this.apiURL = this.facebook.getLoginAPI();
		}else if(this.state.equals("kakao")){
			this.kakao = new Kakao();
			this.apiURL = this.kakao.getLoginAPI();
		}
		return SUCCESS;
	}
	
	//	api call back
	public String callBack() throws Exception {
		init();
		Gson gson = new Gson();
		JSONObject userObject = null;
		UserDTO userDTO = null; 
		if(state.equals("facebook")){
			this.facebook = new Facebook();
			userObject = this.facebook.getUser(this.code);
			if(userObject==null){
				return "error";
			}
			userObject.put("snsCode", 1);
			userObject.put("pictureUrl", facebook.getProfileURL() + "/" + userObject.get("id") + "/picture");
		}else if(state.equals("kakao")){
			this.kakao = new Kakao();
			userObject = this.kakao.getUser(this.code);
			if(userObject==null){
				return "error";
			}
			userObject.put("snsCode", 2);
			userObject.put("name", userObject.get("nickName"));
			userObject.put("pictureUrl", userObject.remove("profileImageURL"));
			
		}
		
		this.whereJson.put("snsCode", userObject.get("snsCode"));
		this.whereJson.put("id", userObject.get("id"));
		this.paramJson.put("whereJson", this.whereJson);
		
		userDTO = (UserDTO)this.fightingPuzzleDAO.getOneRow(this.paramJson);
		int user_seq = 0;
		
		if(userDTO == null){
			// 새로 등록
			userDTO = gson.fromJson(userObject.toJSONString(), UserDTO.class);
			user_seq = this.fightingPuzzleDAO.write(userDTO);
		}else{
			userDTO.setName(userObject.get("name").toString());
			userDTO.setPictureUrl(userObject.get("pictureUrl").toString());
			this.fightingPuzzleDAO.update(userDTO);
			user_seq = userDTO.getSeq();
		}
		
		this.nextActionNamespace = (session.get("nextActionNamespace")==null) ? "/" : session.get("nextActionNamespace").toString();
		
		if (session.get("nextActionName")!=null) {
			this.nextActionName = session.get("nextActionName").toString();
		}else{
			this.nextActionName = "index";
		}
		
		this.nextQueryString = (session.get("nextQueryString")!=null) ? session.get("nextQueryString").toString() : "";
		
		session.remove("nextActionNamespace");
		session.remove("nextActionName");
		session.remove("nextQueryString");
		
		this.session.put("isMember", "true");
		this.session.put("user_seq", user_seq);
		this.session.put("user_picture", userDTO.getPictureUrl());
		this.context.setSession(this.session);
		
		return SUCCESS;
	}
	
	public String logout(){
		init();
		this.session.clear();
		this.context.setSession(this.session);
		return SUCCESS;		
	}
}
