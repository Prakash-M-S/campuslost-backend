package com.campuslost.campuslost.repository;

import com.campuslost.campuslost.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // Find items by status (LOST, FOUND, RESOLVED)
    List<Item> findByStatus(String status);

    // Find items by category
    List<Item> findByCategory(String category);

    // Find items by user
    List<Item> findByUserId(Long userId);

    // Find items by status and category
    List<Item> findByStatusAndCategory(String status, String category);

    // Find first item by status (useful for checking if any items exist)
    Optional<Item> findFirstByStatus(String status);

    // Find first item by user (useful for checking if user has posted any items)
    Optional<Item> findFirstByUserId(Long userId);

    // Find item by title (exact match) - useful for duplicate checking
    Optional<Item> findByTitle(String title);

    // Find item by title and user (to check if user already posted this item)
    Optional<Item> findByTitleAndUserId(String title, Long userId);

    // Search items by title or description containing keyword (case insensitive)
    @Query("SELECT i FROM Item i WHERE LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Item> findByTitleOrDescriptionContainingIgnoreCase(@Param("keyword") String keyword);

    // Find recent items (posted within last N days)
    @Query("SELECT i FROM Item i WHERE i.datePosted >= :fromDate ORDER BY i.datePosted DESC")
    List<Item> findRecentItems(@Param("fromDate") LocalDateTime fromDate);

    // Find most recent item by user (using Spring Data method naming - no LIMIT needed)
    Optional<Item> findFirstByUserIdOrderByDatePostedDesc(Long userId);

    // Find items by status with search keyword
    @Query("SELECT i FROM Item i WHERE i.status = :status AND " +
           "(LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Item> findByStatusAndKeyword(@Param("status") String status, @Param("keyword") String keyword);

    // Find all items ordered by date posted (newest first)
    List<Item> findAllByOrderByDatePostedDesc();
}
