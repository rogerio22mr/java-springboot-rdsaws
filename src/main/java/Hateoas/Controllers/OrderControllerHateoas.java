package Hateoas.Controllers;

import Hateoas.Entities.OrderHateoas;
import Hateoas.Entities.Status;
import Hateoas.Exceptions.OrderNotFoundException;
import Hateoas.Repositories.OrderRepositoryHateoas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OrderControllerHateoas {

    @Autowired
    private OrderRepositoryHateoas orderRepository;

    @GetMapping("orders")
    ResponseEntity<List<OrderHateoas>> getAllOrders() {
        Long idOrder;
        Link linkUri;
        List<OrderHateoas> orderList = orderRepository.findAll();
        if (orderList.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        for (OrderHateoas order: orderList) {
            idOrder = order.getId();
            linkUri = linkTo(methodOn(OrderControllerHateoas.class).getOrderById(idOrder)).withSelfRel();
            order.add(linkUri);
        }
        return new ResponseEntity<>(orderList, HttpStatus.OK);


    }

    @GetMapping("orders/{id}")
    ResponseEntity<OrderHateoas> getOrderById(@PathVariable Long id) {
        Optional<OrderHateoas> orderPointer = orderRepository.findById(id);
        if (orderPointer.isPresent()) {
            OrderHateoas order = orderPointer.get();
            order.add(linkTo(methodOn(OrderControllerHateoas.class).getAllOrders()).withRel("All Orders"));
            return new ResponseEntity<>(order, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("orders")
    public OrderHateoas createOrder(@RequestBody OrderHateoas order) {
        return orderRepository.save(order);
    }

    @PutMapping("orderd/{id}")
    public OrderHateoas updateOrder(@RequestBody OrderHateoas updateOrder , @PathVariable Long id) {
        return orderRepository.findById(id).map(order -> {
            order.setStatus(updateOrder.getStatus());
            order.setDescription(updateOrder.getDescription());
            return orderRepository.save(order);
        }).orElseGet(() -> {
            updateOrder.setId(id);
            return orderRepository.save(updateOrder);
        });
    }

    @DeleteMapping("order/{id}")
    void deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
    }

    @PutMapping("oders/{id}/cancel")
    ResponseEntity<?> cancelOrderById(@PathVariable Long id) {
        OrderHateoas cancelledOrder = orderRepository.findById(id).orElseThrow(
                () -> new OrderNotFoundException(id));
        if (cancelledOrder.getStatus() == Status.IN_PROGRES) {
            cancelledOrder.setStatus(Status.CANCELLED);
            cancelledOrder.add(linkTo(methodOn(OrderControllerHateoas.class).getOrderById(id)).withSelfRel());
            cancelledOrder.add(linkTo(methodOn(OrderControllerHateoas.class).getAllOrders()).withRel("Complete order List"));
            orderRepository.save(cancelledOrder);
            return new ResponseEntity<>(cancelledOrder, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).header(
                HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body("You can't complete the tas, the order has a " + cancelledOrder.getStatus() + " status");
    }

    @PutMapping("oders/{id}/complete")
    ResponseEntity<?> completeOrderById(@PathVariable Long id) {
        OrderHateoas completeOrder = orderRepository.findById(id).orElseThrow(
                () -> new OrderNotFoundException(id));
        if (completeOrder.getStatus() == Status.IN_PROGRES) {
            completeOrder.setStatus(Status.COMPLETED);
            completeOrder.add(linkTo(methodOn(OrderControllerHateoas.class).getOrderById(id)).withSelfRel());
            completeOrder.add(linkTo(methodOn(OrderControllerHateoas.class).getAllOrders()).withRel("Complete order List"));
            orderRepository.save(completeOrder);
            return new ResponseEntity<>(completeOrder, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).header(
                        HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body("You can't complete the tas, the order has a " + completeOrder.getStatus() + " status");
    }


}
