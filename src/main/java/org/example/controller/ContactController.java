package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.entity.Contact;
import org.example.service.ContactService;
import org.example.exeption.ResourceNotFoundException;
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
    @Operation(summary = "Получить все контакты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Contact.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<Contact>> getAllContacts() {
        return ResponseEntity.ok(contactService.getAllContacts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить контакт по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Контакт найден"),
            @ApiResponse(responseCode = "404", description = "Контакт не найден")
    })
    public ResponseEntity<Contact> getContactById(
            @Parameter(description = "Уникальный идентификатор контакта", required = true, example = "1")
            @PathVariable Long id) {
        return contactService.getContactById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить контакты пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<List<Contact>> getContactsByUserId(
            @Parameter(description = "Уникальный идентификатор пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(contactService.getContactsByUserId(userId));
    }

    @GetMapping("/user/{userId}/blocked")
    @Operation(summary = "Получить заблокированные контакты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                    [
                      {
                        "id": 3,
                        "user": {"id": 1, "username": "ivan_dev"},
                        "contactUser": {"id": 5, "username": "spam_user"},
                        "nickname": null,
                        "isBlocked": true,
                        "createdAt": "2026-02-27T16:00:00"
                      }
                    ]
                    """))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<List<Contact>> getBlockedContacts(
            @Parameter(description = "Уникальный идентификатор пользователя", required = true, example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(contactService.getBlockedContacts(userId));
    }

    @PostMapping
    @Operation(summary = "Добавить контакт")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Контакт успешно добавлен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Contact.class),
                            examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "user": {"id": 1, "username": "ivan_dev"},
                      "contactUser": {"id": 2, "username": "maria_design"},
                      "nickname": "Маша Дизайн",
                      "isBlocked": false,
                      "createdAt": "2026-02-27T17:20:00"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Contact> addContact(
            @Parameter(description = "Данные для создания контакта", required = true)
            @RequestBody Contact contact) {
        return ResponseEntity.ok(contactService.addContact(contact));
    }

    @PutMapping("/{id}/block")
    @Operation(summary = "Заблокировать контакт")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Контакт заблокирован"),
            @ApiResponse(responseCode = "404", description = "Контакт не найден")
    })
    public ResponseEntity<Contact> blockContact(
            @Parameter(description = "Уникальный идентификатор контакта", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(contactService.blockContact(id));
    }

    @PutMapping("/{id}/unblock")
    @Operation(summary = "Разблокировать контакт")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Контакт разблокирован"),
            @ApiResponse(responseCode = "404", description = "Контакт не найден")
    })
    public ResponseEntity<Contact> unblockContact(
            @Parameter(description = "Уникальный идентификатор контакта", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(contactService.unblockContact(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить контакт")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Контакт успешно удален"),
            @ApiResponse(responseCode = "404", description = "Контакт не найден")
    })
    public ResponseEntity<Void> deleteContact(
            @Parameter(description = "Уникальный идентификатор контакта", required = true, example = "1")
            @PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }
}