package se.uu.ub.cora.metacreator.recordlink;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.dependency.SpiderRecordCreatorSpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
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
	public void testPLinkDoesNotExist() {
		DataGroup recordLink = DataCreator
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
		DataGroup record = spiderRecordCreatorSpy.record;

		assertEquals(record.getNameInData(), "presentation");
		assertEquals(record.getFirstAtomicValueWithNameInData("mode"), "input");

		assertCorrectPresentationOf(record);
		assertCorrectRecordInfo(record, "someRandomPLink");
		assertEquals(record.getAttributes().get("type"), "pRecordLink");
	}

	private void assertCorrectPresentationOf(DataGroup record) {
		DataGroup presentationOf = record.getFirstGroupWithNameInData("presentationOf");
		assertEquals(presentationOf.getFirstAtomicValueWithNameInData("linkedRecordId"), "someRandomLink");
		assertEquals(presentationOf.getFirstAtomicValueWithNameInData("linkedRecordType"), "metadataRecordLink");
	}

	private void assertCorrectRecordInfo(DataGroup record, String expextedId) {
		DataGroup recordInfo = record.getFirstGroupWithNameInData("recordInfo");
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), expextedId);
		DataGroup dataDivider = recordInfo.getFirstGroupWithNameInData("dataDivider");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordId"), "testSystem");
	}

	private void assertCorrectlyCreatedOutputPCollVar() {
		SpiderRecordCreatorSpy spiderRecordCreatorSpy = instanceFactory.spiderRecordCreators.get(1);
		assertEquals(spiderRecordCreatorSpy.type, "presentationRecordLink");
		DataGroup record = spiderRecordCreatorSpy.record;
		assertEquals(record.getNameInData(), "presentation");
		assertEquals(record.getFirstAtomicValueWithNameInData("mode"), "output");

		assertCorrectPresentationOf(record);
		assertCorrectRecordInfo(record, "someRandomOutputPLink");
		assertEquals(record.getAttributes().get("type"), "pRecordLink");
	}

	@Test
	public void testPLinksAlreadyExist() {
		DataGroup recordLink = DataCreator
				.createRecordLinkWithIdDataDividerNameInDataAndLinkedRecordType("someExistingLink",
						"testSystem", "someRandom", "someRecordType");

		PLinkFromRecordLinkCreator creator = new PLinkFromRecordLinkCreator();
		creator.useExtendedFunctionality(authToken, recordLink);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 0);
	}

	@Test
	public void testPLinkWithLinkAsPartOfName() {
		DataGroup recordLink = DataCreator
				.createRecordLinkWithIdDataDividerNameInDataAndLinkedRecordType("someLinkNameLink",
						"testSystem", "someLink", "someRecordType");

		PLinkFromRecordLinkCreator creator = new PLinkFromRecordLinkCreator();
		creator.useExtendedFunctionality(authToken, recordLink);

		SpiderRecordCreatorSpy spiderRecordCreatorSpy = instanceFactory.spiderRecordCreators.get(0);
		DataGroup record = spiderRecordCreatorSpy.record;

		DataGroup recordInfo = record.getFirstGroupWithNameInData("recordInfo");
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), "someLinkNamePLink");
	}
}
