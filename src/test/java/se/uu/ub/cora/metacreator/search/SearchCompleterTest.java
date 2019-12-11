package se.uu.ub.cora.metacreator.search;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class SearchCompleterTest {
    private SpiderInstanceFactorySpy instanceFactory;
    private String authToken;

    @BeforeMethod
    public void setUp() {
        instanceFactory = new SpiderInstanceFactorySpy();
        SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
        authToken = "testUser";
    }

    @Test
    public void testSearchWithNoTexts() {
        SearchCompleter completer = SearchCompleter.forTextLinkedRecordType("textSystemOne");

        assertNotNull(completer);
        DataGroup search = DataCreator
                .createSearchWithId("someSearch");
        completer.useExtendedFunctionality(authToken, search);
        assertCorrectText(search, "textId", "someSearchText");
        assertCorrectText(search, "defTextId", "someSearchDefText");

    }

    private void assertCorrectText(DataGroup search, String groupId, String linkedRecordId) {
        DataGroup textIdGroup = search.getFirstGroupWithNameInData(groupId);
        assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"), linkedRecordId);
        assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"), "textSystemOne");
    }

    @Test
    public void testSearchWithTexts() {
        SearchCompleter completer = SearchCompleter.forTextLinkedRecordType("textSystemOne");

        DataGroup search = DataCreator
                .createSearchWithId("someSearch");
        DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(search,
                "textId", "textSystemOne", "anExistingText");
        DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(search,
                "defTextId", "textSystemOne", "anExistingDefText");

        completer.useExtendedFunctionality(authToken, search);
        assertCorrectText(search, "textId", "anExistingText");
        assertCorrectText(search, "defTextId", "anExistingDefText");

    }

}
