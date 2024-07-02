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

package com.skydoves.sandwich.retrofit.datasource.disposables

/**
 * @author skydoves (Jaewoong Eum)
 *
 * A disposable container that can hold onto multiple other disposables.
 */
public class CompositeDisposable {

  @Volatile
  public var disposed: Boolean = false
    private set

  private var disposables: MutableSet<Disposable>? = hashSetOf()

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
  public fun add(disposable: Disposable) {
    if (disposable.isDisposed()) {
      return
    }

    if (!disposed) {
      synchronized(this) {
        if (!disposed) {
          disposables?.add(disposable)
          return
        }
      }
    }
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
  public fun remove(disposable: Disposable) {
    if (!disposed) {
      synchronized(this) {
        if (disposed || disposables?.remove(disposable) == false) {
          return
        }
      }
      disposable.dispose()
    }
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
  public fun clear() {
    if (!disposed) {
      var mutableCollection: MutableCollection<Disposable>?
      synchronized(this) {
        if (disposed) {
          return
        }
        mutableCollection = disposables
        disposables = null
        disposed = true
      }
      mutableCollection?.forEach(Disposable::dispose)
    }
  }
}
