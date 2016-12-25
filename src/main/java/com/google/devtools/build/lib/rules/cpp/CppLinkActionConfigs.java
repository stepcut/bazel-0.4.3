// Copyright 2014 The Bazel Authors. All rights reserved.
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
package com.google.devtools.build.lib.rules.cpp;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import java.util.Set;

/**
 * A helper class for creating action_configs for the c++ link action.
 *
 * <p>TODO(b/30109612): Replace this with action_configs in the crosstool instead of putting it in
 * legacy features.
 */
public class CppLinkActionConfigs {

  /** A platform for linker invocations. */
  public static enum CppLinkPlatform {
    LINUX,
    MAC
  }

  public static String getCppLinkActionConfigs(
      CppLinkPlatform platform, Set<String> features, String cppLinkDynamicLibraryToolPath) {
    String cppDynamicLibraryLinkerTool = "";
    if (!features.contains("dynamic_library_linker_tool")) {
      cppDynamicLibraryLinkerTool =
          ""
              + "feature {"
              + "   name: 'dynamic_library_linker_tool'"
              + "   flag_set {"
              + "       action: 'c++-link-dynamic-library'"
              + "       flag_group {"
              + "           flag: '"
              + cppLinkDynamicLibraryToolPath
              + "'"
              + "       }"
              + "   }"
              + "}";
    }

    return Joiner.on("\n")
        .join(
            ImmutableList.of(
                "action_config {",
                "   config_name: 'c++-link-executable'",
                "   action_name: 'c++-link-executable'",
                "   tool {",
                "       tool_path: 'DUMMY_TOOL'",
                "   }",
                "   implies: 'symbol_counts'",
                "   implies: 'linkstamps'",
                "   implies: 'output_execpath_flags_executable'",
                "   implies: 'global_whole_archive_open'",
                "   implies: 'runtime_root_flags'",
                "   implies: 'input_param_flags'",
                "   implies: 'global_whole_archive_close'",
                "   implies: 'force_pic_flags'",
                "}",
                "action_config {",
                "   config_name: 'c++-link-dynamic-library'",
                "   action_name: 'c++-link-dynamic-library'",
                "   tool {",
                "       tool_path: 'DUMMY_TOOL'",
                "   }",
                "   implies: 'build_interface_libraries'",
                "   implies: 'dynamic_library_linker_tool'",
                "   implies: 'symbol_counts'",
                "   implies: 'shared_flag'",
                "   implies: 'linkstamps'",
                "   implies: 'output_execpath_flags'",
                "   implies: 'global_whole_archive_open'",
                "   implies: 'runtime_root_flags'",
                "   implies: 'input_param_flags'",
                "   implies: 'global_whole_archive_close'",
                "}",
                "action_config {",
                "   config_name: 'c++-link-static-library'",
                "   action_name: 'c++-link-static-library'",
                "   tool {",
                "       tool_path: 'DUMMY_TOOL'",
                "   }",
                "   implies: 'global_whole_archive_open'",
                "   implies: 'runtime_root_flags'",
                "   implies: 'input_param_flags'",
                "   implies: 'global_whole_archive_close'",
                "}",
                "action_config {",
                "   config_name: 'c++-link-alwayslink-static-library'",
                "   action_name: 'c++-link-alwayslink-static-library'",
                "   tool {",
                "       tool_path: 'DUMMY_TOOL'",
                "   }",
                "   implies: 'global_whole_archive_open'",
                "   implies: 'runtime_root_flags'",
                "   implies: 'input_param_flags'",
                "   implies: 'global_whole_archive_close'",
                "}",
                "action_config {",
                "   config_name: 'c++-link-pic-static-library'",
                "   action_name: 'c++-link-pic-static-library'",
                "   tool {",
                "       tool_path: 'DUMMY_TOOL'",
                "   }",
                "   implies: 'global_whole_archive_open'",
                "   implies: 'runtime_root_flags'",
                "   implies: 'input_param_flags'",
                "   implies: 'global_whole_archive_close'",
                "}",
                "action_config {",
                "   config_name: 'c++-link-alwayslink-pic-static-library'",
                "   action_name: 'c++-link-alwayslink-pic-static-library'",
                "   tool {",
                "       tool_path: 'DUMMY_TOOL'",
                "   }",
                "   implies: 'global_whole_archive_open'",
                "   implies: 'runtime_root_flags'",
                "   implies: 'input_param_flags'",
                "   implies: 'global_whole_archive_close'",
                "}",
                "feature {",
                "   name: 'build_interface_libraries'",
                "   flag_set {",
                "       expand_if_all_available: 'generate_interface_library'",
                "       action: 'c++-link-dynamic-library'",
                "       flag_group {",
                "           flag: '%{generate_interface_library}'",
                "           flag: '%{interface_library_builder_path}'",
                "           flag: '%{interface_library_input_path}'",
                "           flag: '%{interface_library_output_path}'",
                "       }",
                "   }",
                "}",
                // Order of feature declaration matters, cppDynamicLibraryLinkerTool has to follow
                // right after build_interface_libraries.
                cppDynamicLibraryLinkerTool,
                "feature {",
                "   name: 'symbol_counts'",
                "   flag_set {",
                "       expand_if_all_available: 'symbol_counts_output'",
                "       action: 'c++-link-executable'",
                "       action: 'c++-link-dynamic-library'",
                "       flag_group {",
                "           flag: '-Wl,--print-symbol-counts=%{symbol_counts_output}'",
                "       }",
                "   }",
                "}",
                "feature {",
                "   name: 'shared_flag'",
                "   flag_set {",
                "       action: 'c++-link-dynamic-library'",
                "       flag_group {",
                "           flag: '-shared'",
                "       }",
                "   }",
                "}",
                "feature {",
                "   name: 'linkstamps'",
                "   flag_set {",
                "       action: 'c++-link-executable'",
                "       action: 'c++-link-dynamic-library'",
                "       expand_if_all_available: 'linkstamp_paths'",
                "       flag_group {",
                "           flag: '%{linkstamp_paths}'",
                "       }",
                "   }",
                "}",
                "feature {",
                "   name: 'output_execpath_flags'",
                "   flag_set {",
                "       expand_if_all_available: 'output_execpath'",
                "       action: 'c++-link-dynamic-library'",
                "       flag_group {",
                "           flag: '-o'",
                "           flag: '%{output_execpath}'",
                "       }",
                "   }",
                "}",
                "feature {",
                "   name: 'output_execpath_flags_executable'",
                "   flag_set {",
                "      expand_if_all_available: 'output_execpath'",
                "      action: 'c++-link-executable'",
                "      flag_group {",
                "         flag: '-o'",
                "      }",
                "   }",
                "   flag_set {",
                "      expand_if_all_available: 'skip_mostly_static'",
                "      expand_if_all_available: 'output_execpath'",
                "      action: 'c++-link-executable'",
                "      flag_group {",
                "         flag: '/dev/null'",
                "         flag: '-MMD'",
                "         flag: '-MF'",
                "      }",
                "   }",
                "   flag_set {",
                "      expand_if_all_available: 'output_execpath'",
                "      action: 'c++-link-executable'",
                "      flag_group {",
                "         flag: '%{output_execpath}'",
                "      }",
                "   }",
                "}",
                "feature {",
                "   name: 'global_whole_archive_open'",
                "   flag_set {",
                "      expand_if_all_available: 'global_whole_archive'",
                "      action: 'c++-link-executable'",
                "      action: 'c++-link-dynamic-library'",
                "      action: 'c++-link-static-library'",
                "      action: 'c++-link-alwayslink-static-library'",
                "      action: 'c++-link-pic-static-library'",
                "      action: 'c++-link-alwayslink-pic-static-library'",
                "      flag_group {",
                // TODO: Factor platform difference into respective linux and OSX crosstools.
                String.format(
                    "          flag:'%s'",
                    platform == CppLinkPlatform.LINUX ? "-Wl,-whole-archive" : "-Wl,-all_load"),
                "      }",
                "   }",
                "}",
                "feature {",
                "   name: 'runtime_root_flags',",
                "   flag_set {",
                "       expand_if_all_available: 'runtime_root_flags'",
                "       action: 'c++-link-executable'",
                "       action: 'c++-link-dynamic-library'",
                "       action: 'c++-link-static-library'",
                "       action: 'c++-link-alwayslink-static-library'",
                "       action: 'c++-link-pic-static-library'",
                "       action: 'c++-link-alwayslink-pic-static-library'",
                "       flag_group {",
                "           flag: '%{runtime_root_flags}'",
                "       }",
                "   }",
                "   flag_set {",
                "       expand_if_all_available: 'runtime_root_entries'",
                "       action: 'c++-link-executable'",
                "       action: 'c++-link-dynamic-library'",
                "       action: 'c++-link-static-library'",
                "       action: 'c++-link-alwayslink-static-library'",
                "       action: 'c++-link-pic-static-library'",
                "       action: 'c++-link-alwayslink-pic-static-library'",
                "       flag_group {",
                "           flag: '%{runtime_root_entries}'",
                "       }",
                "   }",
                "}",
                "feature {",
                "   name: 'input_param_flags'",
                "   flag_set {",
                "       expand_if_all_available: 'libopts'",
                "       action: 'c++-link-executable'",
                "       action: 'c++-link-dynamic-library'",
                "       action: 'c++-link-static-library'",
                "       action: 'c++-link-alwayslink-static-library'",
                "       action: 'c++-link-pic-static-library'",
                "       action: 'c++-link-alwayslink-pic-static-library'",
                "       flag_group {",
                "           flag: '%{libopts}'",
                "       }",
                "   }",
                "   flag_set {",
                "       expand_if_all_available: 'whole_archive_linker_params'",
                "       action: 'c++-link-executable'",
                "       action: 'c++-link-dynamic-library'",
                "       action: 'c++-link-static-library'",
                "       action: 'c++-link-alwayslink-static-library'",
                "       action: 'c++-link-pic-static-library'",
                "       action: 'c++-link-alwayslink-pic-static-library'",
                "       flag_group {",
                platform == CppLinkPlatform.LINUX
                    ? "          flag: '-Wl,-whole-archive'\n"
                        + "          flag: '%{whole_archive_linker_params}'\n"
                        + "          flag: '-Wl,-no-whole-archive'"
                    : "          flag: '-Wl,-force_load,%{whole_archive_linker_params}'",
                "       }",
                "   }",
                "   flag_set {",
                "       expand_if_all_available: 'linker_input_params'",
                "       action: 'c++-link-executable'",
                "       action: 'c++-link-dynamic-library'",
                "       action: 'c++-link-static-library'",
                "       action: 'c++-link-alwayslink-static-library'",
                "       action: 'c++-link-pic-static-library'",
                "       action: 'c++-link-alwayslink-pic-static-library'",
                "       flag_group {",
                "           flag: '%{linker_input_params}'",
                "       }",
                "   }",
                "}",
                "feature {",
                "   name: 'global_whole_archive_close'",
                "   flag_set {",
                "       expand_if_all_available: 'global_whole_archive'",
                "       action: 'c++-link-executable'",
                "       action: 'c++-link-dynamic-library'",
                "       action: 'c++-link-static-library'",
                "       action: 'c++-link-alwayslink-static-library'",
                "       action: 'c++-link-pic-static-library'",
                "       action: 'c++-link-alwayslink-pic-static-library'",
                "       flag_group {",
                // TODO: Factor platform difference into respective linux and OSX crosstools.
                String.format(
                    "           flag: '%s'",
                    platform == CppLinkPlatform.LINUX ? "-Wl,-no-whole-archive" : ""),
                "       }",
                "   }",
                "}",
                "feature {",
                "   name: 'force_pic_flags'",
                "   flag_set {",
                "       expand_if_all_available: 'force_pic'",
                "       action: 'c++-link-executable'",
                "       flag_group {",
                "           flag: '-pie'",
                "       }",
                "   }",
                "}"));
  }
}
