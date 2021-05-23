package com.bohniman.api.biosynchronicity.util;

public class AppSettings {

    public final static String TEST_STATUS_PROCESSING = "PROCESSING";
    public final static String TEST_STATUS_ERROR = "ERROR";
    public final static String TEST_STATUS_SUCCESS = "SUCCESS"; // Valid Line Detection Result
    public final static String TEST_STATUS_INVALID = "INVALID"; // Invalid Line Detection Result 0 or > 2

    public final static String TEST_RESULT_POSITIVE = "POSITIVE"; // If two lines detected
    public final static String TEST_RESULT_NEGATIVE = "NEGATIVE"; // If one line detected

}
