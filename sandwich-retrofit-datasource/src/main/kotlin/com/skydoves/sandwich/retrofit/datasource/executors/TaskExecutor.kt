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
 * A task executor that can divide tasks into logical groups.
 *
 *
 * It holds a collection a executors for each group of task.
 *
 *
 * TODO: Don't use this from outside, we don't know what the API will look like yet.
 * @hide
 */

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal abstract class TaskExecutor {

  /**
   * Returns true if the current thread is the main thread, false otherwise.
   *
   * @return true if we are on the main thread, false otherwise.
   */
  abstract val isMainThread: Boolean

  /**
   * Executes the given task in the disk IO thread pool.
   *
   * @param runnable The runnable to run in the disk IO thread pool.
   */
  abstract fun executeOnDiskIO(runnable: Runnable)

  /**
   * Posts the given task to the main thread.
   *
   * @param runnable The runnable to run on the main thread.
   */
  abstract fun postToMainThread(runnable: Runnable, duration: Long)

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
  fun executeOnMainThread(runnable: Runnable, duration: Long) {
    if (isMainThread) {
      runnable.run()
    } else {
      postToMainThread(runnable, duration)
    }
  }
}
