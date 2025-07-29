package com.groupware.repository;

import com.groupware.entity.position.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Integer> {

    Boolean existsByPositionName(String positionName);
    @Query(value = "SELECT * \n" +
            "\tFROM _position\n" +
            "\tWHERE use_status = 1", nativeQuery = true)
    List<Position> positionList();

    @Query(value = "SELECT count(*)\n" +
            "\tFROM _position p\n" +
            "\tWHERE p.id != :id\n" +
            "\tAND p.position_name = :positionName", nativeQuery = true)
    int exitsPositionNameEdit(@Param("id") int id, @Param("positionName") String positionName);
}
