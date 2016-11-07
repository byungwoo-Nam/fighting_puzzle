package util.config;

import java.util.List;

import org.json.simple.JSONObject;

import action.puzzle.Puzzle;
import util.system.StringUtil;

import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

@Data
public class FormValidate{
	
	private AlertMessage alertMessage = new AlertMessage();
	private JSONObject rtnJson = new JSONObject();
	private StringUtil stringUtil = new StringUtil();
	private CodeConfig codeConfig = new CodeConfig();
	
	public FormValidate(){
		this.rtnJson.put("res", false);			// validation결과
		this.rtnJson.put("errorTitle", "");		// 페이지 전달 타이틀
		this.rtnJson.put("errorMsg", "");		// 페이지 전달 메세지
		this.rtnJson.put("elementID", "");		// form요소 id명
	}
	
	public JSONObject fileUploadError(){
		this.rtnJson.put("res", false);
		this.rtnJson.put("errorMsg", this.alertMessage.getFileUploadError());		// 페이지 전달 메세지
		return this.rtnJson;
	}
	
	// 퍼즐 등록/수정 체크(validation)
	public JSONObject puzzleEditorForm(JSONObject paramJson){
		// 파일체크는 단순 객체 크기만 비교하고 client는 script plugin으로, server는 fileuploader에서 한다.
		if(!paramJson.containsKey("puzzleUrl") || Integer.parseInt(paramJson.get("puzzleUrl").toString()) <= 0 ){
			// 퍼즐 이미지 체크
			this.rtnJson.put("errorTitle", alertMessage.getWriteError());
			this.rtnJson.put("errorMsg", alertMessage.getPuzzleImageError());
			this.rtnJson.put("elementID", "puzzleUrl_check");
		}else if(!paramJson.containsKey("col") || paramJson.get("col").equals("") || !codeConfig.getPuzzleSizeJson().containsKey(Integer.parseInt(paramJson.get("col").toString()))){
			// 퍼즐 가로 크기 체크
			this.rtnJson.put("errorTitle", alertMessage.getWriteError());
			this.rtnJson.put("errorMsg", alertMessage.getPuzzleColError());
			this.rtnJson.put("elementID", "col_check");
		}else if(!paramJson.containsKey("row") || paramJson.get("row").equals("") || !codeConfig.getPuzzleSizeJson().containsKey(Integer.parseInt(paramJson.get("row").toString()))){
			// 퍼즐 가로 크기 체크
			this.rtnJson.put("errorTitle", alertMessage.getWriteError());
			this.rtnJson.put("errorMsg", alertMessage.getPuzzleRowError());
			this.rtnJson.put("elementID", "row_check");
		}else if(!paramJson.containsKey("hashtag") || paramJson.get("hashtag").equals("") || !stringUtil.hashtag_validation(paramJson.get("hashtag").toString())){
			// 해시태그 체크
			this.rtnJson.put("errorTitle", alertMessage.getWriteError());
			this.rtnJson.put("errorMsg", alertMessage.getPuzzleHashtagError());
			this.rtnJson.put("elementID", "hashtag_check");
		}else if(false){
			// 추가 가능
		}else{
			this.rtnJson.put("res", true);
		}
		
		return this.rtnJson;
	}
	
	// 퍼즐 기록 등록/수정 체크(validation)
	public JSONObject recordEditorForm(JSONObject paramJson) throws Exception{
		Puzzle puzzle = new Puzzle();
		paramJson.put("seq", paramJson.containsKey(("puzzle_seq")) ? paramJson.get("puzzle_seq") : 0);
		puzzle.initForAjax(paramJson);
		if(!paramJson.containsKey("puzzle_seq") || paramJson.get("puzzle_seq").equals("") || puzzle.getData() == null){
			// 퍼즐 존재 체크
			this.rtnJson.put("errorTitle", alertMessage.getWriteError());
			this.rtnJson.put("errorMsg", alertMessage.getRecordPuzzleError());
			this.rtnJson.put("elementID", "puzzle_check");
		}else if(!paramJson.containsKey("time") || paramJson.get("time").equals("")){
			// 시간 체크
			this.rtnJson.put("errorTitle", alertMessage.getWriteError());
			this.rtnJson.put("errorMsg", alertMessage.getRecordTimeError());
			this.rtnJson.put("elementID", "time_check");
		}else if(false){
			// 추가 가능
		}else{
			this.rtnJson.put("res", true);
		}
		
		return this.rtnJson;
	}
	
	// 댓글 기록 등록/수정 체크(validation)
	public JSONObject replyEditorForm(JSONObject paramJson) throws Exception{
		Puzzle puzzle = new Puzzle();
		paramJson.put("seq", paramJson.containsKey(("puzzle_seq")) ? paramJson.get("puzzle_seq") : 0);
		puzzle.initForAjax(paramJson);
		if(!paramJson.containsKey("puzzle_seq") || paramJson.get("puzzle_seq").equals("") || puzzle.getData() == null){
			// 퍼즐 존재 체크
			this.rtnJson.put("errorTitle", alertMessage.getWriteError());
			this.rtnJson.put("errorMsg", alertMessage.getReplyPuzzleError());
			this.rtnJson.put("elementID", "puzzle_check");
		}else if(!paramJson.containsKey("content") || paramJson.get("content").equals("")){
			// 댓글 체크
			this.rtnJson.put("errorTitle", alertMessage.getWriteError());
			this.rtnJson.put("errorMsg", alertMessage.getReplyContentError());
			this.rtnJson.put("elementID", "content_check");
		}else if(false){
			// 추가 가능
		}else{
			this.rtnJson.put("res", true);
		}
		
		return this.rtnJson;
	}
}