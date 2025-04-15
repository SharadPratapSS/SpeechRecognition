## 📢 VOSK Android Speech Recognition Integration

This repository demonstrates how to integrate **VOSK API** for offline speech recognition in an Android application using Kotlin.

### 📦 Dependencies

Add the following dependencies to your `build.gradle` (app-level):

```kotlin
implementation("net.java.dev.jna:jna:5.13.0@aar")
implementation("com.alphacephei:vosk-android:0.3.47")
```

### 🗜️ Downloading the Model

1. Visit the official VOSK model repository:  
   https://alphacephei.com/vosk/models

2. Download a **lightweight model** suitable for your use-case (e.g., small English model for minimal footprint).

3. Once downloaded:
   - Extract the model folder.
   - Ensure it contains a file named `uuid`.  
     If not, create a new text file named `uuid` inside the model folder, type **any random number** into it, and save.

### 📂 Project Setup

1. Open your project in **Project View** in Android Studio.
2. Place the **entire model folder** inside the `assets` directory of your app.

Example structure:
```
YourApp/
└── app/
    └── src/
        └── main/
            └── assets/
                └── vosk-model-small-en-us-0.15/
```

### 🛡 Background Recognition Support

To enable VOSK to run in the background:

- Implement a **persistent foreground service**.
- Foreground services keep recognition active even when the app is not in the foreground or the screen is off.

### 🧐 Efficient Usage with Singleton

If your app only needs speech recognition in limited parts of the application:

- Use a **singleton object** to initialize and manage the VOSK model and recognizer.
- This avoids multiple model loads and improves performance.

### ✅ Pro Tips

- VOSK runs **completely offline**, making it perfect for privacy-sensitive or low-connectivity scenarios.
- Use lightweight models for faster startup and lower memory usage.
- Remember to release the recognizer and model when not in use to free up system resources.

