package se.uu.ub.cora.metacreator.recordtype;

import java.util.List;

import se.uu.ub.cora.metacreator.group.PGroupConstructor;
import se.uu.ub.cora.spider.data.SpiderDataElement;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.record.SpiderRecordCreator;

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

	public void createGroup() {
		PGroupConstructor pGroupConstructor = new PGroupConstructor(authToken);

		SpiderDataGroup dataGroup = pGroupConstructor
				.constructPGroupWithIdDataDividerPresentationOfChildrenAndMode(presentationId,
						dataDivider, presentationOf, metadataChildReferences, mode);
		SpiderRecordCreator spiderRecordCreator = SpiderInstanceProvider.getSpiderRecordCreator();
		spiderRecordCreator.createAndStoreRecord(authToken, "presentationGroup", dataGroup);
	}

	public void setMetadataChildReferences(List<SpiderDataElement> metadataChildReferences) {
		this.metadataChildReferences = metadataChildReferences;
	}

}
