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

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skydoves.sandwich.retrofit.apiMessage
import com.skydoves.sandwich.retrofit.apiResponseOf
import com.skydoves.sandwich.retrofit.headers
import com.skydoves.sandwich.retrofit.raw
import com.skydoves.sandwich.retrofit.responseOf
import com.skydoves.sandwich.retrofit.statusCode
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException

@RunWith(JUnit4::class)
internal class ApiResponseTest : ApiAbstract<DisneyService>() {

  private lateinit var service: DisneyService
  private val client: DisneyClient = mock()

  @Before
  fun initService() {
    service = createService(DisneyService::class.java)
  }

  @Test
  @Throws(IOException::class)
  fun success() {
    val response = Response.success("foo")
    val apiResponse = ApiResponse.responseOf { response }
    assertThat(apiResponse, instanceOf(ApiResponse.Success::class.java))

    val success = apiResponse as ApiResponse.Success<String>
    assertThat(success.data, `is`("foo"))
    assertThat(success.statusCode.code, `is`(200))
    assertThat(success.raw, `is`(response.raw()))
    assertThat(success.headers, `is`(response.headers()))
  }

  @Test
  fun successFromRequest() {
    enqueueResponse("/DisneyPosters.json")

    val responseBody = requireNotNull(service.fetchDisneyPosterList().execute().body())
    mockWebServer.takeRequest()

    assertThat(responseBody[0].id, `is`(0L))
    assertThat(responseBody[0].name, `is`("Frozen II"))
    assertThat(responseBody[0].release, `is`("2019"))

    val onResult: (response: ApiResponse<List<Poster>>) -> Unit = {
      assertThat(it, instanceOf(ApiResponse.Success::class.java))
      val response: List<Poster> = requireNotNull((it as ApiResponse.Success).data)
      assertThat(response[0].id, `is`(0L))
      assertThat(response[0].name, `is`("Frozen II"))
      assertThat(response[0].release, `is`("2019"))
    }

    whenever(client.fetchDisneyPosters(onResult)).thenAnswer {
      val response: (response: ApiResponse<List<Poster>>) -> Unit = it.getArgument(0)
      response(apiResponseOf { Response.success(responseBody) })
    }
    client.fetchDisneyPosters(onResult)
  }

  @Test
  fun successExtensionFromRequest() {
    enqueueResponse("/DisneyPosters.json")

    val responseBody = requireNotNull(service.fetchDisneyPosterList().execute().body())
    mockWebServer.takeRequest()

    val onResult: (response: ApiResponse<List<Poster>>) -> Unit = {
      assertThat(it, instanceOf(ApiResponse.Success::class.java))
      val response = requireNotNull((it as ApiResponse.Success))
      response.onSuccess {
        val first = data.firstOrNull()
        assertThat(first?.id, `is`(0L))
        assertThat(first?.name, `is`("Frozen II"))
        assertThat(first?.release, `is`("2019"))
      }
    }

    whenever(client.fetchDisneyPosters(onResult)).thenAnswer {
      val response: (response: ApiResponse<List<Poster>>) -> Unit = it.getArgument(0)
      response(apiResponseOf { Response.success(responseBody) })
    }
    client.fetchDisneyPosters(onResult)
  }

  @Test
  fun error() {
    val retrofit: Retrofit = Retrofit.Builder()
      .baseUrl(mockWebServer.url("/"))
      .addConverterFactory(MoshiConverterFactory.create())
      .build()

    val service = retrofit.create(DisneyService::class.java)
    mockWebServer.enqueue(MockResponse().setResponseCode(404).setBody("foo"))

    val response: Response<List<Poster>> = service.fetchDisneyPosterList().execute()
    assertThat(response.isSuccessful, `is`(false))
    assertThat(response.code(), `is`(404))
    assertThat(response.errorBody()!!.string(), `is`("foo"))
  }

  @Test
  fun errorFromRequest() {
    val retrofit: Retrofit = Retrofit.Builder()
      .baseUrl(mockWebServer.url("/"))
      .addConverterFactory(MoshiConverterFactory.create())
      .build()

    val service = retrofit.create(DisneyService::class.java)
    mockWebServer.enqueue(MockResponse().setResponseCode(404).setBody("foo"))

    val responseBody = requireNotNull(service.fetchDisneyPosterList().execute().errorBody())

    val onResult: (response: ApiResponse<List<Poster>>) -> Unit = {
      assertThat(it, instanceOf(ApiResponse.Failure.Error::class.java))
      val response = requireNotNull((it as ApiResponse.Failure.Error))
      assertThat(response.statusCode.code, `is`(404))
      assertThat(response.apiMessage, `is`("foo"))

      val errorResponse = response.map(ErrorEnvelopeMapper)
      assertThat(errorResponse, instanceOf(ErrorEnvelope::class.java))
      assertThat(errorResponse.code, `is`(response.statusCode.code))
      assertThat(errorResponse.message, `is`(response.apiMessage))
    }

    whenever(client.fetchDisneyPosters(onResult)).thenAnswer {
      val response: (response: ApiResponse<List<Poster>>) -> Unit = it.getArgument(0)
      response(ApiResponse.Failure.Error(Response.error<List<Poster>>(404, responseBody)))
    }
    client.fetchDisneyPosters(onResult)
  }

  @Test
  fun errorExtensionFromRequest() {
    val retrofit: Retrofit = Retrofit.Builder()
      .baseUrl(mockWebServer.url("/"))
      .addConverterFactory(MoshiConverterFactory.create())
      .build()

    val service = retrofit.create(DisneyService::class.java)
    mockWebServer.enqueue(MockResponse().setResponseCode(404).setBody(""))

    val responseBody = requireNotNull(service.fetchDisneyPosterList().execute().errorBody())

    val onResult: (response: ApiResponse<List<Poster>>) -> Unit = {
      assertThat(it, instanceOf(ApiResponse.Failure.Error::class.java))
      val response = requireNotNull((it as ApiResponse.Failure.Error))
      response.onError {
        assertThat(statusCode.code, `is`(404))
        map(ErrorEnvelopeMapper) {
          assertThat(this, instanceOf(ErrorEnvelope::class.java))
          assertThat(this.code, `is`(response.statusCode.code))
          assertThat(this.message, `is`(response.apiMessage))
        }
      }
    }

    whenever(client.fetchDisneyPosters(onResult)).thenAnswer {
      val response: (response: ApiResponse<List<Poster>>) -> Unit = it.getArgument(0)
      response(ApiResponse.Failure.Error(Response.error<List<Poster>>(404, responseBody)))
    }
    client.fetchDisneyPosters(onResult)
  }
}
