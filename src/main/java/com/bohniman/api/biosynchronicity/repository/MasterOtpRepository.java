package com.bohniman.api.biosynchronicity.repository;

import java.util.Optional;

import com.bohniman.api.biosynchronicity.model.MasterOtp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterOtpRepository extends JpaRepository<MasterOtp, Long> {

    Optional<MasterOtp> findByEmail(String email);

}
