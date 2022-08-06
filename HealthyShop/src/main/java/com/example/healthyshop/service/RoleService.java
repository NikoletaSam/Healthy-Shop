package com.example.healthyshop.service;

import com.example.healthyshop.model.Role;
import com.example.healthyshop.model.enums.RoleName;
import com.example.healthyshop.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void initRoles(){
        if (roleRepository.count() == 0){
            List<Role> roles = Arrays.stream(RoleName.values())
                    .map(Role::new)
                    .toList();

            roleRepository.saveAll(roles);
        }
    }
}
