package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class TxBasicTest {
    @Autowired
    BasicService basicService;

    @Test
    void proxyCheck() {
        //BasicService$$EnhancerBySpringCGLIB...
        log.info("aop class={}", basicService.getClass());
        assertThat(AopUtils.isAopProxy(basicService)).isTrue();
    }

    @Test
    void txTest() {
        basicService.tx();
        basicService.nonTx();
    }

    @TestConfiguration
    static class TxApplyBasicConfig {
        @Bean
        BasicService basicService() {
            return new BasicService();
        }
    }

    @Slf4j
    @Transactional(readOnly = true)
    static class BasicService {

        //우선순위는 항상 더 구체적이고 자세한 것이 높은 우선순위를 가진다.
        //클래스 레벨에서 (readOnly = true)이지만 메서드 레벨에서 (readOnly = false) 이기때문에 tx()는 읽기 작업과 쓰기 작업 모두 할 수 있다.
        //(readOnly = true) : 읽기만 가능
        //(readOnly = false) : 읽기. 쓰기 모두 가능
        @Transactional(readOnly = false)
        public void tx() {
            log.info("call tx");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive(); //현재 쓰레드에 트랜잭션이 적용되어 있는지 확인할 수 있는 기능
            log.info("tx active={}", txActive);
        }
        public void nonTx() {
            log.info("call nonTx");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive(); //현재 쓰레드에 트랜잭션이 적용되어 있는지 확인할 수 있는 기능
            log.info("tx active={}", txActive);
        }
    }
}