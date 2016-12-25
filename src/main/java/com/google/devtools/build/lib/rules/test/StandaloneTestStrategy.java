// Copyright 2014 The Bazel Authors. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.devtools.build.lib.rules.test;

import com.google.common.collect.ImmutableSet;
import com.google.devtools.build.lib.actions.ActionExecutionContext;
import com.google.devtools.build.lib.actions.Artifact;
import com.google.devtools.build.lib.actions.BaseSpawn;
import com.google.devtools.build.lib.actions.EnvironmentalExecException;
import com.google.devtools.build.lib.actions.ExecException;
import com.google.devtools.build.lib.actions.ExecutionStrategy;
import com.google.devtools.build.lib.actions.Executor;
import com.google.devtools.build.lib.actions.ResourceManager;
import com.google.devtools.build.lib.actions.ResourceManager.ResourceHandle;
import com.google.devtools.build.lib.actions.ResourceSet;
import com.google.devtools.build.lib.actions.Spawn;
import com.google.devtools.build.lib.actions.SpawnActionContext;
import com.google.devtools.build.lib.actions.TestExecException;
import com.google.devtools.build.lib.analysis.RunfilesSupplierImpl;
import com.google.devtools.build.lib.analysis.config.BinTools;
import com.google.devtools.build.lib.analysis.config.BuildConfiguration;
import com.google.devtools.build.lib.events.Event;
import com.google.devtools.build.lib.events.EventKind;
import com.google.devtools.build.lib.events.Reporter;
import com.google.devtools.build.lib.util.io.FileOutErr;
import com.google.devtools.build.lib.vfs.Path;
import com.google.devtools.build.lib.vfs.PathFragment;
import com.google.devtools.build.lib.view.test.TestStatus.BlazeTestStatus;
import com.google.devtools.build.lib.view.test.TestStatus.TestCase;
import com.google.devtools.build.lib.view.test.TestStatus.TestResultData;
import com.google.devtools.common.options.OptionsClassProvider;
import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Runs TestRunnerAction actions.
 */
@ExecutionStrategy(contextType = TestActionContext.class, name = { "standalone" })
public class StandaloneTestStrategy extends TestStrategy {
  // TODO(bazel-team) - add tests for this strategy.
  public static final String COLLECT_COVERAGE =
      "external/bazel_tools/tools/test/collect_coverage.sh";

  private final Path workspace;

  public StandaloneTestStrategy(
      OptionsClassProvider requestOptions,
      BinTools binTools,
      Map<String, String> clientEnv,
      Path workspace) {
    super(requestOptions, binTools, clientEnv);
    this.workspace = workspace;
  }

  @Override
  public void exec(TestRunnerAction action, ActionExecutionContext actionExecutionContext)
      throws ExecException, InterruptedException {
    Path execRoot = actionExecutionContext.getExecutor().getExecRoot();
    Path coverageDir = execRoot.getRelative(TestStrategy.getCoverageDirectory(action));

    Path runfilesDir = null;
    try {
      runfilesDir =
          TestStrategy.getLocalRunfilesDirectory(
              action,
              actionExecutionContext,
              binTools,
              action.getLocalShellEnvironment(),
              action.isEnableRunfiles());
    } catch (ExecException e) {
      throw new TestExecException(e.getMessage());
    }

    Path testTmpDir =
        TestStrategy.getTmpRoot(workspace, execRoot, executionOptions)
            .getChild(getTmpDirName(action.getExecutionSettings().getExecutable().getExecPath()));
    Path workingDirectory = runfilesDir.getRelative(action.getRunfilesPrefix());

    TestRunnerAction.ResolvedPaths resolvedPaths = action.resolve(execRoot);
    Map<String, String> env =
        getEnv(action, execRoot, runfilesDir, testTmpDir, resolvedPaths.getXmlOutputPath());

    Map<String, String> info = new HashMap<>();

    // This key is only understood by StandaloneSpawnStrategy.
    info.put("timeout", "" + getTimeout(action));
    info.putAll(action.getTestProperties().getExecutionInfo());

    Artifact testSetup = action.getRuntimeArtifact(TEST_SETUP_BASENAME);
    Spawn spawn =
        new BaseSpawn(
            getArgs(testSetup.getExecPathString(), COLLECT_COVERAGE, action),
            env,
            info,
            new RunfilesSupplierImpl(
                runfilesDir.asFragment(), action.getExecutionSettings().getRunfiles()),
            action,
            action.getTestProperties().getLocalResourceUsage(executionOptions.usingLocalTestJobs()),
            ImmutableSet.of(resolvedPaths.getXmlOutputPath().relativeTo(execRoot)));

    Executor executor = actionExecutionContext.getExecutor();

    try {
      prepareFileSystem(action, testTmpDir, coverageDir, workingDirectory);

      ResourceSet resources =
          action.getTestProperties().getLocalResourceUsage(executionOptions.usingLocalTestJobs());

      try (FileOutErr fileOutErr =
              new FileOutErr(
                  action.getTestLog().getPath(),
                  action
                      .resolve(actionExecutionContext.getExecutor().getExecRoot())
                      .getTestStderr());
          ResourceHandle handle = ResourceManager.instance().acquireResources(action, resources)) {
        TestResultData data =
            execute(actionExecutionContext.withFileOutErr(fileOutErr), spawn, action);
        appendStderr(fileOutErr.getOutputPath(), fileOutErr.getErrorPath());
        finalizeTest(actionExecutionContext, action, data);
      }
    } catch (IOException e) {
      executor.getEventHandler().handle(Event.error("Caught I/O exception: " + e));
      throw new EnvironmentalExecException("unexpected I/O exception", e);
    }
  }

