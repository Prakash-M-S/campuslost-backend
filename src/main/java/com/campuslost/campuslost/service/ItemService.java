package com.campuslost.campuslost.service;

import com.campuslost.campuslost.dto.ItemRequest;
import com.campuslost.campuslost.dto.response.ItemResponse;
import com.campuslost.campuslost.entity.Item;
import com.campuslost.campuslost.entity.User;
import com.campuslost.campuslost.repository.ItemRepository;
import com.campuslost.campuslost.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    // Create new item
    public ItemResponse createItem(ItemRequest request, String userEmail) {
        Optional<User> userOpt = userRepository.findByEmail(userEmail);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        Item item = new Item(
            request.getTitle(),
            request.getDescription(),
            request.getCategory(),
            request.getStatus(),
            request.getLocation(),
            request.getContactInfo(),
            user
        );

        Item savedItem = itemRepository.save(item);
        return new ItemResponse(savedItem);
    }

    // Get all items
    public List<ItemResponse> getAllItems() {
        return itemRepository.findAllByOrderByDatePostedDesc()
                .stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }

    // Get items by status
    public List<ItemResponse> getItemsByStatus(String status) {
        return itemRepository.findByStatus(status.toUpperCase())
                .stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }

    // Get items by category
    public List<ItemResponse> getItemsByCategory(String category) {
        return itemRepository.findByCategory(category)
                .stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }

    // Search items by keyword
    public List<ItemResponse> searchItems(String keyword) {
        return itemRepository.findByTitleOrDescriptionContainingIgnoreCase(keyword)
                .stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }

    // Get recent items (last 30 days)
    public List<ItemResponse> getRecentItems() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return itemRepository.findRecentItems(thirtyDaysAgo)
                .stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }

    // Get items by user
    public List<ItemResponse> getUserItems(String userEmail) {
        Optional<User> userOpt = userRepository.findByEmail(userEmail);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        return itemRepository.findByUserId(userOpt.get().getId())
                .stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }

    // Get item by ID
    public ItemResponse getItemById(Long id) {
        Optional<Item> itemOpt = itemRepository.findById(id);
        if (itemOpt.isEmpty()) {
            throw new RuntimeException("Item not found");
        }
        return new ItemResponse(itemOpt.get());
    }

    // Update item
    public ItemResponse updateItem(Long id, ItemRequest request, String userEmail) {
        Optional<Item> itemOpt = itemRepository.findById(id);
        if (itemOpt.isEmpty()) {
            throw new RuntimeException("Item not found");
        }

        Item item = itemOpt.get();

        // Check if user owns this item
        if (!item.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You can only update your own items");
        }

        // Update item fields
        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        item.setCategory(request.getCategory());
        item.setStatus(request.getStatus());
        item.setLocation(request.getLocation());
        item.setContactInfo(request.getContactInfo());

        Item updatedItem = itemRepository.save(item);
        return new ItemResponse(updatedItem);
    }

    // Delete item
    public void deleteItem(Long id, String userEmail) {
        Optional<Item> itemOpt = itemRepository.findById(id);
        if (itemOpt.isEmpty()) {
            throw new RuntimeException("Item not found");
        }

        Item item = itemOpt.get();

        // Check if user owns this item
        if (!item.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You can only delete your own items");
        }

        itemRepository.delete(item);
    }

    // Advanced search with filters
    public List<ItemResponse> searchWithFilters(String keyword, String status, String category) {
        List<Item> items;

        if (keyword != null && !keyword.trim().isEmpty() && status != null && !status.trim().isEmpty()) {
            items = itemRepository.findByStatusAndKeyword(status.toUpperCase(), keyword);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            items = itemRepository.findByTitleOrDescriptionContainingIgnoreCase(keyword);
        } else if (status != null && !status.trim().isEmpty() && category != null && !category.trim().isEmpty()) {
            items = itemRepository.findByStatusAndCategory(status.toUpperCase(), category);
        } else if (status != null && !status.trim().isEmpty()) {
            items = itemRepository.findByStatus(status.toUpperCase());
        } else if (category != null && !category.trim().isEmpty()) {
            items = itemRepository.findByCategory(category);
        } else {
            items = itemRepository.findAllByOrderByDatePostedDesc();
        }

        return items.stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }
}
