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

import io.springlets.web.mvc.util.MethodLinkBuilder;
import io.springlets.web.mvc.util.MethodLinkBuilderFactory;
import io.springlets.web.mvc.util.MethodLinkFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Unit tests for the {@link SimpleMethodLinkBuilderFactory} class.
 * @author Cèsar Ordiñana at http://www.disid.com[DISID Corporation S.L.]
 */
@RunWith(MockitoJUnitRunner.class)
public class SimpleMethodLinkBuilderFactoryTest {

  private MethodLinkBuilderFactory<?> factory;

  @Mock
  private MethodLinkFactory<?> linkFactory;

  @Before
  public void setupContext() {
    factory = new SimpleMethodLinkBuilderFactory<>(linkFactory);
  }

  /**
   * Test method for {@link io.springlets.web.mvc.support.SimpleMethodLinkBuilderFactory#to(java.lang.String)}.
   */
  @Test
  public void testTo() {
    // Setup

    // Exercise
    MethodLinkBuilder<?> linkTo = factory.to("test");

    // Verify
    assertThat(linkTo).isNotNull().as("The created LinkBuilder can't be null");
  }

}
