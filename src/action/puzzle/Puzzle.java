package action.puzzle;

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

import dao.FightingPuzzleDAO;
import dto.HashtagDTO;
import dto.LikeDTO;
import dto.PuzzleDTO;
import dto.ReplyDTO;
import lombok.Data;
import util.config.CodeConfig;
import util.config.FormValidate;
import util.file.FileManager;

@Data
public class Puzzle extends ActionSupport  {
	private Map session;
	private ActionContext context;
	private WebApplicationContext wac;
	private FormValidate formValidate = new FormValidate();
	private CodeConfig codeConfig = new CodeConfig();
	private FightingPuzzleDAO puzzleDAO;
	private FightingPuzzleDAO hashtagDAO;
	private PuzzleDTO puzzleDTO;
	private HashtagDTO hashtagDTO;
	private List<PuzzleDTO> dataList;
	
	private JSONObject paramJson = new JSONObject();
	private JSONObject whereJson = new JSONObject();
	private JSONObject tempJson = new JSONObject();
	private JSONObject validateResJson = new JSONObject();
	private String rtnString;
	
	private int sortCol = 0;				// 	정렬 컬럼
    private String sortVal = "";			// 	정렬 내용
    private int pageNum;					//	페이지번호
    private int puzzleCountPerPage;		//	한페이지에 보일 기본 퍼즐 수
    private boolean searchMode = false;
    private boolean searchMode2 = false;
    
	private JSONObject sortColKindJson = new JSONObject() {{
		// db상의 name과 매칭
		put(0,"P.seq");			// 기본값 정렬
		put(1,"P.seq");			// 등록번호 정렬
	}};
	
	private boolean isAjax;	//	ajax요청시
	
	private int seq;				//	퍼즐seq
	private int user_seq;		//	회원seq
	private int col;				//	퍼즐 가로
	private int row;				//	퍼즐 세로
	private String hashtag;		//	퍼즐 해시태그
	
	// 첨부파일 영역
	private List<File> puzzleUrl = new ArrayList<File>();								//	베너이미지
	private List<String> puzzleUrlContentType = new ArrayList<String>(); 		//	퍼즐이미지 첨부파일 종류
	private List<String> puzzleUrlFileName = new ArrayList<String>(); 			//	퍼즐이미지 첨부파일명
	private List<String> puzzleUrlDeleteFileName = new ArrayList<String>();		//	퍼즐이미지 삭제 요청시 파일명
	
	public Puzzle() {
		ServletContext servletContext = ServletActionContext.getServletContext();
	    this.wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	    this.puzzleDTO = new PuzzleDTO();
	    this.hashtagDTO = new HashtagDTO();
	}

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	//	요소 초기화 및 세팅
	public void init(){
		this.puzzleDAO = (FightingPuzzleDAO)this.wac.getBean("puzzle");
		this.hashtagDAO = (FightingPuzzleDAO)this.wac.getBean("hashtag");
		
		this.puzzleCountPerPage = this.puzzleCountPerPage==0 ? codeConfig.getPuzzleCountPerPage() : this.puzzleCountPerPage;
		
		this.context = ActionContext.getContext();	//session을 생성하기 위해
		this.session = this.context.getSession();		// Map 사용시
	}
	
	//	ajax로 요청시 요소 초기화
	public void initForAjax(JSONObject jsonObject){
		this.seq = jsonObject.containsKey("seq") ? Integer.parseInt(jsonObject.get("seq").toString()) : this.seq;
		this.pageNum = jsonObject.containsKey("pageNum") ? Integer.parseInt(jsonObject.get("pageNum").toString()) : this.pageNum;
		this.whereJson = jsonObject.containsKey("whereJson") ? (JSONObject) jsonObject.get("whereJson") : this.whereJson;
		this.searchMode = jsonObject.containsKey("searchMode") ? (boolean)jsonObject.get("searchMode") : this.searchMode;
		this.searchMode2 = jsonObject.containsKey("searchMode2") ? (boolean)jsonObject.get("searchMode2") : this.searchMode2;
		this.isAjax = true;
	}
	
