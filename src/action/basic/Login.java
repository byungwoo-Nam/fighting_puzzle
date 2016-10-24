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
import util.config.CodeConfig;

@Data
public class Login extends ActionSupport  {
	private Map session;
	private ActionContext context;
	private WebApplicationContext wac;
	private Facebook facebook;
	private FightingPuzzleDAO fightingPuzzleDAO;
	
	private JSONObject paramJson = new JSONObject();
	private JSONObject whereJson = new JSONObject();
	
	private String apiURL;	//	api 호출 주소
	private String code;	//	callback 파라미터
	private String state;		//	callback 종류(페이스북or카카오)
	
	private String nextActionNamespace;
	private String nextActionName;
	
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
		if(state.equals("facebook")){
			this.facebook = new Facebook();
			this.apiURL = this.facebook.getLoginAPI();
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
			userObject = facebook.getUser(code);
			userObject.put("snsCode", 1);
			userObject.put("pictureUrl", facebook.getGraphURL() + "/" + userObject.get("id") + "/picture");
		}
		
		if(userObject==null){
			return "error";
		}

		this.whereJson.put("snsCode", userObject.get("snsCode"));
		this.whereJson.put("id", userObject.get("id"));
		this.paramJson.put("whereJson", this.whereJson);
		
		userDTO = (UserDTO)this.fightingPuzzleDAO.getOneRow(this.paramJson);
		int user_seq = 0;
		
		if(userDTO == null){
			// 새로 등록
			System.out.println("새로등록");
			userDTO = gson.fromJson(userObject.toJSONString(), UserDTO.class);
			user_seq = this.fightingPuzzleDAO.write(userDTO);
		}else{
			user_seq = userDTO.getSeq();
		}
		
		this.nextActionNamespace = (session.get("nextActionNamespace")==null) ? "/" : session.get("nextActionNamespace").toString();
		
		if (session.get("nextActionName")!=null) {
			this.nextActionName = session.get("nextActionName").toString();
		}else{
			this.nextActionName = "index";
		}
		
		session.remove("nextActionNamespace");
		session.remove("nextActionName");
		
		this.session.put("isMember", "true");
		this.session.put("user_seq", user_seq);
		this.context.setSession(this.session);
		
		return SUCCESS;
	}
}
