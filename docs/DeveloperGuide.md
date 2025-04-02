---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# AB-3 Developer Guide

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

The UI consists of a MainWindow that is made up of several parts:
- Command Interface: CommandBox, ResultDisplay
- List Panels:
    - `PersonListPanel` with `PersonCard` and `PreferenceListPanel` with `PreferenceCard`
    - `ListingListPanel` with `ListingCard` and `OwnerListPanel` with `OwnerCard`
    - `TagListPanel` 
- Utility Components: `HelpWindow`, `StatusBarFooter`

All these components, including the `MainWindow`, `inherit` from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

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

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("deletePerson 1")` API call as an example.

<puml src="diagrams/DeletePersonSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `deletePerson 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeletePersonCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.

</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeletePersonCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeletePersonCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddPersonCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddPersonCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddPersonCommandParser`, `DeletePersonCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />

**Note:** The attributes of `Person`, `Tag`, `PriceRange` that have been promoted to a class are abstracted into separate class diagrams for ease of reading.

The `Model` component,

* stores the match estate data composed of `Person`, `PropertyPreference`,  `Listing` and `Tag` and `PriceRange` objects.
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores the currently 'selected' `PropertyPreference` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<PropertyPreference>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores the currently 'selected' `Listing` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Listing>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores the currently 'selected' `Tag` objects (e.g., results of a search query) as a separate _filtered_ set which is exposed to outsiders as an unmodifiable `ObservableList<Tag>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

`Person`

<puml src="diagrams/PersonAttributesClassDiagram.puml" width="450" />

* Stores person-related data as `Person` objects contained in a `UniquePersonList` object.
* Each `Person` may have multiple`PropertyPreference` and `Listing` associations.
* Each `Person` is identified their phone number.

</box>

<box type="info" seamless>

`PropertyPreference`

* Each `PropertyPreference` may have a `PriceRange`, and multiple `Tag` associations.
**Note:** A `PropertyPreference` can only be tied to 1 `Person` to group criterias for a property that are looking for under a representative person responsible for the purchase.

</box>

<box type="info" seamless>

`Listing`

<puml src="diagrams/ListingAttributesClassDiagram.puml" width="450" />

* Stores listing-related data as `Listing` objects contained in a `UniqueListingList` object.
* Each `Listing` may have a `PriceRange`, and multiple `Tag`  and `Owner` associations.
* Each `Listing` should have either a `Unit Number` or a `House Number` but not both.
* Each `Listing` is identified by a combination of the `Postal Code` and either the `Unit Number` or the `House Number` depending on which one is present.
**Note:** A `Listing` can have multiple `Person` to reflect co-ownership scenarios. Additionally in certain cases joint approval of a listing is required for a purchase.

</box>

<box type="info" seamless>

`PriceRange`

<puml src="diagrams/PriceRangeAttributesClassDiagram.puml" width="450" />

* Each `PriceRange` may have a lower bound price and a upper bound price.
* If neither bound is specified, the `PriceRange` is unbounded.

</box>

<box type="info" seamless>

`Tag`
* Stores tag-related data as `Tag` objects contained in a `TagRegistry` object.
* Each `Tag` may have multiple `Listing`  and `PropertyPreference` associations.
* Each `Tag` is identified by a string tag name.
**Note:** An arguably better representation of Tag is to split the tag that stores the association from the tag name, promoting the tag name into a class.

<puml src="diagrams/BetterTagAttributesClassDiagram.puml" width="450" />

</box>

<box type="info" seamless>

`SearchContext` and `SearchType`
* SearchContext represents the current active filters for Tags, PriceRange, and PropertyPreference.
* SearchType enumeration represents the types (Person/ Listing) of searches that can be performed.

<puml src="diagrams/BetterTagAttributesClassDiagram.puml" width="450" />

</box>

<box type="info" seamless>

**Bidirectional navigability:**

`Listing` and `Person`:
- Listings require references to their owners to display on the UI.
- Listings uses reference to the owners to ensure that when matching, owners of a listing are not matched to them.
- Person uses reference to the listings to ensure that they should have a `seller` display label. Alternatively a counter could also be used, however a direct reference would better ensure correctness.
- Person uses references to the listings to ensure cascading deletion of a person also removes the person as a owner from their listings. Searching through all listings to maintain the cascading delete would be inefficient.

`Person` and `PropertyPreference`:
- Person requires references to their preferences to display on the UI.
- Preferences requires references to their owner, such that when a tag is removed from the preference (by cascading deletion of a tag). The person the preference belongs to can update the display on the UI.
- Preferences uses references to their owner to ensure cascading deletion of a preference also removes the preference from their owner without needing to search all persons which is especially inefficient since a preference can only have 1 owner.

`Listing` and `Tags`:
- Listing requires references to tags to display on the UI.
- Tags uses references to the listing to ensure that they have an accurate instance usage label. Alternatively a counter could also be used, however a direct reference would better ensure correctness.
- Tags uses references to their listing to ensure cascading deletion of a tag also removes the tag from the listings that uses it. Searching through all listings, would be inefficient.

`PropertyPreference` and `Tags`:
- Property preferences requires references to tags to display on the UI.
- Tags uses references to the property preferences to ensure that they have an accurate instance usage label. Alternatively a counter could also be used, however a direct reference would better ensure correctness.
- Tags uses references to their property preferences to ensure cascading deletion of a tag also removes the tag from the property preferences that uses it. Searching through all preferences, would be inefficient.

<puml src="diagrams/BetterTagAttributesClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* stores match estate data and user preferences locally on disk in JSON format, and reads them back into the model when needed.
* is defined by the `Storage` interface, which inherits from both `AddressBookStorage` and `UserPrefsStorage`.
  This allows it to be used for storing either address book data or user preferences independently.
* is implemented by `StorageManager`, which delegates to:
  * `JsonAddressBookStorage` — handles storage and retrieval of address book data.
  * `JsonUserPrefsStorage` — handles storage and retrieval of user preferences.
* uses `JsonSerializableAddressBook` as the top-level serializable container to read/write the full address book.
* relies on Jackson-compatible intermediary classes (e.g. `JsonAdaptedPerson`, `JsonAdaptedListing`, `JsonAdaptedTag`,
  `JsonAdaptedPreference`, and `JsonAdaptedPriceRange`) to convert between JSON and model types.
* depends on model classes such as `ReadOnlyAddressBook` and `UserPrefs`, as it handles the persistence of these entities.

**Note:**  
In `JsonAdaptedListing`, the `ownerKeys` field contains phone numbers as strings.  
These are used to look up the corresponding `Person` objects during deserialization — forming an indirect association from `Listing` to `Person`.

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
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Edit Tag feature

The Edit Tag feature allows users to edit the name of an existing tag. This feature is useful when users want to change the name of a tag that they have already created. The following sequence diagram shows how the Edit Tag feature works:

<puml src="diagrams/EditTagSequenceDiagram.puml" alt="EditTagSequenceDiagram" />

The associated Listings and PropertyPreferences will be updated to reflect the new tag name.

### \[Proposed\] Edit Listing feature

The Edit Listing feature allows users to edit the details of an existing listing. This feature is useful when users want to update information such as postal code, unit/house number, price range, or property name of a listing. The following sequence diagram shows how the Edit Listing feature works:

<puml src="diagrams/EditListingSequenceDiagram.puml" alt="EditListingSequenceDiagram" />

The edit operation will update the listing's details while maintaining its existing tags and availability status. The system ensures that no duplicate listings can be created by checking the postal code and unit/house number combination.

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

### Target User Profile:

**Profile**
Tim is a busy, independent real estate agent working in Singapore, managing multiple buyers and sellers simultaneously.

He often communicates with clients through messaging applications and understands their preferences and specifications from chat logs. During client meetups, open houses, and property tours, he may also take notes on common likes and dislikes among clients.

He records these details in an Excel sheet on his laptop, sometimes on the fly, and is highly experienced in fast typing.

Tim has noticed that many client requests can be matched with specific listings he has, but manually filtering and matching these requests with listings can be cumbersome.

**Work Patterns**

- Primarily works alone but may collaborate with other agents to gain information on additional listings, so he needs to send and receive listing and client information easily.
- Commonly works with clients, many of whom have preferences that overlap with those of other clients.
- Uses both a laptop and a smartphone for work.
- Prefers typing and, by extension, CLI tools for speed and efficiency, as he frequently needs to quickly note down client preferences.
- Regularly shares the tools he uses with other agents he is familiar with.
- Struggles to manage client details across messaging apps and spreadsheets.
- Needs to match buyers with sellers quickly and accurately.
- Works on the go, handling leads from different sources.
- Wants to keep client contacts separate from personal contacts for **PDPA** compliance.
- Prefers offline tools due to security concerns regarding potential customer data leaks.
- Also favors offline tools due to the possibility of an unstable internet connection.
- Does not use **CRM** or organizational tools, as they are beyond his job scope as an independent real estate agent.

### Value Proposition:
Real estate agents often struggle to manage buyers and sellers through messaging apps.
MatchEstate allows tracking of buyers and sellers easily as well as their preferences and offerings respectively.
It enables fast matching of buyers and sellers. It is tailored for those who prefer CLIs.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority  | As a …​                                                        | I want to …​                                                                                      | So that I can…​                                                                               |
|-----------|----------------------------------------------------------------|---------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------|
| `* *`     | potential user                                                 | see a list of all available commands on startup                                                   | quickly learn how to use the app                                                              |
| `* *`     | forgetful real estate agent                                    | view a help menu                                                                                  | reference how to use the system effectively                                                   |
| `* *`     | potential user                                                 | have the application come preloaded with sample data                                              | easily try all the commands before adding my real clients                                     |
| `* *`     | new user                                                       | clear the sample data                                                                             | populate the application with actual data                                                     |
| `* *`     | real estate agent                                              | clear all data                                                                                    | reset the system when necessary                                                               |
| `* * *`   | new user                                                       | add my previously accumulated buyer and seller profile information                                | populate the data in the app with my previously recorded data                                 |
| `* * *`   | new user                                                       | remove the app's default tags that are created                                                    | eventually replace it with my own personalized tags                                           |
| `* * *`   | real estate agent                                              | add buyer information                                                                             | manage my buyers easily                                                                       |
| `* * *`   | real estate agent                                              | add seller information                                                                            | manage my sellers easily                                                                      |
| `* * *`   | real estate agent                                              | view all clients                                                                                  | easily track the clients I have                                                               |
| `* *`     | real estate agent                                              | edit a person's details                                                                           | update their information when needed                                                          |
| `* * *`   | real estate agent                                              | delete a client                                                                                   | only track relevant clients                                                                   |
| `* *`     | real estate agent                                              | find a client by name                                                                             | quickly retrieve the details of a specific client                                             |
| `* * *`   | real estate agent                                              | add property information                                                                          | manage my listings easily                                                                     |
| `* * *`   | real estate agent                                              | add a property to a person                                                                        | track how many properties they are selling                                                    |
| `* * *`   | real estate agent                                              | add a property to a person                                                                        | track the property's current seller                                                           |
| `* * *`   | real estate agent                                              | add the preferences of a person                                                                   | track what type of properties they are interested in purchasing                               |
| `* * *`   | real estate agent                                              | add more than one property preference                                                             | simultaneously track multiple distinct properties a client is interested in purchasing        |
| `* * *`   | real estate agent                                              | add tags to a property preference                                                                 | categorize a person's buying interests more effectively                                       |
| `* * *`   | real estate agent                                              | remove a tag from a property preference                                                           | adjust my information to match a person's interests as they change                            |
| `* *`     | real estate agent                                              | overwrite tags in a property preference                                                           | completely update a person's buying criteria as necessary                                     |
| `* * *`   | real estate agent                                              | delete a property preference from a person                                                        | remove no longer relevant buying interests from the systemy                                   |
| `* * *`   | real estate agent                                              | add a property                                                                                    | track the information in the system                                                           |
| `* * *`   | real estate agent                                              | view all listings                                                                                 | easily track the properties that are listed for sale                                          |
| `* * *`   | real estate agent                                              | add tags to a property                                                                            | categorize the property by its key details                                                    |
| `* * *`   | careless real estate agent                                     | remove a tag from a property                                                                      | correct the details if necessary                                                              |
| `* *`     | careless real estate agent                                     | overwrite tags in a property                                                                      | completely update its categorization if necessary                                             |
| `* * *`   | real estate agent                                              | delete a property                                                                                 | remove no longer relevant listings from the system                                            |
| `* * *`   | real estate agent                                              | mark a property as unavailable                                                                    | ensure that listings that are on hold are no longer considered for matching                   |
| `* * `    | real estate agent                                              | mark a property as available when the listing is no longer on hold                                | ensure that listings that are no longer on hold can be considered for matching.               |
| `* *   `  | real estate agent                                              | match a person with properties based on the number of common tags and budget                      | quickly find listings that fit their preferences                                              |
| `* *  `   | real estate agent                                              | match a property with potential buyers based on the number of common tags and their budget        | identify interested persons efficiently.                                                      |
| `* * `    | real estate agent                                              | filter properties based on a person's preferences                                                 | provide targeted property recommendations.                                                    |
| `* * * `  | real estate agent                                              | add custom tags                                                                                   | customize the categorization of property preferences and properties according to my workflow. |
| `* * *  ` | real estate agent                                              | add custom tags                                                                                   | capture niche preferences (such as pet-friendly).                                             |
| `* * * `  | real estate agent                                              | list all tags                                                                                     | see the available categories in the system.                                                   |
| `* * * `  | real estate agent                                              | delete a tag                                                                                      | remove outdated and unnecessary categorization from the system.                               |
| `* * *  ` | careless real estate agent                                     | delete a tag                                                                                      | remove tags that I have mistakenly added.                                                     |
| `* *   `  | real estate agent                                              | search for properties by tags                                                                     | quickly find relevant listings.                                                               |
| `* *  `   | real estate agent                                              | search for persons by tags                                                                        | find buyers based on their property preferences                                               |
| `*   `    | real estate agent                                              | attach notes to my clients                                                                        | keep track of specific client requirements beyond common tags                                 |
| `* *   `  | real estate agent                                              | update my buyer's information when new information is available                                   | ensure that my information remains accurate and up to date                                    |
| `* *   `  | real estate agent                                              | update my seller's information when new information is available                                  | ensure that my information remains accurate and up to date                                    |
| `* *   `  | real estate agent                                              | update my listing's information when new information is available                                 | ensure that my information remains accurate and up to date.                                   |
| `*     `  | inexperienced user                                             | undo accidental deletions or edits                                                                | revert mistakes quickly                                                                       |
| `* *   `  | real estate agent                                              | filter listings based on price range                                                              | focus on properties within the buyer's budget.                                                |
| `*     `  | real estate agent                                              | be able to list properties that clients have already toured                                       | avoid re-touring them on the same listing by accident.                                        |
| `*     `  | real estate agent                                              | be able to add properties that the clients liked                                                  | keep track of the properties that they are interested in.                                     |
| `*     `  | independent real estate agent                                  | be able to export a list of buyers                                                                | share them with other agents for collaboration.                                               |
| `*     `  | frequent user                                                  | archive buyers and sellers who are inactive                                                       | reduce clutter when matching/searching.                                                       |
| `*     `  | efficient user who may not have access to the app at all times | write bulk commands in a text file to add, edit or delete client details at the end of the day    | update information without having the application on hand.                                    |
| `*     `  | real estate agent                                              | view the status of a deal (e.g., Inquiry, Viewing, Offer, Closed)                                 | understand the deal's progression at a glance.                                                |
| `*     `  | real estate agent                                              | be able to create backups and restore data via CLI commands                                       | do not risk losing client information in the event of data corruption.                        |
| `*     `  | real estate agent                                              | be able to view a log of all commands executed                                                    | review my past commands.                                                                      |
| `*     `  | power user                                                     | create custom command aliases (e.g., mb for match --batch)                                        | customize the commands to my workflow and work more efficiently.                              |
| `*     `  | real estate agent                                              | store notes for each deal                                                                         | keep track of important discussions.                                                          |
| `*     `  | real estate agent                                              | create a simple summary of all buyers and listings                                                | use it for basic data analytics purposes and understand my portfolio demographic.             |
| `*     `  | real estate agent                                              | auto-generate templated messages for matched buyers and sellers and export them to WhatsApp/Email | ensure that the process of writing electronic direct mails is simplified.                     |


### Use cases

(For all use cases below, the **System** is the `MatchEstate` and the **Actor** is the `user`, unless specified otherwise)

#### Use case: UC01 - List persons

**MSS**

1.  User requests to list persons.
2.  MatchEstate shows a list of persons.
3.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 1a. The list is empty.

  * 1a1. MatchEstate displays a message for an empty list.

    Use case resumes at 3.

#### Use case: UC02 - List listings

Similar to <u>UC01</u> except for listings instead.

**MSS**

1.  User requests to list listings.
2.  MatchEstate shows a list of listings.
3.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 1a. The list is empty.

  * 1a1. MatchEstate displays a message for an empty list.

    Use case resumes at 3.

#### Use case: UC03 - List tag

Similar to <u>UC01</u> except for tags instead.

**MSS**

1.  User requests to list tags.
2.  MatchEstate shows a list of tags.
3.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 1a. The list is empty.

  * 1a1. MatchEstate displays a message for an empty list.

    Use case resumes at 3.

#### Use case: UC04 - Add a person

**MSS**

1.  User requests to <u>list persons(UC01)</u>.
2.  User specifies a person to add.
3.  MatchEstate adds the person.
4.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 2a. The specified person is invalid.

  * 2a1. MatchEstate shows an error message.

  Use case ends.

* 2b. The specified person already exists.

  * 2b1. MatchEstate shows an error message.

    Use case ends.

#### Use case: UC05 - Add a listing

**MSS**

1.  User requests to <u>list listings(UC02)</u>.
2.  User requests to <u>list tags(UC03)</u>.
3.  User specifies a listing and its associated existing tags and new tags to add.
4.  MatchEstate adds the listing.
5.  MatchEstate creates the new tags specified.
6.  MatchEstate adds the tags to the listing.
7.  MatchEstate updates the usage number of the tags.
8.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 3a. The specified listing is invalid.

  * 3a1. MatchEstate shows an error message.

  Use case ends.

* 3b. The specified listing already exists.

  * 3b1. MatchEstate shows an error message.

    Use case ends.

* 3c. New tags to be created already exist.

  * 3c1. MatchEstate shows an error message.

  Use case ends.

* 3d. Existing tags specified do not exist.

  * 3d1. MatchEstate shows an error message.

  Use case ends.

* 4a. No new tags to be created.

  Use case resumes at 6.

* 4b. No existing and new tags specified.

  Use case resumes at 8.

#### Use case: UC06 - Add a tag

Similar to <u>UC04</u> except for tags instead.

**MSS**

1.  User requests to <u>list tags(UC03)</u>.
2.  User specifies new tags to create.
3.  MatchEstate creates new tags.
4.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 2a. The specified tag is invalid.

  * 2a1. MatchEstate shows an error message.

  Use case ends.

* 2b. New tag to be created already exists.

  * 2b1. MatchEstate shows an error message.

  Use case ends.

#### Use case: UC07 - Add owner of listing

**MSS**

1.  User requests to <u>list persons(UC01)</u>.
2.  User requests to <u>list listings(UC02)</u>.
3.  User requests add a specified person to a specified listing.
4.  MatchEstate adds the person to the listing.
5.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 3a. The person index is invalid.

  * 3a1. MatchEstate shows an error message.

  Use case ends.

* 3b. The listing index is invalid.

  * 3b1. MatchEstate shows an error message.

  Use case ends.

#### Use case: UC08 - Add property preference to person

Similar to <u>UC05</u> except listing persons and adding property preference to the specified person. Note that preferences can be duplicates, so there are no extensions for it already existing.

**MSS**

1.  User requests to <u>list persons(UC01)</u>.
2.  User requests to <u>list tags(UC03)</u>.
3.  User specifies a property preference with its associated existing tags and new tags to add to a specified person.
4.  MatchEstate adds a new property preference to the person.
5.  MatchEstate creates new tags.
6.  MatchEstate adds the tags to the property preference.
7.  MatchEstate updates the usage number of the tags
8.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 3a. The person index is invalid.

  * 3a1. MatchEstate shows an error message.

  Use case ends.

* 3b. New tags to be created already exist.

  * 3c1. MatchEstate shows an error message.

  Use case ends.

* 3d. Existing tags specified do not  exist.

  * 3d1. MatchEstate shows an error message.

  Use case ends.

* 4a. No new tags to be created.

  Use case resumes at 6.

* 4b. No existing and new tags specified.

  Use case resumes at 8.

#### Use case: UC09 - Add tag to property preference

Similar to <u>UC08</u> but references an existing property preference instead. Note that it throws an error if no existing and new tags are specified.

**MSS**

1.  User requests to <u>list persons(UC01)</u>.
2.  User requests to <u>list tags(UC03)</u>.
3.  User requests to add new and existing tags to a property preference.
4.  MatchEstate creates new tags.
5.  MatchEstate adds the tags to the property preference.
6.  MatchEstate updates the usage number of the tags.
7.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 3a. The person index is invalid.

  * 3a1. MatchEstate shows an error message.

  Use case ends.

* 3b. The property preference index is invalid.

  * 3b1. MatchEstate shows an error message.

  Use case ends.

* 3c. New tags to be created already exist.

  * 3c1. MatchEstate shows an error message.

  Use case ends.

* 3d. Existing tags specified do not exist.

  * 3d1. MatchEstate shows an error message.

  Use case ends.

* 3e. No new tags to be created.

  Use case resumes at 5.

* 3f. No existing and new tags specified.

  * 3f1. MatchEstate shows an error message.

  Use case ends.

#### Use case: UC10 - Add tag to listing

Similar to <u>UC09</u> but references an existing listing instead.

**MSS**

1.  User requests to list listings(UC02).
2.  User requests to list tags(UC03).
3.  User requests to add a tag to a listing.
4.  MatchEstate creates new tags.
5.  MatchEstate adds the tags to the listing.
6.  MatchEstate updates the usage number of the tags.
7.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 3a. The listing index is invalid.

  * 3a1. MatchEstate shows an error message.

  Use case ends.

* 3b. New tags to be created already exist.

  * 3b1. MatchEstate shows an error message.

  Use case ends.

* 3c. Existing tags specified do not exist.

  * 3c1. MatchEstate shows an error message.

  Use case ends.

* 3d. No new tags to be created.

  Use case resumes at 5.

* 3e. No existing and new tags specified.

  * 3e1. MatchEstate shows an error message.

  Use case ends.

#### Use case: UC11 - Delete tag from property preference

Similar to <u>UC09</u> but does not create new tags and removes the tags instead.

**MSS**

1.  User requests to <u>list persons(UC01)</u>.
2.  User requests to <u>list tags(UC03)</u>.
3.  User requests to delete tags from a property preference.
4.  MatchEstate removes the tags from the property preferences.
5.  MatchEstate updates the usage number of the tags.
6.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 3a. The person index is invalid.

  * 3a1. MatchEstate shows an error message.

  Use case ends.

* 3b. The property preference index is invalid.

  * 3b1. MatchEstate shows an error message.

  Use case ends.

* 3c. Tags specified do not exist.

  * 3c1. MatchEstate shows an error message.

  Use case ends.

* 3d. No tag specified.

  * 3d1. MatchEstate shows an error message.

  Use case ends.

#### Use case: UC12 - Delete tag from listing

Similar to <u>UC010</u> but does not create new tags and removes the tags instead.

**MSS**

1.  User requests to <u>list listings(UC02)</u>.
2.  User requests to <u>list tags(UC03)</u>.
3.  User requests to delete tags from a property.
4.  MatchEstate removes the tags from the listing.
5.  MatchEstate updates the usage number of the tags
6.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 3a. The listing index is invalid.

  * 3a1. MatchEstate shows an error message.

  Use case ends.

* 3b. Tags specified do not exist.

  * 3b1. MatchEstate shows an error message.

  Use case ends

* 3c. The tag is not present in the property.

  * 3c1. MatchEstate shows an error message.

  Use case ends.

* 3d. No tags specified.

  * 3d1. MatchEstate shows an error message.

  Use case ends.

#### Use case: UC13 - Delete owners of a listing

Similar to <u>UC07</u> but removes instead of adds. Note that the owners are displayed with the listing, hence there is no need for <u>UC01</u>.

**MSS**

1.  User requests to <u>list listings(UC02)</u>.
2.  User requests to remove a specified owner from a specified listing.
3.  MatchEstate removes the owner from the listing.
4.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 2a. The listing index is invalid.

  * 2a1. MatchEstate shows an error message.

  Use case ends.

* 2b. The owner index is invalid.

  * 2b1. MatchEstate shows an error message.

  Use case ends.

#### Use case: UC14 - Delete property preference

Similar to <u>UC08</u> but removes instead of adds. Note that tags are appropriately removed when deleting a property preference, hence there is no need for <u>UC03</u>.

**MSS**

1.  User requests to <u>list persons(UC01)</u>.
2.  User requests to delete a property preference from a person.
3.  MatchEstate updates the usage number of tags used by the person’s property preferences.
4.  MatchEstate removes the property preference from the person.
5.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 2a. The person index is invalid.

  * 2a1. MatchEstate shows an error message.

  Use case ends.

* 2b. The preference index is invalid.

  * 2b1. MatchEstate shows an error message.

  Use case ends.

* 2c. The property preferences do not have tags.

  Use case resumes at 4.

#### Use case: UC15 - Delete listing

**MSS**

1.  User requests to <u>list listing(UC02)</u>.
2.  User requests to delete a listing.
3.  MatchEstate updates the usage number of tags used by the listing.
4.  MatchEstate updates the listing list of all its owners.
5.  MatchEstate deletes the listing.
6.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 2a. The listing index is invalid.

  * 2a1. MatchEstate shows an error message.

  Use case ends.

* 2b. The listing does not have tags.

  Use case resumes at 5.

* 2c. The listing does not have tags and has no owner.

  Use case resumes at 6.

* 2b. The listing does not have tags and has no owner.

  Use case resumes at 6.

#### Use case: UC16 - Delete person

**MSS**

1.  User requests to <u>list persons(UC01)</u>.
2.  User requests to delete a specific person by index.
3.  MatchEstate updates the usage number of tags used by the person’s property preferences.
4.  MatchEstate deletes the person’s property preferences.
5.  MatchEstate deletes ownership of listing for all of the person’s listing.
6.  MatchEstate deletes the person.
7.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 2a. The person index is invalid.

  * 2a1. MatchEstate shows an error message.

  Use case ends.

* 2b. The person has no property preferences.

  Use case resumes at 5.

* 2c. None of the person's property preferences have tags.

  Use case resumes at 4.

* 2d. The person has no property preferences and has no listings.

  Use case resumes at 6.

* 4a. The person has no listings.

  Use case resumes at 6.

#### Use case: UC17 - Delete tag

**MSS**

1.  User requests to <u>list tags(UC03)</u>.
2.  User requests to delete a tag.
3.  MatchEstate deletes the tag from its property preferences.
4.  MatchEstate deletes the tag from its listings.
5.  MatchEstate deletes the tag.
6.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 2a. The tag specified does not exist.

  * 2a1. MatchEstate shows an error message.

  Use case ends.

* 2b. The tag has no property preferences using it.

  Use case resumes at 4.

* 2c. The tag has no property preferences and listings using it

  Use case resumes at 5.

* 3a. The tag has no listings using it

  Use case resumes at 5.

#### Use case: UC18 - Edit a person

**MSS**

1.  User requests to <u>list persons(UC01)</u>.
2.  User requests to edit a person.
3.  MatchEstate edits the person.
4.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 2a. The person index is invalid.

  * 2a1. MatchEstate shows an error message.

  Use case ends.

* 2b. The expected edited person already exists.

  * 2b1. MatchEstate shows an error message.

  Use case ends.

#### Use case: UC19 - Mark a listing as unavailable

**MSS**

1.  User requests to <u>list listings(UC02)</u>.
2.  User specifies a listing to mark as unavailable.
3.  MatchEstate marks the listing as unavailable.
4.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 2a. The listing index is invalid.

  * 2a1. MatchEstate shows an error message.

    Use case ends.

#### Use case: UC20 - Mark a listing as available

**MSS**

1.  User requests to <u>list listings(UC02)</u>.
2.  User specifies a listing to mark as available.
3.  MatchEstate marks the listing as available.
4.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 2a. The listing index is invalid.

  * 2a1. MatchEstate shows an error message.

    Use case ends.

#### Use case: UC21 - Overwrite tags in property preference

Similar to <u>UC09</u> and <u>UC011</u>. Note that overwrite is effectively a combination of adding and deleting.

**MSS**

1.  User requests to <u>list persons(UC01)</u>.
2.  User requests to <u>list tags(UC03)</u>.
3.  User requests to overwrite the tags of a property preference with new and existing tags.
4.  MatchEstate creates new tags.
5.  MatchEstate removes all tags from the property preference.
6.  MatchEstate adds the tags specified to the property preference.
7.  MatchEstate updates the usage number of the tags.
8.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 3a. The person index is invalid.

  * 3a1. MatchEstate shows an error message.

  Use case ends.

* 3b. The property preference index is invalid.

  * 3b1. MatchEstate shows an error message.

  Use case ends.

* 3c. New tags to be created already exist.

  * 3c1. MatchEstate shows an error message.

  Use case ends.

* 3d. Existing tags specified do not exist.

  * 3d1. MatchEstate shows an error message.

  Use case ends.

* 3e. No new tags to be created.

  Use case resumes at 5.

* 3f. No new tags to be created, no existing tags.

  * 3f1. MatchEstate shows an error message.

  Use case ends.

* 3g. No new tags to be created and property preference has no tags.

  Use case resumes at 6.

* 4a. Property preference has no tags.

  Use case resumes at 6.

#### Use case: UC22 - Overwrite tags in listings

Similar to <u>UC21</u> except for listings instead.

**MSS**

1.  User requests to <u>list listings(UC02)</u>.
2.  User requests to <u>list tags(UC03)</u>.
3.  User requests to overwrite the tags of a listing with new and existing tags.
4.  MatchEstate creates new tags.
5.  MatchEstate removes all tags from the listings.
6. MatchEstate adds the tags specified to the listings.
7.  MatchEstate updates the usage number of the tags
8.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 3a. The listing index is invalid.

  * 3a1. MatchEstate shows an error message.

  Use case ends.

* 3b. New tags to be created already exist.

  * 3b1. MatchEstate shows an error message.

  Use case ends.

* 3c. Existing tags specified do not exist.

  * 3c1. MatchEstate shows an error message.

  Use case ends.

* 3d. No new tags to be created.

  Use case resumes at 5.

* 3e. No new tags to be created, no existing tags.

  * 3e1. MatchEstate shows an error message.

  Use case ends.

* 3f. No new tags to be created and listings have no tags.

  Use case resumes at 6.

* 4a. Property preference has no tags.

  Use case resumes at 6.

#### Use case: UC23 - Search person by name

**MSS**

1.  User requests to <u>list persons(UC01)</u>.
2.  User specifies a search criteria containing one or more keywords.
3.  MatchEstate filters the list of persons, displaying only those that contain any of the specified keywords.
4.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 2a. The specified keywords is invalid.

  * 2a1. MatchEstate shows an error message.

  Use case ends.

* 3a. No persons match the specified keywords.

  * 3a1. MatchEstate displays a message indicating no results.

  Use case ends.

#### Use case: UC24 - Search listings by tags

Similar to <u>UC23</u> except with a has all tags criteria.

**MSS**

1.  User requests to <u>list listings(UC02)</u>.
2.  User specifies a search criteria containing one or more tags.
3.  MatchEstate filters the list of listings, displaying only those that contain all specified tags.
4.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 2a. The tag specified does not exist.

  * 2a1. MatchEstate shows an error message.

  Use case ends.

* 2b. No tags specified.

  * 2b1. MatchEstate shows an error message.

  Use case ends.

* 3a. No listings match the specified tags.

  * 3a1. MatchEstate displays a message indicating no results.

  Use case ends.

#### Use case: UC25 - Search for persons by property preference tags

Similar to <u>UC24</u> except for property preferences.

**MSS**

1.  User requests to <u>list persons(UC01)</u>.
2.  User specifies a search criteria containing one or more tags.
3.  MatchEstate filters the list of persons, displaying only those that contain all specified tags.
4.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 2a. The specified tag does not exist.

  * 2a1. MatchEstate shows an error message.

  Use case ends.

* 2b. No tags specified.

  * 2b1. MatchEstate shows an error message.

  Use case ends.

* 3a. No persons match the specified preference tags.

  * 3a1. MatchEstate displays a message indicating no results.

  Use case ends.

#### Use case: UC26 - Search for owner in listings

**MSS**

1.  User requests to <u>list persons(UC01)</u>.
2.  User requests to <u>list listings(UC02)</u>.
3.  User specifies the person to search as the owner in the list of listings.
4.  MatchEstate filters the listings, displaying on those with the specified person as an owner.
5.  MatchEstate a success message.

    Use case ends.

**Extensions**

* 3a. The person index is invalid.

  * 3a1. MatchEstate shows an error message.

  Use case ends.

* 4a. No listing has the specified owner.

  * 4a1. MatchEstate displays a message indicating no results.

  Use case ends.

#### Use case: UC27 - Match for preference

**MSS**

1.  User requests to list <u>list persons(UC01)</u>.
2.  User requests to <u>list listings (UC02)</u>.
3.  User specifies a property preference in a person to find a matching listing.
4.  MatchEstate filters the list of listings, displaying based on if any tags and price range of the available listing not owned by the person matches the property preference.
5.  MatchEstate sorts the filtered listings by the highest number of matching criteria.
6.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 2a. The person index is invalid.

  * 2a1. MatchEstate shows an error message.

  Use case ends.

* 2b. The property preference index is invalid.

  * 2b1. MatchEstate shows an error message.

* 4a. No listings match the specified property preference.

  * 4a1. MatchEstate displays a message indicating no results.

  Use case ends.

#### Use case: UC28 - Match for listing

Similar to <u>UC27</u> except for a listing.

**MSS**

1.  User requests to list <u>list persons(UC01)</u>.
2.  User requests to list <u>list listings(UC02)</u>.
3.  User specifies a listing to find a matching property preference across all persons.
4.  MatchEstate filters the list of property preferences, displaying persons who do not own the listing, based on if any tags and price range of any of their preferences matches the listing.
5.  MatchEstate sorts the filtered persons by the highest number of matching criteria in their preferences.
6.  MatchEstate displays a success message.

    Use case ends.

**Extensions**

* 2a. The listing index is invalid.

  * 2a1. MatchEstate shows an error message.

  Use case ends.

* 4a. No persons have a property preference that matches the specified listing.

  * 4a1. MatchEstate displays a message indicating no results.

  Use case ends.

#### Use case: UC29 - Clear data

**MSS**

1.  User requests to clear all data in the system.
2.  MatchEstate clears all tags, persons, preferences and listings.
3.  MatchEstate displays a success message

    Use case ends.

#### Use case: UC30 - Viewing help

**MSS**

1.  User requests to view a list of commands.
2.  MatchEstate displays a help window.
3.  MatchEstate displays a success message.

    Use case ends.

#### Use case: UC31 - Exit MatchEstate

**MSS**

1.  User requests to exit MatchEstate.
2.  MatchEstate closes.

    Use case ends.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to support at least 500 buyers and 500 sellers without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. The user interface should be intuitive enough that non-tech savvy users can use it.
5. Should complete all user commands within 2 seconds.
6. Should automatically save data to prevent data loss.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **Listing**: Entry of a property that is currently on the market. Contains property address information. Used interchangeably with Property.
* **Property**: The physical real estate itself. Used interchangeably with Listing.
* **Property Preference**: The preference of people based on price range and tags.
* **Tag Registry**: A Singleton used to store Tags to be put on properties and property preferences.

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
