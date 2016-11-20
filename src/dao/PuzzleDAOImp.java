package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import dto.HashtagDTO;
import dto.PuzzleDTO;
import dto.ReplyDTO;
import util.system.StringUtil;

@Repository
public class PuzzleDAOImp implements FightingPuzzleDAO {

	private PuzzleDTO puzzleDTO; 
	private NamedParameterJdbcTemplate jdbcTemplate;
	private String table_name = " PUZZLE  ";
	private Map<Integer, Object> puzzleMap;
	
	@Autowired
	public void setDataSource(DataSource dataSource){ 
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public PuzzleDAOImp(){
	
	}
	
	public RowMapper getPuzzleRM(){
		return new RowMapper(){
        	public PuzzleDTO mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException{
        		int seq = rs.getInt("p.seq");
        		PuzzleDTO puzzleDTO;
        		if(puzzleMap != null){
        			puzzleDTO = (PuzzleDTO)puzzleMap.get(seq);
        		}else{
        			puzzleDTO = null;
        			puzzleMap = new LinkedHashMap<Integer, Object>();
        		}
        		
        		if(puzzleDTO == null){
        			puzzleDTO = new PuzzleDTO();
        			puzzleDTO.setSeq(seq);
        			puzzleDTO.setUser_seq(rs.getInt("p.user_seq"));
        			puzzleDTO.setRow(rs.getInt("p.row"));
        			puzzleDTO.setCol(rs.getInt("p.col"));
        			puzzleDTO.setPuzzleUrl(rs.getString("p.puzzleUrl"));
        			puzzleDTO.setRegDate(rs.getString("p.regdate"));
        			puzzleDTO.setUserName(rs.getString("u.name"));
        			puzzleDTO.setUserPicture(rs.getString("u.pictureUrl"));
        			puzzleDTO.setPrintDate(rs.getString("printDate"));
        			puzzleDTO.setBestRecord(rs.getInt("bestRecord"));
        			puzzleDTO.setPlayCnt(rs.getInt("playCnt"));
        			puzzleDTO.setReplyCnt(rs.getInt("replyCnt"));
        			puzzleDTO.setLikeCnt(rs.getInt("likeCnt"));
        			puzzleMap.put(seq, puzzleDTO);
        		}
        		
        		// hashtag select
        		HashtagDTO hashtagDTO = new HashtagDTO();
        		int h_seq = rs.getInt("h.seq");
        		hashtagDTO.setSeq(h_seq);
        		hashtagDTO.setPuzzle_seq(seq);
        		hashtagDTO.setHashtag(rs.getString("h.hashtag"));
        		if(!puzzleDTO.getHashtagList().contains(hashtagDTO)){
        			puzzleDTO.getHashtagList().add(hashtagDTO);
        		}
        		
        		// reply select
        		ReplyDTO replyDTO = new ReplyDTO();
        		int r_seq = rs.getInt("r.seq");
        		replyDTO.setSeq(r_seq);
        		replyDTO.setPuzzle_seq(seq);
        		replyDTO.setContent(rs.getString("r.content"));
        		replyDTO.setRegDate(rs.getString("r.regDate"));
        		replyDTO.setPrintDate(rs.getString("r.printDate"));
        		replyDTO.setUserName(rs.getString("r.userName"));
        		replyDTO.setUserPicture(rs.getString("r.userPicture"));
        		if(!puzzleDTO.getReplyList().contains(replyDTO) && r_seq != 0){
        			puzzleDTO.getReplyList().add(replyDTO);
        		}
        		
        		return puzzleDTO;
        	}
        };
	}
	
	//	조건에 맞는 퍼즐목록
	public Object getOneRow(JSONObject paramJson) {
		JSONObject sqlJson = new JSONObject();
		List<PuzzleDTO> list = new ArrayList<PuzzleDTO>();
		JSONObject whereJson = (JSONObject) (paramJson.containsKey("whereJson") ? paramJson.get("whereJson") : null);
		String sql = "";
		
		sqlJson.put("one", 1);
		
        sql += "	SELECT P.*, U.name as userName , U.pictureUrl as userPicture,	\n";
        sql += "	IFNULL(( SELECT R2.time FROM( SELECT R2.puzzle_seq, Min(time) as time FROM RECORD R2 GROUP BY R2.puzzle_seq) R1 INNER JOIN RECORD R2 ON R1.puzzle_seq = R2.puzzle_seq AND R2.time = R1.time where R2.puzzle_seq = P.seq ),0) AS bestRecord,	\n";
        sql += "	( SELECT COUNT(*) FROM REPLY WHERE puzzle_seq = P.seq ) AS replyCnt,	\n";
        sql += "	( SELECT COUNT(*) FROM ( SELECT * FROM RECORD GROUP BY user_seq ) AS RG WHERE puzzle_seq = P.seq ) AS playCnt,	\n";
        sql += "	( SELECT COUNT(*) FROM `LIKE` WHERE puzzle_seq = P.seq ) AS likeCnt,	\n";
        sql += "	(	CASE	\n";
        sql += "			WHEN (TIMESTAMPDIFF( SECOND , P.regDate, NOW( ))) < 60 THEN CONCAT(TIMESTAMPDIFF( SECOND , P.regDate, NOW( )), '초 전')	\n";			// 60 = 1분 이전
        sql += "			WHEN (TIMESTAMPDIFF( SECOND , P.regDate, NOW( ))) < 60*60 THEN CONCAT(TIMESTAMPDIFF( MINUTE , P.regDate, NOW( )), '분 전')	\n";		// 60*60 = 1시간 이전
        sql += "			WHEN (TIMESTAMPDIFF( SECOND , P.regDate, NOW( ))) < 60*60*24 THEN CONCAT(TIMESTAMPDIFF( HOUR , P.regDate, NOW( )), '시간 전')	\n";	// 60*60*24 = 1일 이전
        sql += "			WHEN (TIMESTAMPDIFF( SECOND , P.regDate, NOW( ))) < 60*60*24*6 THEN CONCAT(TIMESTAMPDIFF( DAY , P.regDate, NOW( )), '일 전')	\n";	// 60*60*24*6 = 6일 이전
        sql += "		ELSE (	\n";
        sql += "			CASE		\n";
		sql += "				WHEN DATE_FORMAT(P.regDate,'%p') = 'AM' THEN DATE_FORMAT(P.regDate, '%Y.%m.%d 오전 %l:%i:%s')		\n";
		sql += "			ELSE		\n";
		sql += "				DATE_FORMAT(P.regDate, '%Y.%m.%d 오후 %l:%i:%s')		\n";
		sql += "			END	)		\n";
		sql += "	END	) AS printDate		\n";
		sql += "	FROM	" + table_name;
        sql += "	P JOIN USER U ON P.user_seq = U.seq	\n";
        sql += "	WHERE :one = :one	\n		";
        
        if(whereJson!=null && !whereJson.isEmpty()){
            for( Object key : whereJson.keySet() ){
            	sqlJson.put(key, whereJson.get(key));
            	sql += " and " + key + " = :"+key+"		\n";
            }
        }
        
        System.out.println(sql);
        
		list = this.jdbcTemplate.query(sql, sqlJson, new BeanPropertyRowMapper(PuzzleDTO.class));
        this.puzzleDTO = (list.size() == 1) ? list.get(0) : null;
        
        return this.puzzleDTO;
	}
	
	//	LIST
	public Object getList(JSONObject paramJson) {
		JSONObject sqlJson = new JSONObject();
		List<PuzzleDTO>list = new ArrayList<PuzzleDTO>();
		boolean isCount = paramJson.containsKey("isCount") ? (boolean)paramJson.get("isCount") : false;
		JSONObject whereJson = (JSONObject) (paramJson.containsKey("whereJson") ? paramJson.get("whereJson") : null);
		JSONObject searchJson = (JSONObject) (paramJson.containsKey("searchJson") ? paramJson.get("searchJson") : null);
		int pageNum = paramJson.containsKey("pageNum") ? (int)paramJson.get("pageNum") : 0;
		int countPerPage = paramJson.containsKey("countPerPage") ? (int)paramJson.get("countPerPage") : 0;
		int countPerPage2 = paramJson.containsKey("countPerPage2") ? (int)paramJson.get("countPerPage2") : 0;
		int startNum = (pageNum-1)*countPerPage;
		String sortCol = paramJson.containsKey("sortCol") ? (String)paramJson.get("sortCol") : "";
		String sortVal = paramJson.containsKey("sortVal") ? (String)paramJson.get("sortVal") : "";
		boolean searchMode = paramJson.containsKey("searchMode") ? (boolean)paramJson.get("searchMode") : false;
		boolean searchMode2 = paramJson.containsKey("searchMode2") ? (boolean)paramJson.get("searchMode2") : false;
		
		String sql = "";
		
		sqlJson.put("one", 1);
		sqlJson.put("startNum", startNum);
		sqlJson.put("countPerPage", countPerPage);
		sqlJson.put("countPerPage2", countPerPage2);
		
		if(searchMode){
			sql += "	SELECT P.seq, P.puzzleUrl , IFNULL(( SELECT COUNT( DISTINCT user_seq ) FROM RECORD where puzzle_seq = P.seq GROUP BY puzzle_seq ),0) AS playCnt	\n";
			sql += "	FROM " + table_name;
			sql += "	P join HASHTAG H	\n";
			sql += "	on P.seq = H.puzzle_seq	\n";
			for( Object key : whereJson.keySet() ){
            	sqlJson.put(key, whereJson.get(key));
            	sql += " and " + key + " = :"+key+"		\n";
            }
			sql += "	order by playCnt DESC, puzzle_seq DESC";
			sql += " LIMIT 0, 9	\n";
		}else if(searchMode2){
			sql += "	SELECT P.seq, P.puzzleUrl 	\n";
			sql += "	FROM " + table_name;
			sql += "	P join HASHTAG H	\n";
			sql += "	on P.seq = H.puzzle_seq	\n";
			for( Object key : whereJson.keySet() ){
            	sqlJson.put(key, whereJson.get(key));
            	sql += " and " + key + " = :"+key+"		\n";
            }
			sql += "	order by P.seq DESC";
		}else{
			if(isCount){
				sql += "	SELECT COUNT(*)	\n";
				sql += "	FROM	" + table_name;
			}else{
		        sql += "	SELECT P.seq, P.user_seq, P.row, P.col, P.puzzleUrl, P.regDate, H.seq, H.hashtag, R.seq, R.user_seq, R.content, R.regDate, R.printDate, R.userName, R.userPicture, U.name, U.pictureUrl,	\n";
		        sql += "	( SELECT R2.time FROM( SELECT R2.puzzle_seq, Min(time) as time FROM RECORD R2 GROUP BY R2.puzzle_seq) R1 INNER JOIN RECORD R2 ON R1.puzzle_seq = R2.puzzle_seq AND R2.time = R1.time where R2.puzzle_seq = P.seq ) AS bestRecord,	\n";
		        sql += "	( SELECT COUNT(*) FROM REPLY WHERE puzzle_seq = P.seq ) AS replyCnt,	\n";
		        sql += "	( SELECT COUNT( DISTINCT user_seq ) FROM RECORD where puzzle_seq = P.seq GROUP BY puzzle_seq ) AS playCnt,	\n";
		        sql += "	( SELECT COUNT(*) FROM `LIKE` WHERE puzzle_seq = P.seq ) AS likeCnt,	\n";
		        sql += "	(	CASE	\n";
		        sql += "			WHEN (TIMESTAMPDIFF( SECOND , P.regDate, NOW( ))) < 60 THEN CONCAT(TIMESTAMPDIFF( SECOND , P.regDate, NOW( )), '초 전')	\n";			// 60 = 1분 이전
		        sql += "			WHEN (TIMESTAMPDIFF( SECOND , P.regDate, NOW( ))) < 60*60 THEN CONCAT(TIMESTAMPDIFF( MINUTE , P.regDate, NOW( )), '분 전')	\n";		// 60*60 = 1시간 이전
		        sql += "			WHEN (TIMESTAMPDIFF( SECOND , P.regDate, NOW( ))) < 60*60*24 THEN CONCAT(TIMESTAMPDIFF( HOUR , P.regDate, NOW( )), '시간 전')	\n";	// 60*60*24 = 1일 이전
		        sql += "			WHEN (TIMESTAMPDIFF( SECOND , P.regDate, NOW( ))) < 60*60*24*6 THEN CONCAT(TIMESTAMPDIFF( DAY , P.regDate, NOW( )), '일 전')	\n";	// 60*60*24*6 = 6일 이전
		        sql += "		ELSE (	\n";
		        sql += "			CASE		\n";
				sql += "				WHEN DATE_FORMAT(P.regDate,'%p') = 'AM' THEN DATE_FORMAT(P.regDate, '%Y.%m.%d 오전 %h:%i:%s')		\n";
				sql += "			ELSE		\n";
				sql += "				DATE_FORMAT(P.regDate, '%Y.%m.%d 오후 %h:%i:%s')		\n";
				sql += "			END	)		\n";
				sql += "	END	) AS printDate		\n";
				sql += "	FROM	";
				sql += "	(	SELECT * FROM " + table_name;
				
				if(!sortCol.equals("")){
		        	sql += "	ORDER BY seq " + sortVal + "		\n";
		        }
				
		        if(isCount || pageNum==0){
				}else{
					sql += " LIMIT :startNum, :countPerPage	\n";
				}
		        
		        sql += "	)	\n";
				sql += "	P JOIN USER U ON P.user_seq = U.seq	\n";
			    sql += "	INNER JOIN HASHTAG H ON P.seq = H.puzzle_seq	\n";
			    sql += "	LEFT JOIN ( SELECT a.*, ( CASE WHEN DATE_FORMAT(a.regDate,'%p') = 'AM' THEN DATE_FORMAT(a.regDate, '%Y.%m.%d 오전 %h:%i:%s') ELSE DATE_FORMAT(a.regDate, '%Y.%m.%d 오후 %h:%i:%s') END ) AS printDate,  U.name AS userName, U.pictureUrl AS userPicture FROM REPLY AS a LEFT JOIN REPLY AS a2 ON a.puzzle_seq = a2.puzzle_seq AND a.seq <= a2.seq JOIN USER U ON a.user_seq = U.seq GROUP BY a.seq HAVING COUNT(*) <= :countPerPage2 ORDER BY a.puzzle_seq DESC, a.seq DESC ) R ON P.seq = R.puzzle_seq	\n";
			}
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
	        
	        //sql += "	GROUP BY P.seq		\n";
	        
	        if(!sortCol.equals("")){
	        	sql += "	ORDER BY " + sortCol + " " + sortVal + "		\n";
	        	sql += "	, R.seq DESC	\n";
	        }else{
	        	sql += "	ORDER BY R.seq DESC	\n";
	        }
		}
        
        System.out.println("sql:::"+sql);
        
        if(isCount){
        	return this.jdbcTemplate.queryForInt(sql,sqlJson);
		}else{
			if(searchMode || searchMode2){
				list  = this.jdbcTemplate.query(sql, sqlJson, new BeanPropertyRowMapper(PuzzleDTO.class));
			}else{
				this.jdbcTemplate.query(sql, sqlJson, getPuzzleRM());
				list = (puzzleMap==null) ? null : new ArrayList(puzzleMap.values());
				puzzleMap = null;
			}
	        return list;
		}
	}
	
	//	Write
	public int write(Object dto) {
		String sql = "";
		sql += "	INSERT INTO " + table_name + "	\n";
		sql += "	(user_seq, row, col, puzzleUrl)	\n";
		sql += "	values(:user_seq, :row, :col, :puzzleUrl)	\n";
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("user_seq", ((PuzzleDTO)dto).getUser_seq(), Types.NUMERIC);
		paramSource.addValue("row", ((PuzzleDTO)dto).getRow(), Types.NUMERIC);
		paramSource.addValue("col", ((PuzzleDTO)dto).getCol(), Types.NUMERIC);
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
