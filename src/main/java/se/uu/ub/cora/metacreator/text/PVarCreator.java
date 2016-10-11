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

public class PVarCreator {
	private String textVarId;
	private String dataDividerString;

	private PVarCreator(String textVarId, String dataDividerString) {
		this.textVarId = textVarId;
		this.dataDividerString = dataDividerString;
	}

	public static PVarCreator withTextVarIdAndDataDivider(String textVarId,
			String dataDividerString) {
		return new PVarCreator(textVarId, dataDividerString);
	}

	public SpiderDataGroup createInputPVar() {
		String pVarId = textVarId + "PVar";
		String mode = "input";
		return createPVarWithIdAndMode(pVarId, mode);
	}

	private SpiderDataGroup createPVarWithIdAndMode(String pVarId, String mode) {
		SpiderDataGroup pVarGroup = SpiderDataGroup.withNameInData("presentation");

		pVarGroup.addAttributeByIdWithValue("type", "pVar");

		pVarGroup.addChild(createRecordInfoWithIdAndDataDividerRecordId(pVarId));

		pVarGroup.addChild(createPresentationOf());

		pVarGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("mode", mode));
		pVarGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("inputType", "input"));

		return pVarGroup;
	}

	private SpiderDataGroup createPresentationOf() {
		SpiderDataGroup presentationOf = SpiderDataGroup.withNameInData("presentationOf");

		presentationOf.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType",
				"metadataTextVariable"));
		presentationOf
				.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", textVarId));
		return presentationOf;
	}

	private SpiderDataGroup createRecordInfoWithIdAndDataDividerRecordId(String pVarId) {
		SpiderDataGroup recordInfo = SpiderDataGroup.withNameInData("recordInfo");
		recordInfo.addChild(SpiderDataAtomic.withNameInDataAndValue("id", pVarId));

		SpiderDataGroup dataDivider = SpiderDataGroup.withNameInData("dataDivider");
		recordInfo.addChild(dataDivider);
		dataDivider.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDivider.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", dataDividerString));
		return recordInfo;
	}

	public SpiderDataGroup createOutputPVar() {
		String pVarId = textVarId + "OutputPVar";
		String mode = "output";
		return createPVarWithIdAndMode(pVarId, mode);
	}
}
