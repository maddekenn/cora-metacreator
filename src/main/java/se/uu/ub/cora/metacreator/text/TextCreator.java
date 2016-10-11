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

public class TextCreator {
	private String textId;
	private String dataDividerString;

	private TextCreator(String textId, String dataDividerString) {
		this.textId = textId;
		this.dataDividerString = dataDividerString;
	}

	public static TextCreator withTextIdAndDataDivider(String textId, String dataDividerString) {
		return new TextCreator(textId, dataDividerString);
	}

	public SpiderDataGroup createText() {
		SpiderDataGroup textGroup = SpiderDataGroup.withNameInData("text");

		SpiderDataGroup recordInfo = createRecordInfoWithIdAndDataDividerRecordId();
		textGroup.addChild(recordInfo);

		SpiderDataGroup textPartSv = createTextPartWithTextIdTypeLangText("default", "sv",
				"Text för:");
		textGroup.addChild(textPartSv);
		SpiderDataGroup textPartEn = createTextPartWithTextIdTypeLangText("alternative", "en",
				"Text for:");
		textGroup.addChild(textPartEn);
		return textGroup;
	}

	private SpiderDataGroup createTextPartWithTextIdTypeLangText(String type, String lang,
			String text) {
		SpiderDataGroup textPart = SpiderDataGroup.withNameInData("textPart");
		textPart.addAttributeByIdWithValue("type", type);
		textPart.addAttributeByIdWithValue("lang", lang);
		textPart.addChild(SpiderDataAtomic.withNameInDataAndValue("text", text + textId));
		return textPart;
	}

	private SpiderDataGroup createRecordInfoWithIdAndDataDividerRecordId() {
		SpiderDataGroup recordInfo = SpiderDataGroup.withNameInData("recordInfo");
		recordInfo.addChild(SpiderDataAtomic.withNameInDataAndValue("id", textId));

		SpiderDataGroup dataDivider = SpiderDataGroup.withNameInData("dataDivider");
		recordInfo.addChild(dataDivider);
		dataDivider.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDivider.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", dataDividerString));
		return recordInfo;
	}

}
