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

import action.puzzle.Record;
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
		System.out.println("dataIO");
		String dataMode = jsonObject.get("dataMode").toString();
		if(dataMode.equals("record")){
			Record record = new Record();
			record.initForAjax(jsonObject);
			record.writeAction();
		}
		
		Gson gson = new Gson();
		this.rtnString = gson.toJson(this.validateMsgJson);		
	}
	
//	public String ajaxGetData() throws Exception{
//		init();
//		Gson gson = new Gson();
//		JSONObject jsonObject = (JSONObject) JSONValue.parse(data);
//		String dataKind = jsonObject.get("dataKind").toString();
//		System.out.println(jsonObject);
//		if(this.isAdmin && dataKind.equals("shop")){
//			jsonObject.put("queryMode","one");
//			ShopMember shopMember= new ShopMember();
//			this.rtnString = gson.toJson(shopMember.getData(jsonObject));
//		}else if(this.isAdmin && dataKind.equals("shopList")){
//			jsonObject.put("queryMode","list");
//			ShopMember shopMember= new ShopMember();
//			this.rtnString = gson.toJson(shopMember.getData(jsonObject));
//		}else if(this.isAdmin && dataKind.equals("faq")){
//			Faq faq = new Faq();
//			this.rtnString = gson.toJson(faq.getData(jsonObject));
//		}else if(dataKind.equals("generalProduct")){
//			GeneralProduct generalProduct = new GeneralProduct();
//			Map<String, Object> data = generalProduct.getOriginalData(Integer.parseInt(jsonObject.get("seq").toString()));
//			this.rtnString = gson.toJson(data);
//		}else if(dataKind.equals("generalProductList")){
//			GeneralProduct generalProduct = new GeneralProduct();
//			this.rtnString = gson.toJson(generalProduct.getData(jsonObject));
//		}else if(this.isAdmin && dataKind.equals("qna")){
//			Qna qna = new Qna();
//			this.rtnString = gson.toJson(qna.getData(jsonObject));
//		}else if(dataKind.equals("chartData")){
//			ChartData chartData = new ChartData();
//			this.rtnString = gson.toJson(chartData.getData(jsonObject));
//		}
//		
//		return SUCCESS;		
//	}
	
//	public String ajaxAction() throws Exception{
//		init();
//		JSONObject jsonObject = (JSONObject) JSONValue.parse(data);
//		String actionName = jsonObject.get("actionName").toString();
//		if(actionName.equals("deliveryOK")){
//			Order order = new Order();
//			order.deliveryOK(jsonObject);
////			this.rtnString = true;
//		}
//		
//		Gson gson = new Gson();
//		this.rtnString = gson.toJson(validateMsgMap);
//		
//		return SUCCESS;	
//	}
}
