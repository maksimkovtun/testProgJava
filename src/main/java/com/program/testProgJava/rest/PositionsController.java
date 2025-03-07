package com.program.testProgJava.rest;

import com.program.testProgJava.dao.entities.PositionsEntity;
import com.program.testProgJava.dao.repositories.PositionsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/add")
    public ResponseEntity<PositionsEntity> addPosition(@RequestBody PositionsEntity position) {
        PositionsEntity savedPosition = positionsRepository.save(position);
        return ResponseEntity.ok(savedPosition);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PositionsEntity> updatePosition(@PathVariable Long id, @RequestBody PositionsEntity updatedPosition) {
        return positionsRepository.findById(id)
                .map(existingPosition -> {
                    existingPosition.setName(updatedPosition.getName());
                    positionsRepository.save(existingPosition);
                    return ResponseEntity.ok(existingPosition);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable Long id) {
        if (positionsRepository.existsById(id)) {
            positionsRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
