package com.nyang.drip.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nyang.drip.board.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
    private BoardMapper boardMapper; // Mapper 인터페이스 주입
	
    @Override
    public List<Map<String, Object>> getBoardList(Map<String, Object> params) {

    	return boardMapper.selectBoardList(params);
                                                   
    }
    
    @Override
    public Map<String, Object> getBoardDetail(Map<String, Object> params) {
    
    	return boardMapper.selectBoardDetail(params);
    	
    }
    
    
}