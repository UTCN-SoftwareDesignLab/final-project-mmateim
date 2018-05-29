package demo.service;

import demo.dto.LocationDto;
import demo.entity.Location;

import java.util.List;

public interface LocationService {
    List<Location> getAll();
    Location findById(Integer id);
    Location create(LocationDto locationDto);
    Location update(LocationDto locationDto, Integer id);
    void delete(Integer id);
}
