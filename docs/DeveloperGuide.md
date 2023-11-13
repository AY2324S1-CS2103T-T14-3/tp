---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# HealthSync Developer Guide

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
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point).

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

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).
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

Classes used by multiple components are in the `seedu.addressbook.commons` package.

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

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how the undo operation works:

<puml src="diagrams/UndoSequenceDiagram.puml" alt="UndoSequenceDiagram" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

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
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### Implementation of Singular, Optional Fields

#### Proposed Implementation

A way to add a property to `Person` that could be nullable was needed, to reflect optionality within our
field implementation. While optional fields like `MedicalHistory` exist in HealthSync, these fields are
Collections, and can natively handle the empty state. Singular optional properties can be added this way,
but is hard to distinguish between properties that allow for multiple entries and fields that don't.

<puml src="diagrams/AppointmentClassDiagram0.puml" width="250" />

The optional fields could be implemented directly to `Person` as shown in the partial class diagram above.
However, several other packages depend upon `Person` as well, including `UI` and `Storage`.
These packages already make assumptions on the `non-null` property on the variables of `Person`.

<puml src="diagrams/AppointmentSequence0.puml" width="250" />

The diagram above illustrates a possible path that may arise if the optional property of the field is
not explicitly defined. There is a need to explicitly denote that our optional field is possibly an
empty value without having its implementers perform the check themselves, so that the compiler is
able to assist in our coding.

<puml src="diagrams/AppointmentClassDiagram1.puml" width="250" />

Therefore, the implementation of optional fields now return its value wrapped in the Java
`Optional` wrapper. In this example, when `getAppointment` is called now, the implementer will be
informed of the type mismatch with the `Appointment` type, explicitly informing the implementer that
this value is potentially `null`. By doing it in this way, we also guarantee to the implementer that
the fields that do not generate an `Optional` wrapper cannot be empty.

#### Design Considerations:

**Aspect: Handling of Optional Fields**

* **Alternative 1 (current choice):** Allow `null` values
  * Pros: Simple to implement, and easy to understand what it means.
  * Cons: Checks for null value required for every implementation of the optional field.
    * This is partially circumvented using the `Optional` wrapper, which caused the team to favor
      this implementation.
* **Alternative 2:** Use abstracted "empty-value" objects that extend the given field
  * Pros: No check of the empty state required by implementers, allows for simple code outside its
          implementation.
  * Cons: Non-negligible abstraction required to create an empty object and have it work in all cases
          where the empty case is needed.

### Implementation of Appointment Field

`Appointment` is a special field belonging to the `Person` model class, as it implicitly stores a temporal
relationship within itself.

#### Proposed Implementation
`Appointment` is distinctly different from other fields in `Person` in that it cannot store its values as
a String directly - otherwise, this would complicate the process of defining temporal relationships within itself.

<puml src="diagrams/AppointmentClassDiagram2.puml" width="250" />

Above is a partial class diagram of `Appointment`. Note that several static members were excluded as they are not
relevant to its data-structure properties in `HealthSync`.

The default Java packages provides its implementation of temporal objects in the `java.time` package. In particular,
`LocalDateTime` and its variants were the most relevant to us, as it allows the user to record time without needing
to account for timezone differences. This is powerful, as our target audience is not expected to change locations in
a significant way that causes them to change time regions entirely.

With the use of the `java.time` package, the project could not use only `regex` for `Appointment`.
This is due to the level of checks required to parse a temporal object, with the amount of dependencies that exist
between the day, month and year fields. The provided `DateTimeFormatter` and `DateTimeFormatterBuilder` classes helps
create the parser objects used for `Appointment`. However, the classes do not account for the combined time format that
HealthSync requests of its users.

Therefore, `Appointment` uses a combination of `regex` and `DateTimeFormatter` to resolve its user input.

<puml src="diagrams/AppointmentActivity0.puml" width="250" />

A partial activity diagram illustrating the relevant segment of the parse process.

