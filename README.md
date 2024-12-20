# SLution
A hand-detection-based application for translating sign language into text in real time.

# Detailed Requirements

## Names:
- **Ofir Evgi**  
- **Omer Shukroon**  

---

## Stakeholders

### End Users:
- **Individuals with disabilities** who communicate using sign language.  
- **People who want to communicate** with sign language users (e.g., family, friends, and colleagues).  
- **Government institutions and large companies** that aim to make their services more accessible to individuals with disabilities in an easy and efficient way.  

### Administrators:
Includes developers, investors, and system managers responsible for:
- Managing and maintaining the application's database, infrastructure, and features.  
- Monitoring system performance, fixing bugs, and ensuring application quality.  
- Overseeing the project's success and its future development.  

---

## Functional Requirements

### **End Users**

#### Registration and Login:
- Users can register in the app with personal details (e.g., email and password).  
- Users can securely log into the app.  

#### User Profile Management:
- Users can update their personal details (e.g., name, email, password).  
- Users can select their preferred sign language: **Hebrew Sign Language (ISL)** or **English Sign Language (ASL)**.  
- When the user selects their preferred sign language in their profile, the system will automatically set the default text language for conversations to the chosen language.  

#### Navigation from the Home Screen:
Users can access the following functions from the home screen:  

- **Conversation History**:
  - Users can browse saved conversations.  
  - Users can resume a saved conversation.  
  - Users can mark sentences as favorites for future use.  

- **Favorite Sentences**:
  - Users can view a list of sentences marked as favorites.  
  - Users can remove sentences from the list.  

- **Starting a New Conversation**:
  - Users can start a new conversation.  

    During setup:
    - Users can select the language for text display (e.g., sign language in Hebrew but text displayed in English).  

    In the conversation:
    - Users can translate sign language into text in real-time using the camera.  
    - Users can type messages using the keyboard.  
    - Users can convert speech to text.  
    - Users can access the list of favorite sentences for quick use of saved phrases.  

#### Conversation Translation Screen:
The screen is divided into two sections:
- **Top section**:
  -  Displays the camera view (front or rear).  
  - Users can see themselves performing sign language.  
  - Users can approve or edit the translated sentence.  

---

### **Administrators**

#### Database Management:
- Update and manage the database of gestures and sentences.  

#### User Management:
- Manage user accounts (e.g., resetting passwords, deleting accounts).  

#### System Maintenance:
- Monitor and fix errors related to gesture recognition or system performance.  

#### Feature Development:
- Test and implement new features based on user feedback.  

---

## Non-Functional Requirements

### Performance:
- The system will process sign language gestures in real-time with a delay of no more than **1-3 seconds**.  
- The system will achieve at least **90% accuracy** in gesture recognition under standard conditions.  

### Scalability:
- The system will support increased user loads without performance degradation.  
- The database of gestures and sentences can be expanded to include additional signs or languages.  

### Reliability:
- The system will ensure **99.9% availability** for continuous operation.  
- Backup mechanisms will ensure recovery from unexpected failures.  

### Security:
- All user data, including personal details and translations, will be encrypted during transmission and storage.  
- The application will comply with relevant data security regulations (e.g., GDPR).  

### Usability:
- The app interface will be intuitive and suitable for users with varying levels of technological proficiency.  
- Accessibility features will include:
  - Font size adjustment.  
  - High contrast themes.  
  - Screen reader support.  

### Compatibility:
- The app will be compatible with **Android**.  
- The app will support front and rear cameras with various resolutions.  

### Maintenance:
- The app's architecture will be modular, allowing components to be updated without disrupting the entire system.  
- The code will be well-documented to facilitate future development and bug fixes.  

### Localization:
- The system will support **English** in the user interface.  

### Data Management:
- The system will automatically save the **last 10 conversations**.  
- Sentences marked as favorites will be stored separately for quick retrieval.  

