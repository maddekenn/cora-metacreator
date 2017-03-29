package se.uu.ub.cora.metacreator.recordlink;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.dependency.SpiderRecordCreatorSpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class PLinkFromRecordLinkCreatorTest {
	private SpiderInstanceFactorySpy instanceFactory;
	private String authToken;

	@BeforeMethod
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		authToken = "testUser";
	}

	@Test
	public void testPCollVarsDoesNotExist() {
		SpiderDataGroup recordLink = DataCreator
				.createRecordLinkWithIdDataDividerNameInDataAndLinkedRecordType("someRandomLink",
						"testSystem", "someRandom", "someRecordType");

		PLinkFromRecordLinkCreator creator = new PLinkFromRecordLinkCreator();
		creator.useExtendedFunctionality(authToken, recordLink);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 2);
		assertCorrectlyCreatedInputPLink();
		assertCorrectlyCreatedOutputPCollVar();
	}

	private void assertCorrectlyCreatedInputPLink() {
		SpiderRecordCreatorSpy spiderRecordCreatorSpy = instanceFactory.spiderRecordCreators.get(0);
		assertEquals(spiderRecordCreatorSpy.type, "presentationRecordLink");
		SpiderDataGroup record = spiderRecordCreatorSpy.record;

		assertEquals(record.getNameInData(), "presentation");
		assertEquals(record.extractAtomicValue("mode"), "input");

		assertCorrectPresentationOf(record);
		assertCorrectRecordInfo(record, "someRandomPLink");
		assertEquals(record.getAttributes().get("type"), "pRecordLink");
	}

	private void assertCorrectPresentationOf(SpiderDataGroup record) {
		SpiderDataGroup presentationOf = record.extractGroup("presentationOf");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordId"), "someRandomLink");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordType"), "metadataRecordLink");
	}

	private void assertCorrectRecordInfo(SpiderDataGroup record, String expextedId) {
		SpiderDataGroup recordInfo = record.extractGroup("recordInfo");
		assertEquals(recordInfo.extractAtomicValue("id"), expextedId);
		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "testSystem");
	}

	private void assertCorrectlyCreatedOutputPCollVar() {
		SpiderRecordCreatorSpy spiderRecordCreatorSpy = instanceFactory.spiderRecordCreators.get(1);
		assertEquals(spiderRecordCreatorSpy.type, "presentationRecordLink");
		SpiderDataGroup record = spiderRecordCreatorSpy.record;
		assertEquals(record.getNameInData(), "presentation");
		assertEquals(record.extractAtomicValue("mode"), "output");

		assertCorrectPresentationOf(record);
		assertCorrectRecordInfo(record, "someRandomOutputPLink");
		assertEquals(record.getAttributes().get("type"), "pRecordLink");
	}

	@Test
	public void testPLinksAlreadyExist() {
		SpiderDataGroup recordLink = DataCreator
				.createRecordLinkWithIdDataDividerNameInDataAndLinkedRecordType("someExistingLink",
						"testSystem", "someRandom", "someRecordType");

		PLinkFromRecordLinkCreator creator = new PLinkFromRecordLinkCreator();
		creator.useExtendedFunctionality(authToken, recordLink);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 0);
	}
}
