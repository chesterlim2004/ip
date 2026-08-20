---
name: test-ui
description: Run fail-fast, command-by-command regression tests for this project's console UI from test/ui-test-plan.md, compare actual responses with exact expected output, and show a recorded console session. Use after every application code update, when console commands or messages change, or when asked to test the chatbot UI.
---

# Test UI

Run the console test plan as an exact, interactive regression suite. Keep one application process alive within each test case so later commands can inspect state created by earlier commands.

## Workflow

1. Read `test/ui-test-plan.md` completely.
2. If the code update changes commands, formatting, or observable console behavior, update the relevant cases and expected output before testing. Add a case when existing coverage does not exercise the change.
3. Ensure every case has an `id`, `aim`, and one or more exchanges containing `input` and `expected_output`. Mark the command that should terminate the application with `expect_exit: true`.
4. From the repository root, select Java 25 and run:

   ```bash
   source "$HOME/.sdkman/bin/sdkman-init.sh" && \
     sdk use java 25.0.3.fx-zulu >/dev/null && \
     python3 .codex/skills/test-ui/scripts/run_ui_tests.py
   ```

5. Preserve fail-fast behavior. On a mismatch, do not run another command or test case. Report the failed case, input, expected output, actual output, and the console record produced so far.
6. After a successful run, show the console input/output record and report the path `test/ui-test-session.md`.

## Test plan format

Store configuration and test cases in the first `json` code fence in `test/ui-test-plan.md`. Express each expected response as a list of exact console lines. Use variables such as `{{LINE}}` only when they are declared in the plan's top-level `variables` object.

The runner compiles all Java sources beneath the configured source directory, verifies that Java 25 is active, interacts with the program one command at a time, and writes a Markdown transcript. It requires only the Python standard library.

## Resource

Use `scripts/run_ui_tests.py` as the deterministic test runner. Do not replace its exact comparisons with substring checks.
