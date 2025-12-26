package com.campuslost.campuslost.repository;

import com.campuslost.campuslost.entity.Item;
import com.campuslost.campuslost.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOrderByDatePostedDesc();

    List<Item> findByStatusOrderByDatePostedDesc(String status);

    List<Item> findByCategoryOrderByDatePostedDesc(String category);

    List<Item> findByUserOrderByDatePostedDesc(User user);

    List<Item> findByStatusAndCategoryOrderByDatePostedDesc(String status, String category);

    List<Item> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrderByDatePostedDesc(String keyword1, String keyword2);

    List<Item> findByDatePostedAfterOrderByDatePostedDesc(LocalDateTime fromDate);

    List<Item> findByTitleContainingIgnoreCaseAndStatusOrderByDatePostedDesc(String keyword, String status);

    List<Item> findByTitleContainingIgnoreCaseAndCategoryOrderByDatePostedDesc(String keyword, String category);

    List<Item> findByTitleContainingIgnoreCaseAndStatusAndCategoryOrderByDatePostedDesc(String keyword, String status, String category);
}
