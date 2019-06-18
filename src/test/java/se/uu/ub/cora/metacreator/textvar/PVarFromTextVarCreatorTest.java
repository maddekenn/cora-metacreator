/*
 * Copyright 2016 Olov McKie
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

package se.uu.ub.cora.metacreator.textvar;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.dependency.SpiderRecordCreatorSpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class PVarFromTextVarCreatorTest {
	private SpiderInstanceFactorySpy instanceFactory;
	private String userId;

	@BeforeMethod
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		userId = "testUser";
	}

	@Test
	public void testNoExistingPVars() {
		PVarFromTextVarCreator creator = new PVarFromTextVarCreator();

		DataGroup textVarGroup = DataCreator.createTextVarGroupWithIdAndTextIdAndDefTextId(
				"textIdNoPVarsInStorageTextVar", "textIdNoPVarsInStorageTextVarText",
				"textIdNoPVarsInStorageTextVarDefText");

		creator.useExtendedFunctionality(userId, textVarGroup);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 2);
		assertCorrectPVarCreatedWithUserIdAndTypeAndId(0, "textIdNoPVarsInStoragePVar");
		assertCorrectPVarCreatedWithUserIdAndTypeAndId(1, "textIdNoPVarsInStorageOutputPVar");
	}

	private void assertCorrectPVarCreatedWithUserIdAndTypeAndId(int createdPVarNo,
			String createdIdForPVar) {
		SpiderRecordCreatorSpy spiderRecordCreator1 = instanceFactory.spiderRecordCreators
				.get(createdPVarNo);
		assertEquals(spiderRecordCreator1.authToken, userId);
		assertEquals(spiderRecordCreator1.type, "presentationVar");
		DataGroup createdTextRecord = spiderRecordCreator1.record;
		DataGroup recordInfo = createdTextRecord.getFirstGroupWithNameInData("recordInfo");
		String id = recordInfo.getFirstAtomicValueWithNameInData("id");
		assertEquals(id, createdIdForPVar);
	}

	@Test
	public void testExistingInputPVar() {
		PVarFromTextVarCreator creator = new PVarFromTextVarCreator();

		DataGroup textVarGroup = DataCreator.createTextVarGroupWithIdAndTextIdAndDefTextId(
				"textIdInputPVarInStorageTextVar", "textIdInputPVarInStorageTextVarText",
				"textIdInputPVarInStorageTextVarDefText");

		creator.useExtendedFunctionality(userId, textVarGroup);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 1);
		assertCorrectPVarCreatedWithUserIdAndTypeAndId(0, "textIdInputPVarInStorageOutputPVar");
	}

	@Test
	public void testExistingOutputPVar() {
		PVarFromTextVarCreator creator = new PVarFromTextVarCreator();

		DataGroup textVarGroup = DataCreator.createTextVarGroupWithIdAndTextIdAndDefTextId(
				"textIdOutputPVarInStorageTextVar", "textIdOutputPVarInStorageTextVarText",
				"textIdOutputPVarInStorageTextVarDefText");

		creator.useExtendedFunctionality(userId, textVarGroup);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 1);
		assertCorrectPVarCreatedWithUserIdAndTypeAndId(0, "textIdOutputPVarInStoragePVar");
	}
}
