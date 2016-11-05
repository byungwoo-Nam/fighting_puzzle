package util.config;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ActionConfig{
	
	String boardConfigArray[];
	HashMap hashmap = new HashMap();
	String separator = "&";			// 조건이 여러가지 일 때 구분자
	String falseFlag = ":";				// 조건이 맞지 않을 때 returnURL 시작 부분

	public ActionConfig(){
		/************session체크 모든 action정보 [16.10. 02 by.FighTeam]**********
		
		adminAction : 관리자 페이지
		pageTitle : action에 대한 title(설명)
		
		***********************************************************************/
		
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// ----------------------------basic.xml-----------------------------
		hashmap.put("index", 					"adminAction => false,		pageTitle => ");
		hashmap.put("login", 					"adminAction => false,		pageTitle => ");
		hashmap.put("loginAPI",				"adminAction => false,		pageTitle => ");
		hashmap.put("loginCallback",			"adminAction => false,		pageTitle => ");
		hashmap.put("logout",					"adminAction => false,		pageTitle => ");
		hashmap.put("ajaxConnect",			"adminAction => false,		pageTitle => ");
		
		// ----------------------------favoriteGame.xml-----------------------------
		hashmap.put("favoriteGame", 			"adminAction => false,		pageTitle => ");

		// ----------------------------gameChart.xml-----------------------------
		hashmap.put("gameChart",			"adminAction => false,		pageTitle => ");
		
		// ----------------------------search.xml-----------------------------
		hashmap.put("search",					"adminAction => false,		pageTitle => ");
		
		// ----------------------------myPage.xml-----------------------------
		hashmap.put("myPage",				"adminAction => false,		pageTitle => ");
		hashmap.put("myPage2",				"adminAction => false,		pageTitle => ");
		
		// ----------------------------puzzle.xml-----------------------------
		hashmap.put("puzzleMain",			"adminAction => false,		pageTitle => ");
		hashmap.put("puzzleGame",			"adminAction => false,		pageTitle => ");
		hashmap.put("puzzleWrite",			"adminAction => false,		pageTitle => ");
		hashmap.put("puzzleWriteAction",	"adminAction => false,		pageTitle => ");
		
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	public String getActionAttr(String actionName, String key){		// action이름을 이용하여 원하는 key 정보 추출
		String rtnVal = "";
		if(actionName == null) return "";
		if(!hashmap.containsKey(actionName)){
			return "";
		}else{
			String board_config_str = hashmap.get(actionName).toString().trim();
			
			String config_array[] = board_config_str.split(",");
			
			for(int i = 0 ; i < config_array.length ; i++){
				
				String attr[] = config_array[i].trim().split("=>");
				if(attr[0].trim().equalsIgnoreCase(key)){
					if(attr.length<2 || attr[1].equals("''")) return "";
					return attr[1].trim();
				}
			}
			
		}	
		return rtnVal;
	}
	
	public boolean ActionNameChk(String actionName){		// Action이름 유효성검사
		if(actionName == null || actionName.equals("")){			// 파라미터가 없을경우 실패
			return false;
		}else if(!hashmap.containsKey(actionName)){				// hashmap에 명시되지 않은경우 실패
			return false;
		}else{																// 그외의 경우 성공
			return true;
		}
	}
}