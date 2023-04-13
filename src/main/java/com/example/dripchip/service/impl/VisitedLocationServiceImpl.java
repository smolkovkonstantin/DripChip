package com.example.dripchip.service.impl;

import com.example.dripchip.entites.VisitedLocation;
import com.example.dripchip.repositories.VisitedLocationsDAO;
import com.example.dripchip.service.VisitedLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitedLocationServiceImpl implements VisitedLocationService {

    private final VisitedLocationsDAO visitedLocationsDAO;

    @Override
    public VisitedLocation save(VisitedLocation visitedLocation) {
        var savedVisitedLocation = visitedLocationsDAO.save(visitedLocation);
        visitedLocationsDAO.flush();
        return savedVisitedLocation;
    }
}
