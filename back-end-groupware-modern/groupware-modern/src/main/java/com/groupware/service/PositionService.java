package com.groupware.service;

import com.groupware.entity.position.Position;

import java.util.List;

public interface PositionService {

    List<Position> lstPosition();

    Position createPosition(String positionName);

    Position detailPosition(int id);

    Position editPosition(String positionName, Integer id);

    Position deletePosition(int id);
}
