package com.groupware.service.mes;

import com.groupware.dto.request.AirLeakRequest;
import com.groupware.entity.mes.AirLeak;

public interface AirLeakService {
    AirLeak createAirLeak(AirLeakRequest airLeakRequest);
}
