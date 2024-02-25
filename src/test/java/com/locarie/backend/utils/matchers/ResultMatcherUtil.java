package com.locarie.backend.utils.matchers;

import com.locarie.backend.global.ResultCode;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Component
public class ResultMatcherUtil {
  public ResultMatcher resultStatusShouldBeOk() {
    return MockMvcResultMatchers.status().isOk();
  }

  public ResultMatcher resultStatusShouldBeCreated() {
    return MockMvcResultMatchers.status().isCreated();
  }

  public ResultMatcher resultStatusCodeShouldBeSuccess() {
    return MockMvcResultMatchers.jsonPath("$.status").value(ResultCode.SUCCESS.getCode());
  }

  public ResultMatcher resultStatusCodeShouldBeFailure() {
    return MockMvcResultMatchers.jsonPath("$.status").value(ResultCode.FAIL.getCode());
  }

  public ResultMatcher resultStatusCodeShouldBeInvalidParameters() {
    return MockMvcResultMatchers.jsonPath("$.status").value(ResultCode.RC101.getCode());
  }

  public ResultMatcher resultStatusCodeShouldBeUserNotFound() {
    return MockMvcResultMatchers.jsonPath("$.status").value(ResultCode.RC301.getCode());
  }

  public ResultMatcher resultMessageShouldBeSuccess() {
    return MockMvcResultMatchers.jsonPath("$.message").value(ResultCode.SUCCESS.getMessage());
  }

  public ResultMatcher resultMessageShouldBeFailure() {
    return MockMvcResultMatchers.jsonPath("$.message").value(ResultCode.FAIL.getMessage());
  }
}
