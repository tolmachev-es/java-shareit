package ru.practicum.shareit.request.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;


@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
@Slf4j
public class ItemRequestController {
    public final ItemRequestService itemRequestService;

    @PostMapping
    @Operation(summary = "Добавление запроса на вещь")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный параметры запроса")
    })
    public ItemRequestDto create(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                 @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.create(itemRequestDto, userId);
    }

    @GetMapping
    @Operation(summary = "Получение запросов по пользователю")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список запросов успешно получен"),
            @ApiResponse(responseCode = "400", description = "Неверный параметры запроса")
    })
    public Set<ItemRequestDto> get(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.get(userId);
    }

    @GetMapping("/all")
    @Operation(summary = "Получение всех запросов на добавление вещей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список запросов успешно получен"),
            @ApiResponse(responseCode = "400", description = "Неверный параметры запроса")
    })
    public Set<ItemRequestDto> getWithPagination(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        return itemRequestService.getWithPagination(userId, from, size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение запроса по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос успешно получен на просмотр"),
            @ApiResponse(responseCode = "404", description = "Не найден запрос")
    })
    public ItemRequestDto get(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long id) {
        return itemRequestService.getById(id, userId);
    }
}
