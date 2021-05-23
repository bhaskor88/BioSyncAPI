package com.bohniman.api.biosynchronicity.controller;

import com.bohniman.api.biosynchronicity.exception.BadRequestException;
import com.bohniman.api.biosynchronicity.payload.response.JsonResponse;
import com.bohniman.api.biosynchronicity.security.SecurityUtil;
import com.bohniman.api.biosynchronicity.service.TestService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    TestService testService;

    @Autowired
    SecurityUtil securityUtil;

    @PostMapping("/createTest")
    public ResponseEntity<?> uploadFile(@RequestParam("testImage") MultipartFile file,
            @RequestParam("qr_code") String qr_code, @RequestParam("family_mem_id") Long familyMemberId,
            @RequestParam("testLat") String testLat, @RequestParam("testLng") String testLng) {

        Long userId = securityUtil.getCurrentLoggedUserId();
        if (userId == null) {
            throw new BadRequestException("Unable to determine the logged in user");
        }

        // Trying to register user
        JsonResponse res = testService.createTest(userId, file, qr_code, familyMemberId, testLat, testLng);
        if (!res.getResult()) {
            throw new BadRequestException(res.getMessage());
        } else {
            return ResponseEntity.ok(res);
        }
    }

    @RequestMapping(value = "/getTestResults/{familyMemberId}", method = RequestMethod.POST)
    public ResponseEntity<JsonResponse> getTestResults(@PathVariable Long familyMemberId) {
        Long userId = securityUtil.getCurrentLoggedUserId();
        if (userId == null) {
            throw new BadRequestException("Unable to determine the logged in user");
        }
        // Trying to register user
        JsonResponse res = testService.getTestResults(userId, familyMemberId);
        if (!res.getResult()) {
            throw new BadRequestException(res.getMessage());
        } else {
            return ResponseEntity.ok(res);
        }
    }
}
