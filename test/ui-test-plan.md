# Console UI Test Plan

The runner compiles every Java source under `src/main/java`, starts a fresh `Crystal` process in an isolated working directory for each test case, and checks each response before sending the next command. It supports case-specific starting files and compares expected saved files exactly without touching the developer's real data file. Console output is compared exactly after normalizing line endings. The shared `LINE` variable represents the application's horizontal divider.

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
    "working_dir": "_temp/ui-test-work",
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
            "Crystal: Got it! I've added this task:",
            "         [T][ ] read book",
            "         Now you have 1 task in the list.",
            "{{LINE}}"
          ]
        },
        {
          "input": "deadline do homework /by no idea :-p",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Got it! I've added this task:",
            "         [D][ ] do homework (by: no idea :-p)",
            "         Now you have 2 tasks in the list.",
            "{{LINE}}"
          ]
        },
        {
          "input": "event project meeting /from Mon 2pm /to 4pm",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Got it! I've added this task:",
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
            "Crystal: Got it! I've added this task:",
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
      "aim": "Reject commands that do not begin with a recognized task type",
      "exchanges": [
        {
          "input": "read book",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Oopsies!!! I don't know what that means :-(",
            "{{LINE}}"
          ]
        },
        {
          "input": "list",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Your task list is empty!",
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
      "id": "UI-04",
      "aim": "Report malformed list and task commands and invalid task numbers as Crystal exceptions",
      "exchanges": [
        {
          "input": "list ",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Oopsies!!! To view your task list, simply enter 'list'!",
            "{{LINE}}"
          ]
        },
        {
          "input": "todo",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Oopsies!!! A todo must have a description!",
            "{{LINE}}"
          ]
        },
        {
          "input": "deadline",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Oopsies!!! A deadline must have a description and a /by time!",
            "{{LINE}}"
          ]
        },
        {
          "input": "event",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Oopsies!!! An event must have a description, a /from time and a /to time!",
            "{{LINE}}"
          ]
        },
        {
          "input": "mark",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Oopsies!!! You have to mark a task number!",
            "{{LINE}}"
          ]
        },
        {
          "input": "mark abc",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Oopsies!!! You have to mark a task number!",
            "{{LINE}}"
          ]
        },
        {
          "input": "unmark",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Oopsies!!! You have to unmark a task number!",
            "{{LINE}}"
          ]
        },
        {
          "input": "delete abc",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Oopsies!!! You have to delete a task number!",
            "{{LINE}}"
          ]
        },
        {
          "input": "mark 1",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Oopsies!!! That task number does not exist!",
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
      "id": "UI-05",
      "aim": "Delete a task and shift the remaining tasks forward in the list",
      "exchanges": [
        {
          "input": "todo read book",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Got it! I've added this task:",
            "         [T][ ] read book",
            "         Now you have 1 task in the list.",
            "{{LINE}}"
          ]
        },
        {
          "input": "event project meeting /from Aug 6th 2pm /to 4pm",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Got it! I've added this task:",
            "         [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)",
            "         Now you have 2 tasks in the list.",
            "{{LINE}}"
          ]
        },
        {
          "input": "todo borrow book",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Got it! I've added this task:",
            "         [T][ ] borrow book",
            "         Now you have 3 tasks in the list.",
            "{{LINE}}"
          ]
        },
        {
          "input": "delete 2",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Noted. I've removed this task:",
            "         [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)",
            "         Now you have 2 tasks in the list.",
            "{{LINE}}"
          ]
        },
        {
          "input": "list",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Here are the tasks in your list:",
            "         1.[T][ ] read book",
            "         2.[T][ ] borrow book",
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
      "id": "UI-06",
      "aim": "Exercise every task format and mutation that must be reflected in the saved data file",
      "exchanges": [
        {
          "input": "todo read book",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Got it! I've added this task:",
            "         [T][ ] read book",
            "         Now you have 1 task in the list.",
            "{{LINE}}"
          ]
        },
        {
          "input": "deadline return book /by June 6th",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Got it! I've added this task:",
            "         [D][ ] return book (by: June 6th)",
            "         Now you have 2 tasks in the list.",
            "{{LINE}}"
          ]
        },
        {
          "input": "event project meeting /from Aug 6th 2pm /to 4pm",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Got it! I've added this task:",
            "         [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)",
            "         Now you have 3 tasks in the list.",
            "{{LINE}}"
          ]
        },
        {
          "input": "mark 2",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Nice! I've marked this task as done:",
            "         [D][X] return book (by: June 6th)",
            "{{LINE}}"
          ]
        },
        {
          "input": "todo join sports club",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Got it! I've added this task:",
            "         [T][ ] join sports club",
            "         Now you have 4 tasks in the list.",
            "{{LINE}}"
          ]
        },
        {
          "input": "delete 4",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Noted. I've removed this task:",
            "         [T][ ] join sports club",
            "         Now you have 3 tasks in the list.",
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
      ],
      "expected_files": {
        "data/crystal.txt": [
          "T | 0 | read book",
          "D | 1 | return book | June 6th",
          "E | 0 | project meeting | Aug 6th 2pm | 4pm"
        ]
      }
    },
    {
      "id": "UI-07",
      "aim": "Load saved tasks on restart and persist every mutation against that existing list",
      "initial_files": {
        "data/crystal.txt": [
          "T | 0 | read book",
          "D | 1 | return book | June 6th",
          "E | 0 | project meeting | Aug 6th 2pm | 4pm"
        ]
      },
      "exchanges": [
        {
          "input": "list",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Here are the tasks in your list:",
            "         1.[T][ ] read book",
            "         2.[D][X] return book (by: June 6th)",
            "         3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)",
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
          "input": "unmark 2",
          "expected_output": [
            "{{LINE}}",
            "Crystal: OK, I've marked this task as not done yet:",
            "         [D][ ] return book (by: June 6th)",
            "{{LINE}}"
          ]
        },
        {
          "input": "todo join sports club",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Got it! I've added this task:",
            "         [T][ ] join sports club",
            "         Now you have 4 tasks in the list.",
            "{{LINE}}"
          ]
        },
        {
          "input": "delete 3",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Noted. I've removed this task:",
            "         [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)",
            "         Now you have 3 tasks in the list.",
            "{{LINE}}"
          ]
        },
        {
          "input": "list",
          "expected_output": [
            "{{LINE}}",
            "Crystal: Here are the tasks in your list:",
            "         1.[T][X] read book",
            "         2.[D][ ] return book (by: June 6th)",
            "         3.[T][ ] join sports club",
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
      ],
      "expected_files": {
        "data/crystal.txt": [
          "T | 1 | read book",
          "D | 0 | return book | June 6th",
          "T | 0 | join sports club"
        ]
      }
    }
  ]
}
```

After `UI-07`, the runner verifies that `data/crystal.txt` contains exactly:

```text
T | 1 | read book
D | 0 | return book | June 6th
T | 0 | join sports club
```
