/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.springlets.web.mvc.support;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.util.UriComponents;

import java.util.Map;

/**
 * Unit tests for the {@link MethodLinkFactorySupport} class.
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
@RunWith(MockitoJUnitRunner.class)
public class MethodLinkFactorySupportTest {

  private TestMethodLinkFactorySupport methodLinkFactorySupport;

  @Before
  public void setupContext() {
    methodLinkFactorySupport = new TestMethodLinkFactorySupport();
  }

  /**
   * Test method for {@link io.springlets.web.mvc.support.MethodLinkFactorySupport#getControllerClass()}.
   */
  @Test
  public void testGetControllerClass() {
    assertThat(methodLinkFactorySupport.getControllerClass())
        .isEqualTo(MethodLinkFactorySupportTest.class);
  }

  /**
   * Test method for {@link io.springlets.web.mvc.support.MethodLinkFactorySupport#onController()}.
   */
  @Test
  public void testOnController() {
    // Verify
    assertThat(methodLinkFactorySupport.onController())
        .isInstanceOf(MethodLinkFactorySupportTest.class);
  }

  private static class TestMethodLinkFactorySupport
      extends MethodLinkFactorySupport<MethodLinkFactorySupportTest> {

    public TestMethodLinkFactorySupport() {
      super(MethodLinkFactorySupportTest.class);
    }

    @Override
    public UriComponents toUri(String methodName, Object[] parameters,
        Map<String, Object> pathVariables) {
      return null;
    }

  }

}
