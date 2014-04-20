package gutter.test.json

import org.junit.Test

class JsonTreeParsingTest {
    @Test
    void testSimpleCollectionOfTypeParsing() {
        List<Person> people = '''
        [
            {
                "name": "Kenneth",
                "age": 14
            }
        ]
        '''.stripIndent().parseJSON() as List<Person>
        def alex = people.first()
        assert alex.name == "Kenneth"
        assert alex.age == 14
    }
}
