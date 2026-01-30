# Android Notes Application

A modern Android notes application built with **Kotlin**, following **Clean Architecture** and **MVI (Model-View-Intent)** pattern with **Jetpack Compose** UI.

---

## 📱 Features

- ✅ **Create** new notes with title and description
- ✅ **Read/List** all notes in a scrollable list
- ✅ **Update** existing notes
- ✅ **Delete** notes with confirmation
- ✅ **Sort** notes by date (latest first - automatic)
- ✅ **Input Validation** - Title cannot be empty
- ✅ **Error Handling** - Graceful error messages
- ✅ **Reactive UI** - Real-time updates using Flow

---

## 🏗️ Architecture

This project follows **Clean Architecture** principles with **MVI (Model-View-Intent)** pattern for predictable state management.

### Architecture Layers

```
┌─────────────────────────────────────────────────────────────┐
│                     PRESENTATION LAYER                       │
│  ┌──────────────┐      ┌──────────────┐                    │
│  │   UI (Compose)│ ──→  │   ViewModel  │                    │
│  │   - Screens   │ ←──  │   - MVI      │                    │
│  │   - Components│      │   - StateFlow│                    │
│  └──────────────┘      └──────────────┘                    │
└─────────────────────────────────────────────────────────────┘
                           ↓           ↑
                        Intent      State
                           ↓           ↑
┌─────────────────────────────────────────────────────────────┐
│                      DOMAIN LAYER                            │
│  ┌──────────────┐      ┌──────────────┐                    │
│  │   Use Cases   │      │  Repository  │                    │
│  │  - AddNote    │ ──→  │  Interface   │                    │
│  │  - GetNotes   │      │              │                    │
│  │  - UpdateNote │      └──────────────┘                    │
│  │  - DeleteNote │                                           │
│  └──────────────┘                                           │
│  ┌──────────────┐                                           │
│  │    Models     │                                           │
│  │   - Note      │                                           │
│  └──────────────┘                                           │
└─────────────────────────────────────────────────────────────┘
                           ↓           ↑
┌─────────────────────────────────────────────────────────────┐
│                       DATA LAYER                             │
│  ┌──────────────┐      ┌──────────────┐                    │
│  │  Repository   │ ──→  │     DAO      │                    │
│  │ Implementation│      │  (Room)      │                    │
│  └──────────────┘      └──────────────┘                    │
│                              ↓                               │
│                        ┌──────────────┐                     │
│                        │   Database   │                     │
│                        │   (Room)     │                     │
│                        └──────────────┘                     │
└─────────────────────────────────────────────────────────────┘
```

### MVI Pattern Flow

```
┌──────────┐
│   User   │
└────┬─────┘
     │ Interaction (Click, Input)
     ↓
┌──────────────┐
│      UI      │
│  (Compose)   │
└────┬─────────┘
     │ Dispatches Intent
     ↓
┌──────────────────────┐
│  NoteIntent          │
│  - LoadNotes         │
│  - AddNote           │
│  - UpdateNote        │
│  - DeleteNote        │
│  - SelectNote        │
└────┬─────────────────┘
     │
     ↓
┌──────────────────────┐
│   ViewModel          │
│  processIntent()     │
└────┬─────────────────┘
     │ Calls Use Case
     ↓
┌──────────────────────┐
│   Use Case           │
│  (Business Logic)    │
└────┬─────────────────┘
     │ Calls Repository
     ↓
┌──────────────────────┐
│   Repository         │
└────┬─────────────────┘
     │ Calls DAO
     ↓
┌──────────────────────┐
│   Room Database      │
└────┬─────────────────┘
     │ Flow Updates
     ↓
┌──────────────────────┐
│   NoteState          │
│  - notes: List       │
│  - isLoading: Bool   │
│  - error: String?    │
│  - selectedNote      │
└────┬─────────────────┘
     │ StateFlow Emission
     ↓
┌──────────────────────┐
│   UI Recomposes      │
└──────────────────────┘
```

---

## 📂 Project Structure

```
com.example.notesapp/
│
├── 📁 data/
│   ├── 📁 local/
│   │   ├── NoteEntity.kt          # Room entity
│   │   ├── NoteDao.kt              # Database access object
│   │   └── NoteDatabase.kt         # Room database
│   │
│   └── 📁 repository/
│       └── NoteRepositoryImpl.kt   # Repository implementation
│
├── 📁 domain/
│   ├── 📁 model/
│   │   └── Note.kt                 # Domain model
│   │
│   ├── 📁 repository/
│   │   └── NoteRepository.kt       # Repository interface
│   │
│   └── 📁 usecase/
│       ├── GetNotesUseCase.kt      # Fetch all notes
│       ├── AddNoteUseCase.kt       # Add new note
│       ├── UpdateNoteUseCase.kt    # Update existing note
│       └── DeleteNoteUseCase.kt    # Delete note
│
├── 📁 presentation/
│   ├── 📁 viewmodel/
│   │   ├── NoteIntent.kt           # MVI intents
│   │   ├── NoteState.kt            # MVI state
│   │   └── NoteViewModel.kt        # ViewModel
│   │
│   └── 📁 ui/
│       ├── MainActivity.kt         # Entry point
│       ├── NotesScreen.kt          # Main screen
│       ├── NotesList.kt            # Notes list composable
│       ├── NoteItem.kt             # Note card composable
│       └── NoteDialog.kt           # Add/Edit dialog
│
├── 📁 di/
│   └── AppModule.kt                # Hilt dependency injection
│
└── NotesApplication.kt             # Application class
```

