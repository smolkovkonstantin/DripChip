package com.example.dripchip.service.impl;

import com.example.dripchip.dto.LocationDTO.Response;
import com.example.dripchip.dto.LocationDTO.Request;
import com.example.dripchip.entites.LocationPoint;
import com.example.dripchip.exception.ConflictException;
import com.example.dripchip.exception.NotFoundException;
import com.example.dripchip.repositorie.LocationDAO;
import com.example.dripchip.service.LocationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class LocationServiceImpl implements LocationService {

    private final LocationDAO locationDAO;

    @Override
    public Response.Location addLocation(@Valid Request.Location locationDTO)
            throws ConflictException {

        isExistsByLatitudeAndLongitude(locationDTO);

        LocationPoint location = LocationPoint
                .builder()
                .latitude(locationDTO.getLatitude())
                .longitude(locationDTO.getLongitude())
                .build();

        locationDAO.save(location);

        return parseToDTO(location);
    }

    @Override
    public LocationPoint findLocationById(@Min(1) @NotNull Long pointId) throws NotFoundException {
        return locationDAO.findById(pointId).orElseThrow(() -> new NotFoundException("Location point with point id " + pointId + " not found"));
    }

    @Override
    public Response.Location updateById(@Min(1) @NotNull Long pointId, @Valid Request.Location locationDTO) throws ConflictException, NotFoundException {

        isExistsByLatitudeAndLongitude(locationDTO);

        LocationPoint locationPoint = findLocationById(pointId);

        locationPoint.setLatitude(locationDTO.getLatitude());
        locationPoint.setLongitude(locationDTO.getLongitude());

        locationDAO.save(locationPoint);

        return parseToDTO(locationPoint);
    }


    @Override
    public void deleteById(@Min(1) @NotNull Long pointId) throws NotFoundException {

        findLocationById(pointId);

        locationDAO.deleteById(pointId);
    }

    @Override
    public Response.Location parseToDTO(LocationPoint location) {
        return Response.Location
                .builder()
                .id(location.getId())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }

    private void isExistsByLatitudeAndLongitude(Request.Location locationDTO) throws ConflictException {
        if (locationDAO.findByLatitudeAndLongitude(locationDTO.getLatitude(), locationDTO.getLongitude()).isPresent()) {
            throw new ConflictException(String.format("Location with latitude %s and longitude %s already exists",
                    locationDTO.getLatitude(), locationDTO.getLongitude()));
        }
    }
}
