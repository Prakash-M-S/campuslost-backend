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

    public List<ItemResponse> getAllItems() {
        return itemRepository.findAllByOrderByDatePostedDesc()
                .stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }

    public List<ItemResponse> getItemsByStatus(String status) {
        return itemRepository.findByStatusOrderByDatePostedDesc(status)
                .stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }

    public List<ItemResponse> getItemsByCategory(String category) {
        return itemRepository.findByCategoryOrderByDatePostedDesc(category)
                .stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }

    public List<ItemResponse> searchItems(String keyword) {
        return itemRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrderByDatePostedDesc(keyword, keyword)
                .stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }

    public List<ItemResponse> getRecentItems() {
        LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);
        return itemRepository.findByDatePostedAfterOrderByDatePostedDesc(twentyFourHoursAgo)
                .stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }

    public List<ItemResponse> getUserItems(String userEmail) {
        Optional<User> userOpt = userRepository.findByEmail(userEmail);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        return itemRepository.findByUserOrderByDatePostedDesc(userOpt.get())
                .stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }

    public ItemResponse getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        return new ItemResponse(item);
    }

    public ItemResponse updateItem(Long id, ItemRequest request, String userEmail) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You can only update your own items");
        }

        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        item.setCategory(request.getCategory());
        item.setStatus(request.getStatus());
        item.setLocation(request.getLocation());
        item.setContactInfo(request.getContactInfo());

        Item updatedItem = itemRepository.save(item);
        return new ItemResponse(updatedItem);
    }

    public void deleteItem(Long id, String userEmail) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You can only delete your own items");
        }

        itemRepository.delete(item);
    }

    public List<ItemResponse> filterItems(String keyword, String status, String category) {
        List<Item> items;

        if (keyword != null && status != null && category != null) {
            items = itemRepository.findByTitleContainingIgnoreCaseAndStatusAndCategoryOrderByDatePostedDesc(keyword, status, category);
        } else if (keyword != null && status != null) {
            items = itemRepository.findByTitleContainingIgnoreCaseAndStatusOrderByDatePostedDesc(keyword, status);
        } else if (keyword != null && category != null) {
            items = itemRepository.findByTitleContainingIgnoreCaseAndCategoryOrderByDatePostedDesc(keyword, category);
        } else if (status != null && category != null) {
            items = itemRepository.findByStatusAndCategoryOrderByDatePostedDesc(status, category);
        } else if (keyword != null) {
            items = itemRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrderByDatePostedDesc(keyword, keyword);
        } else if (status != null) {
            items = itemRepository.findByStatusOrderByDatePostedDesc(status);
        } else if (category != null) {
            items = itemRepository.findByCategoryOrderByDatePostedDesc(category);
        } else {
            items = itemRepository.findAllByOrderByDatePostedDesc();
        }

        return items.stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }
}
