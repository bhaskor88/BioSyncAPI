package com.bohniman.api.biosynchronicity.repository;

import com.bohniman.api.biosynchronicity.model.MasterQrCode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterQrCodeRepository extends JpaRepository<MasterQrCode, Long> {

}
