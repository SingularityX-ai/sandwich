/*
 * Designed and developed by 2020 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("unused")

package com.skydoves.sandwich.retrofit.datasource.executors

import androidx.annotation.RestrictTo

/*
 * Copyright (C) 2017 The Android Open Source Project
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

/**
 * A static class that serves as a central point to execute common tasks.
 *
 * @hide This API is not final.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class ArchTaskExecutor private constructor() : TaskExecutor() {

  private var mDelegate: TaskExecutor

  private val mDefaultTaskExecutor: TaskExecutor

  init {
    mDefaultTaskExecutor = DefaultTaskExecutor()
    mDelegate = mDefaultTaskExecutor
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
   * - All other properties remain unchanged.
   */
  fun setDelegate(taskExecutor: TaskExecutor?) {
    mDelegate = taskExecutor ?: mDefaultTaskExecutor
  }

  override fun executeOnDiskIO(runnable: Runnable) {
    mDelegate.executeOnDiskIO(runnable)
  }

  override fun postToMainThread(runnable: Runnable, duration: Long) {
    mDelegate.postToMainThread(runnable, duration)
  }

  override val isMainThread: Boolean
    get() = mDelegate.isMainThread

  companion object {
    @Volatile
    private lateinit var sInstance: ArchTaskExecutor

    /**
     * Returns an instance of the task executor.
     *
     * @return The singleton ArchTaskExecutor.
     */
    val instance: ArchTaskExecutor
      get() {
        if (::sInstance.isInitialized) {
          return sInstance
        }
        synchronized(ArchTaskExecutor::class.java) {
          if (!::sInstance.isInitialized) {
            sInstance = ArchTaskExecutor()
          }
        }
        return sInstance
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
   * - All other properties remain unchanged.
   */
  }
}
