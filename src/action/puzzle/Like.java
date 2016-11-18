package action.puzzle;

import java.util.ArrayList;
import java.util.List;
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
import dto.LikeDTO;
import lombok.Data;
import util.config.CodeConfig;
import util.config.FormValidate;

@Data
public class Like extends ActionSupport  {
	private Map session;
	private ActionContext context;
	private WebApplicationContext wac;
	private FormValidate formValidate = new FormValidate();
	private CodeConfig codeConfig = new CodeConfig();
	private FightingPuzzleDAO likeDAO;
	private LikeDTO likeDTO;
	private List<LikeDTO> dataList;
	
	private JSONObject paramJson = new JSONObject();
	private JSONObject whereJson = new JSONObject();
	private JSONObject ajaxJson = new JSONObject();
	private JSONObject validateResJson = new JSONObject();
	private String rtnString;
	
	private int sortCol = 0;											// 	정렬 컬럼
    private String sortVal = "";										// 	정렬 내용
    
	private JSONObject sortColKindJson = new JSONObject() {{
		// db상의 name과 매칭
		put(0,"seq");			// 기본값 정렬
		put(1,"seq");			// 등록번호 정렬
	}};
	
	private boolean isAjax;	//	ajax요청시
	
	private int seq;				//	좋아요 seq
	private int puzzle_seq;		//	퍼즐 seq
	private int user_seq;		//	회원 seq
	
	public Like() {
		ServletContext servletContext = ServletActionContext.getServletContext();
	    this.wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	    this.likeDTO = new LikeDTO();
	}

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	//	요소 초기화 및 세팅
	public void init(){
		this.likeDAO = (FightingPuzzleDAO)this.wac.getBean("like");
		
		this.context = ActionContext.getContext();	//session을 생성하기 위해
		this.session = this.context.getSession();		// Map 사용시
	}
	
	//	ajax로 요청시 요소 초기화
	public void initForAjax(JSONObject jsonObject){
		this.ajaxJson = jsonObject;
		this.whereJson = this.ajaxJson.containsKey("whereJson") ? (JSONObject) this.ajaxJson.get("whereJson") : this.whereJson;
		this.isAjax = true;
	}
	
	public Object getList() throws Exception{
		init();
		
		this.sortCol = this.sortColKindJson.containsKey(this.sortCol) ? this.sortCol : 0;
		this.sortVal = this.sortVal.equals("ASC") ? "ASC" : "DESC";
		if(this.whereJson==null){
			this.whereJson.put("seq", this.seq);
		}
		
		this.paramJson.put("sortCol", this.sortColKindJson.get(this.sortCol));
		this.paramJson.put("sortVal", this.sortVal);
		this.paramJson.put("whereJson", this.whereJson);
		
		this.dataList = (List<LikeDTO>)this.likeDAO.getList(this.paramJson);
		
		return this.isAjax ? this.dataList : SUCCESS;
	}
	
	public Object getData() throws Exception{
		init();
		
		if(this.seq!=0){
			this.whereJson.put("seq", this.seq);
		}
		
		this.paramJson.put("whereJson", this.whereJson);
		
		this.likeDTO = (LikeDTO) this.likeDAO.getOneRow(this.paramJson);
		
		return this.isAjax ? this.likeDTO : SUCCESS;
	}
	
	public Object writeAction() throws Exception{
		init();

		this.user_seq = (int) session.get("user_seq");
		
		Gson gson = new Gson();
		
		if(this.isAjax){
			this.puzzle_seq = Integer.parseInt(this.ajaxJson.get("puzzle_seq").toString());
		}
		
		this.paramJson.put("puzzle_seq", this.puzzle_seq);
		this.validateResJson = this.formValidate.likeEditorForm(this.paramJson);
		this.paramJson.clear();
		
		if(!(boolean)this.validateResJson.get("res")){
			this.rtnString = gson.toJson(this.validateResJson);
			return "validation";
		}
		
		this.likeDTO.setPuzzle_seq(this.puzzle_seq);
		this.likeDTO.setUser_seq(this.user_seq);
		
		this.seq = this.likeDAO.write(this.likeDTO);
		
		return this.isAjax ? this.seq : SUCCESS;
	}
	
	public Object deleteAction() throws Exception{
		init();
		
		this.user_seq = (int) session.get("user_seq");
		
		if(this.isAjax){
			this.puzzle_seq = Integer.parseInt(this.ajaxJson.get("puzzle_seq").toString());
		}
		
		this.whereJson.put("user_seq", this.user_seq);
		this.whereJson.put("puzzle_seq", this.puzzle_seq);
		this.likeDTO = (LikeDTO) this.getData();
		
		if(this.likeDTO == null){
			return "validation";
		}
		
		this.paramJson.put("seq", this.likeDTO.getSeq());
		this.likeDAO.delete(paramJson);
		
		return this.isAjax ? this.seq : SUCCESS;
	}
}