As seen above, `ParserUtil` verifies if the Appointment user input is trivially valid using
`regex`, before passing the input into the `of` constructor. `DateTimeFormatter` cannot fully verify input strings
against its format without creating a `LocalDate`/`LocalTime` object as a side effect,
so `of` handles a portion of the parse.

#### Design Considerations:

**Aspect: Value to store `Appointment` as**

* **Alternative 1:** Use of raw `String` format for Appointment
    * Pros: Far easier to parse and store as an object.
    * Cons: Hard to extend upon in future use-cases, such as reminders, etc.

* **Alternative 2 (current choice):** Use of Java Temporal-related objects for Appointment
    * Pros: More direct paths of feature extension, such as searching by time period.
    * Cons: Translation to and from Java Temporal objects can be non-trivial.

**Aspect: Constructor for `Appointment` to manage valid user input**

* **Alternative 1:** Directly use constructors for `Appointment`, and using `isValidAppointment` to verify input
    * Pros: Consistency with construction of other fields
    * Cons:
      * Construction called before user input is verified so exception thrown may not be intuitively understood
      * Concerns over failed appointment creation not getting collected by Java Garbage Collection due to accessing
       `now()` resource
      * User input validation for temporal objects create the relevant object as a side effect, wasting resources to
        construct the temporal object twice.

* **Alternative 2 (current choice):** Use of `of` factory method for `Appointment`, and verifying input inside `of`
    * Pros:
      * Explicit demarcation of `Appointment` as a class that can throw an `Exception` during construction
      * Factory method can double as an explicit user verification method
      * Construction of `Appointment` only performed once user input is verified
    * Cons:
      * "Uniqueness" of private constructor for this field only may cause confusion when extending the app
      * Appointment handles part of `parse` for `ParserUtil`

**Aspect: Parsing of `Appointment` Field as multiple fields or single field**

* **Alternative 1 (current choice):** Use of the single `ap/` flag.
  * Pros: Easy to input on the user-end.
  * Cons: Hard to separate time fields, could be troublesome to implement a parse format string.

* **Alternative 2:** Use of 2 flags to denote start and end time for appointment.
  * Pros: Immediate clarity on what fields to implement, and how to parse input string.
  * Cons: Strong dependence between 2 flags requires more fail-state management.

### Delete Feature

#### Description

The `DeleteCommand` allows users to delete a patient's profile or a specified field from the patient's profile.

#### Implementation Details

The `DeleteCommand` is implemented as follows:
- **Command Word**: The command word for this feature is `delete`
- **Usage**: Users invoke the `DeleteCommand` by specifying the command word, followed by the name or IC of the person they wish to delete and any fields they wish to delete.
- **Command Format**: 
  - `delete n/Name [Fields] ...`
  - `delete id/IC_Number [Fields] ...`
  - `delete n/Name id/IC_Number [Fields] ...`
- **DeletePersonDescriptor**: The `DeleteCommand` relies on an `DeletePersonDescriptor` to capture which fields the user wishes to delete from the patient's profile. The descriptor will be passed to the `DeleteCommand` to execute the deletion. Currently, only `Appointment` and `MedicalHistory` can be deleted as they are optional.
- **Validation**: The `DeleteCommand` performs validation to ensure that the IC or Name provided is valid.
- **Execution**: When executed, the `DeleteCommand` identifies the patient to be deleted based on the provided name or IC. When the patient is found, if no there are no specified fields to delete, the entire patient profile will be deleted from the database. Otherwise, the specified fields will be deleted from the patient's profile.

The following activity diagram summarizes what happens when a user executes a new command:

<puml src=diagrams/DeleteActivityDiagram.puml width="250"/>

#### Rationale

- **Flexibility**: The `DeleteCommand` provides flexibility to users, allowing them to choose what to be deleted from the patient's profile, instead of an "all-or-nothing" approach.
- **Data Accuracy**: The `DeleteCommand` allows users to delete outdated or incorrect information from the patient's profile, ensuring that the database is up-to-date and accurate.
- **Privacy and Compliance**: The `DeleteCommand` supports "right to erasure" under the PDPA, allowing users to delete patient's information from the database when requested.

