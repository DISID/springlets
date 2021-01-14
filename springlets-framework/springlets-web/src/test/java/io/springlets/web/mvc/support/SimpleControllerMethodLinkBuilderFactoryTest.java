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
import static org.mockito.Mockito.when;

import io.springlets.web.mvc.util.MethodLinkBuilderFactory;
import io.springlets.web.mvc.util.MethodLinkFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Unit tests for the {@link SimpleControllerMethodLinkBuilderFactory} class.
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SimpleControllerMethodLinkBuilderFactoryTest {

  private SimpleControllerMethodLinkBuilderFactory controllerLinks;

  @Mock
  private MethodLinkFactory<SimpleControllerMethodLinkBuilderFactoryTest> linkFactory;

  @Before
  public void setupContext() {
    controllerLinks = new SimpleControllerMethodLinkBuilderFactory();
  }

  /**
   * Test method for {@link io.springlets.web.mvc.support.SimpleControllerMethodLinkBuilderFactory#of(java.lang.Class)}.
   */
  @Test
  public void testOfClassOfT() {
    // Setup
    when(linkFactory.getControllerClass())
        .thenReturn(SimpleControllerMethodLinkBuilderFactoryTest.class);

    // Exercise
    controllerLinks.register(linkFactory);
    MethodLinkBuilderFactory<SimpleControllerMethodLinkBuilderFactoryTest> linkBuilder =
        controllerLinks.of(SimpleControllerMethodLinkBuilderFactoryTest.class);

    // Verify
    assertThat(linkBuilder).isNotNull().as("The created LinkBuilder can't be null");
  }

  /**
   * Test method for {@link io.springlets.web.mvc.support.SimpleControllerMethodLinkBuilderFactory#of(java.lang.String)}.
   */
  @Test
  public void testOfString() {
    // Setup
    when(linkFactory.getControllerClass())
        .thenReturn(SimpleControllerMethodLinkBuilderFactoryTest.class);

    // Exercise
    controllerLinks.register(linkFactory);
    MethodLinkBuilderFactory<?> linkBuilder =
        controllerLinks.of("SimpleControllerMethodLinkBuilderFactoryTest");

    // Verify
    assertThat(linkBuilder).isNotNull().as("The created LinkBuilder can't be null");
  }

}
