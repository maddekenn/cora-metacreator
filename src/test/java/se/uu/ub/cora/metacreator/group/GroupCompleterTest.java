package se.uu.ub.cora.metacreator.group;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

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

		DataGroup metadataGroup = DataCreator
				.createMetadataGroupWithIdAndTextVarAsChildReference("someMetadataGroup");

		completer.useExtendedFunctionality(authToken, metadataGroup);

		DataGroup textIdGroup = metadataGroup.getFirstGroupWithNameInData("textId");
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someMetadataGroupText");
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"someLinkedRecordType");

		DataGroup defTextIdGroup = metadataGroup.getFirstGroupWithNameInData("defTextId");
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someMetadataGroupDefText");
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"someLinkedRecordType");
	}

	@Test
	public void testGroupWithTexts() {
		GroupCompleter completer = GroupCompleter.forTextLinkedRecordType("someLinkedRecordType");

		DataGroup metadataGroup = DataCreator
				.createMetadataGroupWithIdAndTextVarAsChildReference("someMetadataGroup");

		DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(metadataGroup,
				"textId", "textSystemOne", "anExistingText");
		DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(metadataGroup,
				"defTextId", "textSystemOne", "anExistingDefText");

		completer.useExtendedFunctionality(authToken, metadataGroup);

		DataGroup textIdGroup = metadataGroup.getFirstGroupWithNameInData("textId");
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"anExistingText");
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"textSystemOne");

		DataGroup defTextIdGroup = metadataGroup.getFirstGroupWithNameInData("defTextId");
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"anExistingDefText");
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"textSystemOne");
	}

}
