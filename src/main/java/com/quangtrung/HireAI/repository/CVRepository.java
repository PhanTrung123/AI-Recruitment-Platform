package com.quangtrung.HireAI.repository;

import com.quangtrung.HireAI.entity.CV;
import com.quangtrung.HireAI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CVRepository extends JpaRepository<CV, Long> {
    List<CV> findByUser(User user);

    @Query("SELECT c FROM CV c WHERE c.user = :user ORDER BY c.createdAt DESC")
    List<CV> findByUserOrderByCreatedAtDesc(@Param("user") User user);

    default Optional<CV> findFirstByUserOrderByCreatedAtDesc(User user) {
        List<CV> list = findByUserOrderByCreatedAtDesc(user);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }
}
