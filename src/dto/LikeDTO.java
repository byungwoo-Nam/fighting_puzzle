package dto;

import lombok.Data;
import lombok.Setter;

import lombok.AccessLevel;

@Data
public class LikeDTO {
	private int seq;
	private int puzzle_seq;
	private int user_seq;
}
