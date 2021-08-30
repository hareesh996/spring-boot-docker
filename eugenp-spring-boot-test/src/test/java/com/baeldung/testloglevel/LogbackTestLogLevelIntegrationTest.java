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
import org.springframework.boot.test.system.OutputCaptureRule;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = TestLogLevelApplication.class)
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@ActiveProfiles("logback-test")
@ExtendWith(OutputCaptureExtension.class)
public class LogbackTestLogLevelIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

//    @Rule
//    public OutputCaptureRule outputCapture = new OutputCaptureRule();

    private String baseUrl = "/testLogLevel";

    @Test
    public void givenErrorRootLevelAndDebugLevelForOurPackage_whenCall_thenPrintDebugLogsForOurPackage(CapturedOutput outputCapture) {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThatOutputContainsLogForOurPackage(outputCapture, "DEBUG");
    }

    @Test
    public void givenErrorRootLevelAndDebugLevelForOurPackage_whenCall_thenNoDebugLogsForOtherPackages(CapturedOutput outputCapture) {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThatOutputDoesntContainLogForOtherPackages(outputCapture, "DEBUG");
    }

    @Test
    public void givenErrorRootLevelAndDebugLevelForOurPackage_whenCall_thenPrintErrorLogs(CapturedOutput outputCapture) {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThatOutputContainsLogForOurPackage(outputCapture, "ERROR");
        assertThatOutputContainsLogForOtherPackages(outputCapture, "ERROR");
    }

    private void assertThatOutputContainsLogForOurPackage(CapturedOutput outputCapture, String level) {
        assertThat(outputCapture.toString()).containsPattern("TestLogLevelController.*" + level + ".*");
    }

    private void assertThatOutputDoesntContainLogForOtherPackages(CapturedOutput outputCapture, String level) {
        assertThat(outputCapture.toString().replaceAll("(?m)^.*TestLogLevelController.*$", "")).doesNotContain(level);
    }

    private void assertThatOutputContainsLogForOtherPackages(CapturedOutput outputCapture, String level) {
        assertThat(outputCapture.toString().replaceAll("(?m)^.*TestLogLevelController.*$", "")).contains(level);
    }

}
