package com.project.deviceapi.service;

import com.project.deviceapi.model.Device;
import com.project.deviceapi.model.DeviceState;

import java.util.List;

public interface DeviceService {

    Device create(Device device);

    Device get(Long id);

    List<Device> getAll();

    List<Device> getByBrand(String brand);

    List<Device> getByState(DeviceState state);

    Device update(Long id, Device updated);
    Device updateState(Long id, DeviceState newState);

    void delete(Long id);
}