package barsnik96.chat.service;

import barsnik96.chat.repository.UserRepository;
import barsnik96.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Service
@Validated
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<User> save(@Valid User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @PreAuthorize("#user.id == principal.id")
    public Mono<User> updatePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Mono<User> updateRoles(User user) {
        return userRepository.save(user);
    }

    @PreAuthorize("#user.id == principal.id")
    public Mono<User> updateStatus(User user) {
        return userRepository.save(user);
    }

    public Mono<User> findById(String id) {
        return userRepository.findById(id);
    }

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public Mono<Void> deleteById(String id) {
        return userRepository.deleteById(id);
    }
}
