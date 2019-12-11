package se.uu.ub.cora.metacreator;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.record.SpiderRecordCreator;
import se.uu.ub.cora.spider.record.SpiderRecordReader;
import se.uu.ub.cora.storage.RecordNotFoundException;

public class RecordCreatorHelper {

	private final String authToken;
	private DataGroup dataGroup;
	private String implementingTextType;

	public RecordCreatorHelper(String authToken, DataGroup spiderDataGroup,
			String implementingTextType) {
		this.authToken = authToken;
		this.dataGroup = spiderDataGroup;
		this.implementingTextType = implementingTextType;
	}

	public static RecordCreatorHelper withAuthTokenSpiderDataGroupAndImplementingTextType(
			String authToken, DataGroup spiderDataGroup, String implementingTextType) {
		return new RecordCreatorHelper(authToken, spiderDataGroup, implementingTextType);
	}

	public void createTextsIfMissing() {
		createTextWithTextIdToExtractIfMissing("textId");
		createTextWithTextIdToExtractIfMissing("defTextId");
	}

	private void createTextWithTextIdToExtractIfMissing(String textIdToExtract) {
		DataGroup textIdGroup = this.dataGroup.getFirstGroupWithNameInData(textIdToExtract);
		String textId = textIdGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
		if (textIsMissing(textId)) {
			createTextWithTextId(textId);
		}
	}

	private boolean textIsMissing(String textId) {
		try {
			SpiderRecordReader spiderRecordReader = SpiderInstanceProvider.getSpiderRecordReader();
			spiderRecordReader.readRecord(authToken, implementingTextType, textId);
		} catch (RecordNotFoundException e) {
			return true;
		}
		return false;
	}

	private void createTextWithTextId(String textId) {
		String dataDivider = DataCreatorHelper
				.extractDataDividerStringFromDataGroup(dataGroup);
		createTextInStorageWithTextIdDataDividerAndTextType(textId, dataDivider,
				implementingTextType);
	}

	private void createTextInStorageWithTextIdDataDividerAndTextType(String textId,
			String dataDivider, String implementingTextType) {
		TextConstructor textConstructor = TextConstructor.withTextIdAndDataDivider(textId,
				dataDivider);
		DataGroup textGroup = textConstructor.createText();

		SpiderRecordCreator spiderRecordCreator = SpiderInstanceProvider.getSpiderRecordCreator();
		spiderRecordCreator.createAndStoreRecord(authToken, implementingTextType, textGroup);
	}
}
