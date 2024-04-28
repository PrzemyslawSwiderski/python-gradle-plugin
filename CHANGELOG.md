# Changelog

All notable changes to this project will be documented in this file.

## Unreleased

### Changed

- Updated Commons IO dependency.

## 2.6.2 - 2024-04-27

### Added

- Filtering by `PYTHON` SDK type.

### Changed

- Yaml implementation lib to SnakeYAML for the better backward-compatibility.

## 2.6.1 - 2024-04-25

### Added

- Properly mapping submodules in the SDK Import config.

## 2.6.0 - 2024-04-20

### Added

- [SDK-Import Intellij Plugin](https://github.com/PrzemyslawSwiderski/sdk-import-plugin) support.
- Versioning applied from: `gradle/libs.versions.toml` file.
- The `ideaDir` Plugin property.

### Changed

- Updated Changelog plugin.
- Updated Kotlin to `1.9.23`.

## 2.5.0 - 2024-03-20

### Changed

- Updated Gradle to `8.5` and Java dependencies to the latest ones.
- Changed std out for the `VenvTask` tasks from System out to Gradle's `LIFECYCLE` logging stream.

## 2.4.0 - 2023-08-23

### Added

- Support for the latest Miniconda versions.

### Changed

- `latest` default Miniconda version to `py311_23.5.2-0` in order to avoid future incompatibility issues.
- Updated default Python version to `3.10.12`.
- Installers will be stored in the parent `python` directory.

## 2.3.0 - 2022-11-19

### Added

- Support for M1/M2 on MacOS.

## 2.2.0 - 2022-08-30

### Added

- Logging `Miniconda`/`Anaconda` installer download progress.
- Support for at least Gradle `6.2`.

## 2.1.0 - 2022-07-16

### Changed

- Updated Gradle to `7.5` and Kotlin to `1.7.10` versions.
- Bumped `plugin-publish` to `1.0.0`. Now it includes `java-gradle-plugin` and `maven-publish` out of the box.
- Cleaned up `build.gradle.kts` a little.

## 2.0.0 - 2022-06-18

### Added

- `Anaconda` Conda installer support by introducing a new `condaInstaller` property which can specify Conda installer to
  be downloaded.
- `systemArch` property so that target operating system architecture can be overridden.
- `sample-anaconda-project` sample project [here](./examples/sample-anaconda-project)

### Changed

- Renamed `minicondaVersion`, `minicondaRepoUrl`, `minicondaRepoUsername`, `minicondaRepoPassword`
  and `minicondaRepoHeaders` parameters
  to `condaVersion`, `condaRepoUrl`, `condaRepoUsername`, `condaRepoPassword` and `condaRepoHeaders`

## 1.7.0 - 2022-06-17

### Added

- Gradle [configuration cache](https://docs.gradle.org/current/userguide/configuration_cache.html) support.
- Added new `installDir` property support to customize installation directory.
- Replaced `standardInput` and `standardOutput` of `VenvTask` fields with optional `inputFile` and `outputFile` so that
  it
  is supported by configuration cache and is Gradle recommended approach.

## 1.6.0 - 2022-06-14

### Added

- Added new `minicondaRepoUrl`, `minicondaRepoUsername`, `minicondaRepoPassword` and `minicondaRepoHeaders` parameters
  in the `pythonPlugin` extension.

## 1.5.0 - 2022-05-24

### Changed

- Fixed `listPluginProperties` task.
- Plugin is compatible with JDK 1.8 by using a toolchain.
- Updated Project Workflow and Gradle build.

## 1.4.0 - 2022-05-24

### Changed

- Updated Kotlin to `1.6.21` version.
- Updated Gradle to `7.4.2` version.
- Updated default Python version to a `3.10.4`.

## 1.3.0 - 2021-09-27

### Changed

- Updated Kotlin to `1.5.21` version.
- Updated Gradle to `7.2` version.

## 1.2.3 - 2021-03-20

### Changed

- Updated default Python version to a `3.9.2`.

## 1.2.2 - 2020-10-04

### Added

- Possibility to specify miniconda3 version by `minicondaVersion` property.

### Changed

- Default Python version updated to a `3.8.5`.

## 1.2.1 - 2020-06-29

### Changed

- Refactored and simplified plugin.

### Fixed

- Conda env fix.

## 1.2.0 - 2020-06-28

### Fixed

- Stdinput/output fix.

## [1.1.8 - 1.1.14] - 2020-06-24

### Fixed

- Linux conda activation fix.

## 1.1.7 - 2020-06-24

### Fixed

- Working dir fix.

## 1.1.6 - 2020-06-24

### Added

- Added Miniconda backend.
- Added test in linux docker.

### Removed

- Removed Jet brains envs plugin.

### Changed

- Plugin structure simplified.
