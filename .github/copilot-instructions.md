# Copilot / AI Agent Instructions for OOP-mini-project

Purpose
- Provide concise, actionable guidance so an AI coding agent becomes productive quickly in this repo.

Big picture
- This is a small Java OOP project in `src/` with no explicit packages. The app is run from `MainDashboard.java` and uses several manager classes to encapsulate domain logic: `DatabaseManager`, `ProductManager`, `ScheduleManager`, `TransactionManager`.
- Persistent schema and initialization live in `CreateDatabase.java` and `DATABASE_STRUCTURE.md` (review before changing DB code).

Build & run (Windows)
- The repo uses simple javac/java tooling. Common sequence:

```powershell
mkdir -Force bin
javac -d bin src\*.java
java -cp bin MainDashboard
# Or use provided scripts:
.\run.bat
.\run.ps1
```

Project structure & key files
- `src/` — All Java source files (no package declarations). Key files:
  - `MainDashboard.java` — application entry point and UI loop.
  - `CreateDatabase.java` — schema setup; run before first use or when resetting DB.
  - `DatabaseManager.java` — low-level DB access and file/connection handling.
  - `ProductManager.java`, `ScheduleManager.java`, `TransactionManager.java` — domain logic and persistence wrappers.
  - `DATABASE_STRUCTURE.md` — canonical schema and relationships.

Patterns & conventions (discoverable in code)
- Managers: each domain area has a `*Manager` class that owns data access and validation; prefer extending or calling these managers rather than touching DB access directly.
- Models: simple POJOs (e.g., `Product.java`, `Schedule.java`, `Transaction.java`) are used to pass data between managers and the UI.
- No external build tool: keep changes compatible with plain `javac` (avoid Maven/Gradle-specific features).

Editing guidance for AI
- When adding methods, follow existing naming (camelCase) and keep signatures simple—no frameworks or annotations are present.
- Keep all new classes in `src/` and avoid adding packages unless you update the compile/run instructions accordingly.
- If you change the DB schema, update `DATABASE_STRUCTURE.md` and `CreateDatabase.java` in the same PR.

Debugging & tests
- There are no automated tests. Use `System.out.println` for quick debugging (consistent with project style) and run `MainDashboard` to exercise flows.
- To reproduce issues: compile all files to `bin/` and run `MainDashboard` from PowerShell/Command Prompt.

Integration points & risks
- Persistent storage is managed locally — review `DatabaseManager.java` before modifying I/O logic. Back up DB files when experimenting.
- There are likely direct file or in-memory assumptions (no DI framework). Avoid introducing heavy infrastructure.

PR notes for AI agents
- Keep changes minimal and focused; include a short description referencing the affected managers and `DATABASE_STRUCTURE.md` when DB changes are involved.
- Run the app locally and confirm the main UI loop starts before marking a change as ready.

Where to look first (starter checklist)
1. Read [src/MainDashboard.java](src/MainDashboard.java) to understand runtime flow.
2. Open [src/DatabaseManager.java](src/DatabaseManager.java) and [CreateDatabase.java](src/CreateDatabase.java) for persistence details.
3. Inspect [DATABASE_STRUCTURE.md](DATABASE_STRUCTURE.md) for schema expectations.

If anything is unclear or a new workflow is required, ask the repo owner for the expected database backend and sample data files.

— End of guidance —
