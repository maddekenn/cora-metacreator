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

package se.uu.ub.cora.metacreator.numbervar;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.dependency.SpiderRecordCreatorSpy;
import se.uu.ub.cora.metacreator.testdata.DataCreator;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class PNumVarFromNumberVarCreatorTest {
	private SpiderInstanceFactorySpy instanceFactory;
	private String userId;

	@BeforeMethod
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		userId = "testUser";
	}

	@Test
	public void testNoExistingPNumVars() {
		PNumVarFromNumberVarCreator creator = new PNumVarFromNumberVarCreator();

		DataGroup numVarGroup = DataCreator.createNumberVarUsingIdNameInDataAndDataDivider(
				"numVarNoPNumVarsInStorageNumberVar", "someNumVar", "testSystem");

		creator.useExtendedFunctionality(userId, numVarGroup);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 2);
		assertCorrectPVarCreatedWithUserIdAndTypeAndId(0, "numVarNoPNumVarsInStoragePNumVar");
		assertCorrectPVarCreatedWithUserIdAndTypeAndId(1, "numVarNoPNumVarsInStorageOutputPNumVar");
	}

	private void assertCorrectPVarCreatedWithUserIdAndTypeAndId(int createdPNumVarNo,
			String createdIdForPNumVar) {
		SpiderRecordCreatorSpy spiderRecordCreator = instanceFactory.spiderRecordCreators
				.get(createdPNumVarNo);
		assertEquals(spiderRecordCreator.authToken, userId);
		assertEquals(spiderRecordCreator.type, "presentationNumberVar");
		DataGroup createdRecord = spiderRecordCreator.record;
		DataGroup recordInfo = createdRecord.getFirstGroupWithNameInData("recordInfo");
		String id = recordInfo.getFirstAtomicValueWithNameInData("id");
		assertEquals(id, createdIdForPNumVar);
	}

	@Test
	public void testExistingInputPNumVar() {
		PNumVarFromNumberVarCreator creator = new PNumVarFromNumberVarCreator();

		DataGroup numVarGroup = DataCreator.createNumberVarUsingIdNameInDataAndDataDivider(
				"numVarInputPNumVarInStorageNumberVar", "someNumVar", "testSystem");

		creator.useExtendedFunctionality(userId, numVarGroup);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 1);
		assertCorrectPVarCreatedWithUserIdAndTypeAndId(0,
				"numVarInputPNumVarInStorageOutputPNumVar");
	}

	@Test
	public void testExistingOutputPVar() {
		PNumVarFromNumberVarCreator creator = new PNumVarFromNumberVarCreator();

		DataGroup numVarGroup = DataCreator.createNumberVarUsingIdNameInDataAndDataDivider(
				"numVarOutputPNumVarInStorageNumberVar", "someNumVar", "testSystem");

		creator.useExtendedFunctionality(userId, numVarGroup);

		assertEquals(instanceFactory.spiderRecordCreators.size(), 1);
		assertCorrectPVarCreatedWithUserIdAndTypeAndId(0, "numVarOutputPNumVarInStoragePNumVar");
	}
}
