package hello.itemservice.validation;

import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageCodesResolverTest {
    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    //String[] resolveMessageCodes(String errorCode, String objectName);

    //errorCode: 검증 과정에서 발견된 오류의 코드
    //objectName: 검증 대상 객체의 이름
    @Test
    void messageCodesResolverObject() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
        assertThat(messageCodes).containsExactly("required.item", "required");
    }

    //String[] resolveMessageCodes(String errorCode, String objectName, String field, Class<?> fieldType);

    //errorCode: 검증 과정에서 발견된 오류의 코드.
    //objectName: 검증 대상 객체의 이름.
    //field: 검증 대상 필드의 이름입니다. 필드 수준의 오류 코드 생성에 사용된다.
    //fieldType: 검증 대상 필드의 타입입니다. 필드 타입을 기반으로 메시지 코드를 생성하는 데 사용될 수 있다.
    @Test
    void messageCodesResolverField() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
        assertThat(messageCodes).containsExactly(
                "required.item.itemName",
                "required.itemName",
                "required.java.lang.String",
                "required"
        );
    }
}
