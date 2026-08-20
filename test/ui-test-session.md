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
- To delete a task, enter 'delete [task number]'
- To exit, enter 'bye']
____________________________________________________________
You: todo read book
____________________________________________________________
Crystal: Got it! I've added this task:
         [T][ ] read book
         Now you have 1 task in the list.
____________________________________________________________
You: deadline do homework /by no idea :-p
____________________________________________________________
Crystal: Got it! I've added this task:
         [D][ ] do homework (by: no idea :-p)
         Now you have 2 tasks in the list.
____________________________________________________________
You: event project meeting /from Mon 2pm /to 4pm
____________________________________________________________
Crystal: Got it! I've added this task:
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
- To delete a task, enter 'delete [task number]'
- To exit, enter 'bye']
____________________________________________________________
You: todo read book
____________________________________________________________
Crystal: Got it! I've added this task:
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

## UI-03: Reject commands that do not begin with a recognized task type

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
- To delete a task, enter 'delete [task number]'
- To exit, enter 'bye']
____________________________________________________________
You: read book
____________________________________________________________
Crystal: Oopsies!!! I don't know what that means :-(
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

## UI-04: Report malformed task commands and invalid task numbers as Crystal exceptions

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
- To delete a task, enter 'delete [task number]'
- To exit, enter 'bye']
____________________________________________________________
You: todo
____________________________________________________________
Crystal: Oopsies!!! A todo must have a description!
____________________________________________________________
You: deadline
____________________________________________________________
Crystal: Oopsies!!! A deadline must have a description and a /by time!
____________________________________________________________
You: event
____________________________________________________________
Crystal: Oopsies!!! An event must have a description, a /from time and a /to time!
____________________________________________________________
You: mark abc
____________________________________________________________
Crystal: Oopsies!!! Please enter a valid task number!
____________________________________________________________
You: mark 1
____________________________________________________________
Crystal: Oopsies!!! That task number does not exist!
____________________________________________________________
You: bye
____________________________________________________________
Crystal: Bye!!! Hope to see you again soon!
____________________________________________________________
```

## UI-05: Delete a task and shift the remaining tasks forward in the list

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
- To delete a task, enter 'delete [task number]'
- To exit, enter 'bye']
____________________________________________________________
You: todo read book
____________________________________________________________
Crystal: Got it! I've added this task:
         [T][ ] read book
         Now you have 1 task in the list.
____________________________________________________________
You: event project meeting /from Aug 6th 2pm /to 4pm
____________________________________________________________
Crystal: Got it! I've added this task:
         [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
         Now you have 2 tasks in the list.
____________________________________________________________
You: todo borrow book
____________________________________________________________
Crystal: Got it! I've added this task:
         [T][ ] borrow book
         Now you have 3 tasks in the list.
____________________________________________________________
You: delete 2
____________________________________________________________
Crystal: Noted. I've removed this task:
         [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
         Now you have 2 tasks in the list.
____________________________________________________________
You: list
____________________________________________________________
Crystal: Here are the tasks in your list:
         1.[T][ ] read book
         2.[T][ ] borrow book
____________________________________________________________
You: bye
____________________________________________________________
Crystal: Bye!!! Hope to see you again soon!
____________________________________________________________
```

Result: PASS (5 test cases)
