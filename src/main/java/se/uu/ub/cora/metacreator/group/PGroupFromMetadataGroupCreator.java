package se.uu.ub.cora.metacreator.group;

import java.util.List;

import se.uu.ub.cora.metacreator.DataCreatorHelper;
import se.uu.ub.cora.spider.data.SpiderDataElement;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;
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
		setParametersForCreation(spiderDataGroup);

		String id = getIdForInputPGroup();
		possiblyConstructAndCreatePGroupWithIdAndMode(id, "input");

		String outputId = getIdForOutputPGroup();
		possiblyConstructAndCreatePGroupWithIdAndMode(outputId, "output");

	}

	private void setParametersForCreation(SpiderDataGroup spiderDataGroup) {
		constructor = new PGroupConstructor(authToken);
		metadataId = DataCreatorHelper.extractIdFromDataGroup(spiderDataGroup);
		dataDivider = DataCreatorHelper
				.extractDataDividerStringFromDataGroup(spiderDataGroup);
		metadataChildReferences = spiderDataGroup
				.extractGroup("childReferences").getChildren();
	}

	private String getIdForInputPGroup() {
		return metadataId.substring(0, metadataId.indexOf("Group")) + "PGroup";
	}

	private void possiblyConstructAndCreatePGroupWithIdAndMode(String id, String mode) {
		if(pGroupIsMissing(id)) {
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
		SpiderDataGroup inputPGroup = constructor
                .constructPGroupWithIdDataDividerPresentationOfChildrenAndMode(id,dataDivider, metadataId, metadataChildReferences,
						mode);
		createRecord("presentationGroup", inputPGroup);
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
