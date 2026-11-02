# Petex

This repository currently contains an **early Android interface preview**, not a functioning assistant. The room has a presence strip, a static orb, an inactive intent bar, and an on-demand device snapshot showing current battery and storage readings. In a debug build, Orb States opens a study of six standard states and Alarmed. There is no live microphone, speech, messaging, animation, or autonomous action behind the interface. The snapshot is a real reading, not a battery-health test, storage forecast, or background monitor.

The experience specification was provided in the task, but `PETEX_BUILD.md`, `docs/MESSAGING_CONTROL.md`, `docs/PETEX_EXPERIENCE_SPEC.md`, and `AGENTS.md` were not present in the initial checkout. Phase 0 messaging is a priority and cannot be implemented or validated without its missing specification. See [experience phases](docs/EXPERIENCE_PHASES.md).

## Build and check

Requires JDK 17, Android SDK Platform 35, and access to the Gradle and Android/Google Maven repositories. Set `ANDROID_HOME` or provide `sdk.dir` in an untracked `local.properties`.

```sh
./gradlew :app:assembleDebug :app:testDebugUnitTest
python3 -m unittest discover -s tests -v
```

Install `app/build/outputs/apk/debug/app-debug.apk` on an Android device or emulator. Tap Orb States, then use Previous/Next to inspect all seven visual states. Before E1 can pass, capture real screenshots or a recording and confirm rendering and colours on device. The Python checks are source-level guardrails only; they do not prove the Android app compiles or renders.

The `Android E1 validation` workflow runs the JVM tests and, on an Android emulator, cycles the debug selector in both directions and captures the room, device snapshot and all seven states. If the workflow runs successfully, download its `e1-device-screenshots` artifact for manual review; merely adding the workflow is not proof of a successful build or visual acceptance. To run the device test locally, start an emulator and run `./gradlew :app:connectedDebugAndroidTest`; screenshots are saved on the device under `Pictures/PetexE1/` and can be retrieved with `adb pull /sdcard/Pictures/PetexE1/`.

## Install the debug visual study

On an Android 8.0 (API 26) or newer phone, download the `petex-debug-apk` artifact from a successful **Android E1 validation** run in GitHub Actions (you may need to sign in to GitHub). Extract the ZIP and open `app-debug.apk` on the phone. Android may ask you to allow installs from the browser or file manager; allow it only if you trust the APK, then disable that permission again after installing. This is a debug build for testing, not the full Petex assistant.
