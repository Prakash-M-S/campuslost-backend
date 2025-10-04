package com.campuslost.campuslost.controller;

import com.campuslost.campuslost.dto.ItemRequest;
import com.campuslost.campuslost.dto.response.ItemResponse;
import com.campuslost.campuslost.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
// CORS handled globally in SecurityConfig
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * Create new item (POST /api/items)
     */
    @PostMapping
    public ResponseEntity<?> createItem(@RequestBody ItemRequest request, Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            ItemResponse item = itemService.createItem(request, userEmail);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error creating item: " + e.getMessage());
        }
    }

    /**
     * Get all items (GET /api/items)
     */
    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        List<ItemResponse> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    /**
     * Get items by status (GET /api/items/status/{status})
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ItemResponse>> getItemsByStatus(@PathVariable String status) {
        List<ItemResponse> items = itemService.getItemsByStatus(status);
        return ResponseEntity.ok(items);
    }

    /**
     * Get items by category (GET /api/items/category/{category})
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ItemResponse>> getItemsByCategory(@PathVariable String category) {
        List<ItemResponse> items = itemService.getItemsByCategory(category);
        return ResponseEntity.ok(items);
    }

    /**
     * Search items (GET /api/items/search?keyword=value)
     */
    @GetMapping("/search")
    public ResponseEntity<List<ItemResponse>> searchItems(@RequestParam String keyword) {
        List<ItemResponse> items = itemService.searchItems(keyword);
        return ResponseEntity.ok(items);
    }

    /**
     * Get recent items (GET /api/items/recent)
     */
    @GetMapping("/recent")
    public ResponseEntity<List<ItemResponse>> getRecentItems() {
        List<ItemResponse> items = itemService.getRecentItems();
        return ResponseEntity.ok(items);
    }

    /**
     * Get user's items (GET /api/items/my)
     */
    @GetMapping("/my")
    public ResponseEntity<?> getUserItems(Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            List<ItemResponse> items = itemService.getUserItems(userEmail);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error fetching user items: " + e.getMessage());
        }
    }

    /**
     * Get item by ID (GET /api/items/{id})
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getItemById(@PathVariable Long id) {
        try {
            ItemResponse item = itemService.getItemById(id);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Item not found: " + e.getMessage());
        }
    }

    /**
     * Update item (PUT /api/items/{id})
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id,
                                      @RequestBody ItemRequest request,
                                      Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            ItemResponse item = itemService.updateItem(id, request, userEmail);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating item: " + e.getMessage());
        }
    }

    /**
     * Delete item (DELETE /api/items/{id})
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id, Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            itemService.deleteItem(id, userEmail);
            return ResponseEntity.ok("Item deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error deleting item: " + e.getMessage());
        }
    }

    /**
     * Search items with filters (GET /api/items/filter)
     * Supports: ?status=LOST&keyword=phone or ?category=Electronics&status=FOUND
     */
    @GetMapping("/filter")
    public ResponseEntity<List<ItemResponse>> filterItems(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category) {
        List<ItemResponse> items = itemService.searchWithFilters(keyword, status, category);
        return ResponseEntity.ok(items);
    }
}
