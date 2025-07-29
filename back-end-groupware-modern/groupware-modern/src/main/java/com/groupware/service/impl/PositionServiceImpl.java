package com.groupware.service.impl;

import com.groupware.entity.position.Position;
import com.groupware.exception.CommonException;
import com.groupware.repository.PositionRepository;
import com.groupware.service.PositionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class PositionServiceImpl implements PositionService {
    final PositionRepository positionRepository;

    @Override
    public List<Position> lstPosition() {
        try {
            return positionRepository.positionList();
        } catch (Exception e) {
            log.error("### Error get positionList: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public Position createPosition(String positionName) {
        try {
            if (positionName.length() < 3) {
                throw new RuntimeException("Position name not null and min is 3 char");
            }
            if (positionRepository.existsByPositionName(positionName)) {
                throw new RuntimeException("Position name has been duplicated");
            }
            Position position = new Position();
            position.setPositionName(positionName);
            position.setUseStatus(true);
            return positionRepository.save(position);
        } catch (Exception e) {
            log.error("### Error createPosition: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public Position detailPosition(int id) {
        try {
            return positionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Not found id position"));
        } catch (Exception e) {
            log.error("### Error detailPosition:  " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public Position editPosition(String positionName, Integer id) {
        try {
            Position position = positionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Not found line id"));
            if (positionRepository.exitsPositionNameEdit(id, positionName) != 0){
                throw CommonException.of("Position Name has been duplicated");
            }
            position.setPositionName(positionName);
            return positionRepository.save(position);
        } catch (Exception e) {
            log.error("### Error editPosition: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public Position deletePosition(int id) {
        try {
            Position position = positionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("not found position id"));
            position.setUseStatus(false);
            return positionRepository.save(position);
        } catch (Exception e){
            log.error("### Error deletePosition: "+ e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }
}
