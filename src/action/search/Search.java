package action.search;

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
import lombok.Data;
import util.config.CodeConfig;
import util.config.FormValidate;
import util.file.FileManager;

@Data
public class Search extends ActionSupport  {
	private Map session;
	private ActionContext context;
	private WebApplicationContext wac;
	private FormValidate formValidate = new FormValidate();
	private CodeConfig codeConfig = new CodeConfig();
	private FightingPuzzleDAO searchDAO;
	private SearchDTO searchDTO;
	private PuzzleDTO puzzleDTO;
	private List<SearchDTO> dataList1;
	private List<SearchDTO> dataList2;
	private List<PuzzleDTO> puzzleDataList1;
	private List<PuzzleDTO> puzzleDataList2;
	
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
	
	private int seq;				//	퍼즐seq
	private int user_seq;		//	회원seq
	private String keyword;	//	퍼즐 해시태그
	
	// 첨부파일 영역
	private List<File> puzzleUrl = new ArrayList<File>();								//	베너이미지
	private List<String> puzzleUrlContentType = new ArrayList<String>(); 		//	퍼즐이미지 첨부파일 종류
	private List<String> puzzleUrlFileName = new ArrayList<String>(); 			//	퍼즐이미지 첨부파일명
	private List<String> puzzleUrlDeleteFileName = new ArrayList<String>();		//	퍼즐이미지 삭제 요청시 파일명
	
	public Search() {
		ServletContext servletContext = ServletActionContext.getServletContext();
	    this.wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	    this.searchDTO = new SearchDTO();
	    this.puzzleDTO = new PuzzleDTO();
	}

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	//	요소 초기화 및 세팅
	public void init(){
		this.searchDAO = (FightingPuzzleDAO)this.wac.getBean("search");
		
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
		
		this.user_seq = (int) session.get("user_seq");
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
		
		this.dataList1 = (List<SearchDTO>) this.searchDAO.getList(this.paramJson);
		
		
		this.whereJson.put("S.user_seq", this.user_seq);
		this.paramJson.put("whereJson", this.whereJson);
		this.paramJson.remove("searchMode");
		this.paramJson.put("searchMode2", true);
		this.dataList2 = (List<SearchDTO>) this.searchDAO.getList(this.paramJson);
		
		return this.isAjax ? null : SUCCESS;
	}
	
	public Object getData() throws Exception{
		init();
	
		this.writeAction();
		
		Puzzle puzzle = new Puzzle();
		
		this.pageNum = this.pageNum == 0 ? 1 : this.pageNum;
		this.sortCol = this.sortColKindJson.containsKey(this.sortCol) ? this.sortCol : 0;
		this.sortVal = this.sortVal.equals("ASC") ? "ASC" : "DESC";
		
		this.whereJson.put("H.hashtag", this.keyword);
		
		this.paramJson.put("pageNum",this.pageNum);
		this.paramJson.put("countPerPage",this.puzzleCountPerPage);
		this.paramJson.put("countPerPage2", codeConfig.getReplyPreviewCountPerPage());
		this.paramJson.put("sortCol", this.sortColKindJson.get(this.sortCol));
		this.paramJson.put("sortVal", this.sortVal);
		this.paramJson.put("searchMode", true);
		this.paramJson.put("whereJson", this.whereJson);
		
		puzzle.initForAjax(this.paramJson);
		this.puzzleDataList1 = (List<PuzzleDTO>) puzzle.getList();
		
		this.paramJson.remove("searchMode");
		this.paramJson.put("searchMode2", true);
		puzzle.initForAjax(this.paramJson);
		this.puzzleDataList2 = (List<PuzzleDTO>) puzzle.getList();
		
		return this.isAjax ? null : SUCCESS;
	}
	
	public Object writeAction() throws Exception{
		init();

		this.user_seq = (int) session.get("user_seq");
		
		Gson gson = new Gson();
		
		this.paramJson.put("keyword", this.keyword);
		this.validateResJson = this.formValidate.searchEditorForm(paramJson);
		paramJson.clear();
		
		if(!(boolean)this.validateResJson.get("res")){
			this.rtnString = gson.toJson(this.validateResJson);
			return "validation";
		}

		this.searchDTO.setKeyword(this.keyword);
		this.searchDTO.setUser_seq(this.user_seq);

		this.seq = this.searchDAO.write(this.searchDTO);
		
		return this.isAjax ? this.seq : SUCCESS;
	}
}
