/*
 * Copyright 2016, 2017 Uppsala University Library
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
package se.uu.ub.cora.metacreator.recordtype;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class RecordTypeMetaCompleterTest {
	private SpiderInstanceFactorySpy instanceFactory;
	private String userId;
	private RecordTypeMetaCompleter metaCompleter;

	@BeforeMethod
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		metaCompleter = new RecordTypeMetaCompleter();
		userId = "testUser";
	}

	@Test
	public void testDefaultValuesWhenAllValuesMissing() {

		DataGroup recordType = DataCreator.createSpiderDataGroupForRecordTypeWithId("myRecordType");
		metaCompleter.useExtendedFunctionality(userId, recordType);
		assertAllValuesWereAddedCorrectly(recordType);
	}

	private void assertAllValuesWereAddedCorrectly(DataGroup recordType) {
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType, "metadataId"),
				"myRecordTypeGroup");
		assertEquals(
				extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType, "newMetadataId"),
				"myRecordTypeNewGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType,
				"presentationViewId"), "myRecordTypeOutputPGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType,
				"presentationFormId"), "myRecordTypePGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType,
				"newPresentationFormId"), "myRecordTypeNewPGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType,
				"menuPresentationViewId"), "myRecordTypeMenuPGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType,
				"listPresentationViewId"), "myRecordTypeListPGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType,
				"autocompletePresentationView"), "myRecordTypeAutocompletePGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType, "textId"),
				"myRecordTypeText");
		assertEquals(extractLinkedRecordTypeFromLinkInDataGroupByNameInData(recordType, "textId"),
				"coraText");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType, "defTextId"),
				"myRecordTypeDefText");
		assertEquals(
				extractLinkedRecordTypeFromLinkInDataGroupByNameInData(recordType, "defTextId"),
				"coraText");
		assertEquals(recordType.getFirstAtomicValueWithNameInData("public"), "false");

	}

	@Test
	public void testDefaultValuesWhenAllValuesPresent() {
		DataGroup recordType = DataCreator.createSpiderDataGroupForRecordTypeWithId("mySpecial");
		DataCreator.addAllValuesToSpiderDataGroup(recordType, "mySpecial");

		metaCompleter.useExtendedFunctionality(userId, recordType);

		assertEquals(recordType.getChildren().size(), 15);

		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType, "metadataId"),
				"mySpecialGroup");
		assertEquals(
				extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType, "newMetadataId"),
				"mySpecialNewGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType,
				"presentationViewId"), "mySpecialOutputPGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType,
				"presentationFormId"), "mySpecialPGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType,
				"newPresentationFormId"), "mySpecialNewPGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType,
				"menuPresentationViewId"), "mySpecialMenuPGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType,
				"listPresentationViewId"), "mySpecialListPGroup");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType, "textId"),
				"mySpecialText");
		assertEquals(extractLinkedRecordTypeFromLinkInDataGroupByNameInData(recordType, "textId"),
				"implementingText");
		assertEquals(extractLinkedRecordIdFromLinkInDataGroupByNameInData(recordType, "defTextId"),
				"mySpecialDefText");
		assertEquals(
				extractLinkedRecordTypeFromLinkInDataGroupByNameInData(recordType, "defTextId"),
				"implementingText");
	}

	private String extractLinkedRecordIdFromLinkInDataGroupByNameInData(DataGroup dataGroup,
			String nameInData) {
		DataGroup link = (DataGroup) dataGroup.getFirstChildWithNameInData(nameInData);
		DataAtomic linkedRecordId = (DataAtomic) link.getFirstChildWithNameInData("linkedRecordId");
		return linkedRecordId.getValue();
	}

	private String extractLinkedRecordTypeFromLinkInDataGroupByNameInData(DataGroup dataGroup,
			String nameInData) {
		DataGroup link = (DataGroup) dataGroup.getFirstChildWithNameInData(nameInData);
		DataAtomic linkedRecordType = (DataAtomic) link
				.getFirstChildWithNameInData("linkedRecordType");
		return linkedRecordType.getValue();
	}

}