  private Map<String, String> getEnv(
      TestRunnerAction action, Path execRoot, Path runfilesDir, Path tmpDir, Path xmlOutputPath) {
    Map<String, String> vars = getDefaultTestEnvironment(action);
    BuildConfiguration config = action.getConfiguration();

    vars.putAll(config.getLocalShellEnvironment());
    vars.putAll(action.getTestEnv());

    String tmpDirString;
    if (tmpDir.startsWith(execRoot)) {
      tmpDirString = tmpDir.relativeTo(execRoot).getPathString();
    } else {
      tmpDirString = tmpDir.getPathString();
    }

    String testSrcDir = runfilesDir.relativeTo(execRoot).getPathString();
    vars.put("JAVA_RUNFILES", testSrcDir);
    vars.put("PYTHON_RUNFILES", testSrcDir);
    vars.put("TEST_SRCDIR", testSrcDir);
    vars.put("TEST_TMPDIR", tmpDirString);
    vars.put("TEST_WORKSPACE", action.getRunfilesPrefix());
    vars.put("XML_OUTPUT_FILE", xmlOutputPath.relativeTo(execRoot).getPathString());
    if (!action.isEnableRunfiles()) {
      vars.put("RUNFILES_MANIFEST_ONLY", "1");
    }

    PathFragment coverageDir = TestStrategy.getCoverageDirectory(action);
    if (isCoverageMode(action)) {
      vars.put("COVERAGE_DIR", coverageDir.toString());
      vars.put("COVERAGE_OUTPUT_FILE", action.getCoverageData().getExecPathString());
    }

    return vars;
  }

  private TestResultData execute(
      ActionExecutionContext actionExecutionContext, Spawn spawn, TestRunnerAction action)
      throws ExecException, InterruptedException {
    Executor executor = actionExecutionContext.getExecutor();
    Closeable streamed = null;
    Path testLogPath = action.getTestLog().getPath();
    TestResultData.Builder builder = TestResultData.newBuilder();

    long startTime = executor.getClock().currentTimeMillis();
    SpawnActionContext spawnActionContext = executor.getSpawnActionContext(action.getMnemonic());
    try {
      try {
        if (executionOptions.testOutput.equals(TestOutputFormat.STREAMED)) {
          streamed = new StreamedTestOutput(
              Reporter.outErrForReporter(
                  actionExecutionContext.getExecutor().getEventHandler()), testLogPath);
        }
        spawnActionContext.exec(spawn, actionExecutionContext);

        builder.setTestPassed(true)
            .setStatus(BlazeTestStatus.PASSED)
            .setCachable(true)
            .setPassedLog(testLogPath.getPathString());
      } catch (ExecException e) {
        // Execution failed, which we consider a test failure.

        // TODO(bazel-team): set cachable==true for relevant statuses (failure, but not for
        // timeout, etc.)
        builder
            .setTestPassed(false)
            .setStatus(e.hasTimedOut() ? BlazeTestStatus.TIMEOUT : BlazeTestStatus.FAILED)
            .addFailedLogs(testLogPath.getPathString());
        if (spawnActionContext.shouldPropagateExecException()) {
          throw e;
        }
      } finally {
        long duration = executor.getClock().currentTimeMillis() - startTime;
        builder.addTestTimes(duration);
        builder.setRunDurationMillis(duration);
        if (streamed != null) {
          streamed.close();
        }
      }

      TestCase details = parseTestResult(
          action.resolve(actionExecutionContext.getExecutor().getExecRoot()).getXmlOutputPath());
      if (details != null) {
        builder.setTestCase(details);
      }
      
      if (isCoverageMode(action)) {
        builder.setHasCoverage(true);
      }

      return builder.build();
    } catch (IOException e) {
      throw new TestExecException(e.getMessage());
    }
  }

  /**
   * Outputs test result to the stdout after test has finished (e.g. for --test_output=all or
   * --test_output=errors). Will also try to group output lines together (up to 10000 lines) so
   * parallel test outputs will not get interleaved.
   */
  protected void processTestOutput(Executor executor, FileOutErr outErr, TestResult result)
      throws IOException {
    Path testOutput = executor.getExecRoot().getRelative(result.getTestLogPath().asFragment());
    boolean isPassed = result.getData().getTestPassed();
    try {
      if (TestLogHelper.shouldOutputTestLog(executionOptions.testOutput, isPassed)) {
        TestLogHelper.writeTestLog(testOutput, result.getTestName(), outErr.getOutputStream());
      }
    } finally {
      if (isPassed) {
        executor.getEventHandler().handle(Event.of(EventKind.PASS, null, result.getTestName()));
      } else {
        if (result.getData().getStatus() == BlazeTestStatus.TIMEOUT) {
          executor.getEventHandler().handle(
              Event.of(EventKind.TIMEOUT, null, result.getTestName()
                  + " (see " + testOutput + ")"));
        } else {
          executor.getEventHandler().handle(
              Event.of(EventKind.FAIL, null, result.getTestName() + " (see " + testOutput + ")"));
        }
      }
    }
  }

  private final void finalizeTest(ActionExecutionContext actionExecutionContext,
      TestRunnerAction action, TestResultData data) throws IOException, ExecException {
    TestResult result = new TestResult(action, data, false);
    postTestResult(actionExecutionContext.getExecutor(), result);

    processTestOutput(actionExecutionContext.getExecutor(),
        actionExecutionContext.getFileOutErr(), result);
    // TODO(bazel-team): handle --test_output=errors, --test_output=all.

    if (!executionOptions.testKeepGoing && data.getStatus() != BlazeTestStatus.PASSED) {
      throw new TestExecException("Test failed: aborting");
    }
  }

  @Override
  public TestResult newCachedTestResult(
      Path execRoot, TestRunnerAction action, TestResultData data) {
    return new TestResult(action, data, /*cached*/ true);
  }
}
