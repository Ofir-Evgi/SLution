# SLution 

A real-time sign language translation mobile app that uses camera-based hand detection and voice-to-text technologies to bridge communication gaps between signers and non-signers.


## Application Video

https://github.com/user-attachments/assets/37157049-2a1d-4c9c-9133-de720696a47d


---

## [Link to the code](https://drive.google.com/file/d/1SGqzc_kz5zUH4K8c5orE-jeOrSSsHcMh/view?usp=sharing)

---

## Developed By  
- **Ofir Evgi**  
- **Omer Shukroon**  

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
- Real-time processing with a target delay of **5-10 seconds**  
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

## Machine Learning Architecture Overview

SLution uses a powerful **ensemble of custom-trained Convolutional Neural Network (CNN) models** to recognize American Sign Language (ASL) letters from real-time camera input. These models were trained from scratch on diverse and well-structured ASL datasets using **TensorFlow**, and deployed to the mobile application using **TensorFlow Lite** for efficient on-device inference.

### How It Works

- Each video frame captured by the device's camera is analyzed in real time by **four independent CNN models** running locally on the device.
- Each model outputs a **softmax probability vector** representing its confidence across all possible ASL letters.
- The outputs are **averaged across all models** to form a single prediction vector.
- A letter is only displayed to the user once it has been **consistently predicted over 10 consecutive frames**, significantly improving stability and accuracy.

### Model Architecture Highlights

- Two base models operate on **64×64 pixel inputs**, optimized for speed on lower-end devices.
- Two advanced models use **300×300 resolution** with **batch normalization** for improved precision.
- All models include **multiple convolutional layers**, dropout regularization, and were trained using **data augmentation** to enhance generalization.

### Current Status

The models are fully integrated into the translation interface and run entirely **offline**, ensuring low latency and no dependency on cloud services. The fallback system allows users to continue using manual input or voice-to-text if camera-based recognition is unavailable.

Future releases may explore extending recognition to full words or dynamic signs, and introducing support for other sign languages.

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
See the [`Application_Screens_README`](./Application_Screens_README.md)  file for full UI design and flows.

---

##  Note
This is a final-year project submitted to the Computer Science department. The codebase and machine learning model are developed independently and integrated into a unified mobile solution.

---
