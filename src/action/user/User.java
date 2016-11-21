package action.user;

import java.io.File;
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

import action.puzzle.Hashtag;
import action.puzzle.Like;
import action.puzzle.Puzzle;
import action.puzzle.Reply;
import dao.FightingPuzzleDAO;
import dto.HashtagDTO;
import dto.LikeDTO;
import dto.PuzzleDTO;
import dto.ReplyDTO;
import dto.SearchDTO;
import dto.UserDTO;
import lombok.Data;
import util.config.CodeConfig;
import util.config.FormValidate;
import util.file.FileManager;

@Data
public class User extends ActionSupport  {
	private Map session;
	private ActionContext context;
	private WebApplicationContext wac;
	private FormValidate formValidate = new FormValidate();
	private CodeConfig codeConfig = new CodeConfig();
	private FightingPuzzleDAO userDAO;
	private UserDTO userDTO;
	private List<UserDTO> dataList;
	
	private JSONObject paramJson = new JSONObject();
	private JSONObject whereJson = new JSONObject();
	private JSONObject tempJson = new JSONObject();
	private JSONObject validateResJson = new JSONObject();
	private String rtnString;
	
	private int sortCol = 0;				// 	정렬 컬럼
    private String sortVal = "";			// 	정렬 내용
    private int pageNum;					//	페이지번호
    private int puzzleCountPerPage;		//	한페이지에 보일 기본 퍼즐 수
    
	private JSONObject sortColKindJson = new JSONObject() {{
		// db상의 name과 매칭
		put(0,"P.seq");			// 기본값 정렬
		put(1,"P.seq");			// 등록번호 정렬
	}};
	
	private boolean isAjax;	//	ajax요청시
	
	private int seq;				//	회원seq
	
	// 첨부파일 영역
	private List<File> puzzleUrl = new ArrayList<File>();								//	베너이미지
	private List<String> puzzleUrlContentType = new ArrayList<String>(); 		//	퍼즐이미지 첨부파일 종류
	private List<String> puzzleUrlFileName = new ArrayList<String>(); 			//	퍼즐이미지 첨부파일명
	private List<String> puzzleUrlDeleteFileName = new ArrayList<String>();		//	퍼즐이미지 삭제 요청시 파일명
	
	public User() {
		ServletContext servletContext = ServletActionContext.getServletContext();
	    this.wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	    this.userDTO = new UserDTO();
	}

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	//	요소 초기화 및 세팅
	public void init(){
		this.userDAO = (FightingPuzzleDAO)this.wac.getBean("user");
		
		this.puzzleCountPerPage = codeConfig.getPuzzleCountPerPage();
		
		this.context = ActionContext.getContext();	//session을 생성하기 위해
		this.session = this.context.getSession();		// Map 사용시
	}
	
	//	ajax로 요청시 요소 초기화
	public void initForAjax(JSONObject jsonObject){
		this.seq = jsonObject.containsKey("seq") ? Integer.parseInt(jsonObject.get("seq").toString()) : this.seq;
		this.pageNum = jsonObject.containsKey("pageNum") ? Integer.parseInt(jsonObject.get("pageNum").toString()) : this.pageNum;
		this.isAjax = true;
	}
	
	public Object getList() throws Exception{
		init();
		
		this.seq = (int) session.get("user_seq");
//		this.whereJson.put("P.seq", this.seq);
//		this.paramJson.put("whereJson", this.whereJson);
//		this.puzzleDTO = (PuzzleDTO) this.puzzleDAO.getOneRow(this.paramJson);
//		
//		this.tempJson.clear();
//		this.tempJson.put("puzzle_seq", this.seq);
//		Hashtag hashtag = new Hashtag();
//		Reply reply = new Reply();
//		Like like = new Like();
//		hashtag.initForAjax(this.tempJson);
//		reply.initForAjax(new JSONObject(){{put("whereJson", tempJson);}});
//		like.initForAjax(new JSONObject(){{put("whereJson", tempJson);}});
//		this.puzzleDTO.setHashtagList((List<HashtagDTO>) hashtag.getList());
//		this.puzzleDTO.setReplyList((List<ReplyDTO>) reply.getList());
//		this.puzzleDTO.setLike((like.getData() == null) ? false : true);
		
//		return this.isAjax ? this.puzzleDTO : SUCCESS;
		
		this.paramJson.put("searchMode", true);
		
		this.dataList = (List<UserDTO>) this.userDAO.getList(this.paramJson);
		
		return this.isAjax ? null : SUCCESS;
	}
	
	public Object getData() throws Exception{
		init();
	
		this.seq = this.seq==0 ? (int) session.get("user_seq") : this.seq;
		
		User user = new User();
		
		this.whereJson.put("seq", this.seq);
		this.paramJson.put("whereJson", this.whereJson);
		
		this.userDTO = (UserDTO) this.userDAO.getOneRow(this.paramJson);
		
		return this.isAjax ? null : SUCCESS;
	}
	
	public Object writeAction() throws Exception{
		init();

//		this.user_seq = (int) session.get("user_seq");
		
		Gson gson = new Gson();
		
//		this.paramJson.put("keyword", this.keyword);
		this.validateResJson = this.formValidate.searchEditorForm(paramJson);
		paramJson.clear();
		
		if(!(boolean)this.validateResJson.get("res")){
			this.rtnString = gson.toJson(this.validateResJson);
			return "validation";
		}

//		this.searchDTO.setKeyword(this.keyword);
//		this.searchDTO.setUser_seq(this.user_seq);
//
//		this.seq = this.searchDAO.write(this.searchDTO);
		
		return this.isAjax ? this.seq : SUCCESS;
	}
}