	public Object getList() throws Exception{
		init();
		
		this.pageNum = this.pageNum == 0 ? 1 : this.pageNum;
		this.sortCol = this.sortColKindJson.containsKey(this.sortCol) ? this.sortCol : 0;
		this.sortVal = this.sortVal.equals("ASC") ? "ASC" : "DESC";
		
		if(this.whereJson!=null){
			this.paramJson.put("whereJson", this.whereJson);
		}
		
		this.paramJson.put("pageNum",this.pageNum);
		this.paramJson.put("countPerPage",this.puzzleCountPerPage);
		this.paramJson.put("countPerPage2", codeConfig.getReplyPreviewCountPerPage());
		this.paramJson.put("sortCol", this.sortColKindJson.get(this.sortCol));
		this.paramJson.put("sortVal", this.sortVal);
		this.paramJson.put("searchMode", this.searchMode);
		this.paramJson.put("searchMode2", this.searchMode2);
		
		this.dataList = (List<PuzzleDTO>)this.puzzleDAO.getList(this.paramJson);
		
		return this.isAjax ? this.dataList : SUCCESS;
	}
	
	public Object getData() throws Exception{
		init();
		
		this.user_seq = (int) session.get("user_seq");
		
		this.whereJson.put("P.seq", this.seq);
		this.paramJson.put("whereJson", this.whereJson);
		this.puzzleDTO = (PuzzleDTO) this.puzzleDAO.getOneRow(this.paramJson);
		
		this.tempJson.clear();
		this.tempJson.put("puzzle_seq", this.seq);
		Hashtag hashtag = new Hashtag();
		Reply reply = new Reply();
		Like like = new Like();
		hashtag.initForAjax(new JSONObject(){{put("whereJson", tempJson);}});
		reply.initForAjax(new JSONObject(){{put("whereJson", tempJson);}});
		like.initForAjax(new JSONObject(){{put("whereJson", tempJson);}});
		this.puzzleDTO.setHashtagList((List<HashtagDTO>) hashtag.getList());
		this.puzzleDTO.setReplyList((List<ReplyDTO>) reply.getList());
		
		this.tempJson.put("user_seq", this.user_seq);
		this.puzzleDTO.setLike((like.getData() == null) ? false : true);
		
		return this.isAjax ? this.puzzleDTO : SUCCESS;
	}
	
	public String writeAction() throws Exception{
		init();

		this.user_seq = (int) session.get("user_seq");
		FileManager fileManager = new FileManager("image", this.user_seq+"");
		
		Gson gson = new Gson();
		
		this.paramJson.put("row", this.row);
		this.paramJson.put("col", this.col);
		this.paramJson.put("hashtag", this.hashtag);
		this.paramJson.put("puzzleUrl", this.puzzleUrl.size());
		this.validateResJson = this.formValidate.puzzleEditorForm(paramJson);
		paramJson.clear();
		
		if(!(boolean)this.validateResJson.get("res")){
			this.rtnString = gson.toJson(this.validateResJson);
			return "validation";
		}else if(!fileManager.fileValidation(getPuzzleUrl(), getPuzzleUrlFileName(), this.puzzleUrl)){
			this.validateResJson = formValidate.fileUploadError();
			this.rtnString = gson.toJson(this.validateResJson);
			return "validation";
		}

		this.puzzleDTO.setRow(this.row);
		this.puzzleDTO.setCol(this.col);
		this.puzzleDTO.setUser_seq(this.user_seq);
		
		/****************** 이미지파일 Insert & upload START ******************/
		String saveFileName = "";
		
		// 이미지 업로드
		for (int i = 0; i < this.puzzleUrl.size(); i++) { 
			saveFileName = fileManager.fileUpload(getPuzzleUrlFileName(), getPuzzleUrl(), i, 0);	//파일업로드
			this.puzzleDTO.setPuzzleUrl(saveFileName);
		}	
		/****************** 이미지파일 Insert & upload END ******************/

		this.seq = this.puzzleDAO.write(this.puzzleDTO);
		
		/****************** 별도테이블(해시태그) Insert START ******************/
		String[] hashtagArray = this.hashtag.split("#");
		this.hashtagDTO.setPuzzle_seq(this.seq);
		for(String s : hashtagArray){
			if(!s.trim().equals("")){
				this.hashtagDTO.setHashtag(s.trim());
				this.hashtagDAO.write(this.hashtagDTO);
			}
		}
		/****************** 별도테이블(해시태그) Insert END ******************/
		
		return SUCCESS;
	}
}
