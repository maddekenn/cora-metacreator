/*
 * Copyright 2018 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.uu.ub.cora.metacreator.group;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataElement;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.record.DataException;

public class PGroupConstructorTest {
	private SpiderInstanceFactorySpy instanceFactory;
	private String authToken;
	List<DataElement> metadataChildReferences;
	PChildRefConstructorFactorySpy childRefConstructorFactory;

	@BeforeMethod
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		authToken = "testUser";
		metadataChildReferences = DataCreatorForPresentationsConstructor.createChildren();
		childRefConstructorFactory = new PChildRefConstructorFactorySpy();
	}

	@Test
	public void testGroupConstructorForInput() {
		PGroupConstructor constructor = PGroupConstructor
				.usingAuthTokenAndPChildRefConstructorFactory(authToken,
						childRefConstructorFactory);
		DataGroup pGroup = constructor
				.constructPGroupWithIdDataDividerPresentationOfChildrenAndMode("someTestPGroup",
						"testSystem", "someTestGroup", metadataChildReferences, "input");
		assertEquals(pGroup.getAttributes().get("type"), "pGroup");
		assertEquals(pGroup.getNameInData(), "presentation");
		assertCorrectRecordInfo(pGroup);
		assertCorrectPresentationOf(pGroup);

		assertCorrectFactoredChildReferences(pGroup);
		assertEquals(instanceFactory.spiderRecordReaders.size(), 12);
		assertEquals(instanceFactory.spiderRecordReaders.size(), 12);
		assertEquals(childRefConstructorFactory.mode, "input");
		assertEquals(childRefConstructorFactory.factored.size(), 6);
		DataGroup childReferences = pGroup.getFirstGroupWithNameInData("childReferences");
		assertEveryOtherChildrenIsText(childReferences);
		assertEquals(childReferences.getChildren().size(), 11);

	}

	private void assertCorrectFactoredChildReferences(DataGroup pGroup) {
		assertCorrectFactoredByIndexAndMetadataRefId(0, "identifierTypeCollectionVar");
		assertCorrectFactoredByIndexAndMetadataRefId(1, "identifierValueTextVar");
		assertCorrectFactoredByIndexAndMetadataRefId(2, "identifierResLink");
		assertCorrectFactoredByIndexAndMetadataRefId(3, "identifierLink");
		assertCorrectFactoredByIndexAndMetadataRefId(4, "identifierChildGroup");
		assertCorrectFactoredByIndexAndMetadataRefId(5, "identifierChildHasNoPresentationTextVar");
	}

	private void assertEveryOtherChildrenIsText(DataGroup childReferences) {
		assertChildIsText(childReferences, 0);
		assertChildIsText(childReferences, 2);
		assertChildIsText(childReferences, 4);
		assertChildIsText(childReferences, 6);
		assertChildIsText(childReferences, 8);
		assertChildIsText(childReferences, 10);
	}

	private void assertChildIsText(DataGroup childReferences, int index) {
		DataGroup textChild = (DataGroup) childReferences.getChildren().get(index);
		DataGroup refGroup = textChild.getFirstGroupWithNameInData("refGroup");
		DataGroup ref = refGroup.getFirstGroupWithNameInData("ref");
		assertEquals(ref.getFirstAtomicValueWithNameInData("linkedRecordType"), "coraText");
	}

	private void assertCorrectFactoredByIndexAndMetadataRefId(int index, String metadataRefId) {
		PChildRefConstructorSpy firstFactored = (PChildRefConstructorSpy) childRefConstructorFactory.factored
				.get(index);
		assertEquals(firstFactored.metadataRefId, metadataRefId);
	}

	private void assertCorrectChildInListByIndexWithRepeatIdAndIdAndType(DataGroup childReferences,
			int index, String repeatId, String id, String recordType) {
		DataGroup firstChild = (DataGroup) childReferences.getChildren().get(index);
		assertEquals(firstChild.getNameInData(), "childReference");
		assertEquals(firstChild.getRepeatId(), repeatId);

		assertEquals(firstChild.getAllGroupsWithNameInData("refGroup").size(), 1);
		DataGroup refGroup = firstChild.getFirstGroupWithNameInData("refGroup");
		assertEquals(refGroup.getRepeatId(), "0");

		DataGroup firstChildRef = refGroup.getFirstGroupWithNameInData("ref");
		assertEquals(firstChildRef.getFirstAtomicValueWithNameInData("linkedRecordType"),
				recordType);
		assertEquals(firstChildRef.getFirstAtomicValueWithNameInData("linkedRecordId"), id);
		String typeAttribute = "presentation";
		if ("coraText".equals(recordType)) {
			typeAttribute = "text";
		}
		assertEquals(firstChildRef.getAttributes().get("type"), typeAttribute);
	}

	private void assertCorrectRecordInfo(DataGroup pGroup) {
		DataGroup recordInfo = pGroup.getFirstGroupWithNameInData("recordInfo");
		assertEquals(recordInfo.getFirstAtomicValueWithNameInData("id"), "someTestPGroup");

		DataGroup dataDivider = recordInfo.getFirstGroupWithNameInData("dataDivider");
		assertEquals(dataDivider.getFirstAtomicValueWithNameInData("linkedRecordId"), "testSystem");
	}

	private void assertCorrectPresentationOf(DataGroup pGroup) {
		DataGroup presentationOf = pGroup.getFirstGroupWithNameInData("presentationOf");
		assertEquals(presentationOf.getFirstAtomicValueWithNameInData("linkedRecordType"),
				"metadataGroup");
		assertEquals(presentationOf.getFirstAtomicValueWithNameInData("linkedRecordId"),
				"someTestGroup");
	}

	@Test
	public void testGroupConstructorForOutput() {
		PGroupConstructor constructor = PGroupConstructor
				.usingAuthTokenAndPChildRefConstructorFactory(authToken,
						childRefConstructorFactory);
		DataGroup pGroup = constructor
				.constructPGroupWithIdDataDividerPresentationOfChildrenAndMode("someTestPGroup",
						"testSystem", "someTestGroup", metadataChildReferences, "output");
		assertEquals(pGroup.getAttributes().get("type"), "pGroup");
		assertCorrectRecordInfo(pGroup);
		assertCorrectPresentationOf(pGroup);

		assertCorrectFactoredChildReferences(pGroup);
		assertEquals(instanceFactory.spiderRecordReaders.size(), 12);
		assertEquals(childRefConstructorFactory.mode, "output");
		assertEquals(childRefConstructorFactory.factored.size(), 6);
		DataGroup childReferences = pGroup.getFirstGroupWithNameInData("childReferences");
		assertEveryOtherChildrenIsText(childReferences);
		assertEquals(childReferences.getChildren().size(), 11);

	}

	@Test(expectedExceptions = DataException.class)
	public void testGroupConstructorWithNoIdentifiedChildren() {
		PGroupConstructor constructor = PGroupConstructor
				.usingAuthTokenAndPChildRefConstructorFactory(authToken,
						childRefConstructorFactory);
		List<DataElement> childReferences = new ArrayList<DataElement>();

		DataGroup childRef = DataCreatorForPresentationsConstructor
				.createMetadataChildRefWithIdAndRepeatId("identifierChildGroupWithUnclearEnding",
						"5");
		childReferences.add(childRef);

		constructor.constructPGroupWithIdDataDividerPresentationOfChildrenAndMode("someTestPGroup",
				"testSystem", "someTestGroup", childReferences, "output");
	}
}
