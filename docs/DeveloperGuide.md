---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# RecruitTrack Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a applicant).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th applicant in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new applicant. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the applicant was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the applicant being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* HR teams handling candidate pipelines
* Manages multiple candidate pipelines
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps
* are data-driven decision makers
* works in a face-paced environment

**Value proposition**: Simplifies candidate tracking by organizing applicant information,
communication history, and hiring stages for quick access in a fast, no-frills CLI environment


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority      | As a …​                             | I want to …​                                                                         | So that I can…​                                   |
|---------------|-------------------------------------|--------------------------------------------------------------------------------------|---------------------------------------------------|
| `* * *`	| user	| specify the stage of the application process each applicant is in	| keep track of each applicant’s progress |
| `* * *`	| user	| filter by an applicant’s application process	| keep track of which applicants are left in each stage |
| `* * *`	| user	| search for a candidate by name or job position	| quickly find relevant information |
| `* * *`	| user	| view a summary of all candidates in a specific hiring stage	| prioritize my workload |
| `* * *`	| user	| view statistics (e.g., number of hired candidates, time needed in each stage)	| evaluate my recruitment process |
| `* * *`	| user	| sort candidates by the date they applied	| review the most recent applications first |
| `* * *`	| user	| add candidates’ details	| track them easily |
| `* * *`	| user	| view a candidate’s status	| know their hiring progress |
| `* * *`	| user	| move candidates to different stages	| manage the hiring process |
| `* * `        | user                                | group applicants based on what jobs they are applying for                            | manage applicants from different jobs more easily |
| `* *`  | 	power user	                        | delete multiple candidates with a single command                                     | 	keep my database clean                           |
| `* *`  | 	power user	                        | tag candidates	                                                                      | easily classify and retrieve important applicants |
| `* *`  | forgetful user                      | 	have timely reminders for scheduled interviews                                      | 	not miss them                                    |
| `* *`  | user who is not familiar with CLIs  | have easy access to a list of supported commands, especially the commonly used ones  | get up to speed with using the CLI                |
| `* *`	| user	| attach a photo to each applicant	| identify the applicants easily                    |
| `* *`	| user	| add notes to each candidate’s profile	| record important details from interviews or assessments |
| `* *`	| user	| bulk update candidate statuses	| save time |
| `* *`	| user	| set deadlines for each hiring stage	| ensure the recruitment process stays on track |
| `* *`	| user	| export candidate data as a CSV file	| analyze and share information easily with my team |
| `* *`	| power user	| schedule automated status updates for candidates (e.g., send follow-up emails after a week)	| streamline my communication workflow |
| `* *`	| user	| assign different access levels to team members	| protect sensitive candidate data while still allowing collaboration |
| `* *`	| user	| add a simple rating (e.g., 1-5 stars) to candidates	| quickly assess their potential |
| `* *`	| user	| assign a candidate to a specific recruiter	| ensure proper follow-up and accountability |
| `*`	| user	| search for particular keywords in past communication history	| revisit specific conversations I’ve had |
| `*`	| long-time user	| delete communication history for specific contacts	| free up storage space when needed |
| `*`	| long-time user	| have the option to assign new shortcuts or keywords to my frequently used commands	| navigate the application more efficiently |
| `*`	| user	| receive notifications when a candidate has been in a stage for too long	| take action to move them forward |
| `*`	| user	| restore mistakenly deleted candidates within a certain time frame	| recover important information if needed |
| `*`	| user	| integrate RecruitTrack with my email client	| track all correspondence with candidates in one place |
| `*`	| user	| generate customized reports based on hiring trends and candidate performance	| make data-driven recruitment decisions |
| `*`	| user	| log communication history	| remember past interactions |
| `*`	| user	| send an email template from the CLI	| quickly communicate with candidates |
| `*`	| user	| view a candidate’s resume and notes before an interview	| prepare relevant questions |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `RecruitTrack` and the **Actor** is the `user`, unless specified otherwise)

**Use case: UC01 - Delete a applicant**

**MSS:**

1.  User requests to list applicants
2.  RecruitTrack shows a list of applicants
3.  User requests to delete a specific applicant in the list
4.  RecruitTrack deletes the applicant

    Use case ends.

**Extensions:**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. RecruitTrack shows an error message.

      Use case resumes from step 2.

**Use case: UC02 - Add a applicant**

**MSS:**

1.  User requests to add a applicant by providing the details
2.  RecruitTrack adds the applicant with the details into the list

    Use case ends.

**Extensions:**

* 1a. The details don't follow the correct format.

    * 1a1. RecruitTrack shows an error message.

* 1b. Lacks mandatory details.

    * 1b1. RecruitTrack shows an error message.

**Use case: UC03 - Edit a applicant**

**MSS:**

1.  User requests to list applicants
2.  RecruitTrack shows a list of applicants
3.  User requests to edit a specific applicant in the list and provides the new details for that applicant
4.  RecruitTrack edit the applicant's details

    Use case ends.

**Extensions:**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. RecruitTrack shows an error message.

      Use case resumes from step 2.

* 3b. The new details don't follow the correct format.

    * 3b1. RecruitTrack shows an error message.

      Use case resumes from step 2.

**Use case: UC04 - Find a applicant**

**MSS:**

1.  User requests to find a applicant by providing the applicant's details.
2.  RecruitTrack find applicants with the given details.

    Use case ends.

**Extensions:**

* 2a. The list is empty.

  Use case ends.

### Non-Functional Requirements
1. Should run on any mainstream OS (Windows, macOS, Linux) with Java 17 or above installed.
2. Should handle up to 1000 candidates without noticeable lag in typical usage.
3. Common commands (e.g., adding candidates, updating statuses, searching) should execute within 200ms under normal load.
4. A user with above-average typing speed should be able to perform most tasks faster using commands than using a mouse.
5. Should provide clear error messages and usage hints for invalid commands.
6. Candidate data should be stored securely, ensuring unauthorized users cannot access sensitive information.
7. Data should be persistently stored and not lost between sessions.

### Glossary

- **Mainstream OS** - Windows, Linux, Unix, MacOS
- **Private contact detail** - A contact detail that is not meant to be shared with others
- **Applicant** – A applicant who has applied for a job and is stored as a contact in the system.
- **Application Metadata** – Information related to an applicant’s job application, such as job position, status, and contact details.
- **Contact** – A stored applicant or candidate with essential details such as name, email, phone, job position, and application status.
- **Custom Status** – A user-defined application status when the `--custom` flag is used.
- **Error Message** – A notification displayed when an invalid input or operation occurs (e.g., invalid email format, missing required fields).
- **Email Validation** – A process ensuring the provided email follows a valid format (e.g., `user@domain.com`).
- **`--force` Flag** – A command option that allows updating an existing contact if a duplicate email or phone number is found.
- **Identifier Type** – A flag (`-n`, `-e`, `-p`, `-id`) used to specify whether a contact is searched by name, email, phone number, or system ID.
- **Job Position** – The role or position that an applicant is applying for, stored in the system.
- **Parameter** – A required or optional value provided in a command (e.g., `NAME`, `EMAIL`, `STATUS`).
- **Status Update** – A change in an applicant’s application stage using the `update` command.
- **System ID** – A unique numerical identifier assigned to each contact in the system.
- **Unique Identifier** – A distinct value (email, phone number, or system ID) used to identify contacts.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a applicant

1. Deleting a applicant while all applicants are being shown

   1. Prerequisites: List all applicants using the `list` command. Multiple applicants in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No applicant is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
