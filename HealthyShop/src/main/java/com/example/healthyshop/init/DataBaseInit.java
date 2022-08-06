package com.example.healthyshop.init;

import com.example.healthyshop.service.CategoryService;
import com.example.healthyshop.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataBaseInit implements CommandLineRunner {
    private RoleService roleService;
    private CategoryService categoryService;

    public DataBaseInit(RoleService roleService, CategoryService categoryService) {
        this.roleService = roleService;
        this.categoryService = categoryService;
    }

    @Override
    public void run(String... args) throws Exception {
        this.roleService.initRoles();
        this.categoryService.initCategories();
    }
}
