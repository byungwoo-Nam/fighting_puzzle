package dto;

import lombok.Data;
import lombok.Setter;

import lombok.AccessLevel;

@Data
public class RecordDTO {
	private int seq;
	private int puzzle_seq;
	private int user_seq;
	private int time;
}
