package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Email;
import seedu.address.model.person.MedicalHistory;
import seedu.address.model.person.Name;
import seedu.address.model.person.NRIC;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new NRIC("T0123456F"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                new Appointment("8 Aug 2023 10AM - 12PM"),
                getMedicalHistorySet("Diabetes"),
                getTagSet("friends")),
            new Person(new Name("Bernice Yu"), new NRIC("S0123452F"),new Phone("99272758"), new Email("berniceyu@example.com"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                    new Appointment("8 Aug 2023 10AM - 12PM"),
                    getMedicalHistorySet("Diabetes"),
                    getTagSet("colleagues", "friends")),
            new Person(new Name("Charlotte Oliveiro"), new NRIC("T0123456Y"),new Phone("93210283"), new Email("charlotte@example.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                    new Appointment("8 Aug 2023 10AM - 12PM"),
                    getMedicalHistorySet("Diabetes"),
                    getTagSet("neighbours")),
            new Person(new Name("David Li"), new NRIC("T0123436F"),new Phone("91031282"), new Email("lidavid@example.com"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                    new Appointment("8 Aug 2023 10AM - 12PM"),
                    getMedicalHistorySet("Diabetes"),
                    getTagSet("family")),
            new Person(new Name("Irfan Ibrahim"), new NRIC("S0123456F"),new Phone("92492021"), new Email("irfan@example.com"),
                new Address("Blk 47 Tampines Street 20, #17-35"),
                    new Appointment("8 Aug 2023 10AM - 12PM"),
                    getMedicalHistorySet("Diabetes"),
                    getTagSet("classmates")),
            new Person(new Name("Roy Balakrishnan"), new NRIC("T0128456F"),new Phone("92624417"), new Email("royb@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"),
                    new Appointment("8 Aug 2023 10AM - 12PM"),
                    getMedicalHistorySet("Diabetes"),
                    getTagSet("colleagues"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<MedicalHistory> getMedicalHistorySet(String... strings) {
        return Arrays.stream(strings)
                .map(MedicalHistory::new)
                .collect(Collectors.toSet());
    }

}
