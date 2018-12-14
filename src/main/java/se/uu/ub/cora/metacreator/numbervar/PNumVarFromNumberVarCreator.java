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

import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;
import se.uu.ub.cora.spider.record.SpiderRecordCreator;
import se.uu.ub.cora.spider.record.SpiderRecordReader;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class PNumVarFromNumberVarCreator implements ExtendedFunctionality {

	private static final String PRESENTATION_NUMBER_VAR = "presentationNumberVar";
	private String authToken;
	private String id;
	private String dataDividerString;

	@Override
	public void useExtendedFunctionality(String authToken, SpiderDataGroup spiderDataGroup) {
		this.authToken = authToken;
		extractIdAndDataDividerFromSpiderDataGroup(spiderDataGroup);

		PNumVarConstructor pNumVarConstructor = PNumVarConstructor.withTextVarIdAndDataDivider(id,
				dataDividerString);

		possiblyCreateInputPNumVar(pNumVarConstructor);
		possiblyCreateOutputPNumVar(pNumVarConstructor);
	}

	private void possiblyCreateOutputPNumVar(PNumVarConstructor pNumVarConstructor) {
		if (pNumVarDoesNotExistInStorage(id, "OutputPNumVar")) {
			SpiderDataGroup outputPNumVar = pNumVarConstructor.createOutputPNumVar();
			createPNumVar(outputPNumVar);
		}
	}

	private void createPNumVar(SpiderDataGroup inputPNumVar) {
		SpiderRecordCreator spiderRecordCreator = SpiderInstanceProvider.getSpiderRecordCreator();
		spiderRecordCreator.createAndStoreRecord(authToken, PRESENTATION_NUMBER_VAR, inputPNumVar);
	}

	private void possiblyCreateInputPNumVar(PNumVarConstructor pNumVarConstructor) {
		if (pNumVarDoesNotExistInStorage(id, "PNumVar")) {
			SpiderDataGroup inputPNumVar = pNumVarConstructor.createInputPNumVar();
			createPNumVar(inputPNumVar);
		}
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

	private boolean pNumVarDoesNotExistInStorage(String id, String suffix) {
		String idWithoutEnding = id.substring(0, id.indexOf("NumberVar"));
		try {
			SpiderRecordReader spiderRecordReader = SpiderInstanceProvider.getSpiderRecordReader();
			spiderRecordReader.readRecord(authToken, PRESENTATION_NUMBER_VAR,
					idWithoutEnding + suffix);
		} catch (RecordNotFoundException e) {
			return true;
		}
		return false;
	}

}
