package se.uu.ub.cora.metacreator.group;

import java.util.List;

import se.uu.ub.cora.metacreator.DataCreatorHelper;
import se.uu.ub.cora.spider.data.SpiderDataElement;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;
import se.uu.ub.cora.spider.record.SpiderRecordCreator;

public class PGroupFromMetadataGroupCreator implements ExtendedFunctionality {

	private String authToken;

	@Override
	public void useExtendedFunctionality(String authToken, SpiderDataGroup spiderDataGroup) {
		this.authToken = authToken;
		PGroupConstructor constructor = new PGroupConstructor();

		String metadataId = DataCreatorHelper.extractIdFromDataGroup(spiderDataGroup);
		String id = metadataId.substring(0, metadataId.indexOf("Group")) + "PGroup";
		String dataDivider = DataCreatorHelper
				.extractDataDividerStringFromDataGroup(spiderDataGroup);
		List<SpiderDataElement> metadataChildReferences = spiderDataGroup
				.extractGroup("childReferences").getChildren();

		SpiderDataGroup inputPGroup = constructor
				.constructPGroupWithIdDataDividerPresentationOfAndChildren(id, dataDivider,
						metadataId, metadataChildReferences, "input");
		createRecord("presentationGroup", inputPGroup);

		SpiderDataGroup outputPGroup = SpiderDataGroup.withNameInData("presentation");
		createRecord("presentationGroup", outputPGroup);
		// constructor.constructPGroupWithIdDataDividerPresentationOfAndChildren(id,
		// dataDivider, presentationOf, metadataChildReferences, mode)

	}

	private void createRecord(String recordTypeToCreate, SpiderDataGroup spiderDataGroupToCreate) {
		SpiderRecordCreator spiderRecordCreatorOutput = SpiderInstanceProvider
				.getSpiderRecordCreator();
		spiderRecordCreatorOutput.createAndStoreRecord(authToken, recordTypeToCreate,
				spiderDataGroupToCreate);
	}

}
