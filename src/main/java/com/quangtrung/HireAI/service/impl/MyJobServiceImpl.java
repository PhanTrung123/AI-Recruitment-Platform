package com.quangtrung.HireAI.service.impl;

import com.quangtrung.HireAI.entity.MyJob;
import com.quangtrung.HireAI.entity.User;
import com.quangtrung.HireAI.repository.MyJobRepository;
import com.quangtrung.HireAI.service.MyJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MyJobServiceImpl implements MyJobService {

    @Autowired
    private MyJobRepository myJobRepository;

    @Override
    public MyJob save(MyJob myJob) {
        return myJobRepository.save(myJob);
    }

    @Override
    public Optional<MyJob> findById(Long id) {
        return myJobRepository.findById(id);
    }

    @Override
    public List<MyJob> findAll() {
        return myJobRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        myJobRepository.deleteById(id);
    }

    @Override
    public long countByUser(User user) {
        return myJobRepository.countByCvUser(user);
    }

    @Override
    public List<MyJob> findByUser(User user) {
        return myJobRepository.findByCvUser(user);
    }

    @Override
    public Optional<MyJob> findByUserAndJobId(User user, Long jobId) {
        return myJobRepository.findByUserAndJobId(user, jobId);
    }
}
