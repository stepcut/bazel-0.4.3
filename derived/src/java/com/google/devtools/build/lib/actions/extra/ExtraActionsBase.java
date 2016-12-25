// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: bazel-out/local-fastbuild/bin/src/main/protobuf/libextra_actions_base_java_proto_srcjar.srcjar.preprocessed/extra_actions_base.proto

package com.google.devtools.build.lib.actions.extra;

public final class ExtraActionsBase {
  private ExtraActionsBase() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
    registry.add(com.google.devtools.build.lib.actions.extra.SpawnInfo.spawnInfo);
    registry.add(com.google.devtools.build.lib.actions.extra.CppCompileInfo.cppCompileInfo);
    registry.add(com.google.devtools.build.lib.actions.extra.CppLinkInfo.cppLinkInfo);
    registry.add(com.google.devtools.build.lib.actions.extra.JavaCompileInfo.javaCompileInfo);
    registry.add(com.google.devtools.build.lib.actions.extra.PythonInfo.pythonInfo);
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_blaze_ExtraActionSummary_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_blaze_ExtraActionSummary_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_blaze_DetailedExtraActionInfo_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_blaze_DetailedExtraActionInfo_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_blaze_ExtraActionInfo_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_blaze_ExtraActionInfo_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_blaze_ExtraActionInfo_AspectParametersEntry_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_blaze_ExtraActionInfo_AspectParametersEntry_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_blaze_ExtraActionInfo_StringList_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_blaze_ExtraActionInfo_StringList_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_blaze_EnvironmentVariable_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_blaze_EnvironmentVariable_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_blaze_SpawnInfo_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_blaze_SpawnInfo_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_blaze_CppCompileInfo_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_blaze_CppCompileInfo_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_blaze_CppLinkInfo_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_blaze_CppLinkInfo_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_blaze_JavaCompileInfo_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_blaze_JavaCompileInfo_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_blaze_PythonInfo_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_blaze_PythonInfo_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\204\001bazel-out/local-fastbuild/bin/src/mai" +
      "n/protobuf/libextra_actions_base_java_pr" +
      "oto_srcjar.srcjar.preprocessed/extra_act" +
      "ions_base.proto\022\005blaze\"D\n\022ExtraActionSum" +
      "mary\022.\n\006action\030\001 \003(\0132\036.blaze.DetailedExt" +
      "raActionInfo\"Z\n\027DetailedExtraActionInfo\022" +
      "\027\n\017triggering_file\030\001 \001(\t\022&\n\006action\030\002 \002(\013" +
      "2\026.blaze.ExtraActionInfo\"\240\002\n\017ExtraAction" +
      "Info\022\r\n\005owner\030\001 \001(\t\022\023\n\013aspect_name\030\006 \001(\t" +
      "\022G\n\021aspect_parameters\030\007 \003(\0132,.blaze.Extr",
      "aActionInfo.AspectParametersEntry\022\n\n\002id\030" +
      "\002 \001(\t\022\020\n\010mnemonic\030\005 \001(\t\032Z\n\025AspectParamet" +
      "ersEntry\022\013\n\003key\030\001 \001(\t\0220\n\005value\030\002 \001(\0132!.b" +
      "laze.ExtraActionInfo.StringList:\0028\001\032\033\n\nS" +
      "tringList\022\r\n\005value\030\001 \003(\t*\t\010\350\007\020\200\200\200\200\002\"2\n\023E" +
      "nvironmentVariable\022\014\n\004name\030\001 \002(\t\022\r\n\005valu" +
      "e\030\002 \002(\t\"\263\001\n\tSpawnInfo\022\020\n\010argument\030\001 \003(\t\022" +
      ",\n\010variable\030\002 \003(\0132\032.blaze.EnvironmentVar" +
      "iable\022\022\n\ninput_file\030\004 \003(\t\022\023\n\013output_file" +
      "\030\005 \003(\t2=\n\nspawn_info\022\026.blaze.ExtraAction",
      "Info\030\353\007 \001(\0132\020.blaze.SpawnInfo\"\310\001\n\016CppCom" +
      "pileInfo\022\014\n\004tool\030\001 \001(\t\022\027\n\017compiler_optio" +
      "n\030\002 \003(\t\022\023\n\013source_file\030\003 \001(\t\022\023\n\013output_f" +
      "ile\030\004 \001(\t\022\033\n\023sources_and_headers\030\005 \003(\t2H" +
      "\n\020cpp_compile_info\022\026.blaze.ExtraActionIn" +
      "fo\030\351\007 \001(\0132\025.blaze.CppCompileInfo\"\226\002\n\013Cpp" +
      "LinkInfo\022\022\n\ninput_file\030\001 \003(\t\022\023\n\013output_f" +
      "ile\030\002 \001(\t\022\035\n\025interface_output_file\030\003 \001(\t" +
      "\022\030\n\020link_target_type\030\004 \001(\t\022\027\n\017link_stati" +
      "cness\030\005 \001(\t\022\022\n\nlink_stamp\030\006 \003(\t\022\"\n\032build",
      "_info_header_artifact\030\007 \003(\t\022\020\n\010link_opt\030" +
      "\010 \003(\t2B\n\rcpp_link_info\022\026.blaze.ExtraActi" +
      "onInfo\030\352\007 \001(\0132\022.blaze.CppLinkInfo\"\200\002\n\017Ja" +
      "vaCompileInfo\022\021\n\toutputjar\030\001 \001(\t\022\021\n\tclas" +
      "spath\030\002 \003(\t\022\022\n\nsourcepath\030\003 \003(\t\022\023\n\013sourc" +
      "e_file\030\004 \003(\t\022\021\n\tjavac_opt\030\005 \003(\t\022\021\n\tproce" +
      "ssor\030\006 \003(\t\022\025\n\rprocessorpath\030\007 \003(\t\022\025\n\rboo" +
      "tclasspath\030\010 \003(\t2J\n\021java_compile_info\022\026." +
      "blaze.ExtraActionInfo\030\350\007 \001(\0132\026.blaze.Jav" +
      "aCompileInfo\"t\n\nPythonInfo\022\023\n\013source_fil",
      "e\030\001 \003(\t\022\020\n\010dep_file\030\002 \003(\t2?\n\013python_info" +
      "\022\026.blaze.ExtraActionInfo\030\355\007 \001(\0132\021.blaze." +
      "PythonInfoB/\n+com.google.devtools.build." +
      "lib.actions.extraP\001"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_blaze_ExtraActionSummary_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_blaze_ExtraActionSummary_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_blaze_ExtraActionSummary_descriptor,
        new java.lang.String[] { "Action", });
    internal_static_blaze_DetailedExtraActionInfo_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_blaze_DetailedExtraActionInfo_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_blaze_DetailedExtraActionInfo_descriptor,
        new java.lang.String[] { "TriggeringFile", "Action", });
    internal_static_blaze_ExtraActionInfo_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_blaze_ExtraActionInfo_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_blaze_ExtraActionInfo_descriptor,
        new java.lang.String[] { "Owner", "AspectName", "AspectParameters", "Id", "Mnemonic", });
    internal_static_blaze_ExtraActionInfo_AspectParametersEntry_descriptor =
      internal_static_blaze_ExtraActionInfo_descriptor.getNestedTypes().get(0);
    internal_static_blaze_ExtraActionInfo_AspectParametersEntry_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_blaze_ExtraActionInfo_AspectParametersEntry_descriptor,
        new java.lang.String[] { "Key", "Value", });
    internal_static_blaze_ExtraActionInfo_StringList_descriptor =
      internal_static_blaze_ExtraActionInfo_descriptor.getNestedTypes().get(1);
    internal_static_blaze_ExtraActionInfo_StringList_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_blaze_ExtraActionInfo_StringList_descriptor,
        new java.lang.String[] { "Value", });
    internal_static_blaze_EnvironmentVariable_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_blaze_EnvironmentVariable_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_blaze_EnvironmentVariable_descriptor,
        new java.lang.String[] { "Name", "Value", });
    internal_static_blaze_SpawnInfo_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_blaze_SpawnInfo_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_blaze_SpawnInfo_descriptor,
        new java.lang.String[] { "Argument", "Variable", "InputFile", "OutputFile", });
    internal_static_blaze_CppCompileInfo_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_blaze_CppCompileInfo_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_blaze_CppCompileInfo_descriptor,
        new java.lang.String[] { "Tool", "CompilerOption", "SourceFile", "OutputFile", "SourcesAndHeaders", });
    internal_static_blaze_CppLinkInfo_descriptor =
      getDescriptor().getMessageTypes().get(6);
    internal_static_blaze_CppLinkInfo_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_blaze_CppLinkInfo_descriptor,
        new java.lang.String[] { "InputFile", "OutputFile", "InterfaceOutputFile", "LinkTargetType", "LinkStaticness", "LinkStamp", "BuildInfoHeaderArtifact", "LinkOpt", });
    internal_static_blaze_JavaCompileInfo_descriptor =
      getDescriptor().getMessageTypes().get(7);
    internal_static_blaze_JavaCompileInfo_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_blaze_JavaCompileInfo_descriptor,
        new java.lang.String[] { "Outputjar", "Classpath", "Sourcepath", "SourceFile", "JavacOpt", "Processor", "Processorpath", "Bootclasspath", });
    internal_static_blaze_PythonInfo_descriptor =
      getDescriptor().getMessageTypes().get(8);
    internal_static_blaze_PythonInfo_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_blaze_PythonInfo_descriptor,
        new java.lang.String[] { "SourceFile", "DepFile", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
