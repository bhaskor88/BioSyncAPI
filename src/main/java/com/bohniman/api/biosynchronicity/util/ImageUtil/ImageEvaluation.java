package com.bohniman.api.biosynchronicity.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

import com.bohniman.api.biosynchronicity.model.TransTestResult;
import com.bohniman.api.biosynchronicity.payload.response.JsonResponse;
import com.bohniman.api.biosynchronicity.repository.TransTestResultRepository;
import com.bohniman.api.biosynchronicity.util.AppSettings;

public class ImageEvaluation {
    private TransTestResultRepository testResultRepository;
    private BufferedImage imageFile;
    private Long testResultId;

    public ImageEvaluation(BufferedImage imageFile, Long testResultId, TransTestResultRepository testResultRepository) {
        this.imageFile = imageFile;
        this.testResultId = testResultId;
        this.testResultRepository = testResultRepository;
    }

    public TransTestResult updateResult() {
        JsonResponse res = new JsonResponse(false);
        Optional<TransTestResult> testResultOpt = testResultRepository.findById(testResultId);
        if (testResultOpt.isPresent()) {
            TransTestResult testResult = testResultOpt.get();
            try {
                res = Evaluate.evaluateImageForLines(imageFile);
                if (res.getResult()) {
                    try {
                        Integer result = Integer.parseInt(res.getPayload().toString());
                        // Update Test Result
                        if (result > 0 && result < 3) {
                            testResult.setStatus(AppSettings.TEST_STATUS_SUCCESS);
                            testResult.setTestResult(
                                    result == 2 ? AppSettings.TEST_RESULT_POSITIVE : AppSettings.TEST_RESULT_NEGATIVE);
                        } else {
                            testResult.setStatus(AppSettings.TEST_STATUS_INVALID);
                        }
                    } catch (NumberFormatException e) {
                        testResult.setStatus(AppSettings.TEST_STATUS_ERROR);
                    }
                } else {
                    // Update Test Result as error
                    testResult.setStatus(AppSettings.TEST_STATUS_ERROR);
                }
            } catch (IOException e) {
                System.err.println("IOException : " + e.getMessage());
                testResult.setStatus(AppSettings.TEST_STATUS_ERROR);
            } catch (Exception e) {
                System.err.println("Exception : " + e.getMessage());
                testResult.setStatus(AppSettings.TEST_STATUS_ERROR);
            }
            // Save the test Result
            System.out.println("Saving to Db....");
            return testResultRepository.save(testResult);
        }
        return null;
    }
}
