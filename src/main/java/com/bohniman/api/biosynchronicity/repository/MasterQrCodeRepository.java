package com.bohniman.api.biosynchronicity.repository;

import java.util.Optional;

import com.bohniman.api.biosynchronicity.model.MasterQrCode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterQrCodeRepository extends JpaRepository<MasterQrCode, Long> {

    Optional<MasterQrCode> findByQrCode(String qrCode);

}
