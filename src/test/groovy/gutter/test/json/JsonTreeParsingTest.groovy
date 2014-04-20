package gutter.test.json

import gutter.json.JSON
import org.junit.Test

class JsonTreeParsingTest {
    @Test
    void testSimpleCollectionOfTypeParsing() {
        List<Person> people = JSON.parse('''
        [
            {
                "name": "Kenneth",
                "age": 14
            }
        ]
        '''.stripIndent()) as List<Person>
        def alex = people.first()
        assert alex.name == "Kenneth"
        assert alex.age == 14
    }
}
