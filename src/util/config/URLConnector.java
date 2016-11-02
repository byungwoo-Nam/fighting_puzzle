package util.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import util.system.StringUtil;

public class URLConnector{
	
	public JSONObject getData(String u, JSONObject p){
		StringBuffer stringBuffer = new StringBuffer();
		JSONObject jsonObject = null;
        
        try {
        	// URL, 파라미터 설정
        	String fullUrl = u+"?";
        	
        	if(!p.isEmpty()){
        		for( Object key : p.keySet() ){
        			fullUrl += (key + "=" + URLEncoder.encode(p.get(key).toString(),"UTF-8") + "&");
                }
        	}
        	
        	// URL 객체 생성
            URL url = new URL(fullUrl);
            
            // URLConnection 생성
            URLConnection urlConnection = url.openConnection();
            
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
             
            String str;
            while((str=bufferedReader.readLine()) != null){
            	stringBuffer.append(str + "\r\n");
            }
             
            // json객체로 변환
            jsonObject = (JSONObject) JSONValue.parse(stringBuffer.toString());
            jsonObject = (jsonObject == null) ? (JSONObject) JSONValue.parse(StringUtil.querystringToJson(stringBuffer.toString())) : jsonObject;
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return jsonObject;
	}
}