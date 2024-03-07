package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v3/items")
@RequiredArgsConstructor
public class ValidationItemControllerV3 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        log.info("init binder {}", dataBinder);
        dataBinder.addValidators(itemValidator);
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v3/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v3/addForm";
    }

//    @PostMapping("/add")
//    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
//
//        //public FieldError(String objectName, String field, String defaultMessage)
//        //objectName : @ModelAttribute 이름
//        //field : 오류가 발생한 필드 이름
//        //defaultMessage : 오류 기본 메시지
//
//        if (!StringUtils.hasText(item.getItemName())) {
//            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
//        }
//
//        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
//            bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
//        }
//
//        if (item.getQuantity() == null || item.getQuantity() >= 10000) {
//            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."));
//        }
//
//        //특정 필드 예외가 아닌 전체 예외
//        //public ObjectError(String objectName, String defaultMessage)
//        //objectName : @ModelAttribute 의 이름
//        //defaultMessage : 오류 기본 메시지
//        if (item.getPrice() != null && item.getQuantity() != null) {
//            int resultPrice = item.getPrice() * item.getQuantity();
//            if (resultPrice < 10000) {
//                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
//            }
//        }
//
//        //검증에 실패하면 다시 입력 폼으로
//        if (bindingResult.hasErrors()) {
//            log.info("errors={}", bindingResult);
//            return "validation/v3/addForm";
//        }
//
//        //성공 로직
//        Item savedItem = itemRepository.save(item);
//        redirectAttributes.addAttribute("itemId", savedItem.getId());
//        redirectAttributes.addAttribute("status", true);
//        return "redirect:/validation/v3/items/{itemId}";
//    }

//    @PostMapping("/add")
//    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
//
//        //public FieldError(String objectName, String field, @Nullable Object rejectedValue, boolean bindingFailure, @Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage)
//
//        //objectName : @ModelAttribute 이름
//        //field : 오류가 발생한 필드 이름
//        //rejectedValue : 사용자가 입력한 값(거절된 값)
//        //bindingFailure : 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값
//        //codes : 메시지 코드
//        //arguments : 메시지에서 사용하는 인자
//        //defaultMessage : 오류 기본 메시지
//
//        if (!StringUtils.hasText(item.getItemName())) {
//            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수입니다."));
//        }
//
//        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
//            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
//        }
//
//        if (item.getQuantity() == null || item.getQuantity() >= 10000) {
//            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 최대 9,999 까지 허용합니다."));
//        }
//
//        //특정 필드 예외가 아닌 전체 예외
//        //public ObjectError(String objectName, String defaultMessage)
//        //objectName : @ModelAttribute 의 이름
//        //defaultMessage : 오류 기본 메시지
//        if (item.getPrice() != null && item.getQuantity() != null) {
//            int resultPrice = item.getPrice() * item.getQuantity();
//            if (resultPrice < 10000) {
//                bindingResult.addError(new ObjectError("item", null, null, "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
//            }
//        }
//
//        //검증에 실패하면 다시 입력 폼으로
//        if (bindingResult.hasErrors()) {
//            log.info("errors={}", bindingResult);
//            return "validation/v3/addForm";
//        }
//
//        //성공 로직
//        Item savedItem = itemRepository.save(item);
//        redirectAttributes.addAttribute("itemId", savedItem.getId());
//        redirectAttributes.addAttribute("status", true);
//        return "redirect:/validation/v3/items/{itemId}";
//    }

