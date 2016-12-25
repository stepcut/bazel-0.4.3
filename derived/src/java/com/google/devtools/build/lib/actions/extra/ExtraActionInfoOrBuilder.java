// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: bazel-out/local-fastbuild/bin/src/main/protobuf/libextra_actions_base_java_proto_srcjar.srcjar.preprocessed/extra_actions_base.proto

package com.google.devtools.build.lib.actions.extra;

public interface ExtraActionInfoOrBuilder extends
    // @@protoc_insertion_point(interface_extends:blaze.ExtraActionInfo)
    com.google.protobuf.GeneratedMessageV3.
        ExtendableMessageOrBuilder<ExtraActionInfo> {

  /**
   * <pre>
   * The label of the ActionOwner of the shadowed action.
   * </pre>
   *
   * <code>optional string owner = 1;</code>
   */
  boolean hasOwner();
  /**
   * <pre>
   * The label of the ActionOwner of the shadowed action.
   * </pre>
   *
   * <code>optional string owner = 1;</code>
   */
  java.lang.String getOwner();
  /**
   * <pre>
   * The label of the ActionOwner of the shadowed action.
   * </pre>
   *
   * <code>optional string owner = 1;</code>
   */
  com.google.protobuf.ByteString
      getOwnerBytes();

  /**
   * <pre>
   * Only set if the owner is an Aspect.
   * Corresponds to AspectValue.AspectKey.getAspectClass.getName()
   * </pre>
   *
   * <code>optional string aspect_name = 6;</code>
   */
  boolean hasAspectName();
  /**
   * <pre>
   * Only set if the owner is an Aspect.
   * Corresponds to AspectValue.AspectKey.getAspectClass.getName()
   * </pre>
   *
   * <code>optional string aspect_name = 6;</code>
   */
  java.lang.String getAspectName();
  /**
   * <pre>
   * Only set if the owner is an Aspect.
   * Corresponds to AspectValue.AspectKey.getAspectClass.getName()
   * </pre>
   *
   * <code>optional string aspect_name = 6;</code>
   */
  com.google.protobuf.ByteString
      getAspectNameBytes();

  /**
   * <pre>
   * Only set if the owner is an Aspect.
   * Corresponds to AspectValue.AspectKey.getParameters()
   * </pre>
   *
   * <code>map&lt;string, .blaze.ExtraActionInfo.StringList&gt; aspect_parameters = 7;</code>
   */
  int getAspectParametersCount();
  /**
   * <pre>
   * Only set if the owner is an Aspect.
   * Corresponds to AspectValue.AspectKey.getParameters()
   * </pre>
   *
   * <code>map&lt;string, .blaze.ExtraActionInfo.StringList&gt; aspect_parameters = 7;</code>
   */
  boolean containsAspectParameters(
      java.lang.String key);
  /**
   * Use {@link #getAspectParametersMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, com.google.devtools.build.lib.actions.extra.ExtraActionInfo.StringList>
  getAspectParameters();
  /**
   * <pre>
   * Only set if the owner is an Aspect.
   * Corresponds to AspectValue.AspectKey.getParameters()
   * </pre>
   *
   * <code>map&lt;string, .blaze.ExtraActionInfo.StringList&gt; aspect_parameters = 7;</code>
   */
  java.util.Map<java.lang.String, com.google.devtools.build.lib.actions.extra.ExtraActionInfo.StringList>
  getAspectParametersMap();
  /**
   * <pre>
   * Only set if the owner is an Aspect.
   * Corresponds to AspectValue.AspectKey.getParameters()
   * </pre>
   *
   * <code>map&lt;string, .blaze.ExtraActionInfo.StringList&gt; aspect_parameters = 7;</code>
   */

  com.google.devtools.build.lib.actions.extra.ExtraActionInfo.StringList getAspectParametersOrDefault(
      java.lang.String key,
      com.google.devtools.build.lib.actions.extra.ExtraActionInfo.StringList defaultValue);
  /**
   * <pre>
   * Only set if the owner is an Aspect.
   * Corresponds to AspectValue.AspectKey.getParameters()
   * </pre>
   *
   * <code>map&lt;string, .blaze.ExtraActionInfo.StringList&gt; aspect_parameters = 7;</code>
   */

  com.google.devtools.build.lib.actions.extra.ExtraActionInfo.StringList getAspectParametersOrThrow(
      java.lang.String key);

  /**
   * <pre>
   * An id uniquely describing the shadowed action at the ActionOwner level.
   * </pre>
   *
   * <code>optional string id = 2;</code>
   */
  boolean hasId();
  /**
   * <pre>
   * An id uniquely describing the shadowed action at the ActionOwner level.
   * </pre>
   *
   * <code>optional string id = 2;</code>
   */
  java.lang.String getId();
  /**
   * <pre>
   * An id uniquely describing the shadowed action at the ActionOwner level.
   * </pre>
   *
   * <code>optional string id = 2;</code>
   */
  com.google.protobuf.ByteString
      getIdBytes();

  /**
   * <pre>
   * The mnemonic of the shadowed action. Used to distinguish actions with the
   * same ActionType.
   * </pre>
   *
   * <code>optional string mnemonic = 5;</code>
   */
  boolean hasMnemonic();
  /**
   * <pre>
   * The mnemonic of the shadowed action. Used to distinguish actions with the
   * same ActionType.
   * </pre>
   *
   * <code>optional string mnemonic = 5;</code>
   */
  java.lang.String getMnemonic();
  /**
   * <pre>
   * The mnemonic of the shadowed action. Used to distinguish actions with the
   * same ActionType.
   * </pre>
   *
   * <code>optional string mnemonic = 5;</code>
   */
  com.google.protobuf.ByteString
      getMnemonicBytes();
}
