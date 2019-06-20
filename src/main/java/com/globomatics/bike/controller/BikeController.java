package com.globomatics.bike.controller;

import com.globomatics.bike.model.Bike;
import com.globomatics.bike.repository.BikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bikes")
public class BikeController {

    @Autowired
    private BikeRepository bikeRepository;

    @GetMapping
    public List<Bike> list(HttpServletRequest httpRequest) {
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println(httpRequest.toString());
        System.out.println(httpRequest.getHeader("role"));
        return bikeRepository.findAll();
       /* if (requestEntity.getHeaders().get("role").equals("admin")) {
            System.out.println("admin here!");
            return bikeRepository.findAll();
        }
        else {
            System.out.println("not admin ((((");
            return bikeRepository.findAll();
        }*/
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestBody Bike bike) {
        bikeRepository.save(bike);
    }

    @GetMapping("/{id}")
    public Bike get(@PathVariable("id") long id) {
        return bikeRepository.getOne(id);
    }


}
