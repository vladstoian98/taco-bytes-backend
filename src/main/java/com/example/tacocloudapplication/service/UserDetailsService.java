package com.example.tacocloudapplication.service;

import com.example.tacocloudapplication.repo.impl.DrinkRepository;
import com.example.tacocloudapplication.repo.impl.OrderRepository;
import com.example.tacocloudapplication.repo.impl.TacoRepository;
import com.example.tacocloudapplication.repo.impl.UserRepository;
import com.example.tacocloudapplication.table.Taco;
import com.example.tacocloudapplication.table.TacoOrder;
import com.example.tacocloudapplication.table.User;
import com.example.tacocloudapplication.table.util.JwtUtil;
import org.hibernate.criterion.Order;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    private final OrderRepository orderRepository;

    private final TacoRepository tacoRepository;

    private final DrinkRepository drinkRepository;

    private final JwtUtil jwtUtil;

    public UserDetailsService(UserRepository userRepository, OrderRepository orderRepository, TacoRepository tacoRepository, DrinkRepository drinkRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.tacoRepository = tacoRepository;
        this.drinkRepository = drinkRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getAuthority()));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    public Integer deleteUserByUsername(String username) {
        Integer userToBeDeletedId = userRepository.getUserIdByUsername(username);

        List<TacoOrder> ordersMadeByToBeDeletedUser = orderRepository.selectTacoOrdersByUserId(userToBeDeletedId);

        for(TacoOrder tacoOrder : ordersMadeByToBeDeletedUser) {
            List<Taco> tacosMadeByToBeDeletedUser = tacoRepository.selectTacosByOrderId(tacoOrder.getId());

            for(Taco taco : tacosMadeByToBeDeletedUser) {
                tacoRepository.deleteEntriesFromIngredientTaco(taco.getId());
            }

            tacoRepository.deleteEntriesFromTacoByOrderId(tacoOrder.getId());
            drinkRepository.deleteFromTacoOrderDrinksByOrderId(tacoOrder.getId());
        }

        orderRepository.deleteFromTacoOrderByUserId(userToBeDeletedId);

        return userRepository.deleteUserByUsername(username);
    }
}
