# Floating AI Pet (Android starter)

A starter Android project for a draggable floating digital face. It includes:
- Floating overlay face (Android `SYSTEM_ALERT_WINDOW`)
- Tap-to-cycle expressions and drag-to-move interaction
- Battery percentage reactions
- Charging and full-battery reactions
- Foreground service notification
- GitHub Actions workflow that builds a debug APK

## Build the APK with GitHub Actions

1. Create a new GitHub repository.
2. Extract this ZIP and upload the **contents** of the `FloatingAIPet` folder to the repository root.
3. Push to `main` (or `master`), or run **Actions → Build Android APK → Run workflow**.
4. Open the completed workflow run and download the `floating-ai-pet-debug-apk` artifact.
5. Inside the downloaded artifact, install `app-debug.apk` on your Android phone.

## Build locally

Requirements: Android Studio with JDK 17 and Android SDK Platform 35.

Open this folder in Android Studio, allow Gradle sync, then run the app or use:

```bash
gradle assembleDebug
```

APK output:
`app/build/outputs/apk/debug/app-debug.apk`

## First launch

1. Open the app.
2. Tap **Start floating pet**.
3. Android opens the **Display over other apps** permission page. Allow it.
4. Return to the app and tap **Start floating pet** again.
5. Drag the face to move it; tap it to cycle expressions.
6. Connect or disconnect the charger and change battery level to see battery reactions.

## Battery reactions included

- 100% / full: celebration face and star
- Charging: happy charging face and lightning symbol
- 21–39%: worried face
- 11–20%: worried face with percentage
- 10% or less: sad face with percentage
- Normal battery: neutral face

## Important notes

- This is a functional **starter prototype**, not the finished AI companion. Voice chat, conversational AI, persistent memory, music detection, notification understanding, custom themes and advanced 3D animations are not implemented yet.
- Android requires the user to grant overlay permission manually.
- Android may restrict foreground services and overlay behaviour depending on OS version and phone manufacturer. The app shows an ongoing notification while the pet is active.
- The foreground service uses Android's `specialUse` service type. For a public Play Store release, review the current Google Play foreground-service policy and required declarations.
- This starter does not request notification-listener access or read message contents.
- The floating pet runs on Android 8.0 (API 26) and newer.
