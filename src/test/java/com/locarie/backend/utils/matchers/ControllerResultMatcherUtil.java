package com.locarie.backend.utils.matchers;

import com.locarie.backend.global.ResultCode;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class ControllerResultMatcherUtil {
  public static ResultMatcher resultStatusShouldBeOk() {
    return MockMvcResultMatchers.status().isOk();
  }

  public static ResultMatcher resultStatusShouldBeCreated() {
    return MockMvcResultMatchers.status().isCreated();
  }

  public static ResultMatcher resultStatusCodeShouldBeSuccess() {
    return MockMvcResultMatchers.jsonPath("$.status").value(ResultCode.SUCCESS.getCode());
  }

  public static ResultMatcher resultStatusCodeShouldBeFailure() {
    return MockMvcResultMatchers.jsonPath("$.status").value(ResultCode.FAIL.getCode());
  }

  public static ResultMatcher resultStatusCodeShouldBeInvalidParameters() {
    return MockMvcResultMatchers.jsonPath("$.status").value(ResultCode.RC101.getCode());
  }

  public static ResultMatcher resultMessageShouldBeSuccess() {
    return MockMvcResultMatchers.jsonPath("$.message").value(ResultCode.SUCCESS.getMessage());
  }

  public static ResultMatcher resultMessageShouldBeFailure() {
    return MockMvcResultMatchers.jsonPath("$.message").value(ResultCode.FAIL.getMessage());
  }
}
