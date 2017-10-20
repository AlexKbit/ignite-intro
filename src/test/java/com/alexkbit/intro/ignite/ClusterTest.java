package com.alexkbit.intro.ignite;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Integration test.
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("cluster-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public @interface ClusterTest {
}
