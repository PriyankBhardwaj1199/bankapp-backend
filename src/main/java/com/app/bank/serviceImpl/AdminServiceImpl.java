package com.app.bank.serviceImpl;

import com.app.bank.entity.User;
import com.app.bank.repository.UserRepository;
import com.app.bank.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<List<User>> fetchAllUserAccounts() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
