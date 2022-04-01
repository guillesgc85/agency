package com.agency.ads.app.controller;

import com.agency.ads.common.route.Route;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping(value = {(Route.HEALTH)}, produces = {(MediaType.APPLICATION_JSON_VALUE)})
public class HealthController {

    /**
     * To check app status
     * @return
     */
    @ApiOperation(
            tags = "ayuda",
            value = "UP"
    )
    @GetMapping
    @ResponseBody
    public ResponseEntity<HashMap<String, String>> getHealthCheck() {
        HashMap<String, String> hashMap = new HashMap();
        hashMap.put("status", "UP");
        ResponseEntity<HashMap<String, String>> response = new ResponseEntity(hashMap, HttpStatus.OK);
        return response;
    }
}
