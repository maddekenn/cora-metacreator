/*
 * Copyright 1026, 2017, 2018 Uppsala University Library
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
 */package se.uu.ub.cora.metacreator.group;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.dependency.SpiderRecordCreatorSpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

import java.util.List;

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
		SpiderDataGroup metadataGroup = DataCreator
				.createMetadataGroupWithIdAndTextVarAsChildReference("someTestGroup");

		PGroupFromMetadataGroupCreator creator = new PGroupFromMetadataGroupCreator();
		creator.useExtendedFunctionality(authToken, metadataGroup);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 2);
		assertTrue(creator.constructor.getPChildRefConstructorFactory() instanceof PChildRefConstructorFactoryImp);

		assertCorrectPGroupWithIndexPGroupIdAndChildId(0, "someTestPGroup", "somePVar", "input", "someTextVarText");
		assertCorrectPGroupWithIndexPGroupIdAndChildId(1, "someTestOutputPGroup", "someOutputPVar",
				"output", "someTextVarText");

	}

	private void assertCorrectPGroupWithIndexPGroupIdAndChildId(int index, String pGroupId,
																String childId, String mode, String textId) {
		SpiderRecordCreatorSpy spiderRecordCreatorSpy = instanceFactory.spiderRecordCreators
				.get(index);
		assertEquals(spiderRecordCreatorSpy.type, "presentationGroup");

		SpiderDataGroup record = spiderRecordCreatorSpy.record;

		assertEquals(record.getNameInData(), "presentation");

		assertCorrectRecordInfo(record, pGroupId);
		assertCorrectPresentationOf(record);

		assertCorrectChildRef(childId, "presentationVar", record, textId);
		assertEquals(record.extractAtomicValue("mode"), mode);
	}

	private void assertCorrectRecordInfo(SpiderDataGroup record, String expectedId) {
		SpiderDataGroup recordInfo = record.extractGroup("recordInfo");
		assertEquals(recordInfo.extractAtomicValue("id"), expectedId);
		SpiderDataGroup dataDivider = recordInfo.extractGroup("dataDivider");
		assertEquals(dataDivider.extractAtomicValue("linkedRecordId"), "test");
	}

	private void assertCorrectPresentationOf(SpiderDataGroup record) {
		SpiderDataGroup presentationOf = record.extractGroup("presentationOf");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordId"), "someTestGroup");
		assertEquals(presentationOf.extractAtomicValue("linkedRecordType"), "metadataGroup");
	}

	private void assertCorrectChildRef(String childId, String childType, SpiderDataGroup record, String textId) {
		SpiderDataGroup childReferences = record.extractGroup("childReferences");
		List<SpiderDataGroup> childReferenceList = childReferences.getAllGroupsWithNameInData("childReference");
		SpiderDataGroup refGroupText = childReferenceList.get(0).extractGroup("refGroup");
		SpiderDataGroup refText = refGroupText.extractGroup("ref");
		assertEquals(refText.extractAtomicValue("linkedRecordType"), "coraText");
		assertEquals(refText.extractAtomicValue("linkedRecordId"), textId);

		SpiderDataGroup refGroupPresentation = childReferenceList.get(1).extractGroup("refGroup");
		SpiderDataGroup ref = refGroupPresentation.extractGroup("ref");
		assertEquals(ref.extractAtomicValue("linkedRecordType"), childType);
		assertEquals(ref.extractAtomicValue("linkedRecordId"), childId);
	}

	@Test
	public void testPGroupsAlreadyExist() {
		SpiderDataGroup metadataGroup = DataCreator
				.createMetadataGroupWithIdAndTextVarAsChildReference("someExistingGroup");

		PGroupFromMetadataGroupCreator creator = new PGroupFromMetadataGroupCreator();
		creator.useExtendedFunctionality(authToken, metadataGroup);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 0);
	}

	@Test
	public void testPGroupsShouldNotBeCreated() {
		SpiderDataGroup metadataGroup = DataCreator
				.createMetadataGroupWithIdAndTextVarAsChildReference("someTestGroup");
		metadataGroup
				.addChild(SpiderDataAtomic.withNameInDataAndValue("excludePGroupCreation", "true"));

		PGroupFromMetadataGroupCreator creator = new PGroupFromMetadataGroupCreator();
		creator.useExtendedFunctionality(authToken, metadataGroup);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 0);
	}

	@Test
	public void testPGroupsExcludeCreationIsTrueSoPGroupsShouldNotBeCreated() {
		SpiderDataGroup metadataGroup = DataCreator
				.createMetadataGroupWithIdAndTextVarAsChildReference("someTestGroup");
		metadataGroup
				.addChild(SpiderDataAtomic.withNameInDataAndValue("excludePGroupCreation", "true"));

		PGroupFromMetadataGroupCreator creator = new PGroupFromMetadataGroupCreator();
		creator.useExtendedFunctionality(authToken, metadataGroup);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 0);
	}

	@Test
	public void testPGroupsExcludeCreationIsFalseSoPGroupsShouldBeCreated() {
		SpiderDataGroup metadataGroup = DataCreator
				.createMetadataGroupWithIdAndTextVarAsChildReference("someTestGroup");
		metadataGroup.addChild(
				SpiderDataAtomic.withNameInDataAndValue("excludePGroupCreation", "false"));

		PGroupFromMetadataGroupCreator creator = new PGroupFromMetadataGroupCreator();
		creator.useExtendedFunctionality(authToken, metadataGroup);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 2);
	}

	@Test
	public void testPGroupsNotPossibleToCreatePGroups() {
		SpiderDataGroup metadataGroup = DataCreator
				.createMetadataGroupWithIdAndTextVarAsChildReference("someTestGroup");
		SpiderDataGroup childReferences = metadataGroup.extractGroup("childReferences");
		childReferences.removeChild("childReference");

		PGroupFromMetadataGroupCreator creator = new PGroupFromMetadataGroupCreator();
		creator.useExtendedFunctionality(authToken, metadataGroup);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 0);
	}
}
