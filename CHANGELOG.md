# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]

### Added

- Gradle [configuration cache](https://docs.gradle.org/current/userguide/configuration_cache.html) support.
- Added new `installDir` property support to customize installation directory.

## [1.5.0] - 2022-05-24

### Changed

- Fixed `listPluginProperties` task.
- Plugin is compatible with JDK 1.8 by using a toolchain.
- Updated Project Workflow and Gradle build.

## [1.4.0] - 2022-05-24

### Changed

- Updated Kotlin to `1.6.21` version.
- Updated Gradle to `7.4.2` version.
- Updated default Python version to a `3.10.4`.

## [1.3.0] - 2021-09-27

### Changed

- Updated Kotlin to `1.5.21` version.
- Updated Gradle to `7.2` version.

## [1.2.3] - 2021-03-20

### Changed

- Updated default Python version to a `3.9.2`.

## [1.2.2] - 2020-10-04

### Added

- Possibility to specify miniconda3 version by `minicondaVersion` property.

### Changed

- Default Python version updated to a `3.8.5`.

## [1.2.1] - 2020-06-29

### Changed

- Refactored and simplified plugin.

### Fixed

- Conda env fix.

## [1.2.0] - 2020-06-28

### Fixed

- Stdinput/output fix.

## [1.1.8 - 1.1.14] - 2020-06-24

### Fixed

- Linux conda activation fix.

## [1.1.7] - 2020-06-24

### Fixed

- Working dir fix.

## [1.1.6] - 2020-06-24

### Added

- Added Miniconda backend.
- Added test in linux docker.

### Removed

- Removed Jet brains envs plugin.

### Changed

- Plugin structure simplified.