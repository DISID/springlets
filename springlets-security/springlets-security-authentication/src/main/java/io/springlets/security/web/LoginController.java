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
package io.springlets.security.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * = _Login_ Controller
 *
 * Controller that provides the login view
 * 
 * @author Juan Carlos García at http://www.disid.com[DISID Corporation S.L.]
 */
@Controller
@RequestMapping("${springlets.security.web.login-path:/login}")
public class LoginController {

  @Value("${springlets.security.web.login-view-name:login}")
  private String viewName;

  @RequestMapping(method = RequestMethod.GET)
  public String login(Model model) {
    return viewName;
  }

}
