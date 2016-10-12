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

package se.uu.ub.cora.metacreator.testdata;

import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public class DataCreator {
	public static SpiderDataGroup createTextVarGroupWithIdAndTextIdAndDefTextId(String id,
			String textId, String defTextId) {
		SpiderDataGroup textVarGroup = SpiderDataGroup.withNameInData("textVar");

		SpiderDataGroup recordInfoGroup = SpiderDataGroup.withNameInData("recordInfo");
		textVarGroup.addChild(recordInfoGroup);
		recordInfoGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("id", id));

		SpiderDataGroup dataDivider = SpiderDataGroup.withNameInData("dataDivider");
		recordInfoGroup.addChild(dataDivider);
		dataDivider.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDivider.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", "cora"));

		textVarGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("nameInData", "my"));
		if (!"".equals(textId)) {
			textVarGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("textId", textId));
		}
		if (!"".equals(defTextId)) {
			textVarGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("defTextId", defTextId));
		}
		textVarGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("regEx", ".*"));

		textVarGroup.addAttributeByIdWithValue("type", "textVariable");
		return textVarGroup;
	}
}
