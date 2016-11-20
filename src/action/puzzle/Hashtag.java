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
import dto.HashtagDTO;
import lombok.Data;
import util.config.CodeConfig;
import util.config.FormValidate;

@Data
public class Hashtag extends ActionSupport  {
	private Map session;
	private ActionContext context;
	private WebApplicationContext wac;
	private FormValidate formValidate = new FormValidate();
	private CodeConfig codeConfig = new CodeConfig();
	private FightingPuzzleDAO hashtagDAO;
	private HashtagDTO hashtagDTO;
	private List<HashtagDTO> dataList;
	
	private JSONObject paramJson = new JSONObject();
	private JSONObject whereJson = new JSONObject();
	private JSONObject searchJson = new JSONObject();
	private JSONObject validateResJson = new JSONObject();
	private String rtnString;
	
	private int sortCol = 0;											// 	정렬 컬럼
    private String sortVal = "";										// 	정렬 내용
    
    private boolean searchMode = false; 
    
	private JSONObject sortColKindJson = new JSONObject() {{
		// db상의 name과 매칭
		put(0,"seq");			// 기본값 정렬
		put(1,"seq");			// 등록번호 정렬
		put(2,"hashtag");		// 해시태그 정렬
	}};
	
	private boolean isAjax;	//	ajax요청시
	
	private int seq;				//	hashtag seq
	private int puzzle_seq;		//	퍼즐 seq
	private String hashtag;		//	해시태그
	
	public Hashtag() {
		ServletContext servletContext = ServletActionContext.getServletContext();
	    this.wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	    this.hashtagDTO = new HashtagDTO();
	}

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	//	요소 초기화 및 세팅
	public void init(){
		this.hashtagDAO = (FightingPuzzleDAO)this.wac.getBean("hashtag");
		
		this.context = ActionContext.getContext();	//session을 생성하기 위해
		this.session = this.context.getSession();		// Map 사용시
	}
	
	//	ajax로 요청시 요소 초기화
	public void initForAjax(JSONObject jsonObject){
		this.whereJson = jsonObject.containsKey("whereJson") ? (JSONObject) jsonObject.get("whereJson") : this.whereJson;
		this.searchJson = jsonObject.containsKey("searchJson") ? (JSONObject) jsonObject.get("searchJson") : this.searchJson;
		this.sortCol = jsonObject.containsKey("sortCol") ? Integer.parseInt(jsonObject.get("sortCol").toString()) : this.sortCol;
		this.sortVal = jsonObject.containsKey("sortVal") ? jsonObject.get("sortVal").toString() : this.sortVal;
		this.searchMode = jsonObject.containsKey("searchMode") ? (boolean)jsonObject.get("searchMode") : this.searchMode;
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
		if(this.searchJson!=null){
			this.paramJson.put("searchJson", this.searchJson);
		}
		this.paramJson.put("searchMode", this.searchMode);
		
		this.dataList = (List<HashtagDTO>)this.hashtagDAO.getList(this.paramJson);
		
		return this.isAjax ? this.dataList : SUCCESS;
	}
	
	public Object getData() throws Exception{
		init();
		
		this.paramJson.put("whereJson", this.whereJson);
		
		this.hashtagDTO = (HashtagDTO) this.hashtagDAO.getOneRow(this.paramJson);
		
		return this.isAjax ? this.hashtagDTO : SUCCESS;
	}
//	
//	public String writeAction() throws Exception{
//		init();
//
//		this.user_seq = (int) session.get("user_seq");
//		FileManager fileManager = new FileManager("image", this.user_seq+"");
//		
//		Gson gson = new Gson();
//		
//		this.paramJson.put("row", this.row);
//		this.paramJson.put("col", this.col);
//		this.paramJson.put("puzzleUrl", this.puzzleUrl.size());
//		this.validateResJson = this.formValidate.puzzleEditorForm(paramJson);
//		paramJson.clear();
//		
//		if(!(boolean)this.validateResJson.get("res")){
//			this.rtnString = gson.toJson(this.validateResJson);
//			return "validation";
//		}else if(!fileManager.fileValidation(getPuzzleUrl(), getPuzzleUrlFileName(), this.puzzleUrl)){
//			this.validateResJson = formValidate.fileUploadError();
//			this.rtnString = gson.toJson(this.validateResJson);
//			return "validation";
//		}
//
//		this.puzzleDTO.setRow(this.row);
//		this.puzzleDTO.setCol(this.col);
//		this.puzzleDTO.setUser_seq(this.user_seq);
//		
//		/****************** 이미지파일 Insert & upload START ******************/
//		String saveFileName = "";
//		
//		// 이미지 업로드
//		for (int i = 0; i < this.puzzleUrl.size(); i++) { 
//			saveFileName = fileManager.fileUpload(getPuzzleUrlFileName(), getPuzzleUrl(), i, 0);	//파일업로드
//			this.puzzleDTO.setPuzzleUrl(saveFileName);
//		}	
//		/****************** 이미지파일 Insert & upload END ******************/
//
//		this.seq = this.puzzleDAO.write(this.puzzleDTO);
//		
//		/****************** 별도테이블(해시태그) Insert START ******************/
//		String[] hashtagArray = this.hashtag.split("#");
//		this.hashtagDTO.setPuzzle_seq(this.seq);
//		for(String s : hashtagArray){
//			if(!s.trim().equals("")){
//				this.hashtagDTO.setHashtag(s.trim());
//				this.hashtagDAO.write(this.hashtagDTO);
//			}
//		}
//		/****************** 별도테이블(해시태그) Insert END ******************/
//		
//		return SUCCESS;
//	}
}
