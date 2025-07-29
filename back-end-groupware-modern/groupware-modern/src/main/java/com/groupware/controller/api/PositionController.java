package com.groupware.controller.api;

import com.groupware.common.CommonResponse;
import com.groupware.service.PositionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/admin/position")
public class PositionController {

    final PositionService positionService;
    @PostMapping
    public CommonResponse<?> createPosition(@RequestParam(defaultValue = "", required = false) String positionName){
        return CommonResponse.success(positionService.createPosition(positionName));
    }

    @GetMapping
    public CommonResponse<?> lstPosition(){
        return CommonResponse.success(positionService.lstPosition());
    }

    @PatchMapping("/{id}")
    public CommonResponse<?> detailPosition(@PathVariable int id) {
        return CommonResponse.success(positionService.detailPosition(id));
    }

    @PutMapping()
    public CommonResponse<?> editPosition(@RequestParam(defaultValue = "", required = false) String positionName
            , @RequestParam(defaultValue = "0", required = false) Integer id){
        return CommonResponse.success(positionService.editPosition(positionName, id));
    }

    @DeleteMapping()
    public CommonResponse<?> deletePosition(@RequestParam(defaultValue = "", required = false) int id){
        return CommonResponse.success(positionService.deletePosition(id));
    }
}
