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
	private SpiderDataGroup spiderDataGroup;
	private String id;

	private TextVarMetaCompleter(String implementingTextType) {
		this.implementingTextType = implementingTextType;
	}

	public static TextVarMetaCompleter forImplementingTextType(String implementingTextType) {
		return new TextVarMetaCompleter(implementingTextType);
	}

	@Override
	public void useExtendedFunctionality(String userId, SpiderDataGroup spiderDataGroup) {
		this.userId = userId;
		this.spiderDataGroup = spiderDataGroup;
		extractIdAndDataDividerFromSpiderDataGroup(spiderDataGroup);

		setOrUseTextIdWithNameInDataToEnsureTextExistsInStorage(id + "Text", "textId");
		setOrUseTextIdWithNameInDataToEnsureTextExistsInStorage(id + "DefText", "defTextId");
	}

	private void extractIdAndDataDividerFromSpiderDataGroup(SpiderDataGroup spiderDataGroup) {
		SpiderDataGroup recordInfoGroup = spiderDataGroup.extractGroup("recordInfo");
		id = extractIdFromSpiderDataGroup(recordInfoGroup);
		dataDividerString = extractDataDividerFromSpiderDataGroup(recordInfoGroup);
	}

	private String extractIdFromSpiderDataGroup(SpiderDataGroup recordInfoGroup) {
		return recordInfoGroup.extractAtomicValue("id");
	}

	private String extractDataDividerFromSpiderDataGroup(SpiderDataGroup recordInfoGroup) {
		return recordInfoGroup.extractGroup("dataDivider").extractAtomicValue("linkedRecordId");
	}

	private void setOrUseTextIdWithNameInDataToEnsureTextExistsInStorage(String textId,
			String nameInData) {
		if (spiderDataGroup.containsChildWithNameInData(nameInData)) {
			ensureTextExistInStorageBasedOnExistingValueInNameInData(nameInData);
		} else {
			ensureTextExistsInStorageBasedOnNewTextId(textId, nameInData);
		}
	}

	private void ensureTextExistsInStorageBasedOnNewTextId(String textId, String nameInData) {
		spiderDataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue(nameInData, textId));
		ensureTextExistsInStorage(textId);
	}

	private void ensureTextExistsInStorage(String textId) {
		if (textDoesNotExistInStorage(textId)) {
			createTextWithTextIdInStorage(textId);
		}
	}

	private void ensureTextExistInStorageBasedOnExistingValueInNameInData(String nameInData) {
		String textId2 = spiderDataGroup.extractAtomicValue(nameInData);
		ensureTextExistsInStorage(textId2);
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
		SpiderDataGroup textGroup = textCreator.createText();

		SpiderRecordCreator spiderRecordCreator = SpiderInstanceProvider.getSpiderRecordCreator();
		spiderRecordCreator.createAndStoreRecord(userId, implementingTextType, textGroup);
	}
}
