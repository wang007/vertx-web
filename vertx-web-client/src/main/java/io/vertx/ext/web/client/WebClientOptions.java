/*
 * Copyright 2017 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.ext.web.client;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.impl.UserAgentUtil;

/**
 * @author Thomas Segismont
 */
@DataObject(generateConverter = true)
public class WebClientOptions {

  /**
   * The default value of whether the Web Client should send a user agent header = true.
   */
  public static final boolean DEFAULT_USER_AGENT_ENABLED = true;

  /**
   * The default user agent string = Vert.x-WebClient/&lt;version&gt;.
   */
  public static final String DEFAULT_USER_AGENT = UserAgentUtil.loadUserAgent();

  private HttpClientOptions httpClientOptions = new HttpClientOptions();
  private boolean userAgentEnabled = DEFAULT_USER_AGENT_ENABLED;
  private String userAgent = DEFAULT_USER_AGENT;

  public WebClientOptions() {
  }

  /**
   * Copy constructor.
   *
   * @param other the options to copy
   */
  public WebClientOptions(WebClientOptions other) {
    this.httpClientOptions = other.httpClientOptions;
    this.userAgentEnabled = other.userAgentEnabled;
    this.userAgent = other.userAgent;
  }

  /**
   * Creates a new instance from JSON.
   *
   * @param json the JSON object
   */
  public WebClientOptions(JsonObject json) {
    WebClientOptionsConverter.fromJson(json, this);
  }

  /**
   * Returns the underlying HttpClient options.
   * Not used when the Web Client is created by wrapping a HttpClient.
   *
   * @return the underlying {@link io.vertx.core.http.HttpClient} options
   */
  public HttpClientOptions getHttpClientOptions() {
    return httpClientOptions;
  }

  /**
   * Sets the underlying HttpClient options.
   * Not used when the Web Client is created by wrapping a HttpClient.
   *
   * @param httpClientOptions the underlying {@link io.vertx.core.http.HttpClient} options
   * @return a reference to this, so the API can be used fluently
   */
  public WebClientOptions setHttpClientOptions(HttpClientOptions httpClientOptions) {
    this.httpClientOptions = httpClientOptions;
    return this;
  }

  /**
   * @return true if the Web Client should send a user agent header, false otherwise
   */
  public boolean isUserAgentEnabled() {
    return userAgentEnabled;
  }

  /**
   * Sets whether the Web Client should send a user agent header. Defaults to true.
   *
   * @param userAgentEnabled true to send a user agent header, false otherwise
   * @return a reference to this, so the API can be used fluently
   */
  public WebClientOptions setUserAgentEnabled(boolean userAgentEnabled) {
    this.userAgentEnabled = userAgentEnabled;
    return this;
  }

  /**
   * @return the user agent header string
   */
  public String getUserAgent() {
    return userAgent;
  }

  /**
   * Sets the Web Client user agent header. Defaults to Vert.x-WebClient/&lt;version&gt;.
   *
   * @param userAgent user agent header value
   * @return a reference to this, so the API can be used fluently
   */
  public WebClientOptions setUserAgent(String userAgent) {
    this.userAgent = userAgent;
    return this;
  }
}