//    @PostMapping("/add")
//    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
//
//        //public FieldError(String objectName, String field, @Nullable Object rejectedValue, boolean bindingFailure, @Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage)
//
//        //objectName : @ModelAttribute 이름
//        //field : 오류가 발생한 필드 이름
//        //rejectedValue : 사용자가 입력한 값(거절된 값)
//        //bindingFailure : 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값
//        //codes : 메시지 코드
//        //arguments : 메시지에서 사용하는 인자
//        //defaultMessage : 오류 기본 메시지
//
//        if (!StringUtils.hasText(item.getItemName())) {
//            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));
//        }
//
//        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
//            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
//        }
//
//        if (item.getQuantity() == null || item.getQuantity() >= 10000) {
//            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
//        }
//
//        //특정 필드 예외가 아닌 전체 예외
//        //public ObjectError(String objectName, String defaultMessage)
//        //objectName : @ModelAttribute 의 이름
//        //defaultMessage : 오류 기본 메시지
//        if (item.getPrice() != null && item.getQuantity() != null) {
//            int resultPrice = item.getPrice() * item.getQuantity();
//            if (resultPrice < 10000) {
//                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice}, null));
//            }
//        }
//
//        //검증에 실패하면 다시 입력 폼으로
//        if (bindingResult.hasErrors()) {
//            log.info("errors={}", bindingResult);
//            return "validation/v3/addForm";
//        }
//
//        //성공 로직
//        Item savedItem = itemRepository.save(item);
//        redirectAttributes.addAttribute("itemId", savedItem.getId());
//        redirectAttributes.addAttribute("status", true);
//        return "redirect:/validation/v3/items/{itemId}";
//    }

//    @PostMapping("/add")
//    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
//
//        //public FieldError(String objectName, String field, @Nullable Object rejectedValue, boolean bindingFailure, @Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage)
//
//        //objectName : @ModelAttribute 이름
//        //field : 오류가 발생한 필드 이름
//        //rejectedValue : 사용자가 입력한 값(거절된 값)
//        //bindingFailure : 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값
//        //codes : 메시지 코드
//        //arguments : 메시지에서 사용하는 인자
//        //defaultMessage : 오류 기본 메시지
//
//        log.info("objectName={}", bindingResult.getObjectName());
//        log.info("target={}", bindingResult.getTarget());
//
//        //void rejectValue(@Nullable String field, String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage);
//
//        //field : 오류 필드명
//        //errorCode : 오류 코드(이 오류 코드는 메시지에 등록된 코드 아님. messageResolver를 위한 오류 코드)
//        //errorArgs : 오류 메시지에서 {0} 을 치환하기 위한 값
//        //defaultMessage : 오류 메시지를 찾을 수 없을 때 사용하는 기본 메시지
//
//        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");
//
//        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
//            bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
//        }
//
//
//        if (item.getQuantity() == null || item.getQuantity() >= 10000) {
//            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
//        }
//
//        //특정 필드 예외가 아닌 전체 예외
//        //void reject(String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage);
//
//        //errorCode : 오류 코드(이 오류 코드는 메시지에 등록된 코드 아님. messageResolver를 위한 오류 코드)
//        //errorArgs : 오류 메시지에서 {0} 을 치환하기 위한 값
//        //defaultMessage : 오류 메시지를 찾을 수 없을 때 사용하는 기본 메시지
//        if (item.getPrice() != null && item.getQuantity() != null) {
//            int resultPrice = item.getPrice() * item.getQuantity();
//            if (resultPrice < 10000) {
//                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
//            }
//        }
//
//        //검증에 실패하면 다시 입력 폼으로
//        if (bindingResult.hasErrors()) {
//            log.info("errors={}", bindingResult);
//            return "validation/v3/addForm";
//        }
//
//        //성공 로직
//        Item savedItem = itemRepository.save(item);
//        redirectAttributes.addAttribute("itemId", savedItem.getId());
//        redirectAttributes.addAttribute("status", true);
//        return "redirect:/validation/v3/items/{itemId}";
//    }

//    @PostMapping("/add")
//    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
//
//        itemValidator.validate(item, bindingResult);
//
//        //검증에 실패하면 다시 입력 폼으로
//        if (bindingResult.hasErrors()) {
//            log.info("errors={}", bindingResult);
//            return "validation/v3/addForm";
//        }
//
//        //성공 로직
//        Item savedItem = itemRepository.save(item);
//        redirectAttributes.addAttribute("itemId", savedItem.getId());
//        redirectAttributes.addAttribute("status", true);
//        return "redirect:/validation/v3/items/{itemId}";
//    }

    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "validation/v3/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }
}

