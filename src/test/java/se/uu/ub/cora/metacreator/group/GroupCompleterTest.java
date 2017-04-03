package se.uu.ub.cora.metacreator.group;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.uu.ub.cora.metacreator.collection.ItemCollectionCompleter;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

import static org.testng.Assert.assertEquals;

public class GroupCompleterTest {
    private SpiderInstanceFactorySpy instanceFactory;
    private String authToken;

    @BeforeMethod
    public void setUp() {
        instanceFactory = new SpiderInstanceFactorySpy();
        SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
        authToken = "testUser";
    }

    @Test
    public void testGroupWithNoTexts() {
        GroupCompleter completer = new GroupCompleter();

        SpiderDataGroup metadataGroup = DataCreator
                .createMetadataGroupWithId("someMetadataGroup");

        completer.useExtendedFunctionality(authToken, metadataGroup);

        assertEquals(metadataGroup.extractAtomicValue("textId"), "someMetadataGroupText");
        assertEquals(metadataGroup.extractAtomicValue("defTextId"), "someMetadataGroupDefText");
    }
}
