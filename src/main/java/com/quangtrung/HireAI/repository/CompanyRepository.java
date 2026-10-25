package com.quangtrung.HireAI.repository;

import com.quangtrung.HireAI.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Page<Company> findByNameContaining(String name, Pageable pageable);

    Company findByUserId(Long userId);

    @Query("SELECT c FROM Company c WHERE c.user.id = :userId AND c.user.role = 'RECRUITER'")
    Optional<Company> findByUserIdAndRole(@Param("userId") Long userId);

    @Query("SELECT c FROM Company c LEFT JOIN FETCH c.jobs WHERE c.status = 'APPROVED' ORDER BY SIZE(c.jobs) DESC")
    List<Company> findTopHiringCompanies();

}
