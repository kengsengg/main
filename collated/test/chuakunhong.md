# chuakunhong
###### \java\seedu\address\logic\commands\AddRemarkCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class AddRemarkCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        AddRemarkCommand addRemarkCommand = prepareCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(addRemarkCommand.MESSAGE_REMARK_PERSON_SUCCESS, editedPerson.getRemark(),
                                                editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(addRemarkCommand, model, expectedMessage, expectedModel);
    }

    /*
    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB).withNric(VALID_NRIC_BOB)
                .withTags(VALID_TAG_HUSBAND).withRemark(VALID_REMARK).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withNric(VALID_NRIC_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = prepareCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(lastPerson, editedPerson);

        //assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() throws IOException {
        EditCommand editCommand = prepareCommand(INDEX_FIRST_PERSON, new EditPersonDescriptor());
        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).withRemark(" ").build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedPerson);

        //assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() throws IOException {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        EditCommand editCommand = prepareCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() throws IOException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit person in filtered list into a duplicate in address book
        Person personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = prepareCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder(personInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() throws IOException {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = prepareCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */

    /*
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() throws IOException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditCommand editCommand = prepareCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = prepareCommand(outOfBoundIndex, descriptor);

        // execution failed -> editCommand not pushed into undoRedoStack

        try {
            assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        } catch (IOException e) {
            fail("The expected CommandException was not thrown.");
        }

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        try {
            assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        } catch (IOException e) {
            fail("The expected CommandException was not thrown.");
        }
        try {
            assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
        } catch (IOException e) {
            fail("The expected CommandException was not thrown.");
        }

    }

    /**
     * 1. Edits a {@code Person} from a filtered list.
     * 2. Undo the edit.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously edited person in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the edit. This ensures {@code RedoCommand} edits the person object regardless of indexing.
     */
    /*
    @Test
    public void executeUndoRedo_validIndexFilteredList_samePersonEdited() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_PERSON, descriptor);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        showPersonAtIndex(model, INDEX_SECOND_PERSON);
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // edit -> edits second person in unfiltered person list / first person in filtered person list
        editCommand.execute();
        undoRedoStack.push(editCommand);

        // undo -> reverts addressbook back to previous state and filtered person list to show all persons
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.updatePerson(personToEdit, editedPerson);
        assertNotEquals(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), personToEdit);
        // redo -> edits same second person in unfiltered person list
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }
    */
    /*
    @Test
    public void equals() throws Exception {
        final EditCommand standardCommand = prepareCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = prepareCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // one command preprocessed when previously equal -> returns false
        commandWithSameValues.preprocessUndoableCommand();
        assertFalse(standardCommand.equals(commandWithSameValues));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }
    */
    /**
     * Returns an {@code EditCommand} with parameters {@code index} and {@code descriptor}
     */
    private AddRemarkCommand prepareCommand(Index index, EditPersonDescriptor descriptor) {
        AddRemarkCommand addRemarkCommand = new AddRemarkCommand(index, descriptor);
        addRemarkCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return addRemarkCommand;
    }

    /**
     * A utility class to help with building EditPersonDescriptor objects.
     */
    public class EditPersonDescriptorBuilder {

        private EditPersonDescriptor descriptor;

        public EditPersonDescriptorBuilder() {
            descriptor = new EditPersonDescriptor();
        }

        public EditPersonDescriptorBuilder(EditPersonDescriptor descriptor) {
            this.descriptor = new EditPersonDescriptor(descriptor);
        }

        /**
         * Returns an {@code EditPersonDescriptor} with fields containing {@code person}'s details
         */
        public EditPersonDescriptorBuilder(Person person) {
            descriptor = new EditPersonDescriptor();
            descriptor.setName(person.getName());
            descriptor.setNric(person.getNric());
            descriptor.setTags(person.getTags());
            descriptor.setSubjects(person.getSubjects());
            descriptor.setRemark(person.getRemark());
        }

        /**
         * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
         */
        public EditPersonDescriptorBuilder withName(String name) {
            descriptor.setName(new Name(name));
            return this;
        }

        /**
         * Sets the {@code Nric} of the {@code EditPersonDescriptor} that we are building.
         */
        public EditPersonDescriptorBuilder withNric(String nric) {
            descriptor.setNric(new Nric(nric));
            return this;
        }

        /**
         * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditPersonDescriptor}
         * that we are building.
         */
        public EditPersonDescriptorBuilder withTags(String... tags) {
            Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
            descriptor.setTags(tagSet);
            return this;
        }

        /**
         * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditPersonDescriptor}
         * that we are building.
         */
        public EditPersonDescriptorBuilder withSubjects(String... subjects) {
            Set<Subject> subjectSet = Stream.of(subjects).map(Subject::new).collect(Collectors.toSet());
            descriptor.setSubjects(subjectSet);
            return this;
        }

        /**
         * Sets the {@code Remark} of the {@code EditPersonDescriptor} that we are building.
         */
        public EditPersonDescriptorBuilder withRemark(String remark) {
            descriptor.setRemark(new Remark(remark));
            return this;
        }

        public EditPersonDescriptor build() {
            return descriptor;
        }
    }
