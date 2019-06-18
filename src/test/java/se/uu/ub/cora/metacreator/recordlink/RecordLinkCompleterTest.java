package se.uu.ub.cora.metacreator.recordlink;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

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
		RecordLinkCompleter completer = RecordLinkCompleter
				.forTextLinkedRecordType("someLinkedRecordType");

		DataGroup recordLink = DataCreator.createMetadataRecordLinkWithId("someRecordLink");

		completer.useExtendedFunctionality(authToken, recordLink);

		DataGroup textIdGroup = recordLink.getFirstGroupWithNameInData("textId");
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someRecordLinkText");
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"someLinkedRecordType");

		DataGroup defTextIdGroup = recordLink.getFirstGroupWithNameInData("defTextId");
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someRecordLinkDefText");
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"someLinkedRecordType");
	}

	@Test
	public void testRecordLinkWithTexts() {
		RecordLinkCompleter completer = RecordLinkCompleter
				.forTextLinkedRecordType("someLinkedRecordType");

		DataGroup recordLink = DataCreator.createMetadataRecordLinkWithId("someRecordLink");
		DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordLink,
				"textId", "textSystemOne", "anExistingText");
		DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(recordLink,
				"defTextId", "textSystemOne", "anExistingDefText");

		completer.useExtendedFunctionality(authToken, recordLink);

		DataGroup textIdGroup = recordLink.getFirstGroupWithNameInData("textId");
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"anExistingText");
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"textSystemOne");

		DataGroup defTextIdGroup = recordLink.getFirstGroupWithNameInData("defTextId");
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"anExistingDefText");
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"textSystemOne");
	}
}
