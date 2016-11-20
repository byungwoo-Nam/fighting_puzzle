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

import dto.HashtagDTO;
import util.system.StringUtil;

@Repository
public class HashtagDAOImp implements FightingPuzzleDAO {

	private HashtagDTO hashtagDTO; 
	private NamedParameterJdbcTemplate jdbcTemplate;
	private String table_name = " HASHTAG  ";
	
	@Autowired
	public void setDataSource(DataSource dataSource){ 
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public HashtagDAOImp(){
	
	}
	
	//	조건에 맞는 해시태그목록
	public Object getOneRow(JSONObject paramJson) {
		JSONObject sqlJson = new JSONObject();
		List<HashtagDTO> list = new ArrayList<HashtagDTO>();
		JSONObject whereJson = (JSONObject) (paramJson.containsKey("whereJson") ? paramJson.get("whereJson") : null);
		String sql = "";
		
		sqlJson.put("one", 1);
		
        sql = "	SELECT * FROM	" + table_name;
        sql += "	WHERE :one = :one	\n		";
        if(whereJson!=null && !whereJson.isEmpty()){
            for( Object key : whereJson.keySet() ){
            	sqlJson.put(key, whereJson.get(key));
            	sql += " and " + key + " = :"+key+"		\n";
            }
        }
        
        System.out.println(sql);
        System.out.println(whereJson);
        System.out.println(sqlJson);
        
        list  = this.jdbcTemplate.query(sql,sqlJson,new BeanPropertyRowMapper(HashtagDTO.class));
        this.hashtagDTO = (list.size() > 0) ? list.get(0) : null;
        
        System.out.println(this.hashtagDTO);
        return this.hashtagDTO;
	}
	
	//	LIST
	public Object getList(JSONObject paramJson) {
		JSONObject sqlJson = new JSONObject();
		List<HashtagDTO> list = new ArrayList<HashtagDTO>();
		boolean isCount = paramJson.containsKey("isCount") ? (boolean)paramJson.get("isCount") : false;
		JSONObject whereJson = (JSONObject) (paramJson.containsKey("whereJson") ? paramJson.get("whereJson") : null);
		JSONObject searchJson = (JSONObject) (paramJson.containsKey("searchJson") ? paramJson.get("searchJson") : null);
		int pageNum = paramJson.containsKey("pageNum") ? (int)paramJson.get("pageNum") : 0;
		int countPerPage = paramJson.containsKey("countPerPage") ? (int)paramJson.get("countPerPage") : 0;
		int startNum = (pageNum-1)*countPerPage;
		String sortCol = paramJson.containsKey("sortCol") ? (String)paramJson.get("sortCol") : "";
		String sortVal = paramJson.containsKey("sortVal") ? (String)paramJson.get("sortVal") : "";
		boolean searchMode = paramJson.containsKey("searchMode") ? (boolean)paramJson.get("searchMode") : false;
		
		String sql = "";
		
		sqlJson.put("one", 1);
		sqlJson.put("startNum", startNum);
		sqlJson.put("countPerPage", countPerPage);
		
		if(isCount){
			sql += "	SELECT COUNT(*)	\n";
		}else{
			sql += "	SELECT * \n";
		}
		
		if(searchMode){
			sql += "	, COUNT( * ) AS groupCount \n";
		}
        sql += " FROM "+ table_name + "  WHERE :one = :one \n";
        if(whereJson!=null && !whereJson.isEmpty()){
            for( Object key : whereJson.keySet() ){
            	sqlJson.put(key, whereJson.get(key));
            	sql += " and " + key + " = :"+key+"		\n";
            }
        }
        if(searchJson!=null && !searchJson.isEmpty()){
            for( Object key : searchJson.keySet() ){
            	sqlJson.put(key, searchJson.get(key) + "%");
            	sql += " and LOWER( "+key+" ) like LOWER( :"+key+" )";
            }
        }
        
        if(searchMode){
			sql += "	GROUP BY hashtag \n";
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
			list  = this.jdbcTemplate.query(sql, sqlJson, new BeanPropertyRowMapper(HashtagDTO.class));
			return list.size()>0 ? list : null;
		}
	}
	
	//	Write
	public int write(Object dto) {
		String sql = "";
		sql += "	INSERT INTO " + table_name + "	\n";
		sql += "	(puzzle_seq, hashtag)	\n";
		sql += "	values(:puzzle_seq, :hashtag)	\n";
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("puzzle_seq", ((HashtagDTO)dto).getPuzzle_seq(), Types.NUMERIC);
		paramSource.addValue("hashtag", ((HashtagDTO)dto).getHashtag(), Types.VARCHAR);
		
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
			return ((HashtagDTO)dto).getSeq();
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
