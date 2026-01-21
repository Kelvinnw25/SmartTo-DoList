# 📝 Smart To-Do List (AI Powered)

![Android](https://img.shields.io/badge/Platform-Android-green)
![Language](https://img.shields.io/badge/Language-Java-orange)
![AI](https://img.shields.io/badge/AI-Google_Gemini-blue)

**Smart To-Do List** is an intelligent productivity application for Android that leverages Generative AI to simplify task management.

Forget manual date pickers. Simply type naturally: *"Submit internship report next Monday at 10 AM"*, and the application will automatically extract the Title, Date, and Time for you.

## ✨ Key Features
* **🤖 AI Task Extraction:** Powered by **Google Gemini 2.5 Flash** to convert natural language into structured task data.
* **📅 Smart Parsing:** Intelligently recognizes relative time context (e.g., "tomorrow", "next week", "tonight").
* **💬 Interactive Chat UI:** User-friendly conversational interface similar to modern messaging apps.
* **🔔 Smart Reminders:** Automatically schedules local notifications for upcoming deadlines.

## 🛠️ Tech Stack & Libraries
This project is built using **Clean Architecture** principles and industry-standard libraries:

* **Language:** Java (Native Android)
* **Architecture:** MVVM (Model-View-ViewModel)
* **Networking:**
    * **Retrofit 2:** Type-safe HTTP client for API communication.
    * **OkHttp 3:** Handles HTTP requests and logging interception.
    * **GSON:** Serializes/deserializes JSON data from the AI response.
* **AI Engine:** Google Generative AI (Gemini Protocol).
* **Local Storage:** Room Database (SQLite).

## ⚙️ Installation & Setup
This project requires a valid Google Gemini API Key to function.

1.  **Clone the Repository** to your local machine.
2.  Open the project in **Android Studio**.
3.  Create a file named `local.properties` in the root directory (if it doesn't exist).
4.  Add your API Key to the file:
    ```properties
    GEMINI_API_KEY=Your_Gemini_API_Key_Here
    ```
    *(You can get a free API Key from Google AI Studio)*
5.  **Sync Project with Gradle** and Run the application.

---
**Created by Kelvin Nathanael** *Computer Science Student - Binus University*