#### Alternative Implementation

- **Alternative 1**: The `DeleteCommand` could be implemented as a `DeleteFieldCommand` and a `DeletePersonCommand`. The `DeleteFieldCommand` will delete the specified fields from the patient's profile, while the `DeletePersonCommand` will delete the entire patient profile from the database. This approach will require the user to invoke two commands to delete a patient's profile and the specified fields from the patient's profile. This approach is not chosen as it is less intuitive and requires more effort from the user.


### Addition of Interface for Find-type commands

#### Proposed Implementation

_{Explain how there is overlap in function for `find`, `delete`, `edit`}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_

### Improved GUI

_The Graphical User Interface (GUI) is designed using JavaFX and employs a combination of HBox, VBox, and StackPane layouts. It also utilizes specific color choices to create an appealing and user-friendly interface._

_Developers can use this information as a reference when working with the HealthSync GUI code._

#### Layout Structure:

The primary layout structure for the HealthSync GUI is based on HBox and VBox containers, which allow for a flexible arrangement of UI elements.

1. HBox
   Location: The HBox is the top-level container in the GUI and spans the entire application window.

2. VBox
   Location: There are two VBox containers within the HBox, which are responsible for organizing various elements of the GUI.

StackPane
The StackPane is used to organize specific UI elements within the VBox containers. It allows for the layering of elements and effective management of screen real estate.

1. StackPane (Person List)

   Location: Inside the first VBox (fx:id="personList").
2. StackPane (Logger Panel)

    Location: Also inside the first VBox (fx:id="personList").
3. StackPane (Result Display, Command Box, and Status Bar)

    Location: These StackPanes are located inside the second VBox

#### Color Choice:

The HealthSync GUI utilizes specific color choices to create a visually pleasing and organized interface, while still maintaining the original Dark Theme.

1. Primary Colour: #43314E
2. Secondary Colour: #231335


### Edit Feature

#### Description

The `EditCommand` allows users to modify the details of an existing person within the address book.

#### Implementation Details

The `EditCommand` is implemented as follows:
- **Command Word**: The command word for this feature is `edit`.
- **Usage**: Users invoke the `EditCommand` by specifying the command word, followed by the name or IC of the person they wish to edit and the fields they wish to modify.
- **Command Format**: 
  - `edit n/Name [Fields] ...`
  - `edit id/IC_Number [Fields] ...`
  - `edit n/Name id/IC_Number [Fields] ...`
- **EditPersonDescriptor**: The `EditCommand` relies on an `EditPersonDescriptor` to capture the details to edit the person with. This descriptor allows for updating various attributes of the person, such as phone, email, address, appointment, and medical histories.
- **Validation**: The `EditCommand` performs validation to ensure at least one field to edit is provided. It also checks for consistency when both a name and IC are provided.
- **Execution**: When executed, the `EditCommand` identifies the person to edit based on the provided name and/or IC. If the person is found, it creates an `editedPerson` with the desired changes. The person is then updated with the new details.

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/EditActivityDiagram.puml" width="250" />

#### Rationale

- **Flexibility**: The `EditCommand` provides flexibility to users by allowing them to choose whether to edit a person by name or IC, as per their convenience.
- **Maintaining Data Integrity**: The feature is designed to maintain the integrity of the address book by updating existing entries rather than creating new ones.

#### Alternatives Considered

- **Alternative 1**: Using Numbering Index to specify the person to edit. In this approach, users would provide the index of the person based on the list instead of specifying a name or IC. For example, they could use a command like `edit 1 p/93029393` to edit the first person of the list with the phone number.
    - **Pros**:
        - **Simplicity**: Using an index is straightforward and doesn't require specifying a name or IC.
        - **Reduced Ambiguity**: Using an index avoids potential ambiguity when multiple individuals have the same name.

    - **Cons**:
         - **Lack of Context**: Users might find it challenging to remember the index of a particular person, especially in a large address book.
         - **Potential Errors**: If the list of persons changes (e.g., due to deletions or additions), the numbering index could become outdated, leading to errors.
         - **Limited Identifiability**: Index numbers do not provide any context about the person, which may be confusing when there are multiple people with the same name or similar information.

