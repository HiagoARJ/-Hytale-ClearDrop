# Changelog 1.0.1

## ⭐ New Features
*   **Notification Toggles:** You can now enable or disable specific chat messages.
    *   Added `/clear auto <true/false>` to toggle the "Next cleanup..." message.
    *   Added `/clear notice <true/false>` to toggle the warning countdown.
    *   Added `/clear cleanupfinished <true/false>` to toggle the post-cleanup report (items removed).
*   **Instant Config Updates:** Changing settings via commands now applies immediately without needing a server restart.

## 🔄 Changes
*   **Project Renaming:** The mod is now officially named **ClearDrop** (previously ClearLag).
*   **Config Structure:** Added new boolean fields to `ClearDrop.json` for notification preferences.

## 🐛 Bug Fixes
*   Fixed an issue where "Starting cleanup..." messages appeared even when notifications were disabled.
*   Fixed a bug that caused duplicate "Cleanup finished" messages to be sent to chat.
