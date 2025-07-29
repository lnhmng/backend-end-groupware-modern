package com.groupware.service;

import com.groupware.entity.line.Line;

import java.util.List;

public interface LineService {
    List<Line> lstLine();

    Line addLine(String line);

    Line detailLine(int id);

    Line editLine(String lineName, Integer id);

    Line deleteLineInfo(int id);
}
