package dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import dto.PuzzleDTO;
import util.system.StringUtil;

@Repository
public class PuzzleDAOImp implements FightingPuzzleDAO {

	private PuzzleDTO puzzleDTO; 
	private NamedParameterJdbcTemplate jdbcTemplate;
	private String table_name = " PUZZLE  ";
	
	@Autowired
	public void setDataSource(DataSource dataSource){ 
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public PuzzleDAOImp(){
	
	}
	
	//	조건에 맞는 퍼즐목록
	public Object getOneRow(JSONObject paramJson) {
		JSONObject sqlJson = new JSONObject();
		List<PuzzleDTO> list = new ArrayList<PuzzleDTO>();
		JSONObject whereJson = (JSONObject) (paramJson.containsKey("whereMap") ? paramJson.get("whereMap") : null);
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
        
        list  = this.jdbcTemplate.query(sql,sqlJson,new BeanPropertyRowMapper(PuzzleDTO.class));
        this.puzzleDTO = (list.size() == 1) ? list.get(0) : null;
        return this.puzzleDTO;
	}
	
	//	LIST
	public Object getList(JSONObject paramJson) {
		Map<String,Object> sqlMap = new HashMap<String,Object>();
		List<PuzzleDTO> list = new ArrayList<PuzzleDTO>();
		boolean isCount = paramJson.containsKey("isCount") ? (boolean)paramJson.get("isCount") : false;
		Map<String, Object> whereMap = (Map<String, Object>) (paramJson.containsKey("whereMap") ? paramJson.get("whereMap") : null);
		Map<String, Object> searchMap = (Map<String, Object>) (paramJson.containsKey("searchMap") ? paramJson.get("searchMap") : null);
		int pageNum = paramJson.containsKey("pageNum") ? (int)paramJson.get("pageNum") : 0;
		int countPerPage = paramJson.containsKey("countPerPage") ? (int)paramJson.get("countPerPage") : 0;
		int startNum = (pageNum-1)*countPerPage;
		String sortCol = paramJson.containsKey("sortCol") ? (String)paramJson.get("sortCol") : "";
		String sortVal = paramJson.containsKey("sortVal") ? (String)paramJson.get("sortVal") : "";
		
		String sql = "";
		
		sqlMap.put("one", 1);
		sqlMap.put("startNum", startNum);
		sqlMap.put("countPerPage", countPerPage);
		
		if(isCount){
			sql += "	SELECT COUNT(*)	\n";
		}else{
			sql += "	SELECT P.user_seq, P.puzzleURL, U.name AS userName, U.pictureUrl AS userPicture, \n";
			sql += "	P.SEQ,	\n";
			sql += "	CASE		\n";
			sql += "	WHEN DATE_FORMAT(P.regDate,'%p') = 'AM' THEN 		\n";
			sql += "	DATE_FORMAT(P.regDate, '%Y.%m.%d 오전 %h:%i:%s')		\n";
			sql += "	ELSE		\n";
			sql += "	DATE_FORMAT(P.regDate, '%Y.%m.%d 오후 %h:%i:%s')		\n";
			sql += "	END AS REGDATE, P.regDate as orig_regdate		\n";
		}
        sql += " FROM "+ table_name + " P JOIN USER U ON P.user_seq = U.seq WHERE :one = :one \n";
        if(whereMap!=null && !whereMap.isEmpty()){
            for( String key : whereMap.keySet() ){
            	sqlMap.put(key, whereMap.get(key));
            	sql += " and " + key + " = :"+key+"		\n";
            }
        }
        if(searchMap!=null && !searchMap.isEmpty()){
            for( String key : searchMap.keySet() ){
            	sqlMap.put(key, "%" + searchMap.get(key) + "%");
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
        	return this.jdbcTemplate.queryForInt(sql,sqlMap);
		}else{
			list  = this.jdbcTemplate.query(sql, sqlMap, new BeanPropertyRowMapper(PuzzleDTO.class));
	        return list;
		}
	}
	
	//	Write
	public int write(Object dto) {
		String sql = "";
		sql += "	INSERT INTO " + table_name + "	\n";
		sql += "	(user_seq, puzzleUrl)	\n";
		sql += "	values(:user_seq, :puzzleUrl)	\n";
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("user_seq", ((PuzzleDTO)dto).getUser_seq(), Types.NUMERIC);
		paramSource.addValue("puzzleUrl", ((PuzzleDTO)dto).getPuzzleUrl(), Types.VARCHAR);
		
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
		sql += "	name = :name, tel1 = :tel1, tel2 = :tel2, tel3 = :tel3	\n";
		
//		if(!((PuzzleDTO)dto).getId().equals("")){
//			sql += ", id = :id	\n";
//			paramSource.addValue("id", ((PuzzleDTO)dto).getId(), Types.VARCHAR);
//		}
//		
//		if(!((UserDTO)dto).getPw().equals("")){
//			sql += ", pw = :pw	\n";
//			paramSource.addValue("pw", ((UserDTO)dto).getPw(), Types.VARCHAR);
//		}
		
		sql += "	where seq = :seq	\n";
		
//		paramSource.addValue("name", ((UserDTO)dto).getName(), Types.VARCHAR);
//		paramSource.addValue("tel1", ((UserDTO)dto).getTel1(), Types.VARCHAR);
//		paramSource.addValue("tel2", ((UserDTO)dto).getTel2(), Types.VARCHAR);
//		paramSource.addValue("tel3", ((UserDTO)dto).getTel3(), Types.VARCHAR);
//		paramSource.addValue("seq", ((UserDTO)dto).getSeq(), Types.NUMERIC);
		
		if(this.jdbcTemplate.update(sql, paramSource) > 0){
			return ((PuzzleDTO)dto).getSeq();
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
