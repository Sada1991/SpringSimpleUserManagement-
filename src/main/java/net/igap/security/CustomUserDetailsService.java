package net.igap.security;

import java.util.Set;
import java.util.stream.Collectors;
import net.igap.model.entity.Account;
import net.igap.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Autowired
    public CustomUserDetailsService(AccountRepository accountRepository3) {
        this.accountRepository = accountRepository3;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
          Account account = accountRepository.findAccountByEnableIsTrueAndUserName(username)
                 .orElseThrow(() ->
                         new UsernameNotFoundException("User not found with username: "+ username));

        Set<GrantedAuthority> authorities = account
                .getRoles()
                .stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());


        return new org.springframework.security.core.userdetails.User(account.getUserName(),
                account.getPassword(),
                authorities);
    }
}
