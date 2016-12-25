// Copyright 2015 The Bazel Authors. All rights reserved.
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
package com.google.devtools.build.lib.actions;

import com.google.common.collect.ImmutableList;
import com.google.devtools.build.lib.actions.Executor.ActionContext;
import java.util.List;

/**
 * An {@link ActionContextProvider} that just provides the {@link ActionContext}s it's given.
 */
final class SimpleActionContextProvider extends ActionContextProvider {
  private final List<ActionContext> actionContexts;

  public SimpleActionContextProvider(ActionContext... contexts) {
    actionContexts = ImmutableList.<ActionContext>copyOf(contexts);
  }

  @Override
  public Iterable<ActionContext> getActionContexts() {
    return actionContexts;
  }
}
