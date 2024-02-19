package com.locarie.backend.utils.matchers;

import org.hamcrest.core.StringContains;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class UserControllerResultMatcherUtil extends ControllerResultMatcherUtil {

  public static ResultMatcher resultMessageShouldContainType() {
    return resultMessageShouldContainField("type");
  }

  public static ResultMatcher resultMessageShouldContainUsername() {
    return resultMessageShouldContainField("username");
  }

  public static ResultMatcher resultMessageShouldContainEmail() {
    return resultMessageShouldContainField("email");
  }

  public static ResultMatcher resultMessageShouldContainPassword() {
    return resultMessageShouldContainField("password");
  }

  public static ResultMatcher resultMessageShouldContainField(String field) {
    return MockMvcResultMatchers.jsonPath("$.message").value(new StringContains(field));
  }
}
