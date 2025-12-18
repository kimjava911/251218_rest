package kr.java.restapi.controller;

import jakarta.validation.Valid;
import kr.java.restapi.model.dto.ItemCreateRequest;
import kr.java.restapi.model.dto.ItemResponse;
import kr.java.restapi.model.dto.ItemUpdateRequest;
import kr.java.restapi.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
// #(4)-2
//@CrossOrigin(origins = "*")  // 모든 출처 허용 (개발용)
// #(4)-3
@CrossOrigin(
        origins = {
                "http://127.0.0.1:5500",    // Live Server
                "http://localhost:5500",    // Live Server (localhost)
                "http://localhost:3000"     // React 개발 서버
        },
        methods = {
                RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE
        },
        allowedHeaders = "*",
        maxAge = 3600
)
public class ItemApiController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemResponse> create(
            @Valid @RequestBody ItemCreateRequest request) {

        ItemResponse response = itemService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // READ: GET /api/items/{id} → 200 OK
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> findById(@PathVariable Long id) {
        ItemResponse response = itemService.findById(id);
        return ResponseEntity.ok(response);
    }

    // READ: GET /api/items → 200 OK
    @GetMapping
    public ResponseEntity<List<ItemResponse>> findAll() {
        List<ItemResponse> responses = itemService.findAll();
        return ResponseEntity.ok(responses);
    }

    // UPDATE: PUT /api/items/{id} → 200 OK
    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ItemUpdateRequest request) {

        ItemResponse response = itemService.update(id, request);
        return ResponseEntity.ok(response);
    }

    // DELETE: DELETE /api/items/{id} → 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}