/*
 * Copyright 2016, 2017 Uppsala University Library
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
package se.uu.ub.cora.metacreator.recordtype;

import se.uu.ub.cora.data.DataAtomicProvider;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataGroupProvider;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;

public class RecordTypeMetaCompleter implements ExtendedFunctionality {

	private DataGroup dataGroup;
	private String id;

	@Override
	public void useExtendedFunctionality(String userId, DataGroup dataGroup) {
		this.dataGroup = dataGroup;

		addValuesToDataGroup();
	}

	private void addValuesToDataGroup() {
		DataGroup recordInfoGroup = dataGroup.getFirstGroupWithNameInData("recordInfo");
		id = recordInfoGroup.getFirstAtomicValueWithNameInData("id");
		addMissingMetadataIds();
		addMissingPresentationIds();
		addMissingTexts();
		addPublicIfMissing();
	}

	private void addMissingMetadataIds() {
		createAndAddLinkWithNameInDataRecordTypeAndRecordIdIfNotExisting("metadataId",
				"metadataGroup", id + "Group");
		createAndAddLinkWithNameInDataRecordTypeAndRecordIdIfNotExisting("newMetadataId",
				"metadataGroup", id + "NewGroup");

	}

	private void createAndAddLinkWithNameInDataRecordTypeAndRecordIdIfNotExisting(String nameInData,
			String linkedRecordType, String linkedRecordId) {
		if (childWithNameInDataIsMissing(nameInData)) {
			DataGroup link = DataGroupProvider.getDataGroupUsingNameInData(nameInData);
			link.addChild(DataAtomicProvider
					.getDataAtomicUsingNameInDataAndValue("linkedRecordType", linkedRecordType));
			link.addChild(DataAtomicProvider.getDataAtomicUsingNameInDataAndValue("linkedRecordId",
					linkedRecordId));
			dataGroup.addChild(link);
		}
	}

	private boolean childWithNameInDataIsMissing(String nameInData) {
		return !dataGroup.containsChildWithNameInData(nameInData);
	}

	private void addMissingPresentationIds() {

		String linkedRecordType = "presentationGroup";

		createAndAddLinkWithNameInDataRecordTypeAndRecordIdIfNotExisting("presentationViewId",
				linkedRecordType, id + "OutputPGroup");
		createAndAddLinkWithNameInDataRecordTypeAndRecordIdIfNotExisting("presentationFormId",
				linkedRecordType, id + "PGroup");
		createAndAddLinkWithNameInDataRecordTypeAndRecordIdIfNotExisting("newPresentationFormId",
				linkedRecordType, id + "NewPGroup");
		createAndAddLinkWithNameInDataRecordTypeAndRecordIdIfNotExisting("menuPresentationViewId",
				linkedRecordType, id + "MenuPGroup");
		createAndAddLinkWithNameInDataRecordTypeAndRecordIdIfNotExisting("listPresentationViewId",
				linkedRecordType, id + "ListPGroup");
		createAndAddLinkWithNameInDataRecordTypeAndRecordIdIfNotExisting(
				"autocompletePresentationView", linkedRecordType, id + "AutocompletePGroup");
	}

	private void addMissingTexts() {
		createAndAddLinkWithNameInDataRecordTypeAndRecordIdIfNotExisting("textId", "coraText",
				id + "Text");
		createAndAddLinkWithNameInDataRecordTypeAndRecordIdIfNotExisting("defTextId", "coraText",
				id + "DefText");
	}

	private void addPublicIfMissing() {
		if (publicIsMissing()) {
			dataGroup.addChild(
					DataAtomicProvider.getDataAtomicUsingNameInDataAndValue("public", "false"));
		}
	}

	private boolean publicIsMissing() {
		return !dataGroup.containsChildWithNameInData("public");
	}
}
