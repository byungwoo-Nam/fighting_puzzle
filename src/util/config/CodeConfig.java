package util.config;

import java.util.LinkedHashMap;
import org.apache.struts2.ServletActionContext;

import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

@Data
public class CodeConfig{
	
	private String serverScheme = ServletActionContext.getRequest().getScheme();
	private String serverName = ServletActionContext.getRequest().getServerName();
	private int serverPort = ServletActionContext.getRequest().getServerPort();
	private String domain = this.serverScheme + "://" + this.serverName + ":" + this.serverPort + "/";
	
    //첨부 가능한 이미지 파일 확장자 모음
    private LinkedHashMap attachImageFileExtMap = new LinkedHashMap() {{
    	put("jpeg","jpeg");
    	put("jpg","jpg");
    	put("gif","gif");
    	put("bmp","bmp");
    	put("png","png");
    }};
}