### Find Feature

#### Description

The `FindCommand` allows users to find existing person(s) within the patient list, using their name or NRIC, and view their field data.

#### Implementation Details

The `FindCommand` is implemented as follows:
- **Command Word**: The command word for this feature is `find`.
- **Usage**: Users invoke the `FindCommand` by specifying the command word, followed by the name or NRIC of the person(s) they wish to find.
- **Command Format**: 
  - `find n/Name [Fields] ...`
  - `find id/IC_Number [Fields] ...`
  - `find n/Name id/IC_Number [Fields] ...`
- **`execute` method**: The `FindCommand` executes the search by using the specified predicates (`NameContainsKeywordsPredicate` or `IdContainsKeywordsPredicate`) to filter and list all persons matching the search criteria.
- **Validation**: The `FindCommand` performs validation to ensure at least one keyword is provided. It searches based on either name or NRIC, to speed up the search and prevent possible conflicts if name and NRIC do not match each other.
- **Execution**: When executed, the `FindCommand` identifies the person(s) being searched for based on the provided name or NRIC. If a name is provided as keyword, a `FindCommand(NameContainsKeywordsPredicate)` is created, and if an NRIC is provided as keyword, a `FindCommand(IdContainsKeywordsPredicate)` is created. `updateFilteredPersonList` will then update the filter of the filtered person list to filter by the given name or NRIC predicate (keyword).

The following sequence diagram shows how the find operation works:

<puml src="diagrams/FindSequenceDiagram.puml" width="400" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/FindActivityDiagram.puml" width="500" />

#### Rationale

- **Flexibility**: The `FindCommand` provides flexibility to users by allowing them to choose whether to find a person by name or NRIC, whichever is faster or available.
- **User Experience**: The keyword matching is case-insensitive, making the search faster and more user-friendly.
- **Data Integrity**: The feature is designed to maintain the integrity of the patient list by not changing any of the patient data.

#### Alternatives Considered

- **Alternative 1**: Using only name to find patients.
    - **Pros**:
        - **Standardisation**: The command format is fixed and will always only be `find n/NAME`, which may be easier to remember.
        - **User Convenience**: Searching primarily by name is a common way to look up a patient in a healthcare system and users may be more familiar with this method.

    - **Cons**:
        - **Potential Errors**: If patients' names change over time, there may be failed searches and other identifiers, like NRIC, may be needed.
        - **Limited Identifiability**: If multiple patients share the same name, they will be indistinguishable name-wise and other identifiers, like NRIC, may be needed.


- **Alternative 2**: Requiring both name and NRIC keywords to find patients within a single find command.
  - **Pros**:
    - **Enhanced Precision**: Combining both name and NRIC is a more unique identification method, making it easier to find a patient sharing a name with other patients.
    - **Patient Verification**: Searching by both criteria adds a layer of verification, ensuring the correct patient is selected.

  - **Cons**:
    - **Additional User Effort**: Users need to provide both name and NRIC, which may take longer or require extra effort, especially if they only have one piece of information readily available.
    - **Increased Ambiguity**: If the name keyword does not match the NRIC keyword, this may lead to potential confusion in which patient is being searched for.

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

* has a need to manage a large database of patient details, which includes health records, contact details, and appointment schedules
* cannot spend more than 2-3 minutes registering/accessing a database system
* work is fast-paced and requires quick access to patient details
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**:

