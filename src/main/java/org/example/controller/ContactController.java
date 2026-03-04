package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.Contact.CreateContactRequest;
import org.example.dto.Contact.ContactResponse;
import org.example.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Contacts", description = "Управление контактами")
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    @Operation(summary = "Получить последние 100 контактов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    })
    public ResponseEntity<List<ContactResponse>> getLast100Contacts() {
        List<ContactResponse> response = contactService.getLast100Contacts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить контакт по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Контакт найден"),
            @ApiResponse(responseCode = "404", description = "Контакт не найден")
    })
    public ResponseEntity<ContactResponse> getContactById(
            @Parameter(description = "ID контакта", required = true, example = "1")
            @PathVariable Long id) {
        ContactResponse response = contactService.getContactById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить последние 100 контактов пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    })
    public ResponseEntity<List<ContactResponse>> getLast100ContactsByUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        List<ContactResponse> response = contactService.getLast100ContactsByUser(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Добавить контакт")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Контакт успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<ContactResponse> addContact(
            @Parameter(description = "Данные контакта", required = true)
            @RequestBody CreateContactRequest request) {
        ContactResponse response = contactService.addContact(request);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}/block")
    @Operation(summary = "Заблокировать контакт")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Контакт заблокирован"),
            @ApiResponse(responseCode = "404", description = "Контакт не найден")
    })
    public ResponseEntity<ContactResponse> blockContact(
            @Parameter(description = "ID контакта", required = true, example = "1")
            @PathVariable Long id) {
        ContactResponse response = contactService.blockContact(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/unblock")
    @Operation(summary = "Разблокировать контакт")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Контакт разблокирован"),
            @ApiResponse(responseCode = "404", description = "Контакт не найден")
    })
    public ResponseEntity<ContactResponse> unblockContact(
            @Parameter(description = "ID контакта", required = true, example = "1")
            @PathVariable Long id) {
        ContactResponse response = contactService.unblockContact(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить контакт")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Контакт успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Контакт не найден")
    })
    public ResponseEntity<Void> deleteContact(
            @Parameter(description = "ID контакта", required = true, example = "1")
            @PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }
}