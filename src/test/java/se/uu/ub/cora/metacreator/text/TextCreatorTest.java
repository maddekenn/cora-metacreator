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

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.metacreator.dependency.SpiderInstanceFactorySpy;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.data.SpiderDataRecord;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.record.SpiderRecordCreator;
import se.uu.ub.cora.spider.record.SpiderRecordReader;

public class TextCreatorTest {

	private SpiderInstanceFactorySpy instanceFactory;

	@BeforeMethod
	public void beforeMethod() {

		setUpDependencyProvider();

	}

	private void setUpDependencyProvider() {
		instanceFactory = new SpiderInstanceFactorySpy();
		SpiderInstanceProvider.setSpiderInstanceFactory(instanceFactory);
	}

	@Test
	public void testCreateTextsFromMetadataId() {
		List<SpiderRecordReader> readerList = new ArrayList<>();
		instanceFactory.recordReaders = readerList;
		SpiderDataGroup spiderDataGroup = SpiderDataGroup.withNameInData("someNameInData");
		SpiderDataRecord record = SpiderDataRecord.withSpiderDataGroup(spiderDataGroup);
		SpiderRecordReader reader1 = new SpiderRecordReaderReturner(record);
		readerList.add(reader1);
		// TextCreator textCreator = new TextCreator();
		//

		List<SpiderRecordCreator> creatorList = new ArrayList<>();
		instanceFactory.recordCreators = creatorList;
		SpiderRecordCreatorReturner creator1 = new SpiderRecordCreatorReturner(record);
		creatorList.add(creator1);

		String userId = "someUserId";
		String textId = "someTextVar";
		String dataDividerString = "cora";
		TextCreator textCreator = TextCreator.withTextIdAndDataDivider(textId, dataDividerString);
		textCreator.createTextInStorage();
		SpiderDataGroup createdGroup = creator1.record;
		assertEquals(createdGroup.getNameInData(), "text");
		// TODO: check entire createdGroup
	}
}
