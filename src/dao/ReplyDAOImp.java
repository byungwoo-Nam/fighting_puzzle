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

import dto.ReplyDTO;
import util.system.StringUtil;

@Repository
public class ReplyDAOImp implements FightingPuzzleDAO {

	private ReplyDTO replyDTO; 
	private NamedParameterJdbcTemplate jdbcTemplate;
	private String table_name = " REPLY  ";
	
	@Autowired
	public void setDataSource(DataSource dataSource){ 
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public ReplyDAOImp(){
	
	}
	
	//	조건에 맞는 해시태그목록
	public Object getOneRow(JSONObject paramJson) {
		JSONObject sqlJson = new JSONObject();
		List<ReplyDTO> list = new ArrayList<ReplyDTO>();
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
        
        list  = this.jdbcTemplate.query(sql,sqlJson,new BeanPropertyRowMapper(ReplyDTO.class));
        this.replyDTO = (list.size() == 1) ? list.get(0) : null;
        return this.replyDTO;
	}
	
	//	LIST
	public Object getList(JSONObject paramJson) {
		JSONObject sqlJson = new JSONObject();
		List<ReplyDTO> list = new ArrayList<ReplyDTO>();
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
			sql += "	SELECT R.*, U.name AS userName, U.pictureUrl AS userPicture, \n";
			sql += "	(	CASE	\n";
	        sql += "			WHEN (TIMESTAMPDIFF( SECOND , R.regDate, NOW( ))) < 60 THEN CONCAT(TIMESTAMPDIFF( SECOND , R.regDate, NOW( )), '초 전')	\n";			// 60 = 1분 이전
	        sql += "			WHEN (TIMESTAMPDIFF( SECOND , R.regDate, NOW( ))) < 60*60 THEN CONCAT(TIMESTAMPDIFF( MINUTE , R.regDate, NOW( )), '분 전')	\n";		// 60*60 = 1시간 이전
	        sql += "			WHEN (TIMESTAMPDIFF( SECOND , R.regDate, NOW( ))) < 60*60*24 THEN CONCAT(TIMESTAMPDIFF( HOUR , R.regDate, NOW( )), '시간 전')	\n";	// 60*60*24 = 1일 이전
	        sql += "			WHEN (TIMESTAMPDIFF( SECOND , R.regDate, NOW( ))) < 60*60*24*6 THEN CONCAT(TIMESTAMPDIFF( DAY , R.regDate, NOW( )), '일 전')	\n";	// 60*60*24*6 = 6일 이전
	        sql += "		ELSE (	\n";
	        sql += "			CASE		\n";
			sql += "				WHEN DATE_FORMAT(R.regDate,'%p') = 'AM' THEN DATE_FORMAT(R.regDate, '%Y.%m.%d 오전 %h:%i:%s')		\n";
			sql += "			ELSE		\n";
			sql += "				DATE_FORMAT(R.regDate, '%Y.%m.%d 오후 %h:%i:%s')		\n";
			sql += "			END	)		\n";
			sql += "	END	) AS printDate		\n";
		}
        sql += "	FROM "+ table_name;
        sql += "	R JOIN USER U ON R.user_seq = U.seq	\n";
        sql += "	WHERE :one = :one	\n		";
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
			list  = this.jdbcTemplate.query(sql, sqlJson, new BeanPropertyRowMapper(ReplyDTO.class));
	        return list;
		}
	}
	
	//	Write
	public int write(Object dto) {
		String sql = "";
		sql += "	INSERT INTO " + table_name + "	\n";
		sql += "	(puzzle_seq, user_seq, content)	\n";
		sql += "	values(:puzzle_seq, :user_seq, :content)	\n";
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("puzzle_seq", ((ReplyDTO)dto).getPuzzle_seq(), Types.NUMERIC);
		paramSource.addValue("user_seq", ((ReplyDTO)dto).getUser_seq(), Types.NUMERIC);
		paramSource.addValue("content", ((ReplyDTO)dto).getContent(), Types.VARCHAR);
		
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
		sql += "	content = :content	\n";
		
		sql += "	where seq = :seq	\n";
		
		paramSource.addValue("content", ((ReplyDTO)dto).getContent(), Types.VARCHAR);
		paramSource.addValue("seq", ((ReplyDTO)dto).getSeq(), Types.NUMERIC);
		
		if(this.jdbcTemplate.update(sql, paramSource) > 0){
			return ((ReplyDTO)dto).getSeq();
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
