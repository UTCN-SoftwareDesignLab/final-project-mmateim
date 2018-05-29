package demo.service;

import demo.dto.LocationDto;
import demo.entity.Location;
import demo.entity.LocationType;
import demo.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    LocationRepository locationRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }


    @Override
    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    @Override
    public Location create(LocationDto locationDto) {
        Location location = new Location(locationDto.getName(), locationDto.getAddress(), LocationType.valueOf(locationDto.getType()));
        return locationRepository.save(location);
    }

    @Override
    public Location update(LocationDto locationDto, Integer id) {
        if (id == null)
            return null;
        Location location = locationRepository.findById(id);
        location.setAddress(locationDto.getAddress());
        location.setName(locationDto.getName());
        location.setType(LocationType.valueOf(locationDto.getType()));
        return locationRepository.save(location);
    }

    @Override
    public void delete(Integer id) {
        locationRepository.delete(id);
    }

    @Override
    public Location findById(Integer id) {
        return locationRepository.findById(id);
    }
}
