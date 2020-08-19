package com.funtree.defocus.security.controller;

import com.funtree.defocus.security.entity.Account;
import com.funtree.defocus.security.entity.SignUpEmail;
import com.funtree.defocus.security.enums.SendEmailResultCode;
import com.funtree.defocus.security.repository.AccountRepository;
import com.funtree.defocus.security.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;

    public SendEmailResultCode sendEmailVerificationCode(String email) {
        return null;
    }
    @PostMapping("/sign-up/email")
    public Account signUpWithEmail(@RequestBody SignUpEmail signUpAccount) {
        return accountService.signUpWithEmail(signUpAccount, "verificationCode");
    }
}
