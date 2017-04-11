package se.uu.ub.cora.metacreator.search;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.uu.ub.cora.metacreator.collection.ItemCollectionCreator;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

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
        SpiderDataGroup search = DataCreator.createSearchWithId("someSearch");
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
        SpiderDataGroup search = DataCreator.createSearchWithId("someSearch");
        DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(
                search, "textId", "textSystemOne", "someExistingText");
        DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(
                search, "defTextId", "textSystemOne", "someExistingDefText");

        SearchCreator creator = SearchCreator.forImplementingTextType("textSystemOne");
        creator.useExtendedFunctionality(authToken, search);

        assertEquals(instanceFactory.spiderRecordCreators.size(), 0);
    }
}
