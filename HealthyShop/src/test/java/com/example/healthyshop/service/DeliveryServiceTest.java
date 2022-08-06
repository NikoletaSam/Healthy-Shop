package com.example.healthyshop.service;

import com.example.healthyshop.model.Delivery;
import com.example.healthyshop.model.Role;
import com.example.healthyshop.model.User;
import com.example.healthyshop.model.dtos.DeliveryDetailsDto;
import com.example.healthyshop.model.enums.RoleName;
import com.example.healthyshop.repository.DeliveryRepository;
import com.example.healthyshop.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceTest {
    private DeliveryService toTest;

    @Mock
    private DeliveryRepository mockDeliveryRepository;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        toTest = new DeliveryService(mockDeliveryRepository, mockUserRepository);
    }

    @Test
    void testAddDeliveryDetails() {
        DeliveryDetailsDto detailsDto = new DeliveryDetailsDto();
        detailsDto.setCountry("Test Country");
        detailsDto.setCity("Test City");
        detailsDto.setAddress("Test Address");

        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("1234567");
        testUser.setAge(20);
        testUser.setFullName("User Testov");
        testUser.setEmail("user@testov.com");
        testUser.setRoles(Set.of(
                new Role(RoleName.USER),
                new Role(RoleName.ADMIN)));
        testUser.setDelivery(new Delivery());

        when(mockUserRepository.findUserByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        toTest.addDeliveryDetails(detailsDto, testUser.getUsername());

        Assertions.assertEquals("Test Country", testUser.getDelivery().getCountry());
        Assertions.assertEquals("Test City", testUser.getDelivery().getCity());
        Assertions.assertEquals("Test Address", testUser.getDelivery().getAddress());
    }

    @Test
    void testFindUserDeliveryDetailsReturnsNull() {
        DeliveryDetailsDto unknownUser = toTest.findUserDeliveryDetails("unknownUser");
        Assertions.assertNull(unknownUser);
    }

    @Test
    void testFindUserDeliveryDetailsReturnsValidData() {
        Delivery testDelivery = new Delivery();
        testDelivery.setCountry("Test Country");
        testDelivery.setCity("Test City");
        testDelivery.setAddress("Test Address");

        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("1234567");
        testUser.setAge(20);
        testUser.setFullName("User Testov");
        testUser.setEmail("user@testov.com");
        testUser.setRoles(Set.of(
                new Role(RoleName.USER),
                new Role(RoleName.ADMIN)));

        testUser.setDelivery(testDelivery);
        testDelivery.setUser(testUser);

        when(mockUserRepository.findUserByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        DeliveryDetailsDto userDeliveryDetails = toTest.findUserDeliveryDetails(testUser.getUsername());

        Assertions.assertEquals("Test Country", userDeliveryDetails.getCountry());
        Assertions.assertEquals("Test City", userDeliveryDetails.getCity());
        Assertions.assertEquals("Test Address", userDeliveryDetails.getAddress());
    }

    @Test
    void testHasLoggedPersonDeliveryDetailsReturnsTrue() {
        Delivery testDelivery = new Delivery();
        testDelivery.setCountry("Test Country");
        testDelivery.setCity("Test City");
        testDelivery.setAddress("Test Address");

        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("1234567");
        testUser.setAge(20);
        testUser.setFullName("User Testov");
        testUser.setEmail("user@testov.com");
        testUser.setRoles(Set.of(
                new Role(RoleName.USER),
                new Role(RoleName.ADMIN)));

        testUser.setDelivery(testDelivery);
        testDelivery.setUser(testUser);

        when(mockUserRepository.findUserByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        boolean hasDetails = toTest.hasLoggedPersonDeliveryDetails(testUser.getUsername());

        Assertions.assertTrue(hasDetails);
    }

    @Test
    void testHasLoggedPersonDeliveryDetailsReturnsFalse() {
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("1234567");
        testUser.setAge(20);
        testUser.setFullName("User Testov");
        testUser.setEmail("user@testov.com");
        testUser.setRoles(Set.of(
                new Role(RoleName.USER),
                new Role(RoleName.ADMIN)));

        testUser.setDelivery(new Delivery());

        when(mockUserRepository.findUserByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        boolean hasDetails = toTest.hasLoggedPersonDeliveryDetails(testUser.getUsername());

        Assertions.assertFalse(hasDetails);
    }

    @Test
    void testHasLoggedPersonDeliveryDetailsReturnsFalseForNonExistentUser() {
        boolean hasDetails = toTest.hasLoggedPersonDeliveryDetails("unknownUser");
        Assertions.assertFalse(hasDetails);
    }
}
