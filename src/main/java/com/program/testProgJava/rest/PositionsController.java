package com.program.testProgJava.rest;

import com.program.testProgJava.dao.entities.PositionsEntity;
import com.program.testProgJava.dao.repositories.PositionsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/store/api/positions")
public class PositionsController {
    private final PositionsRepository positionsRepository;

    public PositionsController(PositionsRepository positionsRepository) {
        this.positionsRepository = positionsRepository;
    }

    @GetMapping
    public ResponseEntity<List<PositionsEntity>> getAllPositions() {
        return ResponseEntity.ok(positionsRepository.findAll());
    }
}
