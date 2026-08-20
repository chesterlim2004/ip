# Console UI Test Plan

The runner compiles every Java source under `src/main/java`, starts a fresh `Crystal` process for each test case, and checks each response before sending the next command. Console output is compared exactly after normalizing line endings. The shared `LINE` variable represents the application's horizontal divider.

Each test case below specifies its aim, command inputs, and expected output. An exchange with `expect_exit` must cause the program to terminate successfully.

```json
{
  "prompt": "You: ",
  "timeout_seconds": 5,
  "variables": {
    "LINE": "____________________________________________________________"
  },
  "program": {
    "source_dir": "src/main/java",
    "classes_dir": "_temp/ui-test-classes",
    "main_class": "Crystal"
  },
  "test_cases": [
    {
      "id": "UI-01",
      "aim": "Add todo, deadline, and event tasks and list their type-specific details",
      "exchanges": [
        {
          "input": "todo read book",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Got it. I've added this task:",
            "         [T][ ] read book",
            "         Now you have 1 task in the list.",
            "{{LINE}}"
          ]
        },
        {
          "input": "deadline do homework /by no idea :-p",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Got it. I've added this task:",
            "         [D][ ] do homework (by: no idea :-p)",
            "         Now you have 2 tasks in the list.",
            "{{LINE}}"
          ]
        },
        {
          "input": "event project meeting /from Mon 2pm /to 4pm",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Got it. I've added this task:",
            "         [E][ ] project meeting (from: Mon 2pm to: 4pm)",
            "         Now you have 3 tasks in the list.",
            "{{LINE}}"
          ]
        },
        {
          "input": "list",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Here are the tasks in your list:",
            "         1.[T][ ] read book",
            "         2.[D][ ] do homework (by: no idea :-p)",
            "         3.[E][ ] project meeting (from: Mon 2pm to: 4pm)",
            "{{LINE}}"
          ]
        },
        {
          "input": "bye",
          "expect_exit": true,
          "expected_output": [
            "{{LINE}}",
            "Crystal: Bye!!! Hope to see you again soon!",
            "{{LINE}}"
          ]
        }
      ]
    },
    {
      "id": "UI-02",
      "aim": "Mark and unmark a task while guarding against repeated status changes",
      "exchanges": [
        {
          "input": "todo read book",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Got it. I've added this task:",
            "         [T][ ] read book",
            "         Now you have 1 task in the list.",
            "{{LINE}}"
          ]
        },
        {
          "input": "mark 1",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Nice! I've marked this task as done:",
            "         [T][X] read book",
            "{{LINE}}"
          ]
        },
        {
          "input": "mark 1",
          "expected_output": [
            "{{LINE}}",
            "Crystal: You have already completed this task!",
            "         [T][X] read book",
            "{{LINE}}"
          ]
        },
        {
          "input": "unmark 1",
          "expected_output": [
            "{{LINE}}",
            "Crystal: OK, I've marked this task as not done yet:",
            "         [T][ ] read book",
            "{{LINE}}"
          ]
        },
        {
          "input": "unmark 1",
          "expected_output": [
            "{{LINE}}",
            "Crystal: You have not completed this task in the first place!",
            "         [T][ ] read book",
            "{{LINE}}"
          ]
        },
        {
          "input": "bye",
          "expect_exit": true,
          "expected_output": [
            "{{LINE}}",
            "Crystal: Bye!!! Hope to see you again soon!",
            "{{LINE}}"
          ]
        }
      ]
    },
    {
      "id": "UI-03",
      "aim": "Reject task descriptions that do not specify a task type",
      "exchanges": [
        {
          "input": "read book",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Please specify if the task is a todo, deadline or event!",
            "{{LINE}}"
          ]
        },
        {
          "input": "list",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Here are the tasks in your list:",
            "{{LINE}}"
          ]
        },
        {
          "input": "bye",
          "expect_exit": true,
          "expected_output": [
            "{{LINE}}",
            "Crystal: Bye!!! Hope to see you again soon!",
            "{{LINE}}"
          ]
        }
      ]
    }
  ]
}
```
