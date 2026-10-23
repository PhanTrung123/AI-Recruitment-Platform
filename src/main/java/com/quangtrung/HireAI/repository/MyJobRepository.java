package com.quangtrung.HireAI.repository;

import com.quangtrung.HireAI.entity.MyJob;
import com.quangtrung.HireAI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyJobRepository extends JpaRepository<MyJob, Long> {
    long countByCvUser(User user);

    @Query("SELECT DISTINCT mj FROM MyJob mj LEFT JOIN FETCH mj.job j LEFT JOIN FETCH j.company LEFT JOIN FETCH mj.cv WHERE mj.cv.user = :user")
    List<MyJob> findByCvUser(@Param("user") User user);

    @Query("SELECT mj FROM MyJob mj WHERE mj.cv.user = :user AND mj.job.id = :jobId")
    Optional<MyJob> findByUserAndJobId(@Param("user") User user, @Param("jobId") Long jobId);

    @Modifying
    @Transactional
    @Query("DELETE FROM MyJob mj WHERE mj.job.job_id = :jobId")
    void deleteByJobId(@Param("jobId") Long jobId);
}
