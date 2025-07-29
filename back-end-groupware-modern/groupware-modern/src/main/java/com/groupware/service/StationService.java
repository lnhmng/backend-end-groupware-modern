package com.groupware.service;

import com.groupware.entity.station.Station;

import java.util.List;

public interface StationService {
    List<Station> lstStation();

    Station addStation(String stationName);

    Station detailStation(int id);
}
