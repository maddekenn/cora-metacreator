package se.uu.ub.cora.metacreator.group;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.dependency.SpiderRecordCreatorSpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class PGroupFromMetadataGroupCreatorTest {
	private SpiderInstanceFactorySpy instanceFactory;
	private String authToken;

	@BeforeMethod
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		authToken = "testUser";
	}

	@Test
	public void testPGroupsDoesNotExist() {
		SpiderDataGroup metadataGroup = DataCreator.createMetadataGroupWithId("someTestGroup");

		PGroupFromMetadataGroupCreator creator = new PGroupFromMetadataGroupCreator();
		creator.useExtendedFunctionality(authToken, metadataGroup);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 2);
		SpiderRecordCreatorSpy spiderRecordCreatorSpy = instanceFactory.spiderRecordCreators.get(0);
		assertEquals(spiderRecordCreatorSpy.type, "presentationGroup");
		SpiderDataGroup record = spiderRecordCreatorSpy.record;

		assertEquals(record.getNameInData(), "presentation");
		// assertEquals(record.extractAtomicValue("mode"), "input");

		assertCorrectPresentationOf(record);
		// assertCorrectRecordInfo(record, "somePCollVar");

		// assertCorrectlyCreatedInputPCollVar();
		// assertCorrectlyCreatedOutputPCollVar();

		// TODO: check mpre stuff and also outputPGroup
	}

	private void assertCorrectPresentationOf(SpiderDataGroup record) {
		SpiderDataGroup presentationOf = record.extractGroup("presentationOf");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordId"), "someTestGroup");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordType"), "metadataGroup");
	}
}
