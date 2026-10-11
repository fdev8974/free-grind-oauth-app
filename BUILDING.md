# Building

First read [BUILDING.md in Open Grind repository](https://git.opengrind.org/open-grind/open-grind/src/branch/main/BUILDING.md) for basics.

- `arm64-v8a` is any modern phone
- `armeabi-v7a` is old 32-bit devices
- `x86_64` is emulators/ChromeOS
- Debug builds only `arm64-v8a`

## Prerequisites

Clone repository:

```bash
git clone --recurse-submodules https://github.com/fdev8974/free-grind-oauth-app.git
```

## Build with Docker (easiest, Linux x86_64 only)

```bash
docker compose build
docker compose run --rm build
# -> app/build/outputs/apk/release/app-<abi>-release-unsigned.apk  (one per ABI)
```

### Clean up Docker

```bash
docker compose down -v
```

## Build with Nix (builds everywhere)

```bash
git submodule update --init
nix run .#build-android
# -> app/build/outputs/apk/release/app-<abi>-release-unsigned.apk  (one per ABI)
```

## Build manually (advanced)

Prerequisites:

- JDK 17
- Android SDK: platform 36 and build-tools 36.0.0

`./gradlew` pins Gradle 9.4.1, so you don't need Gradle installed.

```bash
./gradlew :app:assembleRelease
# -> app/build/outputs/apk/release/app-<abi>-release-unsigned.apk  (one per ABI)
```

## Signing

Free Grind and this app trust each other only when both are signed with the same key:

- This app answers only Free Grind builds whose certificate SHA-256 is listed in `SIGNING_CERTS_SHA256` in [FreeGrindTrust.kt](./app/src/main/java/dev/estopia/free_grind/google_oauth/FreeGrindTrust.kt): the GitHub Actions release key and the local `src-tauri/certs/free-grind-dev.jks`.
- Free Grind accepts tokens only from a `dev.estopia.free_grind.google_oauth` install whose signature matches its own.

So sign this app with the keystore that signed the Free Grind build you use. Copy [contrib/keystore.properties.example](./contrib/keystore.properties.example) and point it at that keystore:

```bash
GRINDR_OAUTH_KEYSTORE_PROPERTIES=/home/you/.config/free-grind/keystore.properties \
  nix run .#build-android
```

`contrib/sign-release.sh <tag>` checks that every APK is signed with the release certificate.

### GitHub Actions

Every push to `main` runs [release-main.yml](./.github/workflows/release-main.yml): it builds the three ABIs, signs them, checks the certificate with `contrib/sign-release.sh` and publishes a release tagged `main-<sha>`. The repository needs these secrets:

- `ANDROID_KEYSTORE_BASE64`: the same value as in the free-grind repository
- `ANDROID_KEYSTORE_PASSWORD` (optional): only needed if the keystore password is not `opengrind`, the one Free Grind's `app/build.gradle.kts` uses

## Verifying a release

```bash
apksigner verify --print-certs free-grind-google-oauth-<tag>-<abi>.apk
```

The certificate SHA-256 must be `7ce9e5cdac620ec798bea6749aed4c6d59bf12ab5e1dc8cb59c117bb929fc9d0`.

## Reproducibility

Follow [Open Grind's REPRODUCIBILITY.md](https://git.opengrind.org/open-grind/open-grind/src/branch/main/REPRODUCIBILITY.md).

| Component                                    | Pinned in                                  |
| -------------------------------------------- | ------------------------------------------ |
| Gradle distribution (+ SHA-256)              | `gradle/wrapper/gradle-wrapper.properties` |
| Android Gradle Plugin, GeckoView, AndroidX   | `gradle/libs.versions.toml`                |
| compileSdk / minSdk / targetSdk, build-tools | `app/build.gradle.kts`                     |
| JDK + Android SDK (Nix build)                | `flake.nix`                                |
| nixpkgs revision                             | `flake.lock`                               |
| Bundled web extension                        | submodule commit (`.gitmodules` + gitlink) |
