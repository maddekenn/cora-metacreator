package se.uu.ub.cora.metacreator.recordtype;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.spider.dependency.SpiderInstanceProvider;
import se.uu.ub.cora.spider.extended.ExtendedFunctionality;
import se.uu.ub.cora.spider.record.SpiderRecordCreator;
import se.uu.ub.cora.spider.record.SpiderRecordReader;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class SearchFromRecordTypeCreator implements ExtendedFunctionality {

	private String authToken;
	private String id;
	private String dataDividerString;

	@Override
	public void useExtendedFunctionality(String authToken, DataGroup spiderDataGroup) {
		this.authToken = authToken;

		extractIdAndRecordTypeAndDataDividerFromSpiderDataGroup(spiderDataGroup);

		possiblyCreateSearch(authToken);
	}

	private void extractIdAndRecordTypeAndDataDividerFromSpiderDataGroup(
			DataGroup spiderDataGroup) {
		DataGroup recordInfoGroup = spiderDataGroup.getFirstGroupWithNameInData("recordInfo");
		id = extractIdFromSpiderDataGroup(recordInfoGroup);
		dataDividerString = extractDataDividerFromSpiderDataGroup(recordInfoGroup);
	}

	private String extractIdFromSpiderDataGroup(DataGroup recordInfoGroup) {
		return recordInfoGroup.getFirstAtomicValueWithNameInData("id");
	}

	private String extractDataDividerFromSpiderDataGroup(DataGroup recordInfoGroup) {
		return recordInfoGroup.getFirstGroupWithNameInData("dataDivider")
				.getFirstAtomicValueWithNameInData("linkedRecordId");
	}

	private void possiblyCreateSearch(String authToken) {
		if (searchDoesNotExistInStorage(id + "Search")) {
			SearchGroupCreator creator = SearchGroupCreator
					.withIdIdAndDataDividerAndRecordType(id + "Search", dataDividerString, id);
			createSearch(authToken, creator);
		}
	}

	private boolean searchDoesNotExistInStorage(String searchId) {
		try {
			SpiderRecordReader spiderRecordReader = SpiderInstanceProvider.getSpiderRecordReader();
			spiderRecordReader.readRecord(authToken, "search", searchId);
		} catch (RecordNotFoundException e) {
			return true;
		}
		return false;
	}

	private void createSearch(String authToken, SearchGroupCreator creator) {
		DataGroup searchGroup = creator.createGroup("");
		SpiderRecordCreator spiderRecordCreator = SpiderInstanceProvider.getSpiderRecordCreator();
		spiderRecordCreator.createAndStoreRecord(authToken, "search", searchGroup);
	}
}
