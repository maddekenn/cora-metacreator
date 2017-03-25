package se.uu.ub.cora.metacreator.collection;

import se.uu.ub.cora.metacreator.DataCreatorHelper;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;
import se.uu.ub.cora.spider.record.SpiderRecordCreator;
import se.uu.ub.cora.spider.record.SpiderRecordReader;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class CollectionVarFromItemCollectionCreator implements ExtendedFunctionality {

	private String authToken;
	private SpiderDataGroup itemCollectionToCreateFrom;
	private String itemCollectionId;
	private String idForCollectionVariable;

	@Override
	public void useExtendedFunctionality(String authToken,
			SpiderDataGroup itemCollectionToCreateFrom) {
		this.authToken = authToken;
		this.itemCollectionToCreateFrom = itemCollectionToCreateFrom;
		extractIds();

		if (collectionVarDoesNotExist(idForCollectionVariable)) {
			SpiderDataGroup collectionVar = extractDataAndConstructCollectionVariable(
					itemCollectionToCreateFrom);
			createRecord("metadataCollectionVariable", collectionVar);
		}
	}

	private void extractIds() {
		itemCollectionId = extractIdFromItemCollection();
		idForCollectionVariable = itemCollectionId + "Var";
	}

	private boolean collectionVarDoesNotExist(String id) {
		SpiderRecordReader reader = SpiderInstanceProvider.getSpiderRecordReader();
		try {
			reader.readRecord(authToken, "metadataCollectionVariable", id);
		} catch (RecordNotFoundException e) {
			return true;
		}
		return false;
	}

	private SpiderDataGroup extractDataAndConstructCollectionVariable(
			SpiderDataGroup itemCollectionToCreateFrom) {

		String nameInData = itemCollectionToCreateFrom.extractAtomicValue("nameInData");
		String dataDivider = DataCreatorHelper
				.extractDataDividerStringFromDataGroup(itemCollectionToCreateFrom);
		return constructCollectionVariable(idForCollectionVariable, nameInData, dataDivider);
	}

	private SpiderDataGroup constructCollectionVariable(String id, String nameInData,
			String dataDivider) {
		CollectionVariableConstructor constructor = new CollectionVariableConstructor();
		return constructor.constructCollectionVarWithIdNameInDataDataDividerAndRefCollection(id,
				nameInData, dataDivider, itemCollectionId);
	}

	private String extractIdFromItemCollection() {
		SpiderDataGroup recordInfo = itemCollectionToCreateFrom.extractGroup("recordInfo");
		return recordInfo.extractAtomicValue("id");
	}

	private void createRecord(String recordTypeToCreate, SpiderDataGroup spiderDataGroupToCreate) {
		SpiderRecordCreator spiderRecordCreatorOutput = SpiderInstanceProvider
				.getSpiderRecordCreator();
		spiderRecordCreatorOutput.createAndStoreRecord(authToken, recordTypeToCreate,
				spiderDataGroupToCreate);
	}
}
