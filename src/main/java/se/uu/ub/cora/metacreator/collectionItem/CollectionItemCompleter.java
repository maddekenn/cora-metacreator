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


		if(textIdDoesNotExist(spiderDataGroup, "textId")) {
			createTextWithTextIdInStorage(spiderDataGroup.extractAtomicValue("textId"));
		}

		if(textIdDoesNotExist(spiderDataGroup, "defTextId")) {
			createTextWithTextIdInStorage(spiderDataGroup.extractAtomicValue("defTextId"));
		}
	}

	private boolean textIdDoesNotExist(SpiderDataGroup spiderDataGroup, String textId) {
		SpiderRecordReader spiderRecordReader = SpiderInstanceProvider.getSpiderRecordReader();
		try {
			spiderRecordReader.readRecord(userId, implementingTextType,
					spiderDataGroup.extractAtomicValue(textId));
		} catch (RecordNotFoundException e) {
			return true;
		}
		return false;
	}

	private String extractIdFromSpiderDataGroup(SpiderDataGroup spiderDataGroup) {
		SpiderDataGroup recordInfoGroup = spiderDataGroup.extractGroup("recordInfo");
		return recordInfoGroup.extractAtomicValue("id");
	}

	private void createTextWithTextIdInStorage(String textId) {
		TextCreator textCreator = TextCreator.withTextIdAndDataDivider(textId, dataDividerString);
		SpiderDataGroup textGroup = textCreator.createText();

		SpiderRecordCreator spiderRecordCreator = SpiderInstanceProvider.getSpiderRecordCreator();
		spiderRecordCreator.createAndStoreRecord(userId, implementingTextType, textGroup);
	}
}
