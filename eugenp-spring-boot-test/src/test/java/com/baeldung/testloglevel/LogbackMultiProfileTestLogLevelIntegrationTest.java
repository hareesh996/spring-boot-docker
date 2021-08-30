package com.baeldung.testloglevel;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = TestLogLevelApplication.class)
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@ActiveProfiles("logback-test2")
@ExtendWith(OutputCaptureExtension.class)
public class LogbackMultiProfileTestLogLevelIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl = "/testLogLevel";

    @Test
    public void givenErrorRootLevelAndTraceLevelForOurPackage_whenCall_thenPrintTraceLogsForOurPackage(CapturedOutput output) {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThatOutputContainsLogForOurPackage(output, "TRACE");
    }

    @Test
    public void givenErrorRootLevelAndTraceLevelForOurPackage_whenCall_thenNoTraceLogsForOtherPackages(CapturedOutput output) {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThatOutputDoesntContainLogForOtherPackages(output, "TRACE");
    }

    @Test
    public void givenErrorRootLevelAndTraceLevelForOurPackage_whenCall_thenPrintErrorLogs(CapturedOutput output) {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThatOutputContainsLogForOurPackage(output, "ERROR");
        assertThatOutputContainsLogForOtherPackages( output,"ERROR");
    }

    private void assertThatOutputContainsLogForOurPackage(CapturedOutput output, String level) {
        assertThat(output.toString()).containsPattern("TestLogLevelController.*" + level + ".*");
    }

    private void assertThatOutputDoesntContainLogForOtherPackages(CapturedOutput output, String level) {
        assertThat(output.toString().replaceAll("(?m)^.*TestLogLevelController.*$", "")).doesNotContain(level);
    }

    private void assertThatOutputContainsLogForOtherPackages(CapturedOutput output, String level) {
        assertThat(output.toString().replaceAll("(?m)^.*TestLogLevelController.*$", "")).contains(level);
    }

}
