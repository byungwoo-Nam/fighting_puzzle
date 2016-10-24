package action.puzzle;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONObject;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import dao.FightingPuzzleDAO;
import dto.PuzzleDTO;
import lombok.Data;
import util.file.FileManager;

@Data
public class Puzzle extends ActionSupport  {
	private Map session;
	private ActionContext context;
	private WebApplicationContext wac;
	private FightingPuzzleDAO fightingPuzzleDAO;
	private PuzzleDTO puzzleDTO;
	private List<PuzzleDTO> dataList;
	
	private JSONObject paramJson = new JSONObject();
	private JSONObject whereJson = new JSONObject();
	
	private int sortCol = 0;											// 	정렬 컬럼
    private String sortVal = "";										// 	정렬 내용
    
	private JSONObject sortColKindJson = new JSONObject() {{
		// db상의 name과 매칭
		put(0,"P.seq");				// 기본값 정렬
		put(1,"P.seq");				// 등록번호 정렬
	}};
	
	private int seq;				//	퍼즐seq
	private int user_seq;		//	회원seq
	private String pageKind;	//	호출한 페이지 종류
	
	// 첨부파일 영역
	private List<File> puzzle_url = new ArrayList<File>();									//	베너이미지
	private List<String> puzzle_urlContentType = new ArrayList<String>(); 			//	퍼즐이미지 첨부파일 종류
	private List<String> puzzle_urlFileName = new ArrayList<String>(); 				//	퍼즐이미지 첨부파일명
	private List<String> puzzle_urlDeleteFileName = new ArrayList<String>();		//	퍼즐이미지 삭제 요청시 파일명
	
	public Puzzle() {
		ServletContext servletContext = ServletActionContext.getServletContext();
	    this.wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	    this.puzzleDTO = new PuzzleDTO();
	}

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	//	요소 초기화 및 세팅
	public void init(){
		this.fightingPuzzleDAO = (FightingPuzzleDAO)this.wac.getBean("puzzle");
		
		this.context = ActionContext.getContext();	//session을 생성하기 위해
		this.session = this.context.getSession();		// Map 사용시
	}
	
	public String getList() throws Exception{
		init();
		
		this.sortCol = this.sortColKindJson.containsKey(this.sortCol) ? this.sortCol : 0;
		this.sortVal = this.sortVal.equals("ASC") ? "ASC" : "DESC";
		this.paramJson.put("sortCol", this.sortColKindJson.get(this.sortCol));
		this.paramJson.put("sortVal", this.sortVal);
		
		this.dataList = (List<PuzzleDTO>)this.fightingPuzzleDAO.getList(this.paramJson);
		
		return SUCCESS;
	}
	
	public String getData() throws Exception{
		init();
		
		this.whereJson.put("P.seq", this.seq);
		this.paramJson.put("whereJson", this.whereJson);
		
		this.puzzleDTO = (PuzzleDTO) this.fightingPuzzleDAO.getOneRow(this.paramJson);
		
		System.out.println(this.puzzleDTO);
		
		return SUCCESS;
	}
	
	public String writeAction() throws Exception{
		init();
		
		this.user_seq = (int) session.get("user_seq");
		FileManager fileManager = new FileManager("image", this.user_seq+"");
		
		/****************** 이미지파일 Insert & upload START ******************/
		String saveFileName = "";
		
		System.out.println("이미지등록!!!!!!!!!!!!");
		System.out.println(this.puzzle_url.size());
		
		// 이미지 업로드
		for (int i = 0; i < this.puzzle_url.size(); i++) { 
			saveFileName = fileManager.fileUpload(getPuzzle_urlFileName(), getPuzzle_url(), i, 0);	//파일업로드
			this.puzzleDTO.setPuzzleUrl(saveFileName);
		}	
		/****************** 이미지파일 Insert & upload END ******************/
		
		this.puzzleDTO.setUser_seq(this.user_seq);
		
		this.fightingPuzzleDAO.write(puzzleDTO);
		
		return SUCCESS;
	}
}
