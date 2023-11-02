---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# HealthSync User Guide

HealthSync is a **desktop app for managing patient details, optimised for use via a Command Line Interface** (CLI)
while still having the benefits of a Graphical User Interface (GUI). If you can type fast, HealthSync can help you
organise and manage patient details faster than traditional GUI apps.

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## [Quick start](#quick-start)

1. Ensure you have [Java](#java) `11` or above installed in your Computer.

2. Download the latest `healthsync.jar` from [here](https://github.com/AY2324S1-CS2103T-T14-3/tp/releases).

3. Copy the file to the folder you want to use as the _home folder_ for your HealthSync.

4. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar healthsync.jar`
   command to run the application.<br>


   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>

   ![Ui](images/Ui_v1.3.1.jpg)

5. Type the command in the command box and press Enter to execute it.
   e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all patients.

   * `add n/John Doe id/S8943782H p/98114839 e/example@email.com a/Example Street` :
      Adds a patient named `John Doe` with the relevant field details to HealthSync.

   * `delete n/Alex Yeoh` : Deletes Alex Yeoh's details from the current list.

   * `clear` : Deletes all patients.

   * `exit` : Exits the app.

6. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## [Features](#features)

<box type="info" seamless>
    Confused by unfamiliar terms? There is a <a href="#glossary">Glossary</a> section below!<br>
    We will also be highlighting more technical terms throughout this User Guide, so do check out
    the Glossary if you are confused.<br>
    <a href="#glossary" class="badge bg-primary">Check it out here.</a>
</box>

### Auto Save

HealthSync data are saved in the hard disk automatically after any command that changes the data.
There is no need to save manually.


### Viewing help: `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a patient: `add`

Adds a patient into the program, with the given patient information.

* A patient's name and IC Number are required when creating a new patient entry.

Format: `add n/NAME id/IC_NUMBER [field] ...`

Example commands:
 * `add n/Aaron Tan Jun Jie id/S8943782H p/98114839 e/example@mailhere a/Serangoon HDB 123`


![result for 'add n/Aaron Tan Jun Jie id/S8943782H p/98114839 e/example@mailhere a/Serangoon HDB 123'](images/addResult.jpg)

Expected outputs when the command succeeds:
 * `Patient Aaron Tan Jun Jie has been added with the fields: id/S8943782H
    p/98114839 e/example@mailhere a/Serangoon HDB 123`

Expected outputs when the command fails:
 * `Unable to add the patient to the database: Patient already exists.`
 * `Unable to add the patient to the database: IC required.`

Tips:
* Use the shortcut `a` for faster data entry
* Double-check patient data to prevent unnecessary errors later on


### Listing all persons: `list`

Shows a list of all persons in the address book.

Format: `list`


![result for 'list'](images/listResult.jpg)


### Editing a patient's details: `edit`

Edits an existing patient's details in the address book.

 * Edits the person with the specified name or id.
 * If an invalid name or IC Number is passed, an error message will be logged.
 * At least one of the optional fields must be provided.
 * Existing fields will be updated to the input values.
 * If the fields do not exist, the corresponding field with details will be added.

Format: `edit n/NAME or id/IC_NUMBER [field] ...`

Example commands:
 * `edit n/Alex Yeoh p/91234567 e/alexyeoh@example.com`


![result for 'edit n/Alex Yeoh p/91234567 e/alexyeoh@example.com'](images/editResult.jpg)

Expected outputs when the command succeeds:
* `Patient Alex Yeoh has been updated with the fields: p/91234567 e/alexyeoh@example.com`

Expected outputs when command fails:
* `Unable to edit the patient: Patient identification does not exist.`

Tips:
* Use the shortcut `e` for faster data editing
* Update multiple fields in a single edit command to save time


### Undoing a command: `undo`

Undoes an undo-able command within the address book.

* An undo-able command includes an edit command, add commmand or delete command
* The command allows you to undo the last command or to undo a specific number of previous commands

Format:
* `undo` or `undo [number]`

Example commands:
*  `undo`
*  `undo 2`


![result for 'undo'](images/undoResult.jpg)

Expected outputs when the command succeeds:
* `The last command has been undone`
* `The last 2 commands have been undone`

Expected outputs when command fails:
* `There is no valid command to be undone`

Tips:
* Use the shortcut `u` for faster command undoing
* Undo multiple commands at once to save time


### Locating persons by name or IC Number: `find`

Searches the patient list for all patients matching the name or IC Number and returns their related information.

 * The search is case-insensitive. e.g `hans` will match `Hans`.
 * The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`.
 * Only the name or IC number is searched.
 * Only full words will be matched e.g. `Han` will not match `Hans`.
 * For the name, only persons matching at least one keyword will be returned (i.e. `OR` search).
   e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`.

Format: `find n/NAME` *or* `id/IC_NUMBER`

Example commands:
* `find n/John` returns `john` and `John Doe`
* `find id/T0123456F` returns `Alex Yeoh`, with IC number `T0123456F` <br>


![result for 'find id/T0123456F'](images/findidT0123456FResult.jpg)

Expected outputs when the command succeeds:
 * `Patient n/NAME or id/IC_NUMBER: [field] …`

Expected output when the command fails:
 * `Unable to find the patient. Check if the patient’s information is correct.`

Tips:
* Use the shortcut `f` for faster patient-finding


### Preserving a `find` command result: `log`

Logs the result of the find command to the logger tab, which can be viewed at all times.

* Saving to the logger tab only works for results of the `find` command.
* The entire result will be saved.
* The result will be saved in the same order and format.
* Saving a new result clears the current saved result from the logger tab and replaces it.

Format: `log`

Example Command: `log` (after entering a FindCommand)


![result for 'log'](images/logResult.jpg)

Expected outputs when the command succeeds:
* `Results of the FindCommand have been saved to the logger tab.`

Expected output when the command fails:
* `There are no FindCommand results. There is nothing to be saved to the logger tab.`

Tips:
* Use the shortcut `lo` for faster patient-logging
* Use `log` command to save data you want to continue referring to
* `log` overwrites the data currently in the logger tab, so you do not need to perform clearing prior


### Adding a new `find` command result to the log: `alog`

Appends the new results of the most recent find command to the current data in the logger tab, which can be viewed at all times.

* Adding to the logger tab only works for results of the `find` command.
* The previously-saved result will remain the same.
* The entire new result will be saved below the previously-saved result.
* The result will be saved in the same order and format.

Format: `alog`

Example Command: `alog` (after entering a FindCommand)


![result for 'alog'](images/alogResult.jpg)

Expected outputs when the command succeeds:
* `Results of the FindCommand have been appended to the logger tab.`

Expected output when the command fails:
* `There are no FindCommand results. There is nothing to be saved to the logger tab.`


Tips:
* Use the shortcut `al` for faster log-appending
* Use `alog` command to save data you want to continue referring to, on top of some others
* `alog` does not overwrite the data and instead adds on to it, so you do not have to keep performing `log` to save more data


### Clearing data from the log: `clog`

Clears all current data in the logger tab.

Format: `clog`

Example Command: `clog`


![result for 'clog'](images/clogResult.jpg)

Expected output:
* `Logger tab has been cleared!`


Tips:
* Use the shortcut `cl` for faster log-clearing
* Use `clog` command if you do not need the data in the current logger tab anymore


### Deleting a person or field: `delete`

Deletes the specified person or the fields for the person from HealthSync.

* Deletes the person with the specified `n/NAME or id/IC_NUMBER`.
* The name or IC number must be a valid input.
* To delete a specified field only instead of the entire person, we indicate the field behind of the identification.
* If multiple people has the same name, HealthSync will display a list of people with that name together with their IC number.

Format: `delete n/NAME or id/IC_NUMBER [field]`

Example commands:
* `delete id/S9987362H` deletes all the details of the person with the specified IC number from HealthSync.
* `delete n/Alex Yeoh` deletes all the details of Alex Yeoh from HealthSync.
* `delete n/Alex Yeoh p/` deletes Alex Yeoh's phone number from his profile.


![result for 'delete n/Alex Yeoh'](images/deleteResult.jpg)

Expected outputs when the command succeeds:
 * `Patient n/NAME or id/IC_NUMBER has been removed from the database`
 * `The [field] of Patient n/NAME or id/IC_NUMBER has been removed from the database`

Expected output when the command fails:
 * `Error code’s message (i.e. Invalid NRIC/ Invalid Field(s) / Database Error) `

Tips:
* Use the shortcut `d` for faster patient-deleting
* Specify the medical history to be deleted using m/ if it's only the medical history data that is to be deleted


<!--
Original format, can consider using
list followed by delete 2 deletes the 2nd person in the address book.
find Betsy followed by delete 1 deletes the 1st person in the results of the find command.
-->


### Delete all patients: `clear`

Deletes all patients from the program.

Format: `clear`


![result for 'clear'](images/clearResult.jpg)


### Exiting the program: `exit`

Exits the program.

Format: `exit`


### Editing the data file

HealthSync data are saved automatically as a JSON file `[JAR file location]/data/healthsync.json`.
Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, HealthSync will discard all data and start with an empty
data file at the next run.  Hence, it is recommended to take a backup of the file before editing it.
</box>

--------------------------------------------------------------------------------------------------------------------

## [FAQ](#faq)

**Q**: What operating systems does HealthSync support?<br>
**A**: HealthSync is a cross-platform application and supports Windows, macOS, and Linux. Simply follow the
[installation guide](#quick-start) and download the jar file to use the application.

**Q**: What are the advantages of using a CLI interface in HealthSync over traditional GUI apps?<br>
**A**: HealthSync's CLI interface offers faster data entry and navigation for users who are comfortable with typing.
It streamlines tasks and provides a more efficient way to manage patient details.

**Q**: Can multiple users access Healthysync simultaneously, and how do I set up user accounts?<br>
**A**: HealthSync is currently an application for a single user. Hence, it cannot be accessed simultaneously
by different users.

**Q**: How do I import patient data from external sources into HealthSync?<br>
**A**: Datafile storing current patient data will be stored in `data/addressbook.json` by default under the same folder.
You may import patient data and store into that file. However, do adhere to the data format present in the current file.

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains
       the data of your previous HealthSync home folder.

**Q**: Can I use HealthSync offline, or does it require an internet connection?<br>
**A**: HealthSync is designed to work offline, ensuring you can access and update patient data even when you don't have
an internet connection.

**Q**: What happens if there is a system crash or power outage while I'm using HealthSync?<br>
**A**: HealthSync includes auto-save functionality to minimize data loss in case of unexpected events.
The app will attempt to recover your work upon restart.


--------------------------------------------------------------------------------------------------------------------

## [Known issues](#known-issues)

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only
   the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by
   the application before running the application again.

--------------------------------------------------------------------------------------------------------------------

## Glossary

### Command Format

Refers to the standardized way you have to type in your instructions to HealthSync.
Currently, there are 2 main types of Command Formats.

 * [Fielded](#fields) commands, which require you to add additional information behind the instruction.
 * Field-less commands, which do not need you to specify any more information to function.

Fielded Command Formats will generally look like this:
```
<KEYWORD> <words/numbers> identity [field]
```

 * `<KEYWORD>` is the word used to tell HealthSync what to do.
   * Example: to tell HealthSync to add a patient, you would use `add` as the keyword.

 * `<words/numbers>` are special values specific to the instruction type. These are generally
   given after the keyword.

 * `identity` represents compulsory identifying fields that need to be included with for that instruction type.
   * `or` can be specified between 2 identifying fields. This means that you may exclude one of the fields
     for that instruction type. 
   * See [Fields](#fields) to understand how identifying fields are specified.

 * `field` refer to the information fields that can be specified in each command.
   * Example: for `add`, you would specify additional information like phone number and address.
   * See [Fields](#fields) to understand how each entry is specified.

 * Square brackets `[...]` indicate that the field is optional to include.

 * `[field] ...` indicate that multiple different field types can be provided.

 * `identity` and `field` can generally be specified in any order.
   * Example: if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

Field-less Command Formats will generally look like this:
```
<KEYWORD>
```

 * For field-less commands, only the `<KEYWORD>` will be read by HealthSync, and the rest of the data you give it
   will be ignored. 
   * Example: if `help 123` was typed in, HealthSync will interpret it as `help`.

### Fields

Fields are the entries you have to input the patient's information for. These would
include names, phone numbers, appointment times, etc. Fields can be classified into
2 groups:
* Identifying fields, to let you know who the patient is. These are unique to the patient entry.
* Information fields, which gives you other information on the patient.

The 2 identifying fields of a patient are given below:

| Tag   | Representative Value  | Example Usage  | General Form in Commands |
|-------|-----------------------|----------------|--------------------------|
| `n/`  | Name                  | `n/Alex`       | `n/NAME`                 |
| `id/` | Identification Number | `id/S2345678A` | `id/IC_NUMBER`           |

1 or more identifying fields must be specified in each command, unless stated otherwise.

In addition to the identifying fields, information fields can be specified behind commands.
The information fields are given below:

| Tag    | Representative Value | Example Usage              | General Form in Commands | Remarks                         |
|--------|----------------------|----------------------------|--------------------------|---------------------------------|
| `p/`   | Phone Number         | `p/91234567`               | `p/PHONE_NUMBER`         |                                 |
| `e/`   | Email Address        | `e/example@a.com`          | `e/EMAIL`                |                                 |
| `a/`   | Address              | `a/Location, Here Rd`      | `a/ADDRESS`              |                                 |
| `m/`   | Medical History      | `m/Asthmatic`              | `m/MEDICAL_HISTORY`      | Can have multiple of this field |
| `ap/`  | Appointment          | `ap/11-2-2023 11:00 12:00` | `ap/APPT `               |                                 |

Unless stated otherwise, these fields are optional.

#### Name

The standard unique identifier for your patient. Each patient should have a unique alphanumeric name assigned to them.

#### NRIC

The ID-based unique identifier for your patient. Each patient should have a unique alphanumeric ID assigned to them.

There is no verification system in place for NRIC. This allows you to use your custom identifier for your patients, if
you wish.

#### Phone Number

A phone number to contact your patients with. Each patient should be assigned to 1 phone number.

Phone numbers must be numeric, and it must be at least 3 digits long. It does not have to be unique.

#### Email Address

An email address to contact your patients with. Each patient should be assigned to 1 email address, which
doesn't need to be unique.

Emails should be in the format `local-part@domainname`.
 * `local-part` is alphanumeric, and may also contain these symbols: `+``_``.``-`
 * `domainname` should be the site that the email leads to, such as `gmail.com`.
 * These must be separated by an `@` symbol.

#### Address

The address to contact your patients. Each patient should be assigned to 1 address that they live in,
and it does not have to be unique.

Addresses do not have a strict format to adhere to.

#### Medical History

The medical history of your patients. These are optional fields to assign to patients, and every
patient can have more than 1 medical history.

Individually, medical histories do not have a strict format to adhere to. However, every medical
history a patient has should be unique from one another.

#### Appointment

The appointment slot assigned to your patients. A patient may have no appointment assigned to them.
Currently, only 1 appointment may be assigned to a patient at a time.

Appointments should be given in this sequence: `Date, Start Time, End Time`. For example,
`1-Aug-2023, 11:00 13:00` is a valid appointment denoting an appointment on 1st August 2023, from 11am
to 1pm.
 * The month and day of the appointment should always be included.
 * Day can be given as a 1 to 2-digit number. It will only be accepted if the day can exist in that month or year.
 * Month can be given as a 1 to 2-digit number or a 3-letter word. Example: `Jun` and `6` both represent June.
 * The year is optional. If not included, HealthSync assumes it to be this year.
 * The date should be hyphenated.
 * The time should be given in 24-hour clock format, with 00:00 as 12am.
 * Colons are optional when time is given with hours and minutes. If no colons are given, you need to pad the hour with
   a zero when necessary. Example: `1200` for 12 noon, `0900` for 9am.
 * You may exclude minutes if you wish. Example: `15` will be interpreted as 3pm.
 * Date and the 2 Times needs to be separated by a comma or a space.

Some examples of valid Appointment formats are listed here:
```
1-Nov 8 10
1-11-2023 10 12
03-03-23 0800 1400
4-Sep, 1800, 2000
```

### CLI
Command Line Interface. Refers to computer programs which require you to type to interact with it.

### GUI
Graphical User Interface. Refers to computer programs with a visual window you can interact with directly.

### Patient List View
The main part of HealthSync where the list of all your patients are displayed.

### Output Box
A small box right below the Patient List View where HealthSync will provide any feedback it has on the instructions
it has performed.

### Command Box
The area where you type your instructions for HealthSync to perform.

### Logger Tab
A 'sticky-note'-like area on the right of the Patient List View.

### Command History Stack
The group of instructions HealthSync remembers performing. This group allows HealthSync to undo the
instructions and return your list to an older state.

### Java
A piece of software that our program builds on. In order to use HealthSync, your computer must be running at least
version 11 of Java. [Get it here.](https://www.oracle.com/sg/java/technologies/javase/jdk11-archive-downloads.html)

### JSON
JavaScript Object Notation. This is the file format used by HealthSync to save and store information.

--------------------------------------------------------------------------------------------------------------------

## [Command summary](#command-summary)

| Action         | Shortcut | Format, Examples                                                                                                           |
|----------------|----------|----------------------------------------------------------------------------------------------------------------------------|
| **Add**        | `a`      | `add n/NAME id/IC_NUMBER [field] ...` <br> e.g., `add n/James Ho id/SXXXX123D p/91234567 a/A Estate, Clementi Rd, 1234665` |
| **Clear**      | `c`      | `clear`                                                                                                                    |
| **Delete**     | `d`      | `delete n/NAME [field]` *or* `delete id/IC_NUMBER [field]`<br> e.g., `delete n/John Doe e/`                                |
| **Edit**       | `e`      | `edit n/NAME [field]` *or* `edit id/IC_NUMBER [field] ... `<br> e.g.,`edit n/James Lee e/jameslee@example.com`             |
| **Find**       | `f`      | `find n/NAME [field]` *or* `find id/IC_NUMBER [field]`<br> e.g., `find n/James Jake` *or* `find id/S872D`                  |
| **Help**       | `h`      | `help`                                                                                                                     |
| **List**       | `li`     | `list`                                                                                                                     |
| **Log**        | `lo`     | `log`                                                                                                                      |
| **Append Log** | `al`     | `alog`                                                                                                                     |
| **Clear Log**  | `cl`     | `clog`                                                                                                                     |
| **Exit**       | `ex`     | `exit`                                                                                                                     |
