package com.example.healthyshop.service;

import com.example.healthyshop.model.Role;
import com.example.healthyshop.model.User;
import com.example.healthyshop.model.enums.RoleName;
import com.example.healthyshop.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HealthyShopUserDetailsServiceTest {
    private HealthyShopUserDetailsService toTest;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp(){
        toTest = new HealthyShopUserDetailsService(mockUserRepository);
    }

    @Test
    void testLoadUserByUsername_UserExists(){
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("1234567");
        testUser.setAge(20);
        testUser.setFullName("User Testov");
        testUser.setEmail("user@testov.com");
        testUser.setRoles(Set.of(
                new Role(RoleName.USER),
                new Role(RoleName.ADMIN)));

        when(mockUserRepository.findUserByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        UserDetails userDetails = toTest.loadUserByUsername(testUser.getUsername());

        Assertions.assertEquals(testUser.getUsername(), userDetails.getUsername());
        Assertions.assertEquals(testUser.getPassword(), userDetails.getPassword());

        Assertions.assertEquals(2, userDetails.getAuthorities().size());

        Iterator<? extends GrantedAuthority> iterator = userDetails.getAuthorities().iterator();
        Assertions.assertEquals("ROLE_" + RoleName.ADMIN, iterator.next().getAuthority());
        Assertions.assertEquals("ROLE_" + RoleName.USER, iterator.next().getAuthority());
    }

    @Test
    void testLoadUserByUsername_UserDoesNotExist(){
        Assertions.assertThrows(UsernameNotFoundException.class, () -> toTest.loadUserByUsername("nonExistent"));
    }
}
