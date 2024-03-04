package hello.itemservice.domain.item;
    /*
        FAST : 빠른배송
        NOMAL : 일반배송
        SLOW : 느린배송
     */

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliveryCode {
    private String code;
    private String displayName;
}
