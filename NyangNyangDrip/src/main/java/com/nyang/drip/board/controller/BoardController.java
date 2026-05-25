package com.nyang.drip.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nyang.drip.board.service.BoardService;
import com.nyang.drip.common.util.CamelUtil;

@RestController // 🌟 중요: 순수한 데이터(JSON)를 리턴하는 컨트롤러로 지정합니다.
@RequestMapping("/api/board") // 🌟 이 컨트롤러의 기본 주소창 주소를 세팅합니다.
@CrossOrigin(origins = "*") // 🌟 프론트엔드가 어디서 호출하든 허용해 주겠다는 치트키 설정!
public class BoardController {

    @Autowired
    private BoardService boardService; // 아까 만든 Service 주입받기

    /**
     * 웹 브라우저나 Vue.js가 GET 방식으로 /api/board/list 주소를 요청하면 이 메서드가 실행됩니다.
     * @param boardMstId	게시판 종류 아이디
     * @param page			요청할 페이지 번호 (기본값: 1) 
     * @param size			한 페이지당 보여줄 게시글 수 (기본값: 5)	
     * @param title			검색할 제목 (선택)
     * @param content		검색할 내용 (선택)
     * @return				게시글 목록 데이터(List<Map<String, Object>>)
     */
    @GetMapping("/list")
    public List<Map<String, Object>> getBoardList(
    		@RequestParam(value = "boardMstId", 	required = false) String boardMstId,
    		@RequestParam(value = "keywordType", 	defaultValue = "") String keywordType,
    		@RequestParam(value = "keyword", 		defaultValue = "") String keyword,
    		@RequestParam(value = "page", 			defaultValue = "1") int page, 				
    	    @RequestParam(value = "size", 			defaultValue = "5") int size 				
    ) {
        
    	
    	int offset = (page - 1) * size; // 여기서 계산 완료!
        int limit = size;
    	
    	Map<String, Object> params = new HashMap<>();
    	params.put("boardMstId", 	boardMstId);
    	params.put("keywordType", 	keywordType);
    	params.put("keyword", 	 	keyword);
    	params.put("offset", 	 	offset);
    	params.put("limit", 	 	limit);
    	
    	// 서비스에게 DB 데이터 긁어오라고 시키고 그 결과를 바로 프론트엔드로 쏩니다!
        return boardService.getBoardList(params);
    }
    
    
    /**
     * 특정 게시글의 상세 정보를 조회합니다.
     * 
     * @param boardMstId 게시판 마스터 아이디
     * @param boardId    게시글 고유 아이디
     * @return 게시글 상세 정보 데이터(Map<String, Object>)
     */
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
    
    
    @PostMapping("/upsert") // 💡 수정이니까 PostMapping
    public ResponseEntity<Map<String, Object>> upsertBoard(@RequestBody Map<String, Object> params) {
    	
    	Map<String, Object> response = new HashMap<>();
        
        try {
            // 💡 핵심: 서비스에서 '실제 수정된 건수(int)'를 받아옵니다. 자바 규칙으로 받음
            //int updatedRows = boardService.updateBoard(CamelUtil.convertToSnakeCase(params));
        	int updatedRows = boardService.upsertBoard(params);
        	
            // 1. 만약 수정된 건수가 0건이라면? (데이터가 없거나 글번호가 잘못됨)
            if (updatedRows == 0) {
                response.put("success", false);
                response.put("message", "수정할 게시글을 찾을 수 없거나 이미 삭제된 글입니다냥... 😿");
                
                // 데이터는 없지만 서버 에러는 아니므로 400(Bad Request)이나 404(Not Found)를 던집니다.
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            // 2. 실제 1건 이상 성공적으로 수정된 경우
            response.put("success", true);
            response.put("message", "게시글이 성공적으로 수정되었습니다냥! 🐾");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "서버 오류로 인해 수정에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PostMapping("/delete") // 💡 delete PostMapping
    public ResponseEntity<Map<String, Object>> deleteBoard(@RequestBody Map<String, Object> params) {
    
    	Map<String, Object> response = new HashMap<>();
        
        try {
            // 💡 핵심: 서비스에서 '실제 수정된 건수(int)'를 받아옵니다. 자바 규칙으로 받음
            //int updatedRows = boardService.updateBoard(CamelUtil.convertToSnakeCase(params));
        	int updatedRows = boardService.deleteBoard(params);
        	
            // 1. 만약 수정된 건수가 0건이라면? (데이터가 없거나 글번호가 잘못됨)
            if (updatedRows == 0) {
                response.put("success", false);
                response.put("message", "수정할 게시글을 찾을 수 없거나 이미 삭제된 글입니다냥... 😿");
                
                // 데이터는 없지만 서버 에러는 아니므로 400(Bad Request)이나 404(Not Found)를 던집니다.
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            // 2. 실제 1건 이상 성공적으로 수정된 경우
            response.put("success", true);
            response.put("message", "게시글이 성공적으로 수정되었습니다냥! 🐾");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "서버 오류로 인해 수정에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    	
    
    }
}