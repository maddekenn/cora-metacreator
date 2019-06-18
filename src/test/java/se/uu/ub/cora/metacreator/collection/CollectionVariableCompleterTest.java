package se.uu.ub.cora.metacreator.collection;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class CollectionVariableCompleterTest {
	private SpiderInstanceFactorySpy instanceFactory;
	private String authToken;

	@BeforeMethod
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		authToken = "testUser";
	}

	@Test
	public void testCollectionVariableWithNoTexts() {
		CollectionVariableCompleter completer = CollectionVariableCompleter
				.forTextLinkedRecordType("someLinkedRecordType");

		DataGroup collectionVariable = DataCreator
				.createCollectionVariableWithIdDataDividerAndNameInData("someCollectionVar",
						"testSystem", "someType");
		collectionVariable.removeChild("textId");
		collectionVariable.removeChild("defTextId");

		completer.useExtendedFunctionality(authToken, collectionVariable);

		DataGroup textIdGroup = collectionVariable.getFirstGroupWithNameInData("textId");
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someCollectionVarText");
		assertEquals(textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"someLinkedRecordType");

		DataGroup defTextIdGroup = collectionVariable.getFirstGroupWithNameInData("defTextId");
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someCollectionVarDefText");
		assertEquals(defTextIdGroup.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"someLinkedRecordType");
	}
}
