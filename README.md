# Trivia Game · CS 1410 team project

A quiz in one window: type a name, pick an avatar, answer multiple choice, see the high scores. Java Swing, built by Jaims and Cooper.

## Run it
Eclipse: File → Import → General → Existing Projects into Workspace → pick `TeamAssignment_TriviaGame`. Run `TriviaGUI`.
Tests: `test/triviaGame/CooperTest.java` (JUnit 5).

## Who wrote what
- **Cooper**: `Question`, `FileHandler`, `QuizManager` (the back end).
- **Jaims**: `TriviaGUI`, `GamePanel`, `ScoreEntry`, `DepthPanel`, `DepthButton`, the art and sounds (the front end), and the tests.
Each file's `@author` says the same. Notes to each other go in a file's doc comment, under "Notes from Cooper".

## Dropping files
Put a dated copy in `drops/` under the date and your name, like `drops/2026-11-20-jaims/`. Nothing in `drops/` is ever overwritten,
so anyone can pull down any version.
