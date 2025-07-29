package com.groupware.controller.api;

import com.groupware.common.CommonResponse;
import com.groupware.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/station")
public class StationController {
    @Autowired
    private StationService stationService;

    @GetMapping
    public CommonResponse<?> lstStation(){
        return CommonResponse.success(stationService.lstStation());
    }

    @PostMapping
    public CommonResponse<?> addStation(@RequestParam(defaultValue = "", required = false) String stationName){
        return CommonResponse.success(stationService.addStation(stationName));
    }

    @PatchMapping("/{id}")
    public CommonResponse<?> detailStation(@PathVariable int id){
        return CommonResponse.success(stationService.detailStation(id));
    }
}
