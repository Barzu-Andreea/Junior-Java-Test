package com.example.carins.web;

import com.example.carins.model.Car;
import com.example.carins.model.Claim;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.ClaimRepository;
import com.example.carins.web.dto.ClaimDto;
import com.example.carins.web.dto.ClaimRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClaimController {

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private CarRepository carRepository;

    @PostMapping("/cars/{carId}/claims")
    public ResponseEntity<ClaimDto> createClaim(
            @PathVariable Long carId,
            @RequestBody ClaimRequest request) {

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));

        Claim claim = new Claim(car, request.claimDate(), request.description(), request.amount());
        Claim saved = claimRepository.save(claim);

        URI location = URI.create(String.format("/api/cars/%d/claims/%d", carId, saved.getId()));
        return ResponseEntity.created(location).body(toDto(saved));
    }

    @GetMapping("/cars/{carId}/history")
    public ResponseEntity<List<ClaimDto>> getHistory(@PathVariable Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));

        List<ClaimDto> history = claimRepository.findByCarIdOrderByClaimDateAsc(carId)
                .stream()
                .map(this::toDto)
                .toList();

        return ResponseEntity.ok(history);
    }

    private ClaimDto toDto(Claim c) {
        var car = c.getCar();
        return new ClaimDto(c.getId(), c.getCar() != null ? car.getId() : null, c.getClaimDate(),
                c.getDescription(),
                c.getAmount()
        );
    }
}
