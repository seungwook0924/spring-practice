package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {
    //supports() : 해당 검증기를 지원하는 여부 확인
    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
    }

    //validate(Object target, Errors errors) : 검증 대상 객체와 BindingResult
    @Override
    public void validate(Object target, Errors errors) {
        Item item = (Item) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "itemName", "required");


        //void rejectValue(@Nullable String field, String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage);
        //field : 오류 필드명
        //errorCode : 오류 코드(이 오류 코드는 메시지에 등록된 코드 아님. messageResolver를 위한 오류 코드)
        //errorArgs : 오류 메시지에서 {0} 을 치환하기 위한 값
        //defaultMessage : 오류 메시지를 찾을 수 없을 때 사용하는 기본 메시지

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            errors.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }


        if (item.getQuantity() == null || item.getQuantity() >= 10000) {
            errors.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        //특정 필드 예외가 아닌 전체 예외
        //void reject(String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage);

        //errorCode : 오류 코드(이 오류 코드는 메시지에 등록된 코드 아님. messageResolver를 위한 오류 코드)
        //errorArgs : 오류 메시지에서 {0} 을 치환하기 위한 값
        //defaultMessage : 오류 메시지를 찾을 수 없을 때 사용하는 기본 메시지
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                errors.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
    }
}
