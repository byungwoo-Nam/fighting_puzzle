package dto;

import lombok.Data;
import lombok.Setter;

import lombok.AccessLevel;

@Data
public class HashtagDTO {
	private int seq;
	private int puzzle_seq;
	private String hashtag;
	
	// make data
	private int groupCount;
}
