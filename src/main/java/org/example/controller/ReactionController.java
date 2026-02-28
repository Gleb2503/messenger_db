package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.entity.Reaction;
import org.example.service.ReactionService;
import org.example.exeption.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Reactions", description = "Управление реакциями")
public class ReactionController {

    private final ReactionService reactionService;

    @GetMapping
    @Operation(summary = "Получить все реакции")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reaction.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<Reaction>> getAllReactions() {
        return ResponseEntity.ok(reactionService.getAllReactions());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить реакцию по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Реакция найдена"),
            @ApiResponse(responseCode = "404", description = "Реакция не найдена")
    })
    public ResponseEntity<Reaction> getReactionById(
            @Parameter(description = "Уникальный идентификатор реакции", required = true, example = "1")
            @PathVariable Long id) {
        return reactionService.getReactionById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Reaction not found with id: " + id));
    }

    @GetMapping("/message/{messageId}")
    @Operation(summary = "Получить реакции на сообщение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                    [
                      {
                        "id": 1,
                        "message": {"id": 1},
                        "user": {"id": 2, "username": "maria_design"},
                        "emoji": "👍",
                        "createdAt": "2026-02-27T17:35:00"
                      },
                      {
                        "id": 2,
                        "message": {"id": 1},
                        "user": {"id": 3, "username": "petr_dev"},
                        "emoji": "❤️",
                        "createdAt": "2026-02-27T17:36:00"
                      }
                    ]
                    """))),
            @ApiResponse(responseCode = "404", description = "Сообщение не найдено")
    })
    public ResponseEntity<List<Reaction>> getReactionsByMessageId(
            @Parameter(description = "Уникальный идентификатор сообщения", required = true, example = "1")
            @PathVariable Long messageId) {
        return ResponseEntity.ok(reactionService.getReactionsByMessageId(messageId));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить реакции пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<List<Reaction>> getReactionsByUserId(
            @Parameter(description = "Уникальный идентификатор пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(reactionService.getReactionsByUserId(userId));
    }

    @PostMapping
    @Operation(summary = "Добавить реакцию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Реакция успешно добавлена",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Reaction.class),
                            examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "message": {"id": 1},
                      "user": {"id": 2, "username": "maria_design"},
                      "emoji": "👍",
                      "createdAt": "2026-02-27T17:35:00"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Сообщение или пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Reaction> addReaction(
            @Parameter(description = "Данные для создания реакции", required = true)
            @RequestBody Reaction reaction) {
        return ResponseEntity.ok(reactionService.addReaction(reaction));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить реакцию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Реакция успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Реакция не найдена")
    })
    public ResponseEntity<Void> removeReaction(
            @Parameter(description = "Уникальный идентификатор реакции", required = true, example = "1")
            @PathVariable Long id) {
        reactionService.removeReaction(id);
        return ResponseEntity.noContent().build();
    }
}