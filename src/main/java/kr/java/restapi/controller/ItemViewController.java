package kr.java.restapi.controller;

import kr.java.restapi.model.dto.ItemResponse;
import kr.java.restapi.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

// #(4)-1
/**
 * View 컨트롤러 (HTML 반환)
 */
@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemViewController {

    private final ItemService itemService;

    // GET /items → 상품 목록 페이지 (SSR)
    @GetMapping
    // import org.springframework.ui.Model;
    public String itemList(Model model) {
        List<ItemResponse> items = itemService.findAll();
        model.addAttribute("items", items);
        return "items";  // templates/items.html
    }
}