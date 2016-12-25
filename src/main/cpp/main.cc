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

#include <memory>

#include "src/main/cpp/blaze.h"
#include "src/main/cpp/option_processor.h"
#include "src/main/cpp/startup_options.h"

int main(int argc, const char *argv[]) {
  std::unique_ptr<blaze::StartupOptions> startup_options(
      new blaze::StartupOptions());
  return blaze::Main(argc, argv,
                     new blaze::OptionProcessor(std::move(startup_options)),
                     nullptr /* TODO(b/32939567): Enable Bazel logging. */);
}
