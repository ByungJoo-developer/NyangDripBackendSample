package com.nyang.drip.config;

import java.util.Collections;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Configuration
public class TransactionAspectConfig {

    private final TransactionManager transactionManager;

    // 스프링 부트가 자동으로 주입해주는 트랜잭션 매니저
    public TransactionAspectConfig(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Bean
    public TransactionInterceptor txAdvice() {
        // 1. 쓰기 트랜잭션 설정 (CUD: 데이터 변경용)
        RuleBasedTransactionAttribute writeAttr = new RuleBasedTransactionAttribute();
        writeAttr.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        // 모든 예외(Exception) 발생 시 롤백 수행하도록 설정
        writeAttr.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));

        // 2. 읽기 전용 트랜잭션 설정 (R: 조회용 성능 최적화)
        RuleBasedTransactionAttribute readAttr = new RuleBasedTransactionAttribute();
        readAttr.setReadOnly(true);

        // 3. 메서드 이름 패턴에 따른 트랜잭션 매칭 규칙 정의
        NameMatchTransactionAttributeSource txSource = new NameMatchTransactionAttributeSource();

        // [기본 쓰기 규칙]
        txSource.addTransactionalMethod("insert*", writeAttr);
        txSource.addTransactionalMethod("update*", writeAttr);
        txSource.addTransactionalMethod("delete*", writeAttr);
        txSource.addTransactionalMethod("modify*", writeAttr);

        // ✨ [커스텀 쓰기 규칙 추가!]
        txSource.addTransactionalMethod("upsert*", writeAttr);    // upsert 로 시작하는 메서드 자동 트랜잭션
        txSource.addTransactionalMethod("sync*", writeAttr);      // sync   로 시작하는 메서드 자동 트랜잭션	
        
        
        // [읽기 전용 규칙]
        txSource.addTransactionalMethod("select*", readAttr);
        txSource.addTransactionalMethod("get*", readAttr);
        txSource.addTransactionalMethod("find*", readAttr);
        txSource.addTransactionalMethod("search*", readAttr);

        return new TransactionInterceptor(transactionManager, txSource);
    }

    @Bean
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        
        // 🎯 [중요] 선배님 프로젝트의 서비스 패키지 경로에 맞게 이 부분을 수정하셔야 합니다!
        // 아래 설정은 com.tarot.service 패키지 및 하위 패키지의 모든 Service 또는 ServiceImpl 클래스를 감시합니다.
        pointcut.setExpression("execution(* com.nyang.drip..service..*Service.*(..))");
        
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }
}
