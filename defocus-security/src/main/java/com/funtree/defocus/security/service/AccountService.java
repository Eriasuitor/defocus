package com.funtree.defocus.security.service;

import com.funtree.defocus.security.entity.Account;
import com.funtree.defocus.security.entity.SignUpEmail;
import com.funtree.defocus.security.repository.AccountRepository;
import org.apache.tomcat.jni.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) throw new UsernameNotFoundException("User not found");
        return account;
    }

    @Transactional
    public Account signUpWithEmail(SignUpEmail signUpAccount, String verificationCode) {
        Account exsited = accountRepository.findByUsernameOrEmail(signUpAccount.getUsername(), signUpAccount.getEmail());
        if(exsited != null) {
            throw new DuplicateKeyException("Username or email has been used by someone else");
        }
        // TODO verify
        Account account = new Account();
        account.setEmail(signUpAccount.getEmail());
        account.setUsername(signUpAccount.getUsername());
        account.setPassword(new BCryptPasswordEncoder().encode(signUpAccount.getPassword()));
        return accountRepository.save(account);
    }
}
