# SLution  
A real-time sign language translation mobile app that uses camera-based hand detection and voice-to-text technologies to bridge communication gaps between signers and non-signers.

---

## Developed By  
- **Ofir Evgi** – 207441346  
- **Omer Shukroon** – 208540856  

---

## Stakeholders

### End Users
- Individuals with disabilities who use sign language as their primary communication method  
- People who wish to communicate effectively with sign language users (family members, friends, coworkers)  
- Public institutions and private organizations aiming to make their services more accessible and inclusive  

### Administrators
Includes developers, system managers, and stakeholders responsible for:
- Maintaining and upgrading the app's infrastructure and features  
- Managing the user database and sign language model  
- Monitoring app usage, performance, and reliability  
- Planning and executing future versions and improvements  

---

## Functional Requirements

### End Users

#### Registration and Login
- Users can log in using Google, email/password, or phone number  
- Authentication is handled securely via Firebase  

#### Profile Management
- Users can update personal details: first name, surname, and email  
- Data is saved both locally and in Firebase  

#### Home Screen
- View recent chat sessions  
- Start a new conversation  
- Access favorite messages  
- Navigate to the user profile  

#### Conversation Screen
- View the current conversation history  
- Send messages via:
  - Keyboard input  
  - Speech-to-text using Google STT  
  - Real-time translation from sign language (via camera + ML model)  
- Mark specific messages as favorites  
- Navigate to the translation screen  

#### Translation Screen
- Divided into two sections:  
  - **Top (3/4)**: Real-time camera view for performing signs  
  - **Bottom (1/4)**: Live text output of the recognized gesture  
- Users can approve and insert the translated text into the chat  

#### Favorite Messages
- View all saved favorite sentences  
- Remove favorites from the list  

---

### Administrators

#### Database and Model Management
- Manage the gesture-to-text mapping database  
- Update or retrain the ML gesture recognition model  
- Handle integration between the model and the camera pipeline  

#### User Management
- Reset user credentials  
- Delete accounts if needed  

#### System Maintenance
- Monitor Firebase connectivity and storage  
- Ensure gesture recognition features work across devices  
- Perform backups and restore data if necessary  

#### Feature Development
- Collect user feedback to improve usability  
- Roll out new modules such as language expansion or chat export  

---

## Non-Functional Requirements

### Performance
- Real-time processing with a target delay of **1–3 seconds**  
- Gesture recognition accuracy: **≥90% under normal lighting and positioning**  

### Scalability
- Designed to support increasing user base without degradation  
- Easily extendable gesture/sign database  

### Reliability
- 99.9% uptime expected  
- Firebase backend ensures resilience and data sync  
- Fallback to manual input if camera or STT fails  

### Security
- End-to-end encryption of user data  
- GDPR-compliant authentication and storage (Firebase)  

### Usability
- Intuitive and accessible UI for both tech-savvy and novice users  
- Visual cues and helpful prompts provided throughout the app  

###  Compatibility
- Android support (API level 29 and above)  
- Compatible with both front and rear cameras  

### Maintainability
- Modular code architecture with clean separation between UI, logic, and services  
- Firebase and CameraX usage isolated in manageable components  
- Code is fully documented and version-controlled  

### Localization
- UI supports **English**  
- Future updates may add multi-language UI and sign language sets  

### Data Management
- Recent conversations stored per user in Firebase  
- Favorite messages stored locally and synced  
- Data persists across sessions  

---

## Machine Learning Integration (Coming Soon)
SLution is being integrated with a custom-trained ML model that recognizes sign language gestures from live camera input using TensorFlow Lite.  
The model runs locally on the device and will be seamlessly integrated into the translation screen for real-time feedback.  

During testing, fallback options will be provided if the model is unavailable. This feature will be fully evaluated in a future release cycle.

---

## Tech Stack

- **Android (Java)**  
- **Firebase Realtime Database & Auth**  
- **Google Speech-to-Text API**  
- **CameraX API**  
- **TensorFlow Lite (ML model)**  
- **SharedPreferences** for local storage  

---

## Screenshots  
*See the `Application_Screens_README` file for full UI design and flows.*

---

##  Note
This is a final-year university project submitted to the Computer Science department. The codebase and machine learning model are developed independently and integrated into a unified mobile solution.

---
