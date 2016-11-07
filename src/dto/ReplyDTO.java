package dto;

import lombok.Data;
import lombok.Setter;

import lombok.AccessLevel;

@Data
public class ReplyDTO {
	private int seq;
	private int puzzle_seq;
	private int user_seq;
	private String content;
	private String regDate;
	
	// join data
	private String userName;
	private String userPicture;
	
	// make data
	private String printDate;
}
