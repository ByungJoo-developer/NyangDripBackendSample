package com.nyang.drip.board.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper // 🌟 이 어노테이션이 핵심입니다!
public interface BoardMapper {

	List<Map<String, Object>> selectBoardSearchList(Map<String, Object> params);
	
    List<Map<String, Object>> selectBoardList(Map<String, Object> params);

    Map<String, Object> selectBoardDetail(Map<String, Object> params);
    
    int upsertBoard(Map<String, Object> params);
    
    int deleteBoard(Map<String, Object> params);
    
    
}