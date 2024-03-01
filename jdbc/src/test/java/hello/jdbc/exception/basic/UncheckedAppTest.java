package hello.jdbc.exception.basic;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class UncheckedAppTest {

    @Test
    void unchecked() {
        Controller controller = new Controller();
        assertThatThrownBy(() -> controller.request())
                .isInstanceOf(Exception.class);
    }

    @Test
    void printEx() {
        Controller controller = new Controller();
        try {
            controller.request();
        } catch (Exception e) {
            //e.printStackTrace();
            log.info("ex", e);
        }
    }

    static class Controller {
        Service service = new Service();
        public void request() {
            service.logic();
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() {
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient {
        public void call() {
            throw new RuntimeConnectException("연결 실패");
        }
    }

    static class Repository {
        public void call() {
            try {
                runSQL();
            } catch (SQLException e) {
                throw new RuntimeSQLException(e);
            }
        }
        private void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException {
        public RuntimeSQLException() {
        }
        public RuntimeSQLException(Throwable cause) {
            super(cause);
            /*
            Throwable 타입의 인자 cause를 받는다.
            이 생성자 내부에서 super(cause);를 호출함으로써, 받아온 원인 예외 cause를 상위 클래스인 RuntimeException의 생성자로 전달한다.
            이 과정을 통해 RuntimeSQLException 인스턴스가 생성될 때, 그 원인이 되는 예외를 포함하게 된다.
            이렇게 예외를 포함시키는 방식은 예외의 원인을 추적하는 데 매우 유용하다.
            예외가 발생했을 때 스택 트레이스에서 이 원인 예외 정보를 함께 확인할 수 있어, 예외 처리와 디버깅에 도움이 된다.
             */
        }
    }
}