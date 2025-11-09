# 💸 SmartSplit — Simplify Group Expense Tracking

[![Android](https://img.shields.io/badge/Platform-Android-green?logo=android)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple?logo=kotlin)](https://kotlinlang.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Build](https://img.shields.io/badge/Build-Passing-success.svg)]()

SmartSplit is an **Android application** designed to make **group expense management and settlement effortless**.  
It helps friends, roommates, or colleagues split bills, track expenses, and calculate who owes whom — all seamlessly and locally using **Room Database** and **MVVM Architecture**.

---

## 🚀 Features

✅ **Create Groups:**  
Easily create a group for a trip, event, or shared expenses.

✅ **Add Members & Expenses:**  
Add members, log shared bills, and assign expenses to individuals or groups.

✅ **Auto Settlement Calculation:**  
Automatically calculate who owes whom using SmartSplit’s built-in algorithm.

✅ **Data Persistence:**  
All data stored locally using **Room Database** — works offline.

✅ **Modern MVVM Architecture:**  
Built with Clean Architecture principles using ViewModel, LiveData, and Repository patterns.

✅ **Lightweight & Fast:**  
Simple UI and smooth interactions optimized for performance.

---

## 🧠 Tech Stack

| Layer | Technologies Used |
|-------|-------------------|
| **Language** | Kotlin |
| **Architecture** | MVVM (Model-View-ViewModel) |
| **Database** | Room (SQLite ORM) |
| **UI Components** | RecyclerView, Fragments, LiveData, ViewBinding |
| **Asynchronous Tasks** | Kotlin Coroutines |
| **Dependency Injection (optional)** | ViewModelProvider |
| **Design Tools** | Material Design Components |

---
## 🏗️ Project Structure
SmartSplit/
│
├── app/
│ ├── src/main/java/com/example/smartsplit/
│ │ ├── data/ # Room Entities & DAO interfaces
│ │ ├── repository/ # Repository for data access abstraction
│ │ ├── ui/group/ # UI for Group screens
│ │ │ ├── expense/ # Add Expense Fragments
│ │ │ ├── settlement/ # Settlement Calculation Fragments
│ │ ├── util/ # Utility classes (calculators, helpers)
│ │ └── vm/ # ViewModels (GroupViewModel, etc.)
│ └── res/ # Layouts, Drawables, Values
│
├── build.gradle
└── README.md


# 🧩 Core Components Overview
1. Room Database

Handles persistent storage of Groups, Members, and Expenses.

@Database(entities = [GroupEntity::class, MemberEntity::class, ExpenseEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}

2. ViewModel + Repository

Implements MVVM separation of concerns and data flow management.

class GroupViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = GroupRepository(application)
    val groups = MutableLiveData<List<GroupEntity>>()
}

3. Settlement Calculator

Custom algorithm to compute net balances and settlements.

object SettlementCalculator {
    fun calculate(groupExpenses: List<ExpenseWithSplits>): List<Transaction> {
        // Custom logic to balance debts among members
    }
}



# 🧪 Testing

Unit tests for core logic (like settlement calculation) are available under:

app/src/test/java/com/example/smartsplit/util/


To run tests:

./gradlew test

# 📈 Future Enhancements

🔄 Cloud sync with Firebase for multi-device access

💬 In-app chat or notes per group

📊 Visual analytics for expense summary

# 🌙 Dark Mode support

📱 Flutter or Jetpack Compose redesign

# 🤝 Contribution Guidelines

Fork this repo 🍴

Create your feature branch:

git checkout -b feature/awesome-feature


Commit changes and push:

git push origin feature/awesome-feature


Submit a Pull Request 🚀

# 🧑‍💻 Author

Rohan Nishad
📍 B.Tech CSE | Android & Java Developer
💼 Treasurer, JIT ACM Student Chapter
🌐 LinkedIn
 • GitHub