HealthSync caters to clinic assistants in small private clinic, enabling them to register and access patient information within 2-3 minutes. It offers a user-friendly platform, optimizing contact management, patient tracking, and health record access, ensuring efficient patient management, appointment scheduling, and comprehensive health record retrieval, enhancing care delivery and saving time.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                         | I want to …​                                        | So that I can…​                                                           |
|----------|------------------------------------------------|----------------------------------------------------|--------------------------------------------------------------------------|
| `* * *`  | beginner of the app for an important operation | auto-save all my data                              | not lose my data when something goes wrong                               |
| `* * *`  | busy frontdesk worker                          | retrieve patient information                       | answer their queries                                                     |
| `* * *`  | frontdesk worker                               | create patient entries                             | add entries when new patients visit                                      |
| `* * *`  | frontdesk worker                               | find a patient by name                             | locate details of persons without having to go through the entire list   |
| `* * *`  | frontdesk worker                               | delete a patient entry                             | clean and update the database when patient no longer exist               |
| `* * *`  | frontdesk worker                               | edit patient entries                               | update their details, especially for upcoming appointment dates          |
| `* * `   | a new user of the app                          | view hints on commonly used commands               | be familiar with the app as soon as possible                             |
| `* * `   | a new user of the app                          | view preloaded sample data                         | know how the basic UI look like when it is populated                     |
| `* * `   | frontdesk worker                               | use app with shortcuts                             | get my task done very quickly                                            |
| `* * `   | frontdesk worker                               | have calendar-like UI to create appointments       | show calendar to patients and allow smoother appointment booking process |
| `* * `   | frontdesk worker                               | see conflicts in appointment schedules             | seamlessly schedule appointments for patients                            |
| `* * `   | frontdesk worker                               | reminder when patient's appointment is coming soon | call and remind patients accordingly                                     |
| `* * `   | healthcare provider                            | document patient encounters(ie. exam notes)        | maintain up-to-date records of patient information                       |
| `* `     | a new user of the app                          | have physical UI Buttons                           | use to execute tasks before I'm familiar with shortcuts                  |
| `* `     | frontdesk worker                               | have a very optimised app                          | do my task and have data reading almost instantly (O(1))                 |
| `* `     | frontdesk worker                               | add tags to patients                               | view and filter patients accordingly                                     |
| `* `     | frontdesk worker                               | leverage on database statistics                    | analyse data (ie. how many appointments booked/ month for doctors)        |
| `* `     | frontdesk worker                               | save back-up or archive patient details somewhere  | maintain a fast application while still having data securely stored      |


*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `HealthSync` and the **Actor** is the `user`, unless specified otherwise)

**Use case: UC1 - Add a patient**

**MSS**

1.  User requests to add a patient into the list.
2.  HealthSync adds the target patient into the list
    and displays the patient inside the updated list.
3.  HealthSync <u>performs an auto-save (UC0A)</u>.

    Use case ends.

**Extensions**

* 1a. The user does not specify one or more of the compulsory fields.

  * 1a1. HealthSync shows an error message.

    Use case ends.

  * 1b. The user specifies an IC that is already exists in the current list.

    * 1b1. HealthSync shows an error message.

      Use case ends.

**Use case: UC2 - Delete a patient**

**MSS**

1.  User requests to delete a specific patient based on an identifier from the list.
2.  HealthSync searches for the patient in the list.
3.  HealthSync deletes the specified patient from the list.
4.  HealthSync <u>performs an auto-save (UC0A)</u>.

    Use case ends.

**Extensions**

* 2a. The user does not exist in the list.

    * 2a1. HealthSync shows an error message.

      Use case ends.

* 2b. HealthSync finds more than 1 patient for the list.

    * 2b1. HealthSync shows a list of patients matching the identifier in the list.
    * 2b2. User indicates the patient to delete in the list.

      Use case continues from step 3.

**Use case: UC3 - Delete fields from a patient**

**MSS**

1.  User requests to delete fields from a specific patient based
    on an identifier from the list.
2.  HealthSync searches for the patient in the list.
3.  HealthSync deletes the fields of a specified patient from the list.
4.  HealthSync <u>performs an auto-save (UC0A)</u>.

    Use case ends.

**Extensions**

* 1a. The user does not specify any fields they want to delete.

    * 1a1. HealthSync <u>deletes the patient from the list instead (UC2).</u>

      Use case ends.

* 1b. The user attempts to delete a name/IC field.

    * 1b1. HealthSync shows an error message.

      Use case ends.

* 2a. The user does not exist in the list.

    * 2a1. HealthSync shows an error message.

      Use case ends.

