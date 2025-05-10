package com.project.deviceapi.service;



import com.project.deviceapi.model.Device;
import com.project.deviceapi.model.DeviceState;
import com.project.deviceapi.repository.DeviceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeviceServiceImplTest {

    private DeviceRepository repository;
    private DeviceServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(DeviceRepository.class);
        service = new DeviceServiceImpl(repository);
    }

    @Test
    void testCreateDevice() {
        Device device = new Device();
        device.setName("Device A");
        device.setBrand("BrandX");
        device.setState(DeviceState.AVAILABLE);

        when(repository.save(device)).thenReturn(device);

        Device result = service.create(device);

        assertEquals("Device A", result.getName());
        verify(repository).save(device);
    }

    @Test
    void testGetDeviceExists() {
        Device device = new Device();
        device.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(device));

        Device result = service.get(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void testGetDeviceNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.get(1L));
    }

    @Test
    void testUpdateDeviceAllowed() {
        Device existing = new Device();
        existing.setId(1L);
        existing.setName("Device A");
        existing.setBrand("BrandX");
        existing.setState(DeviceState.AVAILABLE);

        Device updated = new Device();
        updated.setName("Device A");
        updated.setBrand("BrandX");
        updated.setState(DeviceState.IN_USE);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Device.class))).thenReturn(updated);

        Device result = service.update(1L, updated);

        assertEquals(DeviceState.IN_USE, result.getState());
        verify(repository).save(existing);
    }

    @Test
    void testUpdateDeviceRestricted() {
        Device existing = new Device();
        existing.setId(1L);
        existing.setName("Device A");
        existing.setBrand("BrandX");
        existing.setState(DeviceState.IN_USE);

        Device updated = new Device();
        updated.setName("New Name");
        updated.setBrand("New Brand");
        updated.setState(DeviceState.IN_USE);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        assertThrows(IllegalStateException.class, () -> service.update(1L, updated));
    }

    @Test
    void testUpdateState() {
        Device existing = new Device();
        existing.setId(1L);
        existing.setState(DeviceState.AVAILABLE);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Device.class))).thenReturn(existing);

        Device result = service.updateState(1L, DeviceState.IN_USE);

        assertEquals(DeviceState.IN_USE, result.getState());
        verify(repository).save(existing);
    }

    @Test
    void testDeleteAllowed() {
        Device device = new Device();
        device.setId(1L);
        device.setState(DeviceState.AVAILABLE);

        when(repository.findById(1L)).thenReturn(Optional.of(device));

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void testDeleteBlocked() {
        Device device = new Device();
        device.setId(1L);
        device.setState(DeviceState.IN_USE);

        when(repository.findById(1L)).thenReturn(Optional.of(device));

        assertThrows(IllegalStateException.class, () -> service.delete(1L));
    }
}
