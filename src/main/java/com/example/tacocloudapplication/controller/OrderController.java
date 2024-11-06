package com.example.tacocloudapplication.controller;

import com.example.tacocloudapplication.props.OrderProps;
import com.example.tacocloudapplication.repo.impl.OrderRepository;
import com.example.tacocloudapplication.repo.impl.TacoRepository;
import com.example.tacocloudapplication.repo.impl.UserRepository;
import com.example.tacocloudapplication.service.TacoService;
import com.example.tacocloudapplication.table.Drink;
import com.example.tacocloudapplication.table.Taco;
import com.example.tacocloudapplication.table.TacoOrder;
import com.example.tacocloudapplication.table.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
//@CrossOrigin(origins = {"https://localhost:4200", "http://3.79.153.161"})
public class OrderController {

    private final OrderRepository orderRepository;

    private final TacoService tacoService;

    private final TacoRepository tacoRepository;

    private final UserRepository userRepository;

    private final OrderProps orderProps;

    public OrderController(OrderRepository orderRepository, TacoService tacoService,
                           UserRepository userRepository, OrderProps orderProps, TacoRepository tacoRepository) {
        this.orderRepository = orderRepository;
        this.tacoService = tacoService;
        this.userRepository = userRepository;
        this.orderProps = orderProps;
        this.tacoRepository = tacoRepository;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<TacoOrder> getOrder(@PathVariable Long orderId) {
        Optional<TacoOrder> order = orderRepository.findById(orderId);

        if(order.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(order.get());
    }

    @GetMapping("/past")
    public ResponseEntity<List<TacoOrder>> userPastOrders(Principal principal) {
        User user = userRepository.findByUsername(principal.getName());

        if(user == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Pageable pageable = PageRequest.of(0, orderProps.getPageSize());
        List<TacoOrder> orders = orderRepository.findTacoOrdersByUserIdOrderByPlacedAtDate(user.getId(), pageable);

        if(orders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(orders);
    }

    @PostMapping("/new")
    @Transactional
    public ResponseEntity<TacoOrder> processOrder(@RequestBody TacoOrder order, Principal principal) {
            User user = userRepository.findByUsername(principal.getName());
        float orderTotalPrice = 0;

        if(user == null) {
            return ResponseEntity.badRequest().body(null);
        }

        order.setUser(user);
        user.getTacoOrders().add(order);

        log.info("Order submitted: {}", order);

        for(Taco taco : order.getTacos()) {
            orderTotalPrice += taco.getTotalTacoPrice();
        }

        for(Drink drink : order.getDrinks()) {
            orderTotalPrice += drink.getPrice();
        }

        order.setTotalOrderPrice(orderTotalPrice);

        TacoOrder savedOrder = orderRepository.save(order);

        for(Taco taco : order.getTacos()) {
            tacoRepository.associateOrderToTaco(order.getId(), taco.getId());
        }

        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping("/new")
    public ResponseEntity<List<Taco>> showAvailableTacosForNewOrder(Principal principal) {
        User currentUser = userRepository.findByUsername(principal.getName());

        if(currentUser == null) {
            return ResponseEntity.badRequest().body(null);
        }

        List<Taco> availableTacos = tacoRepository.findByTacoOrderIsNullAndCreatedByUserId(currentUser.getId());

        if(availableTacos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(availableTacos);
    }

    @DeleteMapping("/delete/taco/{tacoId}")
    @Transactional
    public ResponseEntity<Integer> deleteSelectedTacoFromDatabase(@PathVariable Long tacoId) {
        tacoService.deleteSelectedTacoFromDatabase(tacoId);

        return ResponseEntity.ok(tacoRepository.deleteTacoById(tacoId));
    }
}
