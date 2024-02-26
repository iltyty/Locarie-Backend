package com.locarie.backend.utils.expecters;

import com.locarie.backend.utils.matchers.ResultMatcherUtil;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.ResultActions;

@Component
public class ResultExpectUtil {
  private final ResultMatcherUtil matcherUtil;

  public ResultExpectUtil(ResultMatcherUtil matcherUtil) {
    this.matcherUtil = matcherUtil;
  }

  public void thenResultShouldBeCreated(ResultActions result) throws Exception {
    result.andExpect(matcherUtil.resultStatusShouldBeCreated());
    thenResultShouldBeSuccess(result);
  }

  public void thenResultShouldBeOk(ResultActions result) throws Exception {
    result.andExpect(matcherUtil.resultStatusShouldBeOk());
    thenResultShouldBeSuccess(result);
  }

  public void thenResultShouldBeFailed(ResultActions result) throws Exception {
    result
        .andExpect(matcherUtil.resultStatusCodeShouldBeFailure())
        .andExpect(matcherUtil.resultStatusShouldBeOk())
        .andExpect(matcherUtil.resultMessageShouldBeFailure());
  }

  public void thenResultShouldBeSuccess(ResultActions result) throws Exception {
    result
        .andExpect(matcherUtil.resultStatusCodeShouldBeSuccess())
        .andExpect(matcherUtil.resultMessageShouldBeSuccess());
  }

  public void thenResultShouldBeUserNotFound(ResultActions result) throws Exception {
    result.andExpect(matcherUtil.resultStatusCodeShouldBeUserNotFound());
  }

  public void thenResultShouldBeUnauthorized(ResultActions result) throws Exception {
    result
        .andExpect(matcherUtil.resultStatusShouldBeUnauthorized())
        .andExpect(matcherUtil.resultStatusCodeShouldBeUnauthorized());
  }
}
