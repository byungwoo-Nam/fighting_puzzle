package util.config;

import org.json.simple.JSONObject;

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
		}else if(false){
			// 추가 가능
		}else{
			this.rtnJson.put("res", true);
		}
		
		return this.rtnJson;
	}
}