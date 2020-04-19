package web.app.services;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import web.app.dao.DbSqlite;

@Service
public class AuthenticationService implements UserDetailsService {
    private final DbSqlite dbSqlite;

    public AuthenticationService(DbSqlite dbSqlite) {
        this.dbSqlite = dbSqlite;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        web.app.dao.model.User user = dbSqlite.selectUserData(username.toLowerCase());

        UserDetails userDetails = User
                .withUsername((user.getNickname().toLowerCase()))
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
        return userDetails;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
