package com.quangtrung.HireAI.service;

import com.quangtrung.HireAI.entity.MyJob;
import com.quangtrung.HireAI.entity.User;

import java.util.List;
import java.util.Optional;

public interface MyJobService {
    MyJob save(MyJob myJob);
    Optional<MyJob> findById(Long id);
    List<MyJob> findAll();
    void deleteById(Long id);
    long countByUser(User user);
    List<MyJob> findByUser(User user);
    Optional<MyJob> findByUserAndJobId(User user, Long jobId);
}
