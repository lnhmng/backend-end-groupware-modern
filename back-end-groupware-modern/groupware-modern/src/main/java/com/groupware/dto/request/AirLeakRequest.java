package com.groupware.dto.request;

import lombok.Data;

import java.time.LocalTime;
import java.util.Date;

@Data
public class AirLeakRequest {
    private String modelName;
    private String productNumber;
    private Date dateProduct;
    private LocalTime timeProduct;
    private String instrument;
    private String firmware;
    private int jig;
    private String programVersion;
    private String intFile;
    private String testCode;
    private String logVersion;
    private float hwInputAirPress;
    private String hwSupplyAirPress;
    private String hwLeakRate;
    private String hwsLeakRate;
    private String hwSupplyTime;
    private String hwlLeakTime;
    private String hwsLeakTime;
    private float testTime;
    private Boolean judgement;
    private String errorCode;
}
