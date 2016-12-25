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

package com.google.devtools.build.lib.rules.objc;

import static com.google.devtools.build.lib.rules.objc.ObjcProvider.MULTI_ARCH_LINKED_ARCHIVES;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.devtools.build.lib.actions.Artifact;
import com.google.devtools.build.lib.analysis.ConfiguredTarget;
import com.google.devtools.build.lib.analysis.RuleConfiguredTarget.Mode;
import com.google.devtools.build.lib.analysis.RuleConfiguredTargetBuilder;
import com.google.devtools.build.lib.analysis.RuleContext;
import com.google.devtools.build.lib.analysis.TransitiveInfoCollection;
import com.google.devtools.build.lib.analysis.config.BuildConfiguration;
import com.google.devtools.build.lib.collect.nestedset.NestedSet;
import com.google.devtools.build.lib.collect.nestedset.NestedSetBuilder;
import com.google.devtools.build.lib.rules.RuleConfiguredTargetFactory;
import com.google.devtools.build.lib.rules.apple.AppleConfiguration;
import com.google.devtools.build.lib.rules.apple.Platform.PlatformType;
import com.google.devtools.build.lib.rules.cpp.CcToolchainProvider;
import com.google.devtools.build.lib.rules.objc.ObjcCommon.ResourceAttributes;
import com.google.devtools.build.lib.rules.objc.ObjcProvider.Key;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation for the "apple_static_library" rule.
 */
public class AppleStaticLibrary implements RuleConfiguredTargetFactory {

  /**
   * Set of {@link ObjcProvider} keys whose values are subtracted by avoid_deps. Specifically, a
   * value is propagated if present in the transitive "deps" but *not* present in the transitive
   * "avoid_deps".
   */
  private static final ImmutableSet<Key<?>> AVOID_DEPS_KEYS =
      ImmutableSet.<Key<?>>of(
          ObjcProvider.ASSET_CATALOG,
          ObjcProvider.BUNDLE_FILE,
          ObjcProvider.GENERAL_RESOURCE_DIR,
          ObjcProvider.GENERAL_RESOURCE_FILE,
          ObjcProvider.STORYBOARD,
          ObjcProvider.STRINGS,
          ObjcProvider.XCDATAMODEL,
          ObjcProvider.XIB,
          ObjcProvider.XCASSETS_DIR);

  /**
   * Set of {@link ObjcProvider} whose values are propagated regardless of avoid_deps.
   */
  private static final ImmutableSet<Key<?>> PROPAGATE_REGARDLESS_KEYS =
      ImmutableSet.<Key<?>>of(
          ObjcProvider.SDK_DYLIB,
          ObjcProvider.SDK_FRAMEWORK,
          ObjcProvider.WEAK_SDK_FRAMEWORK);

  @VisibleForTesting
  static final String UNSUPPORTED_PLATFORM_TYPE_ERROR_FORMAT =
      "Unsupported platform type \"%s\"";

  @Override
  public final ConfiguredTarget create(RuleContext ruleContext)
      throws InterruptedException, RuleErrorException {
    PlatformType platformType = MultiArchSplitTransitionProvider.getPlatformType(ruleContext);
    ImmutableListMultimap<BuildConfiguration, TransitiveInfoCollection> configToDepsCollectionMap =
        ruleContext.getPrerequisitesByConfiguration("deps", Mode.SPLIT);
    ImmutableListMultimap<BuildConfiguration, ObjcProvider> configToAvoidDepsMap =
        ruleContext.getPrerequisitesByConfiguration("avoid_deps", Mode.SPLIT, ObjcProvider.class);

    Set<BuildConfiguration> childConfigurations = getChildConfigurations(ruleContext);

    IntermediateArtifacts ruleIntermediateArtifacts =
        ObjcRuleClasses.intermediateArtifacts(ruleContext);

    NestedSetBuilder<Artifact> librariesToLipo =
        NestedSetBuilder.<Artifact>stableOrder();
    NestedSetBuilder<Artifact> filesToBuild =
        NestedSetBuilder.<Artifact>stableOrder()
            .add(ruleIntermediateArtifacts.combinedArchitectureArchive());

    ObjcProvider.Builder objcProviderBuilder = new ObjcProvider.Builder();

    for (BuildConfiguration childConfig : childConfigurations) {
      ProtobufSupport protoSupport =
          new ProtobufSupport(ruleContext, childConfig)
              .registerGenerationActions()
              .registerCompilationActions();

      Optional<ObjcProvider> protosObjcProvider = protoSupport.getObjcProvider();

      IntermediateArtifacts intermediateArtifacts =
          ObjcRuleClasses.intermediateArtifacts(ruleContext, childConfig);

      ObjcCommon common =
          common(
              ruleContext,
              childConfig,
              intermediateArtifacts,
              nullToEmptyList(configToDepsCollectionMap.get(childConfig)),
              protosObjcProvider);

      librariesToLipo.add(intermediateArtifacts.strippedSingleArchitectureLibrary());

      new LegacyCompilationSupport(ruleContext, childConfig)
          .registerCompileAndArchiveActions(common)
          .registerFullyLinkActionWithAvoids(
              common.getObjcProvider(),
              intermediateArtifacts.strippedSingleArchitectureLibrary(),
              configToAvoidDepsMap.get(childConfig))
          .validateAttributes();
      ruleContext.assertNoErrors();

      addTransitiveAndAvoid(objcProviderBuilder, common.getObjcProvider(),
          configToAvoidDepsMap.get(childConfig));
    }

    AppleConfiguration appleConfiguration = ruleContext.getFragment(AppleConfiguration.class);

    new LipoSupport(ruleContext)
        .registerCombineArchitecturesAction(
            librariesToLipo.build(),
            ruleIntermediateArtifacts.combinedArchitectureArchive(),
            appleConfiguration.getMultiArchPlatform(platformType));

    RuleConfiguredTargetBuilder targetBuilder =
        ObjcRuleClasses.ruleConfiguredTarget(ruleContext, filesToBuild.build());

    objcProviderBuilder.add(
        MULTI_ARCH_LINKED_ARCHIVES, ruleIntermediateArtifacts.combinedArchitectureArchive());

    targetBuilder.addProvider(ObjcProvider.class, objcProviderBuilder.build());
    return targetBuilder.build();
  }
  
