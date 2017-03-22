package se.uu.ub.cora.metacreator.collectionItem;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.MetadataCompleter;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.dependency.SpiderRecordCreatorSpy;
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
		MetadataCompleter metadataCompleter = new MetadataCompleter();
		CollectionItemCompleter completer = CollectionItemCompleter
				.forImplementingTextTypeWithMetadataCompleter("textSystemOne", metadataCompleter);

		SpiderDataGroup item = DataCreator
				.createCollectionItemGroupWithIdAndTextIdAndDefTextId("firstItem", "", "");

		completer.useExtendedFunctionality(userId, item);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 2);
		 assertCorrectTextCreatedWithUserIdAndTypeAndId(0, "firstItemText");
		 assertCorrectTextCreatedWithUserIdAndTypeAndId(1, "firstItemDefText");
	}

	private void assertCorrectTextCreatedWithUserIdAndTypeAndId(int createdTextNo,
			String createdIdForText) {
		SpiderRecordCreatorSpy spiderRecordCreator = instanceFactory.spiderRecordCreators
				.get(createdTextNo);
		assertEquals(spiderRecordCreator.userId, userId);
		assertEquals(spiderRecordCreator.type, "textSystemOne");
		SpiderDataGroup createdTextRecord = spiderRecordCreator.record;
		SpiderDataGroup recordInfo = createdTextRecord.extractGroup("recordInfo");
		String id = recordInfo.extractAtomicValue("id");
		assertEquals(id, createdIdForText);
	}
}
