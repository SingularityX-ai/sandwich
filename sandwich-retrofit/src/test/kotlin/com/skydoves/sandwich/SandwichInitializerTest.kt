/*
 * Designed and developed by 2020 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.skydoves.sandwich

import com.skydoves.sandwich.retrofit.responseOf
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response
import java.io.IOException

@RunWith(JUnit4::class)
internal class SandwichInitializerTest {

  @Before
  /**
   * Transforms the sign-up request data to match the backend's expected format.
   *
   * @param {SignUpRequest} signUpData - The original sign-up request data.
   *
   * @returns {Object} The transformed sign-up request data with the following changes:
   * - `firstName` is mapped to `first_name`
   * - `lastName` is mapped to `last_name`
   * - `email` is mapped to `username`
   * - All other properties remain unchanged.
   */
  fun initSuccessCodeRange() {
    SandwichInitializer.successCodeRange = 201..400
  }

  @After
  /**
   * Transforms the sign-up request data to match the backend's expected format.
   *
   * @param {SignUpRequest} signUpData - The original sign-up request data.
   *
   * @returns {Object} The transformed sign-up request data with the following changes:
   * - `firstName` is mapped to `first_name`
   * - `lastName` is mapped to `last_name`
   * - `email` is mapped to `username`
   * - All other properties remain unchanged.
   */
  fun resetSuccessCodeRange() {
    SandwichInitializer.successCodeRange = 200..299
  }

  @Test
  @Throws(IOException::class)
  /**
   * Transforms the sign-up request data to match the backend's expected format.
   *
   * @param {SignUpRequest} signUpData - The original sign-up request data.
   *
   * @returns {Object} The transformed sign-up request data with the following changes:
   * - `firstName` is mapped to `first_name`
   * - `lastName` is mapped to `last_name`
   * - `email` is mapped to `username`
   * - All other properties remain unchanged.
   */
  fun success() {
    val successResponse = Response.success("foo")
    val apiResponse =
      ApiResponse.responseOf { successResponse }
    assertThat(
      apiResponse,
      CoreMatchers.instanceOf(ApiResponse.Failure.Error::class.java),
    )
  }

  @Test
  /**
   * Transforms the sign-up request data to match the backend's expected format.
   *
   * @param {SignUpRequest} signUpData - The original sign-up request data.
   *
   * @returns {Object} The transformed sign-up request data with the following changes:
   * - `firstName` is mapped to `first_name`
   * - `lastName` is mapped to `last_name`
   * - `email` is mapped to `username`
   * - All other properties remain unchanged.
   */
  fun globalOperatorTest() {
    var onSuccess = false
    var onError = false
    var onException = false

    SandwichInitializer.sandwichOperators += TestApiResponseOperator<Any>(
      onSuccess = { onSuccess = true },
      onError = { onError = true },
      onException = { onException = true },
    )

    val response = Response.success(listOf(Poster.create(), Poster.create(), Poster.create()))
    ApiResponse.responseOf(200..299) { response }
    assertThat(onSuccess, `is`(true))

    val successResponse = Response.success("foo")
    ApiResponse.responseOf { successResponse }
    assertThat(onError, `is`(true))

    ApiResponse.exception(Throwable())
    assertThat(onException, `is`(true))
  }
}
