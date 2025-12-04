package com.web.bookstore;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;

    @GetMapping("/")
    String index() {
        return "index.html";
    }

    @GetMapping("/test")
    String test() {
        return "test.html";
    }

    @PostMapping("/add")
    String addPost(@RequestParam String title, @RequestParam Integer price) {

        Item item = new Item();
        item.setTitle(title);
        item.setPrice(price);

        itemRepository.save(item); // ← 이제 제대로 저장됨
        return "redirect:/";
    }
}