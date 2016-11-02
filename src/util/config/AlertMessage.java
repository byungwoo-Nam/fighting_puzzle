package util.config;

import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

@Data
public class AlertMessage{
	public AlertMessage(){
	}
	
	//*******************공통사용*******************/
	private String writeError = "등록 실패";
	private String fileUploadError = "파일 첨부가 실패 하였습니다.";
	
	//*******************퍼즐등록관련*******************/
	private String PuzzleImageError = "퍼즐이미지를 등록해 주세요.";
}