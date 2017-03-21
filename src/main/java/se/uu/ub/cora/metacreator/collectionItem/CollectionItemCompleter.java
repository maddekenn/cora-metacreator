package se.uu.ub.cora.metacreator.collectionItem;

import se.uu.ub.cora.metacreator.DataCreatorHelper;
import se.uu.ub.cora.metacreator.MetadataCompleter;
import se.uu.ub.cora.metacreator.text.TextCreator;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;
import se.uu.ub.cora.spider.record.SpiderRecordCreator;
import se.uu.ub.cora.spider.record.SpiderRecordReader;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class CollectionItemCompleter implements ExtendedFunctionality {

	private String implementingTextType;
	private SpiderDataGroup spiderDataGroup;
	private String userId;
	private String dataDividerString;
	private MetadataCompleter metadataCompleter;

	public CollectionItemCompleter(String implementingTextType,
			MetadataCompleter metadataCompleter) {
		this.implementingTextType = implementingTextType;
		this.metadataCompleter = metadataCompleter;
	}

	public static CollectionItemCompleter forImplementingTextTypeWithMetadataCompleter(
			String implementingTextType, MetadataCompleter metadataCompleter) {
		return new CollectionItemCompleter(implementingTextType, metadataCompleter);
	}

	@Override
	public void useExtendedFunctionality(String userId, SpiderDataGroup spiderDataGroup) {
		this.userId = userId;
		this.spiderDataGroup = spiderDataGroup;

		dataDividerString = DataCreatorHelper
				.extractDataDividerStringFromDataGroup(spiderDataGroup);

		String id = extractIdFromSpiderDataGroup(spiderDataGroup);

		metadataCompleter.completeSpiderDataGroupWithTexts(spiderDataGroup);

		SpiderRecordReader spiderRecordReader = SpiderInstanceProvider.getSpiderRecordReader();
		try {
			spiderRecordReader.readRecord(userId, implementingTextType,
					spiderDataGroup.extractAtomicValue("textId"));
		} catch (RecordNotFoundException e) {
			createTextWithTextIdInStorage(spiderDataGroup.extractAtomicValue("textId"));
		}
		try {
			spiderRecordReader.readRecord(userId, implementingTextType,
					spiderDataGroup.extractAtomicValue("defTextId"));
		} catch (RecordNotFoundException e) {
			createTextWithTextIdInStorage(spiderDataGroup.extractAtomicValue("defTextId"));
		}

		// setOrUseTextIdWithNameInDataToEnsureTextExistsInStorage(id + "Text",
		// "textId");
		// setOrUseTextIdWithNameInDataToEnsureTextExistsInStorage(id +
		// "DefText", "defTextId");
	}

	private String extractIdFromSpiderDataGroup(SpiderDataGroup spiderDataGroup) {
		SpiderDataGroup recordInfoGroup = spiderDataGroup.extractGroup("recordInfo");
		return recordInfoGroup.extractAtomicValue("id");
	}

	// private void
	// setOrUseTextIdWithNameInDataToEnsureTextExistsInStorage(String textId,
	// String nameInData) {
	// if (spiderDataGroup.containsChildWithNameInData(nameInData)) {
	// ensureTextExistInStorageBasedOnExistingValueInNameInData(nameInData);
	// } else {
	// ensureTextExistsInStorageBasedOnNewTextId(textId, nameInData);
	// }
	// }
	//
	// private void ensureTextExistsInStorageBasedOnNewTextId(String textId,
	// String nameInData) {
	// spiderDataGroup.addChild(SpiderDataAtomic.withNameInDataAndValue(nameInData,
	// textId));
	// ensureTextExistsInStorage(textId);
	// }
	//
	// private void ensureTextExistsInStorage(String textId) {
	// if (textDoesNotExistInStorage(textId)) {
	// createTextWithTextIdInStorage(textId);
	// }
	// }
	//
	// private void
	// ensureTextExistInStorageBasedOnExistingValueInNameInData(String
	// nameInData) {
	// String textId2 = spiderDataGroup.extractAtomicValue(nameInData);
	// ensureTextExistsInStorage(textId2);
	// }
	//
	// private boolean textDoesNotExistInStorage(String textId) {
	// try {
	// SpiderRecordReader spiderRecordReader =
	// SpiderInstanceProvider.getSpiderRecordReader();
	// spiderRecordReader.readRecord(userId, implementingTextType, textId);
	// } catch (RecordNotFoundException e) {
	// return true;
	// }
	// return false;
	// }
	//
	private void createTextWithTextIdInStorage(String textId) {
		TextCreator textCreator = TextCreator.withTextIdAndDataDivider(textId, dataDividerString);
		SpiderDataGroup textGroup = textCreator.createText();

		SpiderRecordCreator spiderRecordCreator = SpiderInstanceProvider.getSpiderRecordCreator();
		spiderRecordCreator.createAndStoreRecord(userId, implementingTextType, textGroup);
	}
}
