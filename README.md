# RxShelfSafe (Original UI + SQLite Database + Linked List)

**Pharmacy Drug Expiration Tracker**

This version keeps:
- The **original UI** (card layout, borders, button placement, colors, pagination, etc.)
- The **SQLite database** (same as the original project)
- The **Linked List** for batches (`BatchList` + `Batch` nodes)

## What's included

- Full original look and feel
- SQLite database (`rxshelfsafe.db`) via `sqlite-jdbc.jar`
- Linked List as the main in-memory structure for batches
- DAOs for accounts, batches, categories, and removal requests
- Default accounts still work

## Default accounts

| Username | Password  | Role  |
|----------|-----------|-------|
| admin    | admin123  | ADMIN |
| staff    | staff123  | STAFF |

## How to run

1. Open the project in NetBeans / IntelliJ / Eclipse / VS Code
2. Make sure `lib/sqlite-jdbc.jar` is added to the project libraries (classpath)
3. Run `src/RxShelfSafe.java`

The database file (`rxshelfsafe.db`) will be created automatically next to the program when you first run it.

## Project structure

```
src/
├── RxShelfSafe.java
├── model/          ← Account, Batch (node), BatchList (linked list), ItemStatus
├── database/       ← DataStore, DBConnection, DAOs, FileFinder, ExcelExporter
├── frame/          ← LoginScreen, MainFrame
├── panels/         ← Dashboard, Batch (cards), Removal, Report, Category, Account
└── popups/
lib/
└── sqlite-jdbc.jar
```

## Data Structures

- **Singly Linked List** for batches (add, remove, search, sort, count, etc.)
- ArrayList / HashMap for accounts, categories, and removal requests
- SQLite for permanent storage
