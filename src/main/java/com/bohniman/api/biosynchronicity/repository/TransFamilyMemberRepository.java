package com.bohniman.api.biosynchronicity.repository;

import java.util.List;
import java.util.Optional;

import com.bohniman.api.biosynchronicity.model.TransFamilyMember;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransFamilyMemberRepository extends JpaRepository<TransFamilyMember, Long> {
    
    List<TransFamilyMember> findAllByMasterUser_userId(Long userId);

    TransFamilyMember findByIdAndMasterUser_userId(Long familyMemberId, Long userId);

    Optional<TransFamilyMember> findByisPrimaryAndMasterUser_userId(boolean b, Long userId);

}