```
###### \java\seedu\address\logic\commands\TagReplaceCommandTest.java
``` java
public class TagReplaceCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Tag tagToDelete = new Tag("removeTag");
    private Tag tagToBePlace = new Tag("replaceTag");

    @Test
    public void execute_validTagToReplaceEntered_success() throws Exception {
        List<Tag> tagList = model.getAddressBook().getTagList();
        TagReplaceCommand tagReplaceCommand = prepareCommand(tagList);

        String expectedMessage = String.format(TagReplaceCommand.MESSAGE_REPLACE_TAG_SUCCESS, tagList.get(0),
                                                tagList.get(1));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.replaceTag(tagList);

        assertCommandSuccess(tagReplaceCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidTagToRemoveEntered_throwsCommandException() throws IOException {
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tagToDelete);
        tagList.add(tagToBePlace);
        TagReplaceCommand tagReplaceCommand = prepareCommand(tagList);
        assertCommandFailure(tagReplaceCommand, model, Messages.MESSAGE_INVALID_TAG_ENTERED);
    }

    @Test
    public void executeUndoRedo_invalidTagToReplaceEntered_failure() throws IOException {
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tagToDelete);
        tagList.add(tagToBePlace);
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        TagReplaceCommand tagReplaceCommand = prepareCommand(tagList);

        // execution failed -> replaceCommand not pushed into undoRedoStack
        assertCommandFailure(tagReplaceCommand, model, Messages.MESSAGE_INVALID_TAG_ENTERED);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * Returns a {@code DeleteCommand} with the parameter {@code index}.
     */
    private TagReplaceCommand prepareCommand(List<Tag> tagList) {
        TagReplaceCommand tagReplaceCommand = new TagReplaceCommand(tagList);
        tagReplaceCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return tagReplaceCommand;
    }
```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_addAlias() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(AddCommand.COMMAND_ALIAS + " "
                + PersonUtil.getPersonDetails(person));
        assertEquals(new AddCommand(person), command);
    }

```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_deleteAlias() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_editAlias() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_ALIAS + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getPersonDetails(person));
        //assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_findAlias() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_ALIAS + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_historyAlias() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_ALIAS) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_ALIAS + " 3") instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            fail("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }

```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_listAlias() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_ALIAS) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_ALIAS + " 3") instanceof ListCommand);
    }

```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_selectAlias() throws Exception {
        SelectCommand command = (SelectCommand) parser.parseCommand(
                SelectCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_PERSON), command);
    }

```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_redoCommandAlias_returnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_ALIAS) instanceof RedoCommand);
        assertTrue(parser.parseCommand("redo 1") instanceof RedoCommand);
    }

```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_undoCommandAlias_returnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_ALIAS) instanceof UndoCommand);
        assertTrue(parser.parseCommand("undo 3") instanceof UndoCommand);
    }

```
###### \java\seedu\address\logic\parser\TagReplaceCommandParserTest.java
``` java
public class TagReplaceCommandParserTest {

    private TagReplaceCommandParser parser = new TagReplaceCommandParser();


    @Test


    public void parse_validArgs_returnsTagReplaceCommand() {
        List<Tag> tagList = new ArrayList<Tag>(){};
        tagList.add(new Tag("friend"));
        tagList.add(new Tag("husband"));
        assertParseFailure(parser, "t/ ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            TagReplaceCommand.MESSAGE_USAGE));
        //assertParseFailure(parser, "t/", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
        //                   TagReplaceCommand.MESSAGE_USAGE));
        //assertParseSuccess(parser, "t/friend t/husband", new TagReplaceCommand(tagList));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                                                TagReplaceCommand.MESSAGE_USAGE));
    }
```
###### \java\seedu\address\model\person\NricTest.java
``` java
public class NricTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Nric(null));
    }

    @Test
    public void constructor_invalidNric_throwsIllegalArgumentException() {
        String invalidNric = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Nric(invalidNric));
    }

    @Test
    public void isValidNric() {
        // null nric number
        Assert.assertThrows(NullPointerException.class, () -> Nric.isValidNric(null));

        // invalid nric numbers
        assertFalse(Nric.isValidNric("")); // empty string
        assertFalse(Nric.isValidNric(" ")); // spaces only
        assertFalse(Nric.isValidNric("91")); // less than 3 numbers
        assertFalse(Nric.isValidNric("ic")); // non-numeric
        assertFalse(Nric.isValidNric("9011p041")); // alphabets within digits
        assertFalse(Nric.isValidNric("S9312 153A")); // spaces within digits

        // valid nric number
        assertTrue(Nric.isValidNric("S9312154Z"));
    }
}
```
