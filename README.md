# Kage Player

A free, ad-free Android video player made for watching anime on your phone. Everything runs on your device: no ads, no accounts.

## Features

- Plays the video files on your device, with folder and file views.
- Subtitles turn on automatically when a video has them, and you can pick another track at any time.
- Audio and subtitle track selection, hardware and software decoding, playback speed, picture in picture and background play.
- Customisable shortcuts row under the seekbar: choose which buttons you want, or hide the whole row.
- Extra shortcuts: mute, sleep timer, A-B repeat, night mode, mirror mode, vertical flip and audio effect.
- Terms & Conditions shown on first launch. The app only opens after you accept them.

## Build

Builds run on GitHub Actions (`.github/workflows/build-apk.yml`). Each build checks that the XML and Kotlin code are valid, then publishes two signed APKs under Releases: `arm64-v8a` (small, for almost every phone) and `universal` (large, runs on any device). If a build fails, the run summary shows the exact error and the full Gradle log is attached as `gradle-log`.

The build needs the repository secrets `ANDROID_KEYSTORE_BASE64`, `ANDROID_KEYSTORE_PASSWORD` and `ANDROID_KEY_ALIAS`.

## Project layout

| Folder | What it holds |
| --- | --- |
| `app` | Main activity, navigation and the Terms screen |
| `core` | Shared modules: model, data, datastore, media, common code and UI components |
| `feature/player` | The video player |
| `feature/videopicker` | Folder and video lists |
| `feature/playlist` | Playlists |
| `feature/settings` | Settings and About |
| `.github` | Build and helper workflows and scripts |

## License

GNU General Public License v3.0, see `LICENSE`. Kage Player is based on the open-source [Next Player](https://github.com/anilbeesetti/NextPlayer) by Anil Beesetti.
