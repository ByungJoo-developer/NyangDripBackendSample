package com.nyang.drip.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 프로젝트 전역에서 사용하는 문자열 및 데이터 구조 변환 공통 유틸리티 클래스입니다.
 * 주로 프론트엔드(CamelCase)와 백엔드/DB(SnakeCase) 간의 데이터 규격을 맞추는 역할을 합니다.
 * @author 김병주
 * @since 2026. 05. 22
 * @version 1.0
 */
public class CamelUtil {
    
    /**
     * 프론트엔드에서 수신한 소문자 카멜케이스(CamelCase) 구조의 Map을 
     * MyBatis 및 DB 쿼리에서 즉시 사용할 수 있도록 대문자 스네이크 케이스(SNAKE_CASE) 구조의 Map으로 변환합니다.
     * * <p><strong>변환 예시:</strong></p>
     * <ul>
     * <li>boardMstId &rarr; BOARD_MST_ID</li>
     * <li>boardId    &rarr; BOARD_ID</li>
     * <li>title      &rarr; TITLE</li>
     * </ul>
     * * @param camelMap 프론트엔드로부터 전달받은 소문자 카멜케이스 형태의 원본 Map (Key-Value)
     * @return 대문자 스네이크 케이스로 Key 구조가 치환된 새로운 HashMap 객체
     */
    public static Map<String, Object> convertToSnakeCase(Map<String, Object> camelMap) {
        // 방어 코드: 들어온 데이터가 없으면 빈 맵을 반환하여 NullPointerException을 방지합니다.
        if (camelMap == null) {
            return new HashMap<>();
        }

        Map<String, Object> snakeMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : camelMap.entrySet()) {
            String camelKey = entry.getKey();
            
            // 소문자와 대문자가 이어지는 경계 사이에 언더바(_)를 넣고 전체를 대문자로 치환
            String snakeKey = camelKey.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
            
            snakeMap.put(snakeKey, entry.getValue());
        }
        return snakeMap;
    }
    
    
    
}