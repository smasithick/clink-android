# Clink — Clean Share Links in One Tap

![GitHub release](https://img.shields.io/github/v/release/your-username/your-repo?include_prereleases)

[![Donate with PayPal](https://img.shields.io/badge/Donate_with-PayPal-00457C?style=for-the-badge&logo=paypal&logoColor=white)](https://paypal.me/smasithick)  
[![Donate with Buy Me a Coffee](https://img.shields.io/badge/Donate_with-Buy_Me_a_Coffee-FFDD00?style=for-the-badge&logo=buymeacoffee&logoColor=black)](https://buymeacoffee.com/smasithick)

Clink is a lightweight Android app that sanitizes shared links before they hit your clipboard.
When you tap Share on a URL from a browser, social app, or message thread, Clink strips tracking parameters and cleans the link so you can paste the clean destination without marketing noise.

This app was created after a tweet went viral on X saying that tracking query parameters were being added to links, which inspired me to build Clink.

Example tweet link:

https://x.com/smasithick/status/1754561043186299028?a=OhgzoptCa5rqlB7csTs&s=20

<p align="center">
  <img src="assets/clink-icon.svg" alt="Clink app icon" width="220" />
</p>

## Install APK

Download the latest APK here:

- https://github.com/smasithick/clink-android/releases/latest/download/app-debug.apk

Then:

1. Allow installation from unknown sources if your device asks.
2. Install the APK.
3. Share any URL and select Clink.
4. The cleaned URL is copied to your clipboard automatically.

## Why Clink?

Links often include tracking data such as:

- `utm_source`
- `utm_medium`
- `utm_campaign`
- `fbclid`
- `gclid`
- `igshid`
- `twclid`
- `ref=...`

These are useful for analytics, but annoying when you just want a clean URL to share or open.
Clink removes those extras automatically.

## How it works

1. You share a URL from any app.
2. The Android share sheet sends the text to Clink.
3. Clink detects the URL(s) in the shared text.
4. It removes tracking and referral parameters.
5. It copies the cleaned URL to your clipboard.
6. The app exits immediately without showing a UI.

## Example

Before:

https://example.com/product?utm_source=google&utm_medium=cpc&ref=friend&gclid=abc123

After:

https://example.com/product

## Security and installation note

Google Play Protect may show a warning when installing an APK from outside the Google Play Store. This is common for sideloaded apps and does not automatically mean the app is malicious.

Clink is intentionally minimal and does not request any Android permissions. It does not access contacts, storage, camera, microphone, location, or other sensitive data. It only works with the shared text from the Android share sheet and copies the cleaned URL to the clipboard.

If you install the APK from this repository's release page or from a trusted build generated from the source code in this project, it is safe to use.

If Play Protect blocks the install, choose "Install anyway" only when you trust the source and are installing the APK produced from this repository.

## Android compatibility

This project targets Android API 24+ (Android 7.0 and newer).

That means it can generally be installed on:

- Android 7.0 (Nougat)
- Android 8.0+
- Android 9.0+
- Android 10+
- Android 11+
- Android 12+
- Android 13+
- Android 14+

It is not intended for very old Android versions below API 24.

## Optional: build from source

If you want to build the APK yourself, use:

```bash
./gradlew assembleDebug
```

The APK will be generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Notes

- Best used as a utility for cleaning shared links before pasting.
- If the text contains multiple URLs, Clink tries to sanitize each one.
- The app is designed to be fast and unobtrusive rather than interactive.

## License

All rights reserved.

This project is intended for personal use and experimentation. You may install and use the app for personal use, but modification, redistribution, or derivative works are not allowed without explicit written permission.

Made with love by @smasithick

https://x.com/smasithick