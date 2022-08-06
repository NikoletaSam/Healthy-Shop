package com.example.healthyshop.service;

import com.example.healthyshop.model.Delivery;
import com.example.healthyshop.model.User;
import com.example.healthyshop.model.dtos.DeliveryDetailsDto;
import com.example.healthyshop.repository.DeliveryRepository;
import com.example.healthyshop.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeliveryService {
    private DeliveryRepository deliveryRepository;
    private UserRepository userRepository;
    private ModelMapper mapper;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository, UserRepository userRepository) {
        this.deliveryRepository = deliveryRepository;
        this.userRepository = userRepository;
        this.mapper = new ModelMapper();
    }

    public void createDeliveryForUser(User user){
        Delivery delivery = new Delivery();
        delivery.setUser(user);
        deliveryRepository.save(delivery);
        userRepository.save(user);
    }

    public void addDeliveryDetails(DeliveryDetailsDto deliveryDto, String username){
        Optional<User> loggedUser = userRepository.findUserByUsername(username);

        if (loggedUser.isPresent()){
            Delivery delivery = loggedUser.get().getDelivery();

            delivery.setAddress(deliveryDto.getAddress());
            delivery.setCity(deliveryDto.getCity());
            delivery.setCountry(deliveryDto.getCountry());

            deliveryRepository.save(delivery);
        }
    }

    public DeliveryDetailsDto findUserDeliveryDetails(String username){
        Optional<User> loggedUser = userRepository.findUserByUsername(username);

        if (loggedUser.isPresent()){
            Delivery delivery = loggedUser.get().getDelivery();

            return mapper.map(delivery, DeliveryDetailsDto.class);
        }

        return null;
    }

    public boolean hasLoggedPersonDeliveryDetails(String loggedUsername){
        Optional<User> user = userRepository.findUserByUsername(loggedUsername);

        if (user.isPresent()){
            Delivery delivery = user.get().getDelivery();

            return delivery.getAddress() != null;
        }

        return false;
    }
}
