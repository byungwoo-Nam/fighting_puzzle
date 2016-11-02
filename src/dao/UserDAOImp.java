package dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import dto.UserDTO;
import util.system.StringUtil;

@Repository
public class UserDAOImp implements FightingPuzzleDAO {

	private UserDTO userDTO; 
	private NamedParameterJdbcTemplate jdbcTemplate;
	private String table_name = " USER  ";
	
	@Autowired
	public void setDataSource(DataSource dataSource){ 
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public UserDAOImp(){
	
	}
	
	//	조건에 맞는 사용자목록
	public Object getOneRow(JSONObject paramJson) {
		JSONObject sqlJson = new JSONObject();
		List<UserDTO> list = new ArrayList<UserDTO>();
		JSONObject whereJson = (JSONObject) (paramJson.containsKey("whereJson") ? paramJson.get("whereJson") : null);
		String sql = "";
		
		sqlJson.put("one", 1);
		
        sql = "	SELECT * FROM"+ table_name + "WHERE :one = :one	\n";
        if(whereJson!=null && !whereJson.isEmpty()){
            for( Object key : whereJson.keySet() ){
            	sqlJson.put(key, whereJson.get(key));
            	sql += " and " + key + " = :"+key+"		\n";
            }
        }
        
        System.out.println(sql);
        
        list  = this.jdbcTemplate.query(sql,sqlJson,new BeanPropertyRowMapper(UserDTO.class));
        this.userDTO = (list.size() == 1) ? list.get(0) : null;
        return this.userDTO;
	}
	
	//	LIST
	public Object getList(JSONObject paramJson) {
		JSONObject sqlJson = new JSONObject();
		List<UserDTO> list = new ArrayList<UserDTO>();
		boolean isCount = paramJson.containsKey("isCount") ? (boolean)paramJson.get("isCount") : false;
		JSONObject whereJson = (JSONObject) (paramJson.containsKey("whereJson") ? paramJson.get("whereJson") : null);
		JSONObject searchJson = (JSONObject) (paramJson.containsKey("searchJson") ? paramJson.get("searchJson") : null);
		int pageNum = paramJson.containsKey("pageNum") ? (int)paramJson.get("pageNum") : 0;
		int countPerPage = paramJson.containsKey("countPerPage") ? (int)paramJson.get("countPerPage") : 0;
		int startNum = (pageNum-1)*countPerPage;
		String sortCol = paramJson.containsKey("sortCol") ? (String)paramJson.get("sortCol") : "";
		String sortVal = paramJson.containsKey("sortVal") ? (String)paramJson.get("sortVal") : "";
		
		String sql = "";
		
		sqlJson.put("one", 1);
		sqlJson.put("startNum", startNum);
		sqlJson.put("countPerPage", countPerPage);
		
		if(isCount){
			sql += "	SELECT COUNT(*)	\n";
		}else{
			sql += "	SELECT ID, NAME, TEL1, TEL2, TEL3, 	\n";
			sql += "	SEQ,	\n";
			sql += "	CASE		\n";
			sql += "	WHEN DATE_FORMAT(regdate,'%p') = 'AM' THEN 		\n";
			sql += "	DATE_FORMAT(regdate, '%Y.%m.%d 오전 %h:%i:%s')		\n";
			sql += "	ELSE		\n";
			sql += "	DATE_FORMAT(regdate, '%Y.%m.%d 오후 %h:%i:%s')		\n";
			sql += "	END AS REGDATE, regdate as orig_regdate		\n";
		}
        sql += " FROM "+ table_name + " T WHERE :one = :one \n";
        if(whereJson!=null && !whereJson.isEmpty()){
            for( Object key : whereJson.keySet() ){
            	sqlJson.put(key, whereJson.get(key));
            	sql += " and " + key + " = :"+key+"		\n";
            }
        }
        if(searchJson!=null && !searchJson.isEmpty()){
            for( Object key : searchJson.keySet() ){
            	sqlJson.put(key, "%" + searchJson.get(key) + "%");
            	sql += " and LOWER( "+key+" ) like LOWER( :"+key+" )";
            }
        }
        
        if(!sortCol.equals("")){
        	sql += " ORDER BY " + sortCol + " " + sortVal + "		\n";
        }
        
        if(isCount || pageNum==0){
		}else{
			sql += " LIMIT :startNum, :countPerPage	\n";
		}
        
        System.out.println("sql:::"+sql);
        
        if(isCount){
        	return this.jdbcTemplate.queryForInt(sql,sqlJson);
		}else{
			list  = this.jdbcTemplate.query(sql, sqlJson, new BeanPropertyRowMapper(UserDTO.class));
	        return list;
		}
	}
	
	//	Write
	public int write(Object dto) {
		String sql = "";
		sql += "	INSERT INTO " + table_name + "	\n";
		sql += "	(snsCode, name, id, pictureUrl)	\n";
		sql += "	values(:snsCode, :name, :id, :pictureUrl)	\n";
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("snsCode", ((UserDTO)dto).getSnsCode(), Types.NUMERIC);
		paramSource.addValue("name", ((UserDTO)dto).getName(), Types.VARCHAR);
		paramSource.addValue("id", ((UserDTO)dto).getId(), Types.VARCHAR);
		paramSource.addValue("pictureUrl", ((UserDTO)dto).getPictureUrl(), Types.VARCHAR);
		
		int rtnInt = 0;
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try{
			rtnInt = this.jdbcTemplate.update(sql, paramSource, keyHolder);
		}catch(Exception e){
			System.out.println("error::::"+e);
		}
		
		long longKey = keyHolder.getKey().longValue();
		
		if(rtnInt > 0){
			return StringUtil.longToInt(longKey);
		}else{
			return 0;
		}
	}
	
	public int update(Object dto) {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String sql = "";
		sql += "	UPDATE " + table_name + " SET	\n";
		sql += "	name = :name, pictureUrl = :pictureUrl	\n";
		
		if(!((UserDTO)dto).getId().equals("")){
			sql += ", id = :id	\n";
			paramSource.addValue("id", ((UserDTO)dto).getId(), Types.VARCHAR);
		}
		
		sql += "	where seq = :seq	\n";
		
		paramSource.addValue("name", ((UserDTO)dto).getName(), Types.VARCHAR);
		paramSource.addValue("pictureUrl", ((UserDTO)dto).getPictureUrl(), Types.VARCHAR);
		paramSource.addValue("seq", ((UserDTO)dto).getSeq(), Types.NUMERIC);
		
		if(this.jdbcTemplate.update(sql, paramSource) > 0){
			return ((UserDTO)dto).getSeq();
		}else{
			return 0;
		}
	}
	
	//	DELETE
	public int delete(JSONObject paramJson) {
//		int next_seq = getMaxSeq();
//		if(next_seq == 0){
//			next_seq = 1;
//		}
		int seq = paramJson.containsKey("seq") ? (int)paramJson.get("seq") : 0;
		
		String sql = "";
		sql += "	DELETE FROM " + table_name + "	\n";
		sql += "	WHERE seq = :seq	\n";

//		SqlLobValue lobValue = new SqlLobValue(dto.getBbs_content(), lobHandler);

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("seq", seq, Types.NUMERIC);
		int rtnInt = this.jdbcTemplate.update(sql, paramSource);
		if(rtnInt > 0){
			return rtnInt;
		}else{
			return 0;
		}
	}
}
