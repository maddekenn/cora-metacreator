package se.uu.ub.cora.metacreator.collectionitem;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
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
		 .forImplementingTextType("textSystemOne");

		SpiderDataGroup item = DataCreator
				.createCollectionItemGroupWithIdTextIdDefTextIdAndImplementingTextType("firstItem", "", "", "textSystemOne");
		completer.useExtendedFunctionality(userId, item);

		SpiderDataGroup textIdGroup = item.extractGroup("textId");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordId"), "firstItemText");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordType"), "textSystemOne");

		SpiderDataGroup defTextIdGroup = item.extractGroup("defTextId");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordId"), "firstItemDefText");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordType"), "textSystemOne");


	}

	@Test
	public void testWithTextsInData() {
		 CollectionItemCompleter completer = CollectionItemCompleter
		 .forImplementingTextType("textSystemOne");


		SpiderDataGroup item = DataCreator.createCollectionItemGroupWithIdTextIdDefTextIdAndImplementingTextType(
				"firstItem", "someTextId", "someDefTextId","textSystemOne" );
		completer.useExtendedFunctionality(userId, item);

		SpiderDataGroup textIdGroup = item.extractGroup("textId");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordId"), "someTextId");
		assertEquals(textIdGroup.extractAtomicValue("linkedRecordType"), "textSystemOne");

		SpiderDataGroup defTextIdGroup = item.extractGroup("defTextId");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordId"), "someDefTextId");
		assertEquals(defTextIdGroup.extractAtomicValue("linkedRecordType"), "textSystemOne");
	}
}
