package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.Reaction.ReactionResponse;
import org.example.dto.Reaction.CreateReactionRequest;
import org.example.service.ReactionService;
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

    @GetMapping("/message/{messageId}")
    @Operation(summary = "Получить последние 100 реакций сообщения")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    })
    public ResponseEntity<List<ReactionResponse>> getLast100ReactionsByMessage(
            @Parameter(description = "ID сообщения", required = true, example = "1")
            @PathVariable Long messageId) {
        List<ReactionResponse> response = reactionService.getLast100ReactionsByMessage(messageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить последние 100 реакций пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    })
    public ResponseEntity<List<ReactionResponse>> getLast100ReactionsByUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        List<ReactionResponse> response = reactionService.getLast100ReactionsByUser(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Добавить реакцию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Реакция добавлена"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<ReactionResponse> addReaction(
            @Parameter(description = "Данные реакции", required = true)
            @RequestBody CreateReactionRequest request) {
        ReactionResponse response = reactionService.addReaction(request);
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/message/{messageId}/user/{userId}")
    @Operation(summary = "Удалить реакцию пользователя на сообщение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Реакция удалена")
    })
    public ResponseEntity<Void> removeReaction(
            @Parameter(description = "ID сообщения", required = true, example = "1")
            @PathVariable Long messageId,
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        reactionService.removeReaction(messageId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить реакцию по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Реакция удалена"),
            @ApiResponse(responseCode = "404", description = "Реакция не найдена")
    })
    public ResponseEntity<Void> deleteReaction(
            @Parameter(description = "ID реакции", required = true, example = "1")
            @PathVariable Long id) {
        reactionService.deleteReaction(id);
        return ResponseEntity.noContent().build();
    }
}