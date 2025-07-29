package com.groupware.service.impl;

import com.groupware.entity.line.Line;
import com.groupware.exception.CommonException;
import com.groupware.repository.LineRepository;
import com.groupware.service.LineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LineImpl implements LineService {
    @Autowired
    private LineRepository lineRepository;

    @Override
    public List<Line> lstLine() {
        try {
            return lineRepository.lstLine();
        } catch (Exception e) {
            log.error("### Error lstLine() +" + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public Line addLine(String lineName) {
        try {
            if (lineName.isEmpty()) {
                throw CommonException.of("lineName is required");
            }
            Line line = new Line();
            line.setLineName(lineName);
            line.setUseStatus(true);
            return lineRepository.save(line);
        } catch (Exception e) {
            log.error("### Error addLine(): " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public Line detailLine(int id) {
        try {
            return lineRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Not found id line"));
        } catch (Exception e) {
            log.error("### Error: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public Line editLine(String lineName, Integer id) {
        try {
            Line line = lineRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Not found line id"));
            if (lineRepository.exitsLineNameEdit(lineName, id) != 0) {
                throw CommonException.of("Line name has been duplicated");
            }
            line.setLineName(lineName);
            return lineRepository.save(line);
        } catch (Exception e) {
            log.error("### Error: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public Line deleteLineInfo(int id) {
        try {
            Line line = lineRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("not found line id"));
            line.setUseStatus(false);
            return lineRepository.save(line);
        } catch (Exception e) {
            log.error("### Error: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }
}
