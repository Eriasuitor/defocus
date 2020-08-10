package com.funtree.defocus.security.repository;

import com.funtree.defocus.security.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    public Account findByUsername(String username);
}
