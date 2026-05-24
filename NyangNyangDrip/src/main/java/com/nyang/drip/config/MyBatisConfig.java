package com.nyang.drip.config;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.MapWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Map;

/**
 * MyBatis 가 조회 결과를 Map 구조로 뱉을 때, 
 * 전역 설정인 카멜케이스(mapUnderscoreToCamelCase)를 강제로 적용받도록 만드는 전역 설정 클래스입니다.
 * * @author 공통아키텍처 (AA)
 * @since 2026. 05. 22
 */
@Configuration
public class MyBatisConfig {

    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> configuration.setObjectWrapperFactory(new ObjectWrapperFactory() {
            
        	@Override
        	public boolean hasWrapperFor(Object object) {
        	    // 💡 중요: object가 null이 아니고, Map 계열(HashMap, LinkedHashMap 등)이면 
        	    // MyBatis 래퍼(MapWrapper)가 이미 씌워진 게 아닐 때 무조건 가로챕니다!
        	    return object != null && object instanceof Map && !(object instanceof MapWrapper);
        	}

            @Override
            public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
                return new MapWrapper(metaObject, (Map) object) {
                    
                    @Override
                    public String findProperty(String name, boolean useCamelCaseMapping) {
                        // 💡 application.properties 의 map-underscore-to-camel-case=true 설정을 인식합니다.
                        if (useCamelCaseMapping) {
                            StringBuilder sb = new StringBuilder();
                            boolean nextUpper = false;
                            
                            // 대문자 SNAKE_CASE 를 소문자 camelCase 로 한 자 한 자 조립하는 변환부
                            for (int i = 0; i < name.length(); i++) {
                                char c = name.charAt(i);
                                if (c == '_') {
                                    nextUpper = true;
                                } else {
                                    if (nextUpper) {
                                        sb.append(Character.toUpperCase(c));
                                        nextUpper = false;
                                    } else {
                                        sb.append(Character.toLowerCase(c));
                                    }
                                }
                            }
                            return sb.toString();
                        }
                        return name;
                    }
                };
            }
        });
    }
}