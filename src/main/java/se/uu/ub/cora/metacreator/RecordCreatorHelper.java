package se.uu.ub.cora.metacreator;

import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.record.SpiderRecordCreator;
import se.uu.ub.cora.spider.record.SpiderRecordReader;
import se.uu.ub.cora.storage.RecordNotFoundException;

public class RecordCreatorHelper {

	private final String authToken;
	private SpiderDataGroup spiderDataGroup;
	private String implementingTextType;

	public RecordCreatorHelper(String authToken, SpiderDataGroup spiderDataGroup,
			String implementingTextType) {
		this.authToken = authToken;
		this.spiderDataGroup = spiderDataGroup;
		this.implementingTextType = implementingTextType;
	}

	public static RecordCreatorHelper withAuthTokenSpiderDataGroupAndImplementingTextType(
			String authToken, SpiderDataGroup spiderDataGroup, String implementingTextType) {
		return new RecordCreatorHelper(authToken, spiderDataGroup, implementingTextType);
	}

	public void createTextsIfMissing() {
		createTextWithTextIdToExtractIfMissing("textId");
		createTextWithTextIdToExtractIfMissing("defTextId");
	}

	private void createTextWithTextIdToExtractIfMissing(String textIdToExtract) {
		SpiderDataGroup textIdGroup = this.spiderDataGroup.extractGroup(textIdToExtract);
		String textId = textIdGroup.extractAtomicValue("linkedRecordId");
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
				.extractDataDividerStringFromDataGroup(spiderDataGroup);
		createTextInStorageWithTextIdDataDividerAndTextType(textId, dataDivider,
				implementingTextType);
	}

	private void createTextInStorageWithTextIdDataDividerAndTextType(String textId,
			String dataDivider, String implementingTextType) {
		TextConstructor textConstructor = TextConstructor.withTextIdAndDataDivider(textId, dataDivider);
		SpiderDataGroup textGroup = textConstructor.createText();

		SpiderRecordCreator spiderRecordCreator = SpiderInstanceProvider.getSpiderRecordCreator();
		spiderRecordCreator.createAndStoreRecord(authToken, implementingTextType, textGroup);
	}
}
