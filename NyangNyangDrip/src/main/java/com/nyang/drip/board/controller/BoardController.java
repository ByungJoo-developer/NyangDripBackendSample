package com.nyang.drip.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nyang.drip.board.service.BoardService;

@RestController // 🌟 중요: 순수한 데이터(JSON)를 리턴하는 컨트롤러로 지정합니다.
@RequestMapping("/api/board") // 🌟 이 컨트롤러의 기본 주소창 주소를 세팅합니다.
@CrossOrigin(origins = "*") // 🌟 프론트엔드가 어디서 호출하든 허용해 주겠다는 치트키 설정!
public class BoardController {

    @Autowired
    private BoardService boardService; // 아까 만든 Service 주입받기

    // 웹 브라우저나 Vue.js가 GET 방식으로 /api/board/list 주소를 요청하면 이 메서드가 실행됩니다.
    @GetMapping("/list")
    public List<Map<String, Object>> getBoardList(
    		@RequestParam(value = "boardMstId", required = false) String boardMstId,
    		@RequestParam(value = "page", defaultValue = "0") int page, // 몇 페이지인지
    	    @RequestParam(value = "size", defaultValue = "5") int size // 한 페이지에 몇 개 볼지
    ) {
        
    	
    	int offset = (page - 1) * size; // 여기서 계산 완료!
        int limit = size;
    	
    	Map<String, Object> params = new HashMap<>();
    	params.put("boardMstId", boardMstId);
    	params.put("offset", 	 offset);
    	params.put("limit", 	 limit);
    	
    	// 서비스에게 DB 데이터 긁어오라고 시키고 그 결과를 바로 프론트엔드로 쏩니다!
        return boardService.getBoardList(params);
    }
    
    
    @GetMapping("/detail")
    public Map<String, Object> getBoardDetail(
    		@RequestParam(value = "boardMstId", required = false) String boardMstId,
    		@RequestParam(value = "boardId", required = false) String boardId
    ) {
       
    	Map<String, Object> params = new HashMap<>();
    	params.put("boardMstId", boardMstId);
    	params.put("boardId", 	 boardId);
    	
    	// 서비스에게 DB 데이터 긁어오라고 시키고 그 결과를 바로 프론트엔드로 쏩니다!
        return boardService.getBoardDetail(params);
    }
    
}