---

## 🛠️ Tech Stack

### Core
- **Language**: Kotlin
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)

### Architecture & Patterns
- **Architecture**: Clean Architecture (Data, Domain, Presentation layers)
- **Design Pattern**: MVI (Model-View-Intent)
- **Dependency Injection**: Dagger Hilt

### UI
- **UI Framework**: Jetpack Compose
- **Material Design**: Material 3
- **Navigation**: Compose Navigation (can be added)

### Data & Persistence
- **Database**: Room Persistence Library
- **ORM**: Room 2.6.0

### Async & Reactive
- **Coroutines**: Kotlin Coroutines
- **Flow**: StateFlow for state management
- **Lifecycle**: ViewModelScope for coroutine management

### Testing
- **Unit Testing**: JUnit 4
- **Mocking**: Mockito Kotlin
- **Coroutine Testing**: kotlinx-coroutines-test
- **Flow Testing**: Turbine
- **UI Testing**: Compose UI Test Framework
- **Instrumentation**: Espresso

---

## 🔄 Data Flow

### Complete Data Flow Example (Adding a Note)

```
1. USER ACTION
   ↓
   User clicks "Add" FAB and fills form

2. UI LAYER
   ↓
   NotesScreen dispatches intent:
   viewModel.processIntent(NoteIntent.AddNote("Title", "Description"))

3. VIEWMODEL (Presentation)
   ↓
   Receives intent and calls:
   addNoteUseCase(Note(title, description, timestamp))

4. USE CASE (Domain)
   ↓
   Validates input (title not blank)
   Calls: repository.insertNote(note)
   Returns: Result<Unit>

5. REPOSITORY (Data)
   ↓
   Converts domain Note to NoteEntity
   Calls: dao.insertNote(entity)

6. DAO (Room)
   ↓
   Executes SQL: INSERT INTO notes
   Room's Flow automatically emits updated list

7. FLOW PROPAGATION (Reactive)
   ↓
   dao.getAllNotes() emits new list
   Repository converts entities to domain models
   ViewModel collects flow and updates StateFlow

8. STATE UPDATE
   ↓
   StateFlow emits new NoteState with updated notes list

9. UI RECOMPOSITION
   ↓
   Compose observes state change
   UI automatically recomposes
   User sees new note in list
```

### Read Flow (Reactive Updates)

```
Room Database
    ↓ (Flow emission on data change)
DAO.getAllNotes(): Flow<List<NoteEntity>>
    ↓
Repository.getAllNotes(): Flow<List<Note>>
    ↓ (map transformation: Entity → Domain Model)
GetNotesUseCase.invoke(): Flow<List<Note>>
    ↓
ViewModel collects flow
    ↓
StateFlow<NoteState> emits new state
    ↓
Compose UI observes state
    ↓
UI recomposes automatically
```

---

## 🧪 Testing

### Unit Tests

**ViewModel Tests** (`NoteViewModelTest.kt`)
- ✅ Test loading notes updates state correctly
- ✅ Test adding note with valid data
- ✅ Test validation errors for empty title
- ✅ Test deleting note calls use case

**Use Case Tests** (Can be added)
- ✅ Test business logic validation
- ✅ Test error handling

### UI Tests

**Compose Tests** (`NotesScreenTest.kt`)
- ✅ Test empty state displays message
- ✅ Test notes list displays items
- ✅ Test click interactions
- ✅ Test dialog appears on FAB click

### Running Tests

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Run specific test
./gradlew test --tests NoteViewModelTest
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog | 2023.1.1 or newer
- JDK 11 or higher
- Android SDK 34
- Kotlin 1.9.0+

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/connectwitharunrk/notes-app.git
   cd notes-app
   ```

2. **Open in Android Studio**
   - File → Open → Select project directory

3. **Sync Gradle**
   - Android Studio will automatically sync
   - Or manually: File → Sync Project with Gradle Files

4. **Run the app**
   - Click Run button or `Shift + F10`
   - Select emulator or connected device

---

## 🎯 Key Concepts

### Clean Architecture Benefits
- **Separation of Concerns**: Each layer has a single responsibility
- **Testability**: Easy to test each layer independently
- **Maintainability**: Changes in one layer don't affect others
- **Scalability**: Easy to add new features
- **Independence**: UI, database, and frameworks can be changed easily

### MVI Pattern Benefits
- **Unidirectional Data Flow**: Predictable state changes
- **Single Source of Truth**: StateFlow holds all UI state
- **Time-Travel Debugging**: State changes are trackable
- **Thread Safety**: State updates are synchronized
- **Reactive**: UI automatically updates on state changes

### Why Room?
- Type-safe database access
- Compile-time SQL verification
- Supports Flow for reactive queries
- Automatic LiveData/Flow updates
- Migration support

### Why Hilt?
- Compile-time dependency injection
- Reduces boilerplate code
- Scoped dependencies (Singleton, ViewModelScoped)
- Easy testing with test modules
- Integration with Android components

---


---

**Happy Coding! 🚀**
