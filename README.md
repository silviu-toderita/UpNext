# UpNext

## Proposal

**UpNext** is a task management app that lets you **visualize time-sensitive tasks chronologically**. It emerged out of a need for the developer to keep track of all of his homework and personal projects based on due date, a feat that no other task management app could perform well. UpNext can keep track of many tasks at once and display a graph that makes it easy to understand the most important task to focus on.

UpNext will be used by anyone who needs a quick and easy way to manage and visualize their time-sensitive tasks. This includes students, employees, managers, entrepreneurs, and busy instructors. It has wide appeal and will implement unique features and a user-friendly interface to help it stand out from a sea of generic task-management apps. 

This project is interesting to me because chronological task-management has always been a pain. I personally feel stressed when I see a list of tasks I have to do and no immediate way of being able to tell how *busy* I actually am. Visualizing tasks chronologically would help me  feel more in-control of my work and life. For that reason, I'm excited to develop something that will be useful to me and I intend to actively use after development is complete.

## User Stories

- As a user, I want to be able to add a task to my task list
- As a user, I want to have the option of specifying a due date for a task when adding it to my task list
- As a user, I want to edit an existing task
- As a user, I want to mark a task as completed
- As a user, I want to view my remaining tasks chronologically
- As a user, I want my tasks to save automatically whenever I add a new task, change a task, or complete a task
- As a user, I want my tasks to be loaded automatically when I start the application

## Dependencies

- [com.diogonunes:JColor:5.0.1](https://github.com/dialex/JColor)
- [org.json:20200518](https://github.com/stleary/JSON-java)
- [com.toedter.calendar.JCalendar](https://toedter.com/jcalendar/)

## Phase 4: Task 2

For this task, I have made the *TaskList* class in the *main.model* package robust:
- All of the methods in this class are robust:
    - Each method has a specification that includes all possible input values to that method
- The method *add(Task task)* in the *TaskList* class throws a new exception if the given *Task* is already in the TaskList: *DuplicateTaskException*
    - This exception is defined in the *DuplicateTaskException* class of the *main.exceptions* package
    - This exception extends *Exception*, making it a checked exception
    - This exception is tested in the *TaskListTest* class of the *test.model* package:
        - The method *testAddTaskEmptyList()* on line 42 does not expect a *DuplicateTaskException*, because a single *Task* is added to an empty *TaskList*
        - The method *testAddTaskDuplicate()* on line 87 *does* expect a *DuplicateTaskException*, because the same *Task* is added to a *TaskList* twice