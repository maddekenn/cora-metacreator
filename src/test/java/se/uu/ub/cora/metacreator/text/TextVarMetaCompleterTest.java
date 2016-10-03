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

package se.uu.ub.cora.metacreator.text;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.metacreator.dependency.SpiderRecordCreatorSpy;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;

public class TextVarMetaCompleterTest {
	private SpiderInstanceFactorySpy instanceFactory;
	private String userId;

	@BeforeTest
	public void setUp() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
		userId = "testUser";
	}

	@Test
	public void testWithNoTexts() {

		// SpiderDataGroup spiderDataGroup = SpiderDataGroup.withNameInData("someNameInData");
		// SpiderDataRecord record = SpiderDataRecord.withSpiderDataGroup(spiderDataGroup);
		// SpiderRecordReader reader1 = new SpiderRecordReaderReturner(record);
		// readerList.add(reader1);

		TextVarMetaCompleter textVarMetaCompleter = new TextVarMetaCompleter();

		SpiderDataGroup textVarGroup = createTextVarGroup("noTextsTextVar");
		/**
		 * {"name":"metadata","children":[{"name":"recordInfo","children":[{"name":"id","value":
		 * "myTextVar"},{"name"
		 * :"dataDivider","children":[{"name":"linkedRecordType","value":"system"},{"name":
		 * "linkedRecordId","value"
		 * :"cora"}]}]},{"name":"nameInData","value":"nameInData"},{"name":"textId","value":
		 * "myTextVarText"},{"name"
		 * :"defTextId","value":"myTextVarDefText"},{"name":"regEx","value":".*"}],"attributes":{
		 * "type":"textVariable" }}
		 */

		textVarMetaCompleter.useExtendedFunctionality(userId, textVarGroup);
		SpiderRecordCreatorSpy spiderRecordCreator = instanceFactory.spiderRecordCreators.get(0);
		assertEquals(spiderRecordCreator.userId, userId);
		assertEquals(spiderRecordCreator.type, "text");
		SpiderDataGroup createdTextRecord = spiderRecordCreator.record;
		SpiderDataGroup recordInfo = createdTextRecord.extractGroup("recordInfo");
		String id = recordInfo.extractAtomicValue("id");
		assertEquals(id, "noTextsTextVarText");
	}

	private SpiderDataGroup createTextVarGroup(String id) {
		SpiderDataGroup textVarGroup = SpiderDataGroup.withNameInData("textVar");

		SpiderDataGroup recordInfoGroup = SpiderDataGroup.withNameInData("recordInfo");
		textVarGroup.addChild(recordInfoGroup);
		recordInfoGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("id", id));

		SpiderDataGroup dataDivider = SpiderDataGroup.withNameInData("dataDivider");
		recordInfoGroup.addChild(dataDivider);
		dataDivider.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDivider.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", "cora"));

		textVarGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("nameInData", "my"));
		// textVarGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("textId",
		// "myTextVarText"));
		// textVarGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("defTextId",
		// "myTextVarDefText"));
		textVarGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("regEx", ".*"));

		textVarGroup.addAttributeByIdWithValue("type", "textVariable");
		return textVarGroup;
	}
}
