package com.groupware.service.impl;

import com.groupware.entity.station.Station;
import com.groupware.exception.CommonException;
import com.groupware.repository.StationRepository;
import com.groupware.service.StationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StationServiceImpl implements StationService {
    @Autowired
    private StationRepository stationRepository;
    @Override
    public List<Station> lstStation() {
        try {
            return stationRepository.lstStation();
        } catch (Exception e){
            log.error("### Error lstStation: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public Station addStation(String stationName) {
        try {
            if (stationName.length() < 3){
                throw new RuntimeException("Station name not null and min is 3 char");
            }
            if (stationRepository.existsByStationName(stationName)){
                throw new RuntimeException("Station name has been duplicated");
            }
            Station station = new Station();
            station.setStationName(stationName);
            station.setUseStatus(true);
            return stationRepository.save(station);
        } catch (Exception e){
            log.error("### Error addStation: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public Station detailStation(int id) {
        try{
            Station station = stationRepository.findById(id)
                    .orElseThrow(()-> new RuntimeException("Not found station ID"));
            return station;
        } catch (Exception e){
            log.error("### Error detailStation: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }
}
