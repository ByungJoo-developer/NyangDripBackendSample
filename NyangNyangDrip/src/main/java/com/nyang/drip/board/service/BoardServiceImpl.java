package com.nyang.drip.board.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.nyang.drip.board.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
    private BoardMapper boardMapper; // Mapper 인터페이스 주입
	
    @Override
    public List<Map<String, Object>> getBoardList(Map<String, Object> params) {

    	/*************************************************/
    	/** keyword 가 있는 경우 역인덱스 검색 사용으로 분기 처리  	**/
    	/*************************************************/
    	if( StringUtils.hasText((String)params.get("keyword")))  
    		 
    	{
    		String[] keywordArray = ((String)params.get("keyword")).trim().split("\\s+");
    		// MyBatis가 인식할 수 있도록 List로 변환해서 params에 다시 넣어줍니다.
    		params.put("keywordList", Arrays.asList(keywordArray));
    		
    		return boardMapper.selectBoardSearchList(params);
    	}
    	/*************************************************/
    	/** 단순 부분 범위 처리 조회  							**/
    	/*************************************************/
    	else 
    	{
    		return boardMapper.selectBoardList(params);
    	}
                                                   
    }
    
    @Override
    public Map<String, Object> getBoardDetail(Map<String, Object> params) {
    
    	return boardMapper.selectBoardDetail(params);
    	
    }
    
    @Override
    public int upsertBoard(Map<String, Object>  params) {						// 게시글 수정 

    	String title = (String) params.get("title");
        String content = (String) params.get("content");
    	
    	// 1. 제목 공백 분리하여 리스트화
        if (StringUtils.hasText(title)) {
            String[] titleWords = title.trim().split("\\s+");
            params.put("titleKeywordList", Arrays.stream(titleWords).distinct().collect(Collectors.toList()));
        }
        
        // 2. 본문 공백 분리하여 리스트화
        if (StringUtils.hasText(content)) {
            String[] contentWords = content.trim().split("\\s+");
            params.put("contentKeywordList", Arrays.stream(contentWords).distinct().collect(Collectors.toList()));
        }
    	
    	return boardMapper.upsertBoard(params);
    } 
    
    
    public int deleteBoard(Map<String, Object>  params) {						// 게시글 수정
    
    	return boardMapper.deleteBoard(params);
    	
    }
    
    
    
}