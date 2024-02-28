package ru.practicum.shareit.itemRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                         @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestClient.create(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> get(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.get(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getWithPagination(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @RequestParam(defaultValue = "0") Integer from,
                                                    @RequestParam(defaultValue = "10") Integer size) {
        return itemRequestClient.getWithPagination(userId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                      @PathVariable Long id) {
        return itemRequestClient.getById(id, userId);
    }
}
