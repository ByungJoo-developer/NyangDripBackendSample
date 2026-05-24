package com.nyang.drip.board.service;

import java.util.List;
import java.util.Map;

public interface BoardService {

	List<Map<String, Object>> getBoardSearchList(Map<String, Object> params);	// 게시글 목록 조회
	
	List<Map<String, Object>> getBoardList(Map<String, Object> params);	// 게시글 목록 조회
	
	Map<String, Object> getBoardDetail(Map<String, Object> params);		// 게시글 목록 조회

	int updateBoard(Map<String, Object>  params);						// 게시글 수정
	
}
