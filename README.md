# Random Keys (Fabric 26.3)

## Commands
- `/randomkeys shuffle`: shuffle all key bindings.
- `/randomkeys reset`: restore the key bindings captured before the first shuffle.

## Build
Requires Java 25 and Gradle 9.6. Run `gradle build`.
The output JAR is in `build/libs/`.

A GitHub Actions workflow builds the project on every push to `main`.
