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
package com.skydoves.sandwich.retrofit.datasource.adapters

import com.skydoves.sandwich.retrofit.datasource.DataSource
import com.skydoves.sandwich.retrofit.datasource.adapters.internal.DataSourceCallAdapter
import com.skydoves.sandwich.retrofit.datasource.adapters.internal.DataSourceRawCallAdapter
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author skydoves (Jaewoong Eum)
 *
 * DataSourceCallAdapterFactory is an call adapter factory for creating [DataSource].
 *
 * Adding this class to [Retrofit] allows you to return on [DataSource] from service method.
 *
 * ```
 * @GET("DisneyPosters.json")
 * fun fetchDisneyPosterList(): DataSource<List<Poster>>
 * ```
 */
public class DataSourceCallAdapterFactory private constructor() : CallAdapter.Factory() {

  override fun get(
    returnType: Type,
    annotations: Array<Annotation>,
    retrofit: Retrofit,
  ): CallAdapter<*, *>? {
    when (getRawType(returnType)) {
      DataSource::class.java -> {
        val resultType = getParameterUpperBound(0, returnType as ParameterizedType)
        return DataSourceRawCallAdapter<Type>(resultType)
      }

      Call::class.java -> {
        val callType = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawType = getRawType(callType)
        if (rawType != DataSource::class.java) {
          return null
        }
        val resultType = getParameterUpperBound(0, callType as ParameterizedType)
        return DataSourceCallAdapter(resultType)
      }

      else -> return null
    }
  }

  public companion object {
    @JvmStatic
    public fun create(): DataSourceCallAdapterFactory = DataSourceCallAdapterFactory()
  }
/**
 * Transforms the sign-up request data to match the backend's expected format.
 *
 * @param {SignUpRequest} signUpData - The original sign-up request data.
 *
 * @returns {Object} The transformed sign-up request data with the following changes:
 * - `firstName` is mapped to `first_name`
 * - `lastName` is mapped to `last_name`
 * - `email` is mapped to `username`
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
 * - All other properties remain unchanged.
 */
}
