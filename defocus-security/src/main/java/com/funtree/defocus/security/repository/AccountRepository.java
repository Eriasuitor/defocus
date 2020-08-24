package com.funtree.defocus.security.repository;

import com.funtree.defocus.security.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByUsername(String username);

    Account findByUsernameOrEmail(String username, String email);

    @Modifying
    @Transactional
    void removeAccountByUsername(String username);
}
