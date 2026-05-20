package com.nyang.drip.board.service;

import java.util.HashMap;
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
    public List<Map<String, Object>> getBoardList() {

    	Map<String, Object> params = new HashMap<>();
    	params.put("catId", "CAT001");
    	params.put("offset", 0);
    	params.put("limit", 20);

    	return boardMapper.selectBoardList(params);
                                                   
    }
}