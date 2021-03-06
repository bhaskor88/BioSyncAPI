package com.bohniman.api.biosynchronicity.repository;

import com.bohniman.api.biosynchronicity.model.MasterUser;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterUserRepository extends JpaRepository<MasterUser, Long> {

    Optional<MasterUser> findByUsername(String username);
    Optional<MasterUser> findByUsernameAndPassword(String username, String password);

    Optional<MasterUser> findByEmail(String email);

}
