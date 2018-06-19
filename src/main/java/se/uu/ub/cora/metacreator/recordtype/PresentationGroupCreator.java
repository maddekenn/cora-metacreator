/*
 * Copyright 2016, 2017, 2018 Uppsala University Library
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

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.metacreator.group.PChildRefConstructorFactory;
import se.uu.ub.cora.metacreator.group.PPChildRefConstructorFactoryImp;
import se.uu.ub.cora.metacreator.group.PGroupConstructor;
import se.uu.ub.cora.spider.data.SpiderDataElement;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.record.SpiderRecordCreator;
import se.uu.ub.cora.spider.record.SpiderRecordReader;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class PresentationGroupCreator {

	private final String authToken;
	private String presentationId;
	private final String dataDivider;
	private String presentationOf;
	private String mode;
	private List<SpiderDataElement> metadataChildReferences;

	public PresentationGroupCreator(String authToken, String presentationId, String dataDivider) {
		this.authToken = authToken;
		this.presentationId = presentationId;
		this.dataDivider = dataDivider;
	}

	public static PresentationGroupCreator withAuthTokenPresentationIdAndDataDivider(
			String authToken, String presentationId, String dataDivider) {
		return new PresentationGroupCreator(authToken, presentationId, dataDivider);
	}

	public void setPresentationOfAndMode(String presentationOf, String mode) {
		this.presentationOf = presentationOf;
		this.mode = mode;
	}

	public void createPGroupIfNotAlreadyExist() {
		if(recordDoesNotExistInStorage()) {
			createPGroup();
		}
	}

	private boolean recordDoesNotExistInStorage() {
		try {
			SpiderRecordReader spiderRecordReader = SpiderInstanceProvider.getSpiderRecordReader();
			spiderRecordReader.readRecord(authToken, "presentationGroup", presentationId);
		} catch (RecordNotFoundException e) {
			return true;
		}
		return false;
	}

	private void createPGroup() {
		SpiderDataGroup dataGroup = createSpiderDataGroupToCreate();

		SpiderRecordCreator spiderRecordCreator = SpiderInstanceProvider.getSpiderRecordCreator();
		spiderRecordCreator.createAndStoreRecord(authToken, "presentationGroup", dataGroup);
	}

	private SpiderDataGroup createSpiderDataGroupToCreate() {
		PChildRefConstructorFactory constructorFactory = new PPChildRefConstructorFactoryImp();
		PGroupConstructor pGroupConstructor = PGroupConstructor.usingAuthTokenAndPChildRefConstructorFactory(authToken, constructorFactory);

		return pGroupConstructor
                .constructPGroupWithIdDataDividerPresentationOfChildrenAndMode(presentationId,
                        dataDivider, presentationOf, metadataChildReferences, mode);
	}

	public void setMetadataChildReferences(List<SpiderDataElement> metadataChildReferences) {
		this.metadataChildReferences = new ArrayList<>();
		for(SpiderDataElement metadataChildReference : metadataChildReferences) {
			this.metadataChildReferences.add(metadataChildReference);
		}
	}

}
