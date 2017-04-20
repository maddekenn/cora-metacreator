package se.uu.ub.cora.metacreator;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.dependency.SpiderRecordCreatorSpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class RecordCreatorHelperTest {

	private SpiderInstanceFactorySpy instanceFactory;
	private String authToken;

	@BeforeMethod
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		authToken = "testUser";
	}

	@Test
	public void testInit() {
		SpiderDataGroup itemCollection = DataCreator
				.createItemCollectionWithId("someOtherCollection");
		String textId = "someNonExistingText";
		DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(itemCollection,
				"textId", "textSystemOne", textId);
		String defTextId = "someNonExistingDefText";
		DataCreator.addRecordLinkWithNameInDataAndLinkedRecordTypeAndLinkedRecordId(itemCollection,
				"defTextId", "textSystemOne", defTextId);

		RecordCreatorHelper creatorHelper = RecordCreatorHelper
				.withAuthTokenSpiderDataGroupAndImplementingTextType("testUser", itemCollection,
						"textSystemOne");
		creatorHelper.createTextsIfMissing();

		assertEquals(instanceFactory.spiderRecordCreators.size(), 2);
		assertCorrectlyCreatedText(textId, 0);
		assertCorrectlyCreatedText(defTextId, 1);
	}

	private void assertCorrectlyCreatedText(String textId, int createdIndex) {
		SpiderRecordCreatorSpy spiderRecordCreator = instanceFactory.spiderRecordCreators
				.get(createdIndex);
		SpiderDataGroup recordInfo = spiderRecordCreator.record.extractGroup("recordInfo");
		assertEquals(recordInfo.extractAtomicValue("id"), textId);
		assertEquals(spiderRecordCreator.type, "textSystemOne");

		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "test");
	}
}
