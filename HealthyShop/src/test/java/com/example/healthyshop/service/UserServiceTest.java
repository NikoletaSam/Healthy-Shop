package com.example.healthyshop.service;

import com.example.healthyshop.exceptions.UserNotFoundException;
import com.example.healthyshop.model.Role;
import com.example.healthyshop.model.User;
import com.example.healthyshop.model.dtos.UserProfileDto;
import com.example.healthyshop.model.dtos.UserRegistrationDto;
import com.example.healthyshop.model.enums.RoleName;
import com.example.healthyshop.repository.RoleRepository;
import com.example.healthyshop.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private UserService toTest;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private RoleRepository mockRoleRepository;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Mock
    private HealthyShopUserDetailsService mockUserDetailsService;

    @BeforeEach
    void setUp() {
        toTest = new UserService(mockUserRepository, mockRoleRepository, mockPasswordEncoder, mockUserDetailsService);
    }

    @Test
    void testIsUsernameOccupied() {
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

        Assertions.assertTrue(toTest.isUsernameOccupied(testUser.getUsername()));
    }

    @Test
    void testIsEmailOccupied() {
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("1234567");
        testUser.setAge(20);
        testUser.setFullName("User Testov");
        testUser.setEmail("user@testov.com");
        testUser.setRoles(Set.of(
                new Role(RoleName.USER),
                new Role(RoleName.ADMIN)));

        when(mockUserRepository.findUserByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        Assertions.assertTrue(toTest.isEmailOccupied(testUser.getEmail()));
    }

    @Test
    void testRegisterAndLoginForValidData() {
        Role testUserRole = new Role(RoleName.USER);
        when(mockRoleRepository.findByName(RoleName.USER)).thenReturn(Optional.of(testUserRole));

        List<Role> roles = List.of(testUserRole);

        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setAge(20);
        userRegistrationDto.setUsername("userTest");
        userRegistrationDto.setEmail("user@test.com");
        userRegistrationDto.setPassword("1234567");
        userRegistrationDto.setConfirmPassword("1234567");
        userRegistrationDto.setFullName("User Testov");

        when(mockUserDetailsService.loadUserByUsername(userRegistrationDto.getUsername())).thenReturn(new org.springframework.security.core.userdetails.User(
                userRegistrationDto.getUsername(),
                userRegistrationDto.getPassword(),
                roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName().name()))
                        .collect(Collectors.toList())));

        User user = toTest.registerAndLogin(userRegistrationDto);
        Assertions.assertEquals("userTest", user.getUsername());
        Assertions.assertEquals("user@test.com", user.getEmail());
        Assertions.assertEquals("User Testov", user.getFullName());
    }

    @Test
    void testFindAllUsers() {
        Role userRole = new Role(RoleName.USER);
        Role adminRole = new Role(RoleName.ADMIN);

        User testUser = new User("firstUser", "password", "user@testov.com", "Full Name", 30);
        testUser.addRole(userRole);

        User testAdmin = new User("adminUser", "password", "admin@testov.com", "Full Admin", 21);
        testAdmin.addRole(userRole);
        testAdmin.addRole(adminRole);

        List<User> all = new ArrayList<>();
        all.add(testUser);
        all.add(testAdmin);

        when(mockUserRepository.findAll()).thenReturn(all);

        List<UserProfileDto> allUsers = toTest.findAllUsers();

        Assertions.assertEquals(2, allUsers.size());
        Assertions.assertEquals("firstUser", allUsers.get(0).getUsername());
        Assertions.assertEquals("adminUser", allUsers.get(1).getUsername());
    }

    @Test
    void testMakeUserAdminO() {
        Role userRole = new Role(RoleName.USER);
        Role adminRole = new Role(RoleName.ADMIN);

        User testUser = new User("firstUser", "password", "user@testov.com", "Full Name", 30);
        testUser.addRole(userRole);

        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(mockRoleRepository.findByName(RoleName.ADMIN)).thenReturn(Optional.of(adminRole));

        toTest.makeUserAdmin(1L);

        Assertions.assertEquals(2, testUser.getRoles().size());
        Assertions.assertTrue(testUser.getRoles().contains(userRole));
        Assertions.assertTrue(testUser.getRoles().contains(adminRole));
    }

    @Test
    void testMakeUserAdminThrowsExceptionForNonExistentUser() {
        Role adminRole = new Role(RoleName.ADMIN);
        when(mockRoleRepository.findByName(RoleName.ADMIN)).thenReturn(Optional.of(adminRole));

        Assertions.assertThrows(UserNotFoundException.class, () -> toTest.makeUserAdmin(14L));
    }
}
