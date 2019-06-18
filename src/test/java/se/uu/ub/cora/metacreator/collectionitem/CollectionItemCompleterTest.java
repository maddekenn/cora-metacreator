package se.uu.ub.cora.metacreator.collectionitem;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class CollectionItemCompleterTest {
	private SpiderInstanceFactorySpy instanceFactory;
	private String userId;

	@BeforeMethod
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		userId = "testUser";
	}

	@Test
	public void testWithNoTexts() {
		CollectionItemCompleter completer = CollectionItemCompleter
				.forTextLinkedRecordType("textSystemOne");

		DataGroup item = DataCreator
				.createCollectionItemGroupWithIdTextIdDefTextIdAndImplementingTextType("firstItem",
						"", "", "textSystemOne");
		completer.useExtendedFunctionality(userId, item);

		DataGroup textIdGroup = item.getFirstGroupWithNameInData("textId");
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"firstItemText");
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"textSystemOne");

		DataGroup defTextIdGroup = item.getFirstGroupWithNameInData("defTextId");
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"firstItemDefText");
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"textSystemOne");

	}

	@Test
	public void testWithTextsInData() {
		CollectionItemCompleter completer = CollectionItemCompleter
				.forTextLinkedRecordType("textSystemOne");

		DataGroup item = DataCreator
				.createCollectionItemGroupWithIdTextIdDefTextIdAndImplementingTextType("firstItem",
						"someTextId", "someDefTextId", "textSystemOne");
		completer.useExtendedFunctionality(userId, item);

		DataGroup textIdGroup = item.getFirstGroupWithNameInData("textId");
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"), "someTextId");
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"textSystemOne");

		DataGroup defTextIdGroup = item.getFirstGroupWithNameInData("defTextId");
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someDefTextId");
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"textSystemOne");
	}
}
