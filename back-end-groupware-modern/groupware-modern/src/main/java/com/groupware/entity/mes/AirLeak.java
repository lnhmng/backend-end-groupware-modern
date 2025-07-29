package com.groupware.entity.mes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "air_leak")
public class AirLeak {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
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
    private int times;
}
