// Copyright 2016 The Bazel Authors. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.devtools.build.lib.rules.proto;

import com.google.auto.value.AutoValue;
import com.google.devtools.build.lib.actions.Artifact;
import com.google.devtools.build.lib.analysis.TransitiveInfoProvider;

/**
 * A provider that exposes a compiled descriptor set from a proto_library.
 *
 * <p>Produced by passing --descriptor_set_out to the proto-compiler. See
 * https://developers.google.com/protocol-buffers/docs/techniques#self-description
 */
@AutoValue
public abstract class DescriptorSetProvider implements TransitiveInfoProvider {

  /**
   * Be careful while using this artifact - it is the parsing of the transitive set of .proto files.
   * It's possible to cause a O(n^2) behavior, where n is the length of a proto chain-graph.
   */
  public abstract Artifact descriptorSet();

  public static DescriptorSetProvider create(Artifact descriptorSet) {
    return new AutoValue_DescriptorSetProvider(descriptorSet);
  }
}
