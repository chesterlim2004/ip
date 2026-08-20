# UI Test Session

## UI-01: Add todo, deadline, and event tasks and list their type-specific details

```text
____________________________________________________________
  ____ ______   ______ _____  _    _
 / ___|  _ \ \ / / ___|_   _|/ \  | |
| |   | |_) \ V /\___ \ | | / _ \ | |
| |___|  _ < | |  ___) || |/ ___ \| |___
 \____|_| \_\|_| |____/ |_/_/   \_\_____|

Hello!!! I'm Crystal.
[Commands:
- To add a todo, enter 'todo [description]'
- To add a deadline, enter 'deadline [description] /by [deadline]'
- To add an event, enter 'event [description] /from [start] /to [end]'
- To view your list, enter 'list'
- To mark a task as done, enter 'mark [task number]'
- To mark a task as not done, enter 'unmark [task number]'
- To exit, enter 'bye'

____________________________________________________________
You: todo read book
____________________________________________________________
Crystal: Got it. I've added this task:
         [T][ ] read book
         Now you have 1 task in the list.
____________________________________________________________
You: deadline do homework /by no idea :-p
____________________________________________________________
Crystal: Got it. I've added this task:
         [D][ ] do homework (by: no idea :-p)
         Now you have 2 tasks in the list.
____________________________________________________________
You: event project meeting /from Mon 2pm /to 4pm
____________________________________________________________
Crystal: Got it. I've added this task:
         [E][ ] project meeting (from: Mon 2pm to: 4pm)
         Now you have 3 tasks in the list.
____________________________________________________________
You: list
____________________________________________________________
Crystal: Here are the tasks in your list:
         1.[T][ ] read book
         2.[D][ ] do homework (by: no idea :-p)
         3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
You: bye
____________________________________________________________
Crystal: Bye!!! Hope to see you again soon!
____________________________________________________________
```

## UI-02: Mark and unmark a task while guarding against repeated status changes

```text
____________________________________________________________
  ____ ______   ______ _____  _    _
 / ___|  _ \ \ / / ___|_   _|/ \  | |
| |   | |_) \ V /\___ \ | | / _ \ | |
| |___|  _ < | |  ___) || |/ ___ \| |___
 \____|_| \_\|_| |____/ |_/_/   \_\_____|

Hello!!! I'm Crystal.
[Commands:
- To add a todo, enter 'todo [description]'
- To add a deadline, enter 'deadline [description] /by [deadline]'
- To add an event, enter 'event [description] /from [start] /to [end]'
- To view your list, enter 'list'
- To mark a task as done, enter 'mark [task number]'
- To mark a task as not done, enter 'unmark [task number]'
- To exit, enter 'bye'

____________________________________________________________
You: todo read book
____________________________________________________________
Crystal: Got it. I've added this task:
         [T][ ] read book
         Now you have 1 task in the list.
____________________________________________________________
You: mark 1
____________________________________________________________
Crystal: Nice! I've marked this task as done:
         [T][X] read book
____________________________________________________________
You: mark 1
____________________________________________________________
Crystal: You have already completed this task!
         [T][X] read book
____________________________________________________________
You: unmark 1
____________________________________________________________
Crystal: OK, I've marked this task as not done yet:
         [T][ ] read book
____________________________________________________________
You: unmark 1
____________________________________________________________
Crystal: You have not completed this task in the first place!
         [T][ ] read book
____________________________________________________________
You: bye
____________________________________________________________
Crystal: Bye!!! Hope to see you again soon!
____________________________________________________________
```

## UI-03: Reject task descriptions that do not specify a task type

```text
____________________________________________________________
  ____ ______   ______ _____  _    _
 / ___|  _ \ \ / / ___|_   _|/ \  | |
| |   | |_) \ V /\___ \ | | / _ \ | |
| |___|  _ < | |  ___) || |/ ___ \| |___
 \____|_| \_\|_| |____/ |_/_/   \_\_____|

Hello!!! I'm Crystal.
[Commands:
- To add a todo, enter 'todo [description]'
- To add a deadline, enter 'deadline [description] /by [deadline]'
- To add an event, enter 'event [description] /from [start] /to [end]'
- To view your list, enter 'list'
- To mark a task as done, enter 'mark [task number]'
- To mark a task as not done, enter 'unmark [task number]'
- To exit, enter 'bye'

____________________________________________________________
You: read book
____________________________________________________________
Crystal: Please specify if the task is a todo, deadline or event!
____________________________________________________________
You: list
____________________________________________________________
Crystal: Here are the tasks in your list:
____________________________________________________________
You: bye
____________________________________________________________
Crystal: Bye!!! Hope to see you again soon!
____________________________________________________________
```

Result: PASS (3 test cases)
