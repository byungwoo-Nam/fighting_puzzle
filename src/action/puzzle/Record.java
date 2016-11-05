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
import dto.RecordDTO;
import lombok.Data;
import util.config.CodeConfig;
import util.config.FormValidate;
import util.file.FileManager;

@Data
public class Record extends ActionSupport  {
	private Map session;
	private ActionContext context;
	private WebApplicationContext wac;
	private FormValidate formValidate = new FormValidate();
	private CodeConfig codeConfig = new CodeConfig();
	private FightingPuzzleDAO recordDAO;
	private RecordDTO recordDTO;
	private List<RecordDTO> dataList;
	
	private JSONObject paramJson = new JSONObject();
	private JSONObject whereJson = new JSONObject();
	private JSONObject validateResJson = new JSONObject();
	private String rtnString;
	
	private int sortCol = 0;											// 	정렬 컬럼
    private String sortVal = "";										// 	정렬 내용
    
	private JSONObject sortColKindJson = new JSONObject() {{
		// db상의 name과 매칭
		put(0,"seq");			// 기본값 정렬
		put(1,"seq");			// 등록번호 정렬
	}};
	
	private int seq;				//	기록seq
	private int puzzle_seq;		//	퍼즐seq
	private int user_seq;		//	회원seq
	private int time;				//	기록시간(ms)
	
	public Record() {
		ServletContext servletContext = ServletActionContext.getServletContext();
	    this.wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	    this.recordDTO = new RecordDTO();
	}

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	//	요소 초기화 및 세팅
	public void init(){
		this.recordDAO = (FightingPuzzleDAO)this.wac.getBean("record");
		
		this.context = ActionContext.getContext();	//session을 생성하기 위해
		this.session = this.context.getSession();		// Map 사용시
	}
	
	//	ajax로 요청시 요소 초기화
	public void initForAjax(JSONObject jsonObject){
		this.puzzle_seq = jsonObject.containsKey("puzzle_seq") ? Integer.parseInt(jsonObject.get("puzzle_seq").toString()) : null;
		this.time = jsonObject.containsKey("time") ? Integer.parseInt(jsonObject.get("time").toString()) : null;
	}
	
	public String getList() throws Exception{
		init();
		
		this.sortCol = this.sortColKindJson.containsKey(this.sortCol) ? this.sortCol : 0;
		this.sortVal = this.sortVal.equals("ASC") ? "ASC" : "DESC";
		this.paramJson.put("sortCol", this.sortColKindJson.get(this.sortCol));
		this.paramJson.put("sortVal", this.sortVal);
		
		this.dataList = (List<RecordDTO>)this.recordDAO.getList(this.paramJson);
		
		return SUCCESS;
	}
	
	public String getData() throws Exception{
		init();
		
		this.whereJson.put("P.seq", this.seq);
		this.paramJson.put("whereJson", this.whereJson);
		
		this.recordDTO = (RecordDTO) this.recordDAO.getOneRow(this.paramJson);
		
		return SUCCESS;
	}
	
	public String writeAction() throws Exception{
		init();

		this.user_seq = (int) session.get("user_seq");
		
		Gson gson = new Gson();
		
		this.paramJson.put("puzzle_seq", this.puzzle_seq);
		this.paramJson.put("time", this.time);
		this.validateResJson = this.formValidate.recordEditorForm(this.paramJson);
		this.paramJson.clear();
		
		if(!(boolean)this.validateResJson.get("res")){
			this.rtnString = gson.toJson(this.validateResJson);
			return "validation";
		}

		this.recordDTO.setPuzzle_seq(this.puzzle_seq);
		this.recordDTO.setTime(this.time);
		this.recordDTO.setUser_seq(this.user_seq);

		this.seq = this.recordDAO.write(this.recordDTO);
		
		return SUCCESS;
	}
}
