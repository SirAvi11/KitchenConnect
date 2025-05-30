package com.kitchenconnect.kitchen.service.impl;

import com.kitchenconnect.kitchen.DTO.OrderRequest;
import com.kitchenconnect.kitchen.entity.ItemRating;
import com.kitchenconnect.kitchen.entity.Kitchen;
import com.kitchenconnect.kitchen.entity.MenuItem;
import com.kitchenconnect.kitchen.entity.Order;
import com.kitchenconnect.kitchen.entity.OrderDetails;
import com.kitchenconnect.kitchen.entity.Payment;
import com.kitchenconnect.kitchen.entity.Rating;
import com.kitchenconnect.kitchen.entity.User;
import com.kitchenconnect.kitchen.enums.OrderStatus;
import com.kitchenconnect.kitchen.enums.PaymentStatus;
import com.kitchenconnect.kitchen.repository.ItemRatingRepository;
import com.kitchenconnect.kitchen.repository.KitchenRepository;
import com.kitchenconnect.kitchen.repository.MenuItemRepository;
import com.kitchenconnect.kitchen.repository.OrderDetailsRepository;
import com.kitchenconnect.kitchen.repository.OrderRepository;
import com.kitchenconnect.kitchen.repository.PaymentRepository;
import com.kitchenconnect.kitchen.repository.RatingRepository;
import com.kitchenconnect.kitchen.repository.UserRepository;
import com.kitchenconnect.kitchen.service.OrderService;
import com.kitchenconnect.kitchen.service.RatingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KitchenRepository kitchenRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private  ItemRatingRepository itemRatingRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private PaymentRepository paymentRepository;

    // Create a new order
    @Transactional
    public Order placeOrder(OrderRequest orderRequest) {
        // Fetch the user and kitchen
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Kitchen kitchen = kitchenRepository.findById(orderRequest.getKitchenId())
                .orElseThrow(() -> new RuntimeException("Kitchen not found"));

        // Create the order
        Order order = new Order();
        order.setUser(user);
        order.setKitchen(kitchen);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(orderRequest.getTotalAmount());
        order.setDeliveryAddress(orderRequest.getDeliveryAddress());
        order.setContactNumber(orderRequest.getContactNumber());

        // Save the order
        Order savedOrder = orderRepository.save(order);

        // Create and save order details
        List<OrderDetails> orderDetails = orderRequest.getItems().stream()
                .map(item -> {
                    MenuItem foodItem = menuItemRepository.findById(item.getFoodItemId())
                            .orElseThrow(() -> new RuntimeException("Food item not found"));

                    OrderDetails detail = new OrderDetails();
                    detail.setOrder(savedOrder);
                    detail.setMenuItem(foodItem);
                    detail.setQuantity(item.getQuantity());
                    detail.setPrice(foodItem.getPrice());
                    return detail;
                })
                .collect(Collectors.toList());

        orderDetailsRepository.saveAll(orderDetails);

        //Save payment infomation
        double platformFee = orderRequest.getTotalAmount() * 0.05;
        double chefAmount = orderRequest.getTotalAmount() - platformFee;

        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setAmount(orderRequest.getTotalAmount());
        payment.setPlatformFee(platformFee);
        payment.setChefAmount(chefAmount);
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setPaymentMethod("UPI");
        payment.setUpiTransactionId(orderRequest.getUpiTransactionId());
        payment.setPaymentDate(LocalDateTime.now());
        
        paymentRepository.save(payment);

        // // Create and save default Rating
        Rating rating = new Rating();
        rating.setOrderId(savedOrder.getId()); // Use the savedOrder's ID
        rating.setKitchenRating(0); // Default kitchen rating
        rating.setUserNote("Default rating"); // Default user note
        Rating savedRating = ratingRepository.save(rating);

        //Create and save default ItemRatings for each menu item in the order
        List<ItemRating> itemRatings = orderDetails.stream()
                .map(detail -> {
                    ItemRating itemRating = new ItemRating();
                    itemRating.setItemName(detail.getMenuItem().getName()); // Use the menu item's name
                    itemRating.setRatingValue(0); // Default item rating value
                    itemRating.setParentRating(savedRating); // Link to the saved Rating
                    return itemRating;
                })
                .collect(Collectors.toList());

        itemRatingRepository.saveAll(itemRatings);

        return savedOrder;
    }

    // Get all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getAllOrdersByKitchen(Kitchen kitchen){
        return orderRepository.findByKitchen(kitchen);
    }

    // Get order by ID
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }


    // Update order status
    @Transactional
    public Order updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    // Delete an order
    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        orderRepository.delete(order);
    }

    @Override
    @Transactional
    public Order saveOrder(Order order) {
        // Save or update the order in the database
        return orderRepository.save(order);
    }

    public List<OrderDetails> getOrderDetailsById(Long id){
        return orderDetailsRepository.findByOrderId(id);
    }
    
}