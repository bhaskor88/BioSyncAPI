package com.bohniman.api.biosynchronicity.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import com.bohniman.api.biosynchronicity.model.MasterQrCode;
import com.bohniman.api.biosynchronicity.model.TransFamilyMember;
import com.bohniman.api.biosynchronicity.model.TransTestResult;
import com.bohniman.api.biosynchronicity.payload.response.JsonResponse;
import com.bohniman.api.biosynchronicity.repository.MasterQrCodeRepository;
import com.bohniman.api.biosynchronicity.repository.TransFamilyMemberRepository;
import com.bohniman.api.biosynchronicity.repository.TransTestResultRepository;
import com.bohniman.api.biosynchronicity.util.AESEncryption;
import com.bohniman.api.biosynchronicity.util.AppSettings;
import com.bohniman.api.biosynchronicity.util.ImageUtil.ImageEvaluation;
import com.bohniman.api.biosynchronicity.util.ImageUtil.ImageEvalutionThread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TestService {

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    TransFamilyMemberRepository familyMemberRepository;

    @Autowired
    MasterQrCodeRepository masterQrCodeRepository;

    @Autowired
    TransTestResultRepository testResultRepository;

    public JsonResponse createTest(Long userId, MultipartFile file, String qr_code, Long familyMemberId, String testLat,
            String testLng) {
        JsonResponse res = new JsonResponse();
        try {
            TransFamilyMember mem = familyMemberRepository.findByIdAndMasterUser_userId(familyMemberId, userId);

            if (mem != null) {

                String qrCode = AESEncryption.decrypt(decodeURLEncodedString(qr_code));

                Optional<MasterQrCode> qrCodeOpt = masterQrCodeRepository.findByQrCode(qrCode);

                if (qrCodeOpt.isPresent()) {
                    MasterQrCode masterQrCode = qrCodeOpt.get();
                    if (!masterQrCode.isUsed()) {
                        // Save Test Image
                        String targetLocation = fileStorageService.storeFile(file, masterQrCode.getQrCode());

                        // create new test case
                        TransTestResult testResult = new TransTestResult();
                        testResult.setCapturedImagePath(targetLocation);
                        testResult.setFamilyMember(mem);
                        testResult.setQrCode(masterQrCode.getQrCode());
                        testResult.setStatus(AppSettings.TEST_STATUS_PROCESSING);
                        testResult.setLat(testLat);
                        testResult.setLng(testLng);
                        testResult = testResultRepository.save(testResult);
                        if (testResult != null) {
                            masterQrCode.setUsed(true);
                            masterQrCode = masterQrCodeRepository.save(masterQrCode);
                            InputStream is = new ByteArrayInputStream(file.getBytes());
                            BufferedImage image = ImageIO.read(is);

                            // Implementing Threads
                            // Thread imageEvaluationThread = new Thread(new ImageEvalutionThread(image, testResult.getId(), testResultRepository));
                            // imageEvaluationThread.start();

                            // Not Implementing Threads
                            ImageEvaluation imageEvaluation = new ImageEvaluation(image, testResult.getId(), testResultRepository);
                            testResult = imageEvaluation.updateResult();

                            testResult.setFamilyMember(null);
                            testResult.setCapturedImagePath(null);
                            res.setPayload(testResult);
                            res.setResult(true);
                            res.setMessage("New Test Result Saved Successfully!");
                        } else {
                            res.setResult(false);
                            res.setMessage("Failed Saving Test Results!");
                        }
                    } else {
                        res.setResult(false);
                        res.setMessage("Qr Code Already Used !");
                    }
                } else {
                    res.setResult(false);
                    res.setMessage("Invalid Qr Code Received !");
                }

            } else {
                res.setResult(false);
                res.setMessage("Family Member not found for the logged in user !");
            }
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
            res.setResult(false);
            res.setMessage("Some exception has ocurred ! " + e.getMessage());
        }
        return res;
    }

    public String decodeURLEncodedString(String URL) {

        String urlString = "";
        try {
            urlString = URLDecoder.decode(URL, "UTF-8");
        } catch (UnsupportedEncodingException e) {

        }
        return urlString;

    }

    public JsonResponse getTestResults(Long userId, Long familyMemberId) {
        JsonResponse res = new JsonResponse();
        try {
            TransFamilyMember mem = familyMemberRepository.findByIdAndMasterUser_userId(familyMemberId, userId);
            if (mem != null) {
                List<TransTestResult> testList = testResultRepository.findAllByFamilyMember_id(mem.getId());
                if (testList != null && testList.size() > 0) {
                    testList.forEach((result) -> {
                        result.setCapturedImagePath(null);
                    });
                    res.setResult(true);
                    res.setPayload(testList);
                    res.setMessage("Test Result List for the Provided Family Member fetched successfully !");
                } else {
                    res.setResult(true);
                    res.setMessage("No Test Result Present for the Provided Family Member !");
                }
            } else {
                res.setResult(false);
                res.setMessage("Family Member not found for the logged in user !");
            }
        } catch (Exception e) {
            res.setMessage("Some Error has ocurred");
            res.setResult(false);
        }
        return res;
    }

}
