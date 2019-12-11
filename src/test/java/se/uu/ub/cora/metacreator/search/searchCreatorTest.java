package se.uu.ub.cora.metacreator.search;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class searchCreatorTest {
    private SpiderInstanceFactorySpy instanceFactory;
    private String authToken;

    @BeforeMethod
    public void setUp() {
        instanceFactory = new SpiderInstanceFactorySpy();
        SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
        authToken = "testUser";
    }

    @Test
    public void testCreateTextNoTextExists(){
        DataGroup search = DataCreator.createSearchWithId("someSearch");
        DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(
                search, "textId", "textSystemOne", "someNonExistingText");
        DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(
                search, "defTextId", "textSystemOne", "someNonExistingDefText");

        SearchCreator creator = SearchCreator.forImplementingTextType("textSystemOne");
        assertNotNull(creator);
        creator.useExtendedFunctionality(authToken, search);

        assertEquals(instanceFactory.spiderRecordCreators.size(), 2);
    }

    @Test
    public void testCreateTextWhenTextExists(){
        DataGroup search = DataCreator.createSearchWithId("someSearch");
        DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(
                search, "textId", "textSystemOne", "someExistingText");
        DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(
                search, "defTextId", "textSystemOne", "someExistingDefText");

        SearchCreator creator = SearchCreator.forImplementingTextType("textSystemOne");
        creator.useExtendedFunctionality(authToken, search);

        assertEquals(instanceFactory.spiderRecordCreators.size(), 0);
    }
}
