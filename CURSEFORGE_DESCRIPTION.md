# ClearDrop - Advanced Item Cleaner

**ClearDrop** (formerly ClearLag) is a powerful and lightweight server-side mod designed to optimize your Hytale server by periodically removing dropped items. It ensures your server remains lag-free while providing extensive configuration options to suit your community's needs.

## 🚀 Features

*   **Automated Cleanup:** Set minimal intervals to automatically clear dropped items from the world.
*   **Smart Notifications:** Fully customizable startup, warning, and completion messages.
*   **Toggleable Alerts:** Reduce chat spam by enabling or disabling specific notifications (warnings, countdowns, reports) individually.
*   **Manual Control:** Force a cleanup instantly via command when needed.
*   **Multi-World Support:** Efficiently scans and cleans entities across all loaded worlds.
*   **Exclude/Include Logic:** (Internal logic targets specific physics/item components to avoid removing wrong entities).

## 🛠️ Commands

| Command | Usage | Description |
| :--- | :--- | :--- |
| **Main Command** | `/clear` | Shows the help menu with available subcommands. |
| **Clear Now** | `/clear now` | Forces an immediate cleanup of dropped items in all worlds. |
| **Auto Timer** | `/clear auto <minutes>` | Sets the interval (in minutes) for the automatic cleanup task. |
| **Toggle Timer Msg**| `/clear auto <true/false>` | Enables/Disables the "Next cleanup in X minutes" notification. |
| **Warning Time** | `/clear notice <seconds>` | Sets how many seconds before cleanup the warning appears. |
| **Toggle Warn Msg** | `/clear notice <true/false>` | Enables/Disables the "Cleanup in X seconds..." warning message. |
| **Toggle Report** | `/clear cleanupfinished <true/false>` | Enables/Disables the "Cleanup finished... X items removed" message. |

## ⚙️ Configuration

The mod generates a flexible configuration system:

1.  **ClearDrop.json**: Controls the logic.
    *   `MinutesExecution`: How often the cleanup runs.
    *   `CleanupWarningSeconds`: Countdown time before cleanup.
    *   `NotificationAutoEnabled`: Toggle for periodic schedule messages.
    *   `NotificationNoticeEnabled`: Toggle for pre-cleanup warnings.
    *   `NotificationCleanupFinishedEnabled`: Toggle for post-cleanup reports.

2.  **language.json**: Fully translatable! Change every message, prefix (`[Clear Drop]`), colors, and text to fit your server's language or theme.

## 📦 Installation

1.  Download the **ClearDrop** `.jar` file.
2.  Place it in your server's `mods` directory.
3.  Restart the server.
4.  Configure `ClearDrop.json` and `language.json` in the config folder as desired.

---
*Optimized for performance and flexibility.*
