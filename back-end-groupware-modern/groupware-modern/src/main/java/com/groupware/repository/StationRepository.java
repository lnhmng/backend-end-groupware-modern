package com.groupware.repository;

import com.groupware.entity.station.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, Integer> {

    Boolean existsByStationName(String stationName);
    @Query(value = "SELECT *\n" +
            "\tFROM _station\n" +
            "\tWHERE use_status = 1", nativeQuery = true)
    List<Station> lstStation();
}
