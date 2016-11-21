package dto;

import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

@Data
public class UserDTO {
	private int seq;
	private int snsCode;
	private String name;		
	private String id;
	private String pictureUrl;
	private String regDate;
	
	//joinData
	private int puzzleWriteCnt;
	private int playCnt;
	
}
