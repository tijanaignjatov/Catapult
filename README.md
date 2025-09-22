# Catapult – Cat Quiz & Breed Catalog  

**Catapult** is an Android mobile application that combines a **cat breed catalog** with an interactive **quiz game**.  
The project was developed as part of the **Mobile Applications Development course (RAF)**.  



## Project Overview  

The app has two main components:  

1. **Cat Breed Catalog**  
   - Browse and search cat breeds.  
   - View detailed breed information (description, traits, rarity).  
   - Photo gallery and full-screen photo viewer.  
   - Data fetched from **TheCatAPI** and cached locally.  

2. **Cat Knowledge Quiz**  
   - 20 randomly generated questions based on breed data.  
   - Question types:  
     - *Guess the breed* (image + multiple choice).  
     - *Find the odd one out* (temperament mismatch).  
   - 5-minute timer with automatic submission.  
   - Dynamic scoring system with formula and time bonus.  
   - Results can be published to the **Leaderboard API**.  



## Core Features  

- **Local Profile**  
  - Create a local account with name, nickname, and email.  
  - View account details, history of quiz results, and best score.  

- **Breed Catalog**  
  - List of breeds with search functionality.  
  - Breed details with gallery and Wikipedia link.  
  - Photo viewer with swipe navigation.  

- **Quiz Game**  
  - Randomized question and answer generation.  
  - Dynamic difficulty with unique images per session.  
  - Time-limited (5 minutes).  
  - No backtracking – one answer per question.  

- **Leaderboard**  
  - Global leaderboard via **Leaderboard API**.  
  - Shows all results ranked from best to worst.  
  - Displays global ranking for local user.   



## Technologies  

- **Language:** Kotlin  
- **Architecture:** MVI (Model-View-Intent)  
- **UI:** Jetpack Compose, Material Design 3  
- **Async:** Coroutines, Flow  
- **Navigation:** Jetpack Navigation  
- **Networking:** Retrofit, OkHttp, KotlinX Serialization  
- **Persistence:** Room (local DB), DataStore  
- **Dependency Injection:** Hilt  
- **APIs:**  
  - [TheCatAPI](https://thecatapi.com/) (c
