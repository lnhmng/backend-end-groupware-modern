package com.groupware.service.mes.impl;

import com.groupware.dto.request.AirLeakRequest;
import com.groupware.entity.mes.AirLeak;
import com.groupware.exception.CommonException;
import com.groupware.repository.AirLeakRepository;
import com.groupware.service.mes.AirLeakService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AirLeakServiceImpl implements AirLeakService {
    final AirLeakRepository airLeakRepository;
    @Override
    public AirLeak createAirLeak(AirLeakRequest airLeakRequest) {
        try {

            var airLeak = AirLeak.builder()
                    .modelName(airLeakRequest.getModelName())
                    .productNumber(airLeakRequest.getProductNumber())
                    .dateProduct(airLeakRequest.getDateProduct())
                    .timeProduct(airLeakRequest.getTimeProduct())
                    .instrument(airLeakRequest.getInstrument())
                    .firmware(airLeakRequest.getFirmware())
                    .jig(airLeakRequest.getJig())
                    .programVersion(airLeakRequest.getProgramVersion())
                    .intFile(airLeakRequest.getIntFile())
                    .testCode(airLeakRequest.getTestCode())
                    .logVersion(airLeakRequest.getLogVersion())
                    .hwInputAirPress(airLeakRequest.getHwInputAirPress())
                    .hwSupplyAirPress(airLeakRequest.getHwSupplyAirPress())
                    .hwLeakRate(airLeakRequest.getHwLeakRate())
                    .hwsLeakRate(airLeakRequest.getHwsLeakRate())
                    .hwSupplyTime(airLeakRequest.getHwSupplyTime())
                    .hwlLeakTime(airLeakRequest.getHwlLeakTime())
                    .hwsLeakTime(airLeakRequest.getHwsLeakTime())
                    .testTime(airLeakRequest.getTestTime())
                    .judgement(airLeakRequest.getJudgement())
                    .errorCode(airLeakRequest.getErrorCode())
                    .build();

            return airLeakRepository.save(airLeak);
        }catch (Exception e){

            log.error("### Error createAirLeak " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }
}
