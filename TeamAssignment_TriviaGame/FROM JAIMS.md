# From Jaims, 20 Nov: your project, with my half in it

Same folders as the zip you sent (src/triviaGame, bin with the .class files, your Java 25 + module-info). What changed:

- **Your three files** (Question, FileHandler, QuizManager) are yours as sent, plus:
  - `@author` now says Jaims (it said James).
  - In `QuizManager.answer()` the scoring is commented out for the video, as her brief asks for code still in progress. The note there
    lists the three fixes: check the pick before adding points, count tries up on a wrong pick, move on only after checking.
  - At the bottom of QuizManager, a few small methods my screens need (marked "Added by Jaims"). Rename, move or replace any of them.
- **My half**: TriviaGUI, GamePanel, ScoreEntry, DepthPanel, DepthButton, plus images/ and sounds/. Run TriviaGUI.
  Answering won't move on yet, because answer() is commented out: that's the half that's yours.
- **Tests**: `test/triviaGame/CooperTest.java`, JUnit 5, written to your code as it is. 4 pass now. 3 are `@Disabled` until answer() comes back:
  take the `@Disabled` line off each one when it's ready, and they check your 4 / 2 / 1 scoring.
- **Data ideas**: `questions-ideas.txt` is my 20 questions in YOUR format (answers counted from 1, like Rome = 2), and
  `highscores-ideas.txt` is 12 sample scores (name|points|avatar). Use, trim, or ignore them.
  ⚠ One thing to agree on: your answers count from 1; my screens hand QuizManager the button as 0 to 3 (A = 0). Either is fine; we just
  have to pick one.

If Eclipse complains about modules when you run the tests, right-click the project → Build Path → Configure Build Path → Libraries, and move
JUnit 5 to the Classpath (not the Modulepath). Leave me notes in the "Notes from Cooper" part of any doc comment.
