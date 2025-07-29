package com.groupware.controller.api;

import com.groupware.common.CommonResponse;
import com.groupware.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/line")
public class LineController {
    @Autowired
    private LineService lineService;

    @GetMapping
    public CommonResponse<?> getLineInfo() {
        return CommonResponse.success(lineService.lstLine());
    }

    @PostMapping
    public CommonResponse<?> addLineInfo(@RequestParam(defaultValue = "", required = false) String lineName) {
        return CommonResponse.success(lineService.addLine(lineName));
    }

    @PatchMapping("/{id}")
    public CommonResponse<?> detailLineInfo(@PathVariable int id) {
        return CommonResponse.success(lineService.detailLine(id));
    }

    @PutMapping
    public CommonResponse<?> editLineInfo(@RequestParam(defaultValue = "", required = false) String lineName
            , @RequestParam(defaultValue = "0", required = false) Integer id) {
        return CommonResponse.success(lineService.editLine(lineName, id));
    }

    @DeleteMapping
    public CommonResponse<?> deleteLineInfo(@RequestParam(defaultValue = "", required = false) int id){
        return CommonResponse.success(lineService.deleteLineInfo(id));
    }
}
