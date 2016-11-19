package util.config;

import java.util.LinkedHashMap;
import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONObject;

import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

@Data
public class CodeConfig{
	
	private String serverScheme = ServletActionContext.getRequest().getScheme();
	private String serverName = ServletActionContext.getRequest().getServerName();
	private int serverPort = ServletActionContext.getRequest().getServerPort();
	private String domain = this.serverScheme + "://" + this.serverName + ":" + this.serverPort + "/";
	private int puzzleCountPerPage = 5;
	private int replyPreviewCountPerPage = 3;
	private int replyCountPerPage = 10;
	
    //	첨부 가능한 이미지 파일 확장자 모음
    private JSONObject attachImageFileExtJson = new JSONObject() {{
    	put("jpeg","jpeg");
    	put("jpg","jpg");
    	put("gif","gif");
    	put("bmp","bmp");
    	put("png","png");
    }};
    
    //	퍼즐 가로-세로 수
    private JSONObject puzzleSizeJson = new JSONObject() {{
    	put(3, 3);
    	put(4, 4);
    	put(5, 5);
    	put(6, 6);
    }};
}