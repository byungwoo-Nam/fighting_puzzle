package dao;

import org.json.simple.JSONObject;

public interface FightingPuzzleDAO {
	public Object getOneRow(JSONObject paramJson);		//	one row
	public Object getList(JSONObject paramJson);				//	List
	public int write(Object object);								//	Write
	public int update(Object object);								//	update
	public int delete(JSONObject paramJson);					//	Delete
}
