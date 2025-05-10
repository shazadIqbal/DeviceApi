package com.project.deviceapi.service;

import com.project.deviceapi.model.Device;
import com.project.deviceapi.model.DeviceState;
import com.project.deviceapi.repository.DeviceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository repository;

    public DeviceServiceImpl(DeviceRepository repository) {
        this.repository = repository;
    }

    public Device create(Device device) {
        return repository.save(device);
    }

    public Device get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Device not found"));
    }

    public List<Device> getAll() {
        return repository.findAll();
    }

    public List<Device> getByBrand(String brand) {
        return repository.findByBrand(brand);
    }

    public List<Device> getByState(DeviceState state) {
        return repository.findByState(state);
    }

    @Transactional
    public Device update(Long id, Device updated) {
        Device existing = get(id);

        if (existing.getState() == DeviceState.IN_USE) {
            if (!existing.getName().equals(updated.getName()) || !existing.getBrand().equals(updated.getBrand())) {
                throw new IllegalStateException("Cannot update name or brand when device is IN_USE");
            }
        }

        existing.setState(updated.getState());
        existing.setName(updated.getName());
        existing.setBrand(updated.getBrand());
        return repository.save(existing);
    }

    @Override
    @Transactional
    public Device updateState(Long id, DeviceState newState) {
        Device device = get(id);
        device.setState(newState);
        return repository.save(device);
    }

    public void delete(Long id) {
        Device device = get(id);
        if (device.getState() == DeviceState.IN_USE) {
            throw new IllegalStateException("Cannot delete a device that is IN_USE");
        }
        repository.deleteById(id);
    }
}