* 2b. HealthSync finds more than 1 patient for the list.

    * 2b1. HealthSync shows a list of patients matching the identifier in the list.
    * 2b2. User indicates the patient to delete from in the list.

      Use case continues from step 3.

**Use case: UC4 - Edit a patient**

**MSS**

1.  User requests to change a specific user's fields
based on an identifier
    with a new value in the list.
2.  HealthSync searches for the patient in the list.
3.  HealthSync edits the specified patient's fields in the list.
4.  HealthSync <u>performs an auto-save (UC0A)</u>.

    Use case ends.

**Extensions**

* 1a. The user does not specify any fields they want to edit.

    * 1a1. HealthSync shows an error message.

      Use case ends.

* 1b. The user specifies duplicate fields they want to edit.

    * 1b1. HealthSync shows an error message.

      Use case ends.

* 1c. The user specifies no value in a name/IC field that they wish to edit.

    * 1c1. HealthSync shows an error message.

      Use case ends.

* 1d. The user attempts to change the IC of the patient to one that already
      exists in the list.

    * 1d1. HealthSync shows an error message.

      Use case ends.

* 2a. The user does not exist in the list.

    * 2a1. HealthSync shows an error message.

      Use case ends.

* 2b. HealthSync finds more than 1 patient for the list.

    * 2b1. HealthSync shows a list of patients matching the identifier in the list.
    * 2b2. User indicates the patient to edit in the list.

      Use case continues from step 3.

**Use case: UC5 - Find a patient**

**MSS**

1.  User requests for matches to the given query.
2.  HealthSync displays the list of patients matching the query.

    Use case ends.

**Extensions**

* 1a. No matches exist in the list.

    * 1a1. HealthSync displays a "no matches found" message.

      Use case ends.

**Use case: UC0A - Auto-save**

**Actors:** Operating System (OS)

**MSS**

1.  HealthSync requests for permissions from the OS to access its save location.
2.  OS grants HealthSync permission to access its save location.
3.  HealthSync saves the session data into the save location.

    Use case ends.

**Extensions**

* 1a. OS does not grant HealthSync save location permissions.

    * 1a1. HealthSync shows an error message.

    Use case ends.

*{More to be added}*

### Non-Functional Requirements

1. The application should be compatible with the designated operating systems and hardware configurations, as specified in the system requirements.
2. The application should respond promptly to user inputs, with minimal latency and loading times for data retrieval and processing.
3. The user interface should be user-friendly and intuitive, designed to optimize the workflow of frontdesk staff who need to complete tasks within 2-3 minutes.
4. The application should be designed to handle an increasing volume of patient records efficiently without noticeable performance degradation.
5. Ensure that the application complies with PDPA and healthcare regulations.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **Architecture Diagram**: A visual representation that illustrates the high-level design of the application
* **Main**: The function responsible for launching the application
* **UI**: Stands for User Interface
* **API**: Stands for Application Programming Interface, it defines the methods and protocols of the application
* **ObservableList**: A list implementation that allows other objects to observe and be notified when there is changes
* **JSON**: Stands for JavaScript Object Notation, it is a lightweight data interchange format
* **Classes**: Defines an object in the application
* **CLI**: Stands for Command-Line Interface, it is a text-based interface for interaction with computer system or software applications through use of commands
* **IC**: Stands for Identity Card
* **Database**: A structured collection of data organized and stored in computer system
* **Latency**: The time delay between user's action or request and the system's response
* **PDPA**: Stands for Personal Data Protection Act, it is the legislation related to the protection of personal data and privacy


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

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_

## **Appendix: Planned Enhancements**

1. Our current app is unable to handle scenarios where patients share the same name. We plan to make it possible for
   users to add patients with the same name into HealthSync.
2. Our current app does not handle text wrapping on the UI well, and certain text labels may be obscured when user input
   is too long. We plan to explicitly denote the labels on the individual UI cards and allow inputs to stretch its
   container should it be a long input, so users would be able to discern easily what elements they are looking at on
   the UI.
