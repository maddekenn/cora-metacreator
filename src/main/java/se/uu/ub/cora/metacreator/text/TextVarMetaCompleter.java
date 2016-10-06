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

import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;
import se.uu.ub.cora.spider.record.SpiderRecordCreator;
import se.uu.ub.cora.spider.record.SpiderRecordReader;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class TextVarMetaCompleter implements ExtendedFunctionality {

	private String userId;
	private String dataDividerString;
	private String implementingTextType;

	private TextVarMetaCompleter(String implementingTextType) {
		this.implementingTextType = implementingTextType;
		// TODO Auto-generated constructor stub
		// implementingTextType = "textSystemOne";

	}

	public static TextVarMetaCompleter forImplementingTextType(String implementingTextType) {
		return new TextVarMetaCompleter(implementingTextType);
	}

	@Override
	public void useExtendedFunctionality(String userId2, SpiderDataGroup spiderDataGroup) {
		this.userId = userId2;
		String id = spiderDataGroup.extractGroup("recordInfo").extractAtomicValue("id");

		dataDividerString = spiderDataGroup.extractGroup("recordInfo").extractGroup("dataDivider")
				.extractAtomicValue("linkedRecordId");

		String textId = id + "Text";
		String nameInData = "textId";
		if (spiderDataGroup.containsChildWithNameInData(nameInData)) {
			textId = spiderDataGroup.extractAtomicValue(nameInData);
		} else {
			spiderDataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue(nameInData, textId));
		}
		ensureTextExistsInStorage(textId);

		String defTextId = id + "DefText";
		nameInData = "defTextId";
		if (spiderDataGroup.containsChildWithNameInData(nameInData)) {
			defTextId = spiderDataGroup.extractAtomicValue(nameInData);
		} else {
			spiderDataGroup
					.addChild(SpiderDataAtomic.withNameInDataAndValue(nameInData, defTextId));
		}
		ensureTextExistsInStorage(defTextId);
	}

	private void ensureTextExistsInStorage(String textId) {
		if (textDoesNotExistInStorage(textId)) {
			createTextWithTextIdInStorage(textId);
		}
	}

	private boolean textDoesNotExistInStorage(String textId) {
		try {
			SpiderRecordReader spiderRecordReader = SpiderInstanceProvider.getSpiderRecordReader();
			spiderRecordReader.readRecord(userId, implementingTextType, textId);
		} catch (RecordNotFoundException e) {
			return true;
		}
		return false;
	}

	private void createTextWithTextIdInStorage(String textId) {
		TextCreator textCreator = TextCreator.withTextIdAndDataDivider(textId, dataDividerString);
		SpiderDataGroup textGroup = textCreator.createTextInStorage();

		SpiderRecordCreator spiderRecordCreator = SpiderInstanceProvider.getSpiderRecordCreator();
		spiderRecordCreator.createAndStoreRecord(userId, implementingTextType, textGroup);
	}
}
