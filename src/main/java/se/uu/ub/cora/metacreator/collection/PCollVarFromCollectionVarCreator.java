package se.uu.ub.cora.metacreator.collection;

import se.uu.ub.cora.metacreator.DataCreatorHelper;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;
import se.uu.ub.cora.spider.record.SpiderRecordCreator;
import se.uu.ub.cora.spider.record.SpiderRecordReader;

public class PCollVarFromCollectionVarCreator implements ExtendedFunctionality {

	private String authToken;
	private String nameInData;
	private String presentationOf;
	private String dataDivider;
	private PCollVarConstructor constructor;

	@Override
	public void useExtendedFunctionality(String authToken,
			SpiderDataGroup collectionvarToCreateFrom) {
		this.authToken = authToken;

		setParametersForCreation(collectionvarToCreateFrom);

		possiblyCreateInputPCollVar();
		possiblyCreateOutputPCollVar();
	}

	private void setParametersForCreation(SpiderDataGroup collectionvarToCreateFrom) {
		constructor = new PCollVarConstructor();
		nameInData = collectionvarToCreateFrom.extractAtomicValue("nameInData");
		presentationOf = DataCreatorHelper.extractIdFromDataGroup(collectionvarToCreateFrom);
		dataDivider = DataCreatorHelper
				.extractDataDividerStringFromDataGroup(collectionvarToCreateFrom);
	}

	private void possiblyCreateInputPCollVar() {
		String pCollVarId = constructIdForPCollVar(nameInData);
		SpiderRecordReader reader = SpiderInstanceProvider.getSpiderRecordReader();
		try {
			reader.readRecord(authToken, "presentationCollectionVariable", pCollVarId);
		} catch (Exception e) {
			createPCollVarWithIdAndMode(pCollVarId, "input");
		}
	}

	private void createPCollVarWithIdAndMode(String pCollVarId, String mode) {
		SpiderDataGroup pCollVar = constructor
				.constructPCollVarWithIdDataDividerPresentationOfAndMode(pCollVarId, dataDivider,
						presentationOf, mode);
		createRecord("presentationCollectionVar", pCollVar);
	}

	private String constructIdForPCollVar(String nameInData) {
		return nameInData + "PCollVar";
	}

	private void createRecord(String recordTypeToCreate, SpiderDataGroup spiderDataGroupToCreate) {
		SpiderRecordCreator spiderRecordCreatorOutput = SpiderInstanceProvider
				.getSpiderRecordCreator();
		spiderRecordCreatorOutput.createAndStoreRecord(authToken, recordTypeToCreate,
				spiderDataGroupToCreate);
	}

	private void possiblyCreateOutputPCollVar() {
		String pCollVarId = constructIdForOutputPCollVar(nameInData);
		SpiderRecordReader reader = SpiderInstanceProvider.getSpiderRecordReader();
		try {
			reader.readRecord(authToken, "presentationCollectionVariable", pCollVarId);
		} catch (Exception e) {
			createPCollVarWithIdAndMode(pCollVarId, "output");
		}
	}

	private String constructIdForOutputPCollVar(String nameInData) {
		return nameInData + "OutputPCollVar";
	}
}
