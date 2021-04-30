package com.bohniman.api.biosynchronicity.repository;

import java.util.Set;

import com.bohniman.api.biosynchronicity.model.MasterRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterRoleRespository extends JpaRepository<MasterRole,Long>{

    Set<MasterRole> findAllByRoleName(String string);
    
}
