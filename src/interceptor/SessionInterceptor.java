package interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import util.config.ActionConfig;
import util.system.StringUtil;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class SessionInterceptor extends AbstractInterceptor {
	
	ActionConfig actionConfig = new ActionConfig();
		
	/************session체크 모든 intercept [16.10.01 by.Fighteam]**********
	
	접근하는 모든페이지에 대해 체크한다.
	error종류는 struts.xml -> global-results 명시되어 있는 4가지
	ActionConfig에서 체크한 후 return 받아온다.
	1. login			: 회원 로그인이 필요할 경우
	2. adminLogin	: 관리자 로그인이 필요할 경우
	3. notMobile		: 모바일이 아닌 경우
	3. exception		: 기타 모든 오류
	
	***********************************************************************/

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
    	System.out.println("인터셉터시작" + "(sessionIntercepteor.java)");
    	ActionContext context = ActionContext.getContext();
    	ServletContext servletContext = ServletActionContext.getServletContext();
    	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
    	HttpServletRequest request = ServletActionContext.getRequest();
    	Map<String, Object> session = (Map<String, Object>) context.getSession();
    	
    	String nextAction = request.getRequestURI().substring(1);	//	이동할 페이지 액션(다음페이지)
    	String[] nextActionArr = nextAction.split("/");
    	
    	boolean isAdminSession = StringUtil.stringToBool(StringUtil.isNullOrSpace((String)session.get("isAdmin"),"").trim());		//	관리자 세션
    	boolean isMemberSession = StringUtil.stringToBool(StringUtil.isNullOrSpace((String)session.get("isMember"),"").trim());	//	일반회원 세션
    	
    	boolean needAdminAction = StringUtil.stringToBool(actionConfig.getActionAttr(context.getName(), "adminAction"));		// Action의 adminAction 값 추출
    	
    	boolean isMobile = (request.getHeader("User-Agent").toLowerCase().replaceAll("","")).indexOf("mobile") != -1 ? true : false;
    	
    	boolean isNoneSessionPage = context.getName().equals("login") || context.getName().equals("adminLogin") || context.getName().equals("logoutAction") || context.getName().equals("notMobile");
    	
    	String result = "";
    	if(false){
//        	if(!isMobile){
    		result = "notMobile";
    	}else if(!actionConfig.ActionNameChk(context.getName())){		
    		// 1. Action이 명시되어 있는지 체크 -> 없는 액션이면 에러
    		result = "exception";
    	}else if(needAdminAction && !isAdminSession){
    		// 2. 관리자 페이지 불법 접근시
    		result = "/adminLogin";
//        	}else if(!isNoneSessionPage && !isMemberSession){
//        		// login을 제외한 그 외 모든페이지 세션 없을 때
//        		result = "login";
//               	if(nextActionArr.length==1){
//            		session.put("nextActionName", nextActionArr[0]);
//            	}else{
//            		session.put("nextActionNamespace", nextActionArr[0]);
//            		session.put("nextActionName", nextActionArr[1]);
//            	}	***********************************로그인 회원 DB완성되면 주석 제거
    	}
    	
    	System.out.println("context::::" + context.getName() + "(sessionIntercepteor.java)");
    	System.out.println("isAdminSession:"+isAdminSession);
    	System.out.println("result::::" + result + "(sessionIntercepteor.java)");
    	
    	if(result.equals("")){
    		return invocation.invoke();		// 정상적인 진행 
    	}else{
    		return result;
    	}
    }
    
    @Override
    public void init() {
     // TODO Auto-generated method stub
     super.init();
    }
    
    @Override
    public void destroy() {
     // TODO Auto-generated method stub
     super.destroy();
    }
}

