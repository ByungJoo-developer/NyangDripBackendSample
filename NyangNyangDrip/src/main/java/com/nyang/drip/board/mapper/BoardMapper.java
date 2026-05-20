package com.nyang.drip.board.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper // 🌟 이 어노테이션이 핵심입니다!
public interface BoardMapper {
	// 파라미터를 넘겨주기 위해 Map을 인자로 추가합니다
    List<Map<String, Object>> selectBoardList(Map<String, Object> params);

}