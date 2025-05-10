package com.project.deviceapi.controller;

import com.project.deviceapi.model.Device;
import com.project.deviceapi.model.DeviceState;
import com.project.deviceapi.service.DeviceServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
@Tag(name = "Device API", description = "CRUD operations for managing devices")
public class DeviceController {

    private final DeviceServiceImpl service;

    public DeviceController(DeviceServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create a new device")
    public ResponseEntity<Device> create(@RequestBody Device device) {
        return new ResponseEntity<>(service.create(device), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a device by ID")
    public ResponseEntity<Device> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    @Operation(summary = "Get all devices")
    public ResponseEntity<List<Device>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/brand/{brand}")
    @Operation(summary = "Get devices by brand")
    public ResponseEntity<List<Device>> getByBrand(@PathVariable("brand") String brand) {
        return ResponseEntity.ok(service.getByBrand(brand));
    }

    @GetMapping("/state/{state}")
    @Operation(summary = "Get devices by state")
    public ResponseEntity<List<Device>> getByState(@PathVariable("state") DeviceState state) {
        return ResponseEntity.ok(service.getByState(state));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a device")
    public ResponseEntity<Device> update(@PathVariable("id") Long id, @RequestBody Device updated) {
        return ResponseEntity.ok(service.update(id, updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a device")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/state")
    @Operation(summary = "Update the state of a device by ID")
    public ResponseEntity<Device> updateState(
            @PathVariable("id") Long id,
            @RequestParam("state") String stateStr) {

        DeviceState newState;
        try {
            newState = DeviceState.valueOf(stateStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid state. Allowed values: AVAILABLE, IN_USE, INACTIVE");
        }

        Device updatedDevice = service.updateState(id, newState);
        return ResponseEntity.ok(updatedDevice);
    }
}