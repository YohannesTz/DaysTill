# DaysTill

A simple reminder app that allows users to create and display reminders directly on their home screen using a widget.

# Download
you can download the latest binary from [here](https://github.com/YohannesTz/DaysTill/releases/) or just download it [here](https://github.com/YohannesTz/DaysTill/releases/download/1.0/app-release-unsigned.apk)

## Features
- **Home Screen Widget**: Display your reminders directly on the home screen for quick access.
- **Background Syncing**: Periodically updates the reminder text using WorkManager.
- **Persistent Reminders**: Reminders stay on the home screen even after the app is closed.
- **MVVM Architecture**: Ensures a structured and maintainable codebase.

## Technologies Used
- **Kotlin**: The programming language used for the app.
- **Jetpack Compose**: Used for building the app's UI in a declarative manner.
- **Koin**: Dependency injection for managing the app's components.
- **Glance**: Used for creating the app widget that displays the reminder on the home screen.
- **WorkManager**: Manages background tasks to update reminders periodically.

## Screenshots

|  |  |  |  |
|-----------|-----------|-----------|-----------|
| ![Preview 0](./preview/preview_0.png) | ![Preview 1](./preview/preview_1.png) | ![Preview 2](./preview/preview_2.png) | ![Preview 3](./preview/preview_3.png) |

## How It Works
1. **Create Reminder**: Users can create and set reminders from within the app.
2. **Home Screen Widget**: The reminder is displayed on the home screen via a widget powered by the Glance library.
3. **Background Update**: WorkManager schedules background tasks to update the widget periodically.

## Architecture
The app follows the MVVM architecture, separating the responsibilities into:
- **Model**: Represents the data and business logic.
- **View**: The UI layer built with Jetpack Compose.
- **ViewModel**: Bridges the View and Model layers, holding the app's state and handling the business logic.
