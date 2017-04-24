package se.uu.ub.cora.metacreator.recordlink;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.group.GroupCompleter;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

import static org.testng.Assert.assertEquals;

public class RecordLinkCompleterTest {
    private SpiderInstanceFactorySpy instanceFactory;
    private String authToken;

    @BeforeMethod
    public void setUp() {
        instanceFactory = new SpiderInstanceFactorySpy();
        SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
        authToken = "testUser";
    }

    @Test
    public void testRecordLinkWithNoTexts() {
        RecordLinkCompleter completer = RecordLinkCompleter.forTextLinkedRecordType("someLinkedRecordType");

        SpiderDataGroup recordLink = DataCreator
                .createMetadataRecordLinkWithId("someRecordLink");

        completer.useExtendedFunctionality(authToken, recordLink);

        SpiderDataGroup textIdGroup = recordLink.extractGroup("textId");
        assertEquals(textIdGroup.extractAtomicValue("linkedRecordId"), "someRecordLinkText");
        assertEquals(textIdGroup.extractAtomicValue("linkedRecordType"), "someLinkedRecordType");

        SpiderDataGroup defTextIdGroup = recordLink.extractGroup("defTextId");
        assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordId"), "someRecordLinkDefText");
        assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordType"), "someLinkedRecordType");
    }

    @Test
    public void testRecordLinkWithTexts() {
        RecordLinkCompleter completer = RecordLinkCompleter.forTextLinkedRecordType("someLinkedRecordType");

        SpiderDataGroup recordLink = DataCreator
                .createMetadataRecordLinkWithId("someRecordLink");
        DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordLink,
                "textId", "textSystemOne", "anExistingText");
        DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordLink,
                "defTextId", "textSystemOne", "anExistingDefText");

        completer.useExtendedFunctionality(authToken, recordLink);

        SpiderDataGroup textIdGroup = recordLink.extractGroup("textId");
        assertEquals(textIdGroup.extractAtomicValue("linkedRecordId"), "anExistingText");
        assertEquals(textIdGroup.extractAtomicValue("linkedRecordType"), "textSystemOne");

        SpiderDataGroup defTextIdGroup = recordLink.extractGroup("defTextId");
        assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordId"), "anExistingDefText");
        assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordType"), "textSystemOne");
    }
}
