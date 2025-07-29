package com.groupware.repository;

import com.groupware.entity.line.Line;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineRepository extends JpaRepository<Line, Integer> {

    @Query(value = "SELECT *\n" +
            "\tFROM _line\n" +
            "\tWHERE use_status = 1", nativeQuery = true)
    List<Line> lstLine();

    @Query(value = "SELECT count(*)\n" +
            "\tFROM _line l\n" +
            "\tWHERE l.line_name = :lineName\n" +
            "\tAND l.id != :id", nativeQuery = true)
    int exitsLineNameEdit(@Param("lineName") String lineName, @Param("id") int id);
}
