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

package com.google.devtools.build.lib.remote;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.Subscribe;
import com.google.devtools.build.lib.actions.ExecutorBuilder;
import com.google.devtools.build.lib.analysis.config.InvalidConfigurationException;
import com.google.devtools.build.lib.buildtool.BuildRequest;
import com.google.devtools.build.lib.buildtool.buildevent.BuildStartingEvent;
import com.google.devtools.build.lib.events.Event;
import com.google.devtools.build.lib.runtime.BlazeModule;
import com.google.devtools.build.lib.runtime.Command;
import com.google.devtools.build.lib.runtime.CommandEnvironment;
import com.google.devtools.build.lib.util.AbruptExitException;
import com.google.devtools.build.lib.util.ExitCode;
import com.google.devtools.build.lib.vfs.FileSystem;
import com.google.devtools.build.lib.vfs.FileSystem.HashFunction;
import com.google.devtools.common.options.OptionsBase;

/** RemoteModule provides distributed cache and remote execution for Bazel. */
public final class RemoteModule extends BlazeModule {
  private CommandEnvironment env;
  private RemoteActionCache actionCache;
  private RemoteWorkExecutor workExecutor;

  @Override
  public void beforeCommand(Command command, CommandEnvironment env) {
    this.env = env;
    env.getEventBus().register(this);
  }

  @Override
  public void afterCommand() {
    this.env = null;
  }

  @Override
  public void executorInit(CommandEnvironment env, BuildRequest request, ExecutorBuilder builder) {
    builder.addActionContextProvider(
        new RemoteActionContextProvider(env, request, actionCache, workExecutor));
  }

  @Subscribe
  public void buildStarting(BuildStartingEvent event) {
    RemoteOptions options = event.getRequest().getOptions(RemoteOptions.class);

    try {
      // Reinitialize the remote cache and worker from options every time, because the options
      // may change from build to build.

      // Don't provide the remote spawn unless at least action cache is initialized.
      if (ConcurrentMapFactory.isRemoteCacheOptions(options)) {
        actionCache = new ConcurrentMapActionCache(ConcurrentMapFactory.create(options));
      }
      if (GrpcActionCache.isRemoteCacheOptions(options)) {
        actionCache = new GrpcActionCache(options);
      }
      // Otherwise actionCache remains null and remote caching/execution are disabled.

      if (actionCache != null) {
        HashFunction hf = FileSystem.getDigestFunction();
        if (hf != HashFunction.SHA1) {
          env.getBlazeModuleEnvironment().exit(new AbruptExitException(
              "Remote cache/execution requires SHA1 digests, got " + hf
              + ", run with --host_jvm_args=-Dbazel.DigestFunction=SHA1",
              ExitCode.COMMAND_LINE_ERROR));
        }
        if (RemoteWorkExecutor.isRemoteExecutionOptions(options)) {
          workExecutor = new RemoteWorkExecutor(options);
        }
      }
    } catch (InvalidConfigurationException e) {
      env.getReporter().handle(Event.warn(e.toString()));
    }
  }

  @Override
  public Iterable<Class<? extends OptionsBase>> getCommandOptions(Command command) {
    return "build".equals(command.name())
        ? ImmutableList.<Class<? extends OptionsBase>>of(RemoteOptions.class)
        : ImmutableList.<Class<? extends OptionsBase>>of();
  }
}
