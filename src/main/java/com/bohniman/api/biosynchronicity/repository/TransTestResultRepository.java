package com.bohniman.api.biosynchronicity.repository;

import java.util.List;

import com.bohniman.api.biosynchronicity.model.TransTestResult;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransTestResultRepository extends JpaRepository<TransTestResult, Long> {

    List<TransTestResult> findAllByFamilyMember_id(Long id);

}
