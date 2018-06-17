/*
 * Copyright 2017 Uppsala University Library
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
 */package se.uu.ub.cora.metacreator.group;

import java.util.List;

import se.uu.ub.cora.metacreator.DataCreatorHelper;
import se.uu.ub.cora.spider.data.SpiderDataElement;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;
import se.uu.ub.cora.spider.record.DataException;
import se.uu.ub.cora.spider.record.SpiderRecordCreator;
import se.uu.ub.cora.spider.record.SpiderRecordReader;

public class PGroupFromMetadataGroupCreator implements ExtendedFunctionality {

	private String authToken;
	private String metadataId;
	private String dataDivider;
	private List<SpiderDataElement> metadataChildReferences;
	protected PGroupConstructor constructor;

	@Override
	public void useExtendedFunctionality(String authToken, SpiderDataGroup spiderDataGroup) {
		this.authToken = authToken;
		if (pGroupsShouldBeCreated(spiderDataGroup)) {
			tryToCreatePGroups(spiderDataGroup);
		}
	}

	private boolean pGroupsShouldBeCreated(SpiderDataGroup spiderDataGroup) {
		return !spiderDataGroup.containsChildWithNameInData("excludePGroupCreation")
				|| "false".equals(spiderDataGroup.extractAtomicValue("excludePGroupCreation"));
	}

	private void tryToCreatePGroups(SpiderDataGroup spiderDataGroup) {
		setParametersForCreation(spiderDataGroup);
		possiblyCreateInputGroup();
		possiblyCreateOutputPGroup();
	}

	private void setParametersForCreation(SpiderDataGroup spiderDataGroup) {
		constructor = new PGroupConstructor(authToken);
		metadataId = DataCreatorHelper.extractIdFromDataGroup(spiderDataGroup);
		dataDivider = DataCreatorHelper.extractDataDividerStringFromDataGroup(spiderDataGroup);
		metadataChildReferences = spiderDataGroup.extractGroup("childReferences").getChildren();
	}

	private void possiblyCreateInputGroup() {
		String id = getIdForInputPGroup();
		possiblyConstructAndCreatePGroupWithIdAndMode(id, "input");
	}

	private void possiblyCreateOutputPGroup() {
		String outputId = getIdForOutputPGroup();
		possiblyConstructAndCreatePGroupWithIdAndMode(outputId, "output");
	}

	private String getIdForInputPGroup() {
		return metadataId.substring(0, metadataId.indexOf("Group")) + "PGroup";
	}

	private void possiblyConstructAndCreatePGroupWithIdAndMode(String id, String mode) {
		if (pGroupIsMissing(id)) {
			constructAndCreatePGroupWithIdAndMode(id, mode);
		}
	}

	private boolean pGroupIsMissing(String pGroupId) {
		try {
			SpiderRecordReader reader = SpiderInstanceProvider.getSpiderRecordReader();
			reader.readRecord(authToken, "presentationGroup", pGroupId);
		} catch (Exception e) {
			return true;
		}
		return false;
	}

	private void constructAndCreatePGroupWithIdAndMode(String id, String mode) {
		try {
			SpiderDataGroup inputPGroup = constructor
					.constructPGroupWithIdDataDividerPresentationOfChildrenAndMode(id, dataDivider,
							metadataId, metadataChildReferences, mode);
			createRecord("presentationGroup", inputPGroup);
		} catch (DataException e) {
			// do nothing
		}
	}

	private void createRecord(String recordTypeToCreate, SpiderDataGroup spiderDataGroupToCreate) {
		SpiderRecordCreator spiderRecordCreatorOutput = SpiderInstanceProvider
				.getSpiderRecordCreator();
		spiderRecordCreatorOutput.createAndStoreRecord(authToken, recordTypeToCreate,
				spiderDataGroupToCreate);
	}

	private String getIdForOutputPGroup() {
		return metadataId.substring(0, metadataId.indexOf("Group")) + "OutputPGroup";
	}
}
