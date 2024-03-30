package edu.java.bot;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.annotation.EnableRetry;

//@SpringBootTest("app.retry-policy.mode=constant")
@SpringBootTest
@WireMockTest(httpPort = 8080)
@EnableRetry
public class ConstantRetryTest {

}
