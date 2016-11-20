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
	private String accessError = "에러발생";
	private String fileUploadError = "파일 첨부가 실패 하였습니다.";
	
	//*******************퍼즐관련*******************/
	private String PuzzleImageError = "퍼즐이미지를 등록해 주세요.";
	private String PuzzleColError = "가로 퍼즐 값을 선택해 주세요.";
	private String PuzzleRowError = "세로 퍼즐 값을 선택해 주세요.";
	private String PuzzleHashtagError = "올바른 해시태그를 입력해 주세요.";
	
	//*******************레코드관련*******************/
	private String RecordPuzzleError = "퍼즐 정보가 올바르지 않습니다.";
	private String RecordTimeError = "시간 정보가 올바르지 않습니다.";
	
	//*******************댓글관련*******************/
	private String ReplyPuzzleError = "퍼즐 정보가 올바르지 않습니다.";
	private String ReplyContentError = "댓글을 입력해 주세요.";
	
	//*******************댓글관련*******************/
	private String LikePuzzleError = "퍼즐 정보가 올바르지 않습니다.";
	
	//*******************검색관련*******************/
	private String SearchError = "검색이 실패 하였습니다.";
}