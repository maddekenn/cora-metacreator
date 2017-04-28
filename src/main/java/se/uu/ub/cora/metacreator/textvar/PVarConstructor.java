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

import se.uu.ub.cora.metacreator.DataCreatorHelper;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;

public final class PVarConstructor {
	private String textVarId;
	private String dataDividerString;

	private PVarConstructor(String textVarId, String dataDividerString) {
		this.textVarId = textVarId;
		this.dataDividerString = dataDividerString;
	}

	public static PVarConstructor withTextVarIdAndDataDivider(String textVarId,
															  String dataDividerString) {
		return new PVarConstructor(textVarId, dataDividerString);
	}

	public SpiderDataGroup createInputPVar() {
		String pVar = "PVar";
		String pVarId = constructPVarIdWithEnding(pVar);
		String mode = "input";
		return createPVarWithIdAndMode(pVarId, mode);
	}

	private String constructPVarIdWithEnding(String pVar) {
		String prefix = textVarId.substring(0, textVarId.indexOf("TextVar"));
		return prefix + pVar;
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
		return DataCreatorHelper.createRecordInfoWithIdAndDataDivider(pVarId,dataDividerString);
	}

	public SpiderDataGroup createOutputPVar() {
		String pVarId = constructPVarIdWithEnding("OutputPVar");
		String mode = "output";
		return createPVarWithIdAndMode(pVarId, mode);
	}
}
