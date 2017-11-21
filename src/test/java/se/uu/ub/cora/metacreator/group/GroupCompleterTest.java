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
        GroupCompleter completer = GroupCompleter.forTextLinkedRecordType("someLinkedRecordType");

        SpiderDataGroup metadataGroup = DataCreator
                .createMetadataGroupWithIdAndTextVarAsChildReference("someMetadataGroup");

        completer.useExtendedFunctionality(authToken, metadataGroup);

        SpiderDataGroup textIdGroup = metadataGroup.extractGroup("textId");
        assertEquals(textIdGroup.extractAtomicValue("linkedRecordId"), "someMetadataGroupText");
        assertEquals(textIdGroup.extractAtomicValue("linkedRecordType"), "someLinkedRecordType");

        SpiderDataGroup defTextIdGroup = metadataGroup.extractGroup("defTextId");
        assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordId"), "someMetadataGroupDefText");
        assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordType"), "someLinkedRecordType");
    }

    @Test
    public void testGroupWithTexts() {
        GroupCompleter completer = GroupCompleter.forTextLinkedRecordType("someLinkedRecordType");

        SpiderDataGroup metadataGroup = DataCreator
                .createMetadataGroupWithIdAndTextVarAsChildReference("someMetadataGroup");

        DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(metadataGroup,
                "textId", "textSystemOne", "anExistingText");
        DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(metadataGroup,
                "defTextId", "textSystemOne", "anExistingDefText");

        completer.useExtendedFunctionality(authToken, metadataGroup);

        SpiderDataGroup textIdGroup = metadataGroup.extractGroup("textId");
        assertEquals(textIdGroup.extractAtomicValue("linkedRecordId"), "anExistingText");
        assertEquals(textIdGroup.extractAtomicValue("linkedRecordType"), "textSystemOne");

        SpiderDataGroup defTextIdGroup = metadataGroup.extractGroup("defTextId");
        assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordId"), "anExistingDefText");
        assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordType"), "textSystemOne");
    }

}
