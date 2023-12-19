package com.locarie.backend.utils.user;

import com.locarie.backend.global.ResultCode;
import org.hamcrest.core.StringContains;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class UserControllerResultMatcherUtil {
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
