package dto;

import lombok.Data;
import lombok.Setter;

import lombok.AccessLevel;

@Data
public class SearchDTO {
	private int seq;
	private int user_seq;
	private String keyword;
	private String regdate;
}
