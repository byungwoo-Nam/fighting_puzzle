package dto;

import lombok.Data;
import lombok.Setter;

import lombok.AccessLevel;

@Data
public class PuzzleDTO {
	private int seq;
	private int user_seq;
	private String puzzleUrl;
	private String regDate;

	// join data
	private String userName;
	private String userPicture;
}
