package dto;

import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;

@Data
public class PuzzleDTO {
	private int seq;
	private int user_seq;
	private int row;
	private int col;
	private String puzzleUrl;
	private String regDate;

	// join data
	private String userName;
	private String userPicture;
	private int likeCnt; 
	
	// make data
	private List<HashtagDTO> hashtagList = new ArrayList<HashtagDTO>();
	private String printDate;
}