  private void addTransitiveAndAvoid(ObjcProvider.Builder objcProviderBuilder,
      ObjcProvider provider, ImmutableList<ObjcProvider> avoidProviders) {
    for (Key<?> key : PROPAGATE_REGARDLESS_KEYS) {
      objcProviderBuilder.addTransitiveAndPropagate(key, provider);
    }
    for (Key<?> key : AVOID_DEPS_KEYS) {
      addTransitiveAndAvoid(objcProviderBuilder, key, provider, avoidProviders);
    }
  }

  private <T> void addTransitiveAndAvoid(ObjcProvider.Builder objcProviderBuilder, Key<T> key,
      ObjcProvider provider, ImmutableList<ObjcProvider> avoidProviders) {
    HashSet<T> avoidItemsSet = new HashSet<T>();
    for (ObjcProvider avoidProvider : avoidProviders) {
      avoidItemsSet.addAll(avoidProvider.getPropagable(key).toList());
    }
    NestedSet<T> items = provider.getPropagable(key);

    objcProviderBuilder.addAll(key,
        Iterables.filter(items.toList(), Predicates.not(Predicates.in(avoidItemsSet))));
  }

  private ObjcCommon common(
      RuleContext ruleContext,
      BuildConfiguration buildConfiguration,
      IntermediateArtifacts intermediateArtifacts,
      List<TransitiveInfoCollection> propagatedDeps,
      Optional<ObjcProvider> protosObjcProvider) {

    CompilationArtifacts compilationArtifacts =
        CompilationSupport.compilationArtifacts(ruleContext, intermediateArtifacts);

    return new ObjcCommon.Builder(ruleContext, buildConfiguration)
        .setCompilationAttributes(
            CompilationAttributes.Builder.fromRuleContext(ruleContext).build())
        .setCompilationArtifacts(compilationArtifacts)
        .setResourceAttributes(new ResourceAttributes(ruleContext))
        .addDefines(ruleContext.getTokenizedStringListAttr("defines"))
        .addDeps(propagatedDeps)
        .addDepObjcProviders(
            ruleContext.getPrerequisites("bundles", Mode.TARGET, ObjcProvider.class))
        .addDepObjcProviders(protosObjcProvider.asSet())
        .setIntermediateArtifacts(intermediateArtifacts)
        .setAlwayslink(false)
        .build();
  }

  private <T> List<T> nullToEmptyList(List<T> inputList) {
    return inputList != null ? inputList : ImmutableList.<T>of();
  }

  private Set<BuildConfiguration> getChildConfigurations(RuleContext ruleContext) {
    // This is currently a hack to obtain all child configurations regardless of the attribute
    // values of this rule -- this rule does not currently use the actual info provided by
    // this attribute. b/28403953 tracks cc toolchain usage.
    ImmutableListMultimap<BuildConfiguration, CcToolchainProvider> configToProvider =
        ruleContext.getPrerequisitesByConfiguration(":cc_toolchain", Mode.SPLIT,
            CcToolchainProvider.class);

    return configToProvider.keySet();
  }
}
