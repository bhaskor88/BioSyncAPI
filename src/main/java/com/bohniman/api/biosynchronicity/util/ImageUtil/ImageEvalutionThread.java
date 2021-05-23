package com.bohniman.api.biosynchronicity.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

import com.bohniman.api.biosynchronicity.model.TransTestResult;
import com.bohniman.api.biosynchronicity.payload.response.JsonResponse;
import com.bohniman.api.biosynchronicity.repository.TransTestResultRepository;
import com.bohniman.api.biosynchronicity.util.AppSettings;

public class ImageEvalutionThread implements Runnable {

    private TransTestResultRepository testResultRepository;
    private BufferedImage imageFile;
    private Long testResultId;

    public ImageEvalutionThread(BufferedImage imageFile, Long testResultId,
            TransTestResultRepository testResultRepository) {
        this.imageFile = imageFile;
        this.testResultId = testResultId;
        this.testResultRepository = testResultRepository;
    }

    @Override
    public void run() {
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
                // Testing the thread if its working
                // try {
                //     Thread.sleep(15000);
                // } catch (InterruptedException e) {

                // }
            } catch (IOException e) {
                System.err.println("IOException : " + e.getMessage());
                testResult.setStatus(AppSettings.TEST_STATUS_ERROR);
            } catch (Exception e) {
                System.err.println("Exception : " + e.getMessage());
                testResult.setStatus(AppSettings.TEST_STATUS_ERROR);
            }
            // Save the test Result
            System.out.println("Saving to Db....");
            testResultRepository.save(testResult);
        } else {
            System.out.println("Trans Test Result Not Found");
        }
    }
}
