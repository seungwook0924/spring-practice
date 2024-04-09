package hello.springtx.propagation;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LogRepository {
    private final EntityManager em;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(Log logMessage) {
        log.info("log 저장");
        em.persist(logMessage);
        if (logMessage.getMessage().contains("로그예외")) {
            log.info("log 저장시 예외 발생");
            throw new RuntimeException("예외 발생");
        }
    }

    public Optional<Log> find(String message) {

        /*
        getResultList 메소드를 호출하여 쿼리의 실행 결과를 리스트로 가져온다.
        이후 Java 8의 스트림 API를 사용하여 해당 리스트를 스트림으로 변환하고, findAny 메소드를 통해 결과 리스트 중 임의의 하나를 선택한다.
        findAny는 Optional<Log> 타입을 반환
         */
        return em.createQuery("select l from Log l where l.message = :message", Log.class)
                .setParameter("message", message)
                .getResultList().stream().findAny();
    }
}