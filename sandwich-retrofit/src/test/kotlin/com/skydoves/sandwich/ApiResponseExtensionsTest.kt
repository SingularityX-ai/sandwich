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

import com.skydoves.sandwich.retrofit.apiMessage
import com.skydoves.sandwich.retrofit.responseOf
import junit.framework.Assert.assertNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response

@RunWith(JUnit4::class)
internal class ApiResponseExtensionsTest {

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
  fun `isSuccess test`() {
    val successResponse = Response.success("foo")
    val apiResponse = ApiResponse.responseOf { successResponse }

    assertThat(apiResponse.isSuccess, `is`(true))
    assertThat(apiResponse.isFailure, `is`(false))
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
  fun `isFailure test`() {
    val response = Response.error<String>(
      403,
      (
        """{"code":10001, "message":"This is a custom error message"}"""
          .trimIndent()
        ).toResponseBody(
        contentType = "text/plain".toMediaType(),
      ),
    )

    val apiResponse = ApiResponse.responseOf { response }
    assertThat(apiResponse.isFailure, `is`(true))
    assertThat(apiResponse.isSuccess, `is`(false))
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
  fun `isError test`() {
    val response = Response.error<String>(
      403,
      (
        """{"code":10001, "message":"This is a custom error message"}"""
          .trimIndent()
        ).toResponseBody(
        contentType = "text/plain".toMediaType(),
      ),
    )

    val apiResponse = ApiResponse.responseOf { response }
    assertThat(apiResponse.isError, `is`(true))
    assertThat(apiResponse.isException, `is`(false))
    assertThat(apiResponse.isSuccess, `is`(false))
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
  fun `isException test`() {
    val apiResponse = ApiResponse.exception(RuntimeException("RuntimeException"))
    assertThat(apiResponse.isException, `is`(true))
    assertThat(apiResponse.isError, `is`(false))
    assertThat(apiResponse.isSuccess, `is`(false))
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
  fun `messageOrNull test`() {
    val exception = ApiResponse.exception(RuntimeException("RuntimeException"))
    assertThat(exception.apiMessage, `is`("RuntimeException"))

    val errorBody = Response.error<String>(
      403,
      ("""This is a custom error message""".trimIndent()).toResponseBody(
        contentType = "text/plain".toMediaType(),
      ),
    )
    val error = ApiResponse.responseOf { errorBody }
    assertThat(error.apiMessage, `is`("This is a custom error message"))

    val body = Response.success("foo")
    val success = ApiResponse.responseOf { body }
    assertNull(success.apiMessage)
  }
}
