package hello.typeconverter.type;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
//equals()와 hashCode() 메서드를 자동으로 생성, 이 두 메서드는 객체의 동등성 비교와 해시 기반 컬렉션에서 객체를 사용할 때 필수적으로 구현해야 하는 메서드
//객체가 달라도 논리적으로 동등하다면 동등 객체
public class IpPort {
    private String ip;
    private int port;
    public IpPort(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}