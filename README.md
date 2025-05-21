# 📱 Smart Task Manager - Gestion Intelligente de Tâches

Application Android de productivité avec synchronisation Google Agenda et priorisation IA.

## ✨ Fonctionnalités
- 🔐 Auth (Email/Google)
- ✅ CRUD complet des tâches
- 📅 Vues filtrées (Aujourd'hui/À venir/En retard)
- 🔄 Sync bidirectionnelle Google Agenda
- 🤖 Priorisation automatique par IA
- 🛎 Notifications intelligentes

## ⚙ Installation
1. Cloner le repo :
```bash
git clone https://github.com/votre-user/smart-task-manager.git
Configurer Firebase (google-services.json)

Activer Google Calendar API

Placer model.tflite dans app/src/main/assets/

🏗 Structure
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── auth/       # Authentification
│   │   │   ├── tasks/      # Gestion des tâches
│   │   │   ├── ai/         # Modèle TensorFlow
│   │   ├── res/            # Ressources UI
📚 Dépendances
gradle
// Firebase
implementation 'com.google.firebase:firebase-auth'
implementation 'com.google.firebase:firebase-firestore'

// Google
implementation 'com.google.android.gms:play-services-auth:20.6.0'

// TensorFlow
implementation 'org.tensorflow:tensorflow-lite:2.10.0'

