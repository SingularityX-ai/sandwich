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
import com.github.skydoves.sandwich.Configuration

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  id(libs.plugins.android.library.get().pluginId)
  id(libs.plugins.kotlin.android.get().pluginId)
  id(libs.plugins.nexus.plugin.get().pluginId)
}

apply(from = "${rootDir}/scripts/publish-module.gradle.kts")

mavenPublishing {
  val artifactId = "sandwich-retrofit-datasource"
  coordinates(
    Configuration.artifactGroup,
    artifactId,
    rootProject.extra.get("libVersion").toString()
  )

  pom {
    name.set(artifactId)
    description.set(
      "A lightweight and pluggable sealed API library for modeling Retrofit " +
        "responses and handling exceptions on Kotlin and Android."
    )
  }
}

android {
  compileSdk = Configuration.compileSdk
  namespace = "com.skydoves.sandwich.retrofit.datasource"
  defaultConfig {
    minSdk = Configuration.minSdk
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  lint {
    abortOnError = false
  }

  testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
  api(project(":sandwich-retrofit"))

  implementation(libs.appcompat)
  implementation(libs.coroutines)

  // unit test
  testImplementation(libs.junit)
  testImplementation(libs.mockito.core)
  testImplementation(libs.mockito.inline)
  testImplementation(libs.mockito.kotlin)
  testImplementation(libs.mock.webserver)
  testImplementation(libs.retrofit.moshi)
  testImplementation(libs.coroutines.test)
  testImplementation(libs.arch.test)
}
