package com.example.healthyshop.service;

import com.example.healthyshop.exceptions.UserNotFoundException;
import com.example.healthyshop.model.Role;
import com.example.healthyshop.model.User;
import com.example.healthyshop.model.dtos.UserProfileDto;
import com.example.healthyshop.model.dtos.UserRegistrationDto;
import com.example.healthyshop.model.enums.RoleName;
import com.example.healthyshop.repository.RoleRepository;
import com.example.healthyshop.repository.UserRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private HealthyShopUserDetailsService userDetailsService;
    private ModelMapper mapper;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                       HealthyShopUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.mapper = new ModelMapper();
    }

    public boolean isUsernameOccupied(String username){
        Optional<User> userByUsername = this.userRepository.findUserByUsername(username);

        return userByUsername.isPresent();
    }

    public boolean isEmailOccupied(String email){
        Optional<User> userByEmail = this.userRepository.findUserByEmail(email);
        return userByEmail.isPresent();
    }

    public boolean passwordsMatch(String password, String confirmPassword){
        return password.equals(confirmPassword);
    }

    public User registerAndLogin(UserRegistrationDto registrationDto){
        Optional<Role> initialRole = roleRepository.findByName(RoleName.USER);

        TypeMap<UserRegistrationDto, User> typeMap = mapper.getTypeMap(UserRegistrationDto.class, User.class);
        if (typeMap == null){
            TypeMap<UserRegistrationDto, User> propertyMapper = mapper.createTypeMap(UserRegistrationDto.class, User.class);
            Converter<String, String> categoryToString = mappingContext -> passwordEncoder.encode(mappingContext.getSource());
            propertyMapper.addMappings(m -> m.using(categoryToString)
                    .map(UserRegistrationDto::getPassword, User::setPassword));
        }

        User user = mapper.map(registrationDto, User.class);

        user.addRole(initialRole.get());
        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        Authentication auth =
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder
                .getContext()
                .setAuthentication(auth);

        return user;
    }

    public List<UserProfileDto> findAllUsers(){
        TypeMap<User, UserProfileDto> typeMap = mapper.getTypeMap(User.class, UserProfileDto.class);
        if (typeMap == null){
            TypeMap<User, UserProfileDto> propertyMapper = mapper.createTypeMap(User.class, UserProfileDto.class);

            Converter<Set<Role>, Set<RoleName>> categoryToString = mappingContext -> mappingContext
                    .getSource()
                    .stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());
            propertyMapper.addMappings(m -> m.using(categoryToString)
                    .map(User::getRoles, UserProfileDto::setRoles));
        }

        List<User> users = userRepository.findAll();
        List<UserProfileDto> profileDtos = new ArrayList<>();

        for (User currentUser : users){
            UserProfileDto dto = mapper.map(currentUser, UserProfileDto.class);
            profileDtos.add(dto);
        }

        return profileDtos;
    }

    public void makeUserAdmin(long id){
        Optional<User> user = userRepository.findById(id);
        Optional<Role> role = roleRepository.findByName(RoleName.ADMIN);

        if (user.isPresent()){
            user.get().addRole(role.get());
            userRepository.save(user.get());
        } else {
            throw new UserNotFoundException("User not found.");
        }
    }
}
