package se.uu.ub.cora.metacreator.collectionitem;

import se.uu.ub.cora.metacreator.DataCreatorHelper;
import se.uu.ub.cora.metacreator.RecordCreatorHelper;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;
import se.uu.ub.cora.spider.record.SpiderRecordReader;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class TextCreator implements ExtendedFunctionality {

	private String authToken;
	private SpiderDataGroup spiderDataGroup;
	private String implementingTextType;

	public TextCreator(String implementingTextType) {
		this.implementingTextType = implementingTextType;
	}

	@Override
	public void useExtendedFunctionality(String authToken, SpiderDataGroup spiderDataGroup) {
		this.authToken = authToken;
		this.spiderDataGroup = spiderDataGroup;
		createTextsIfMissing();

	}

	public static TextCreator forImplementingTextType(String implementingTextType) {
		return new TextCreator(implementingTextType);
	}

	private void createTextsIfMissing() {
		createTextIfTextIdIsMissing("textId");
		createTextIfTextIdIsMissing("defTextId");
	}

	private void createTextIfTextIdIsMissing(String typeOfTextToCreate) {

		SpiderDataGroup textIdGroup = spiderDataGroup.extractGroup(typeOfTextToCreate);
		String textId = textIdGroup.extractAtomicValue("linkedRecordId");

		if (textIdDoesNotExist(textId)) {
			createTextWithTextIdInStorage(textId);
		}
	}

	private boolean textIdDoesNotExist(String textId) {
		try {
			SpiderRecordReader spiderRecordReader = SpiderInstanceProvider.getSpiderRecordReader();
			spiderRecordReader.readRecord(authToken, implementingTextType,
					textId);
		} catch (RecordNotFoundException e) {
			return true;
		}
		return false;
	}

	private void createTextWithTextIdInStorage(String textId) {
		String dataDivider = DataCreatorHelper
				.extractDataDividerStringFromDataGroup(spiderDataGroup);
		RecordCreatorHelper recordCreatorHelper = new RecordCreatorHelper(authToken);
		recordCreatorHelper.createTextInStorageWithTextIdDataDividerAndTextType(textId, dataDivider,
				implementingTextType);
	}

}
