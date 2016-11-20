package action.basic;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import util.config.*;
import util.system.StringUtil;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import action.puzzle.Hashtag;
import action.puzzle.Like;
import action.puzzle.Puzzle;
import action.puzzle.Record;
import action.puzzle.Reply;
import lombok.Data;

@Data
public class Ajax extends ActionSupport  {
	private Map session;
	private ActionContext context;
	private WebApplicationContext wac;
	private FormValidate formValidate = new FormValidate();
	private JSONObject validateMsgJson = new JSONObject();
	private StringUtil stringUtil = new StringUtil();
	private boolean isAdmin;
	
	private String data;
	private String rtnString;
	
	public Ajax() {
		ServletContext servletContext = ServletActionContext.getServletContext();
	    this.wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	}

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	//	요소 초기화 및 세팅
	public void init(){
		this.context = ActionContext.getContext();	//	session을 생성하기 위해
		this.session = this.context.getSession();		// 	Map 사용시
		this.isAdmin = StringUtil.stringToBool(StringUtil.isNullOrSpace((String)session.get("isAdmin"),"").trim());
	}
	
	public String ajaxConnector() throws Exception{
		init();
		JSONObject jsonObject = (JSONObject) JSONValue.parse(this.data);
		String ajaxMode = jsonObject.get("ajaxMode").toString();
		if(ajaxMode.equals("formValidate")){
			formValidate(jsonObject);
		}else if(ajaxMode.equals("dataInsert")){
			dataInsert(jsonObject);
		}else if(ajaxMode.equals("dataUpdate")){
			dataUpdate(jsonObject);
		}else if(ajaxMode.equals("dataDelete")){
			dataDelete(jsonObject);
		}else if(ajaxMode.equals("getData")){
			this.rtnString = getData(jsonObject);
		}else if(ajaxMode.equals("getTotalCount")){
			this.rtnString = getTotalCount(jsonObject);
		}
		
		return SUCCESS;
	}
	
	public void formValidate(JSONObject jsonObject){
		init();
		String formID = jsonObject.get("formID").toString();
		if(formID.equals("puzzleEditorForm")){
			this.validateMsgJson = this.formValidate.puzzleEditorForm(jsonObject);
		}
		
		Gson gson = new Gson();
		this.rtnString = gson.toJson(this.validateMsgJson);		
	}
	
	public void dataInsert(JSONObject jsonObject) throws Exception{
		init();
		String dataMode = jsonObject.get("dataMode").toString();
		if(dataMode.equals("record")){
			Record record = new Record();
			record.initForAjax(jsonObject);
			record.writeAction();
		}else if(dataMode.equals("reply")){
			Reply reply = new Reply();
			reply.initForAjax(jsonObject);
			reply.writeAction();
		}else if(dataMode.equals("like")){
			Like like = new Like();
			like.initForAjax(jsonObject);
			like.writeAction();
		}
		
		Gson gson = new Gson();
		this.rtnString = gson.toJson(this.validateMsgJson);		
	}
	
	public void dataUpdate(JSONObject jsonObject) throws Exception{
		init();
		String dataMode = jsonObject.get("dataMode").toString();
		if(dataMode.equals("reply")){
			Reply reply = new Reply();
			reply.initForAjax(jsonObject);
			reply.updateAction();
		}
		
		Gson gson = new Gson();
		this.rtnString = gson.toJson(this.validateMsgJson);		
	}
	
	public void dataDelete(JSONObject jsonObject) throws Exception{
		init();
		String dataMode = jsonObject.get("dataMode").toString();
		
		if(dataMode.equals("like")){
			Like like = new Like();
			like.initForAjax(jsonObject);
			like.deleteAction();
		}else if(dataMode.equals("reply")){
			Reply reply = new Reply();
			reply.initForAjax(jsonObject);
			reply.deleteAction();
		}
		
		Gson gson = new Gson();
		this.rtnString = gson.toJson(this.validateMsgJson);	
	}
	
	public String getData(JSONObject jsonObject) throws Exception{
		init();
		String s = "";
		Gson gson = new Gson();
		String dataMode = jsonObject.get("dataMode").toString();
		JSONObject whereJson = new JSONObject();
		JSONObject searchJson = new JSONObject();
		if(dataMode.equals("reply")){
			Reply reply = new Reply();
			whereJson.put("puzzle_seq", jsonObject.get("puzzle_seq"));
			reply.initForAjax(new JSONObject(){{put("whereJson", whereJson); put("pageNum", jsonObject.get("pageNum"));}});
			s = gson.toJson(reply.getList());
		}else if(dataMode.equals("puzzleList")){
			Puzzle puzzle = new Puzzle();
			puzzle.initForAjax(jsonObject);
			Object rtnObj = puzzle.getList();
			s = (rtnObj == null) ? null : gson.toJson(rtnObj);
		}else if(dataMode.equals("search")){
			Hashtag hashtag = new Hashtag();
			searchJson.put("hashtag", jsonObject.get("keyword"));
			jsonObject.put("searchJson", searchJson);
			jsonObject.put("sortCol", 2);
			jsonObject.put("sortVal", "ASC");
			jsonObject.put("searchMode", true);
			hashtag.initForAjax(jsonObject);
			Object rtnObj = hashtag.getList();
			s = (rtnObj == null) ? null : gson.toJson(rtnObj);
		}
		
		return s;	
	}
	
	public String getTotalCount(JSONObject jsonObject) throws Exception{
		init();
		
		String s = "";
		Gson gson = new Gson();
		String dataMode = jsonObject.get("dataMode").toString();
		JSONObject whereJson = new JSONObject();
		
		if(dataMode.equals("reply")){
			Reply reply = new Reply();
			whereJson.put("puzzle_seq", jsonObject.get("puzzle_seq"));
			reply.initForAjax(new JSONObject(){{put("whereJson", whereJson);}});
			s = reply.getTotalCount() + "";
		}
		
		return s;	
	